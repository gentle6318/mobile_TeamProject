package com.jaedeuk.notepad.memo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jaedeuk.notepad.R;
import com.jaedeuk.notepad.model.Memo;
import com.jaedeuk.notepad.util.AlarmReceiver;
import com.jaedeuk.notepad.util.BaseActivity;
import com.jaedeuk.notepad.util.BusProvider;
import com.jaedeuk.notepad.util.PushEvent;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MemoWriteActivity extends BaseActivity {
    final static String TAG = MemoWriteActivity.class.getSimpleName();

    @Bind(R.id.cb_write_alarm)
    CheckBox cb_alarm;
    @Bind(R.id.cb_write_important)
    CheckBox cb_important;
    @Bind(R.id.et_write_text)
    EditText et_text;
    @Bind(R.id.et_write_title)
    EditText et_title;
    @Bind(R.id.et_write_url)
    EditText et_url;
    @Bind(R.id.tv_write_date)
    TextView tv_date;
    @Bind(R.id.tv_write_time)
    TextView tv_time;
    @Bind(R.id.rl_write_container)
    RelativeLayout rl_container;

    private String date = "2016/01/01";
    private String time = "00:00";
    private String flag = "";
    private Memo memo;
    private int position;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        container.setLayoutResource(R.layout.activity_memowrite);
        View containView = container.inflate();
        ButterKnife.bind(this);
        setAppBar();
        setComponent();
        getData();
    }

    private void setAppBar() {
        View titleView = getLayoutInflater().inflate(R.layout.app_bar_write, null);
        Button confirm = (Button) titleView.findViewById(R.id.btn_action_write_confirm);
        confirm.setOnClickListener(new ConfirmClickListener());
        actionBarTitleSet(titleView);
    }

    private void setComponent() {
        View.OnClickListener listener = new ItemClickListener();
        tv_date.setOnClickListener(listener);
        tv_time.setOnClickListener(listener);
        cb_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    rl_container.setVisibility(View.VISIBLE);
                }else{
                    rl_container.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 메모 상세보기에서 수정할 때 데이터값을 불러옴
     */
    private void getData(){
        if(getIntent().getIntExtra("position",-1) > -1){
            memo = (Memo)getIntent().getSerializableExtra("memo");
            position = getIntent().getIntExtra("position",-1);
            flag = getIntent().getStringExtra("flag");

            et_text.setText(memo.getText());
            et_title.setText(memo.getTitle());
            et_url.setText(memo.getUrl());
            tv_date.setText(memo.getDate());
            tv_time.setText(memo.getTime());
            cb_important.setChecked(memo.isImportant());
            cb_alarm.setChecked(memo.isAlarm());
        }
    }

    /**
     * 완료를 눌렀을 때 메모를 저장
     */
    private class ConfirmClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            boolean alarm = cb_alarm.isChecked();
            boolean important = cb_important.isChecked();
            String title = et_title.getText().toString();
            String text = et_text.getText().toString();
            String url = et_url.getText().toString();
            //제목 미입력시 리턴
            if(title.length() < 0){
                Toast.makeText(MemoWriteActivity.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Memo memo = new Memo(title,text,important,alarm); // 메모 객체를 만듬
            //url이 있을 때
            if(url.length() > 0){
                memo.setUrl(url);
            }

            //알람이 있으면 시간과 날짜 데이터를 저장
            if(alarm){
                memo.setDate(date);
                memo.setTime(time);
                setAlarm(memo);
            }
            //일반 글쓰기
            if(flag.equals("")){
                Intent intent = new Intent();
                intent.putExtra("memo",memo);
                setResult(MemoListActivity.MEMOWRITE,intent); //메모쓰기 액티비티의 결과로 줌
                finish();
            }else{ //수정
                BusProvider.getInstance().post(new PushEvent(memo,position));
                finish();
            }
        }
    }

    /**
     * 알람 세팅
     * @param memo
     */
    private void setAlarm(Memo memo){
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MemoWriteActivity.this, AlarmReceiver.class); // 알람 리시버로 보냄
        intent.putExtra("memo",memo);
        intent.putExtra("position",position);

        PendingIntent sender = PendingIntent.getBroadcast(MemoWriteActivity.this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        //알람시간 calendar에 set해주기
        calendar.set(mYear,mMonth,mDay,mHour,mMin,0);

        Log.e(TAG,"알림 등록");
        //알람 예약
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    /**
     * 날짜, 시간 클릭 리스너
     */
    private class ItemClickListener implements View.OnClickListener{
        Calendar currentDate = Calendar.getInstance();
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_write_date://날짜
                    new DatePickerDialog(MemoWriteActivity.this, new TimeSetListener(),
                            currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                case R.id.tv_write_time://시간
                    boolean isAm = currentDate.get(Calendar.AM_PM) != 1 ? true : false;
                    int hour = isAm ? currentDate.get(Calendar.HOUR) : currentDate.get(Calendar.HOUR) + 12;
                    int minute = currentDate.get(Calendar.MINUTE);
                    new TimePickerDialog(MemoWriteActivity.this, new TimeSetListener(), hour, minute, false).show();
                    break;
            }
        }
    }

    /**
     * 날짜와 시간을 선택했을 때 값을 가져옴
     */
    private class TimeSetListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String _year = String.valueOf(year);
            String _month = String.valueOf(monthOfYear + 1);
            String _day = String.valueOf(dayOfMonth);
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            date = _year + "/" + _month + "/" + _day;

            tv_date.setText(date);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String _minute = String.valueOf(minute);
            String _hour = String.valueOf(hourOfDay);
            mMin = minute;
            mHour = hourOfDay;

            _hour = _hour.length() < 2 ? "0" + _hour : _hour;
            _minute = _minute.length() < 2 ? "0" + _minute : _minute;

            time = _hour + ":" + _minute;

            tv_time.setText(time);
        }
    }

}
