package com.jaedeuk.notepad.memo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaedeuk.notepad.R;
import com.jaedeuk.notepad.model.Memo;
import com.jaedeuk.notepad.util.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemoDetailActivity extends BaseActivity {
    final static String TAG = MemoDetailActivity.class.getSimpleName();

    @Bind(R.id.tv_detail_date)
    TextView tv_date;
    @Bind(R.id.tv_detail_text)
    TextView tv_text;
    @Bind(R.id.tv_detail_time)
    TextView tv_time;
    @Bind(R.id.tv_detail_title)
    TextView tv_title;
    @Bind(R.id.btn_detail_url)
    Button btn_url;

    private Memo memo;
    private int position;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        container.setLayoutResource(R.layout.activity_memodetail);
        View containView = container.inflate();
        ButterKnife.bind(this);
        setAppBar();
        setData();
    }

    /**
     * 상단바 세팅
     */
    private void setAppBar() {
        View titleView = getLayoutInflater().inflate(R.layout.app_bar_detail, null);
        Button btn_delete = (Button) titleView.findViewById(R.id.btn_action_detail_delete);
        Button btn_edit = (Button) titleView.findViewById(R.id.btn_action_detail_edit);
        btn_delete.setOnClickListener(new DeleteClickListener());
        btn_edit.setOnClickListener(new EditClickListener());
        actionBarTitleSet(titleView);
    }

    /**
     * 메모 데이터 세팅
     */
    private void setData(){
        memo = (Memo)getIntent().getSerializableExtra("memo");
        position = getIntent().getIntExtra("position",-1);
        Log.e(TAG,"position :"+position);
        tv_title.setText(memo.getTitle());
        tv_date.setText(memo.getDate());
        tv_time.setText(memo.getTime());
        tv_text.setText(memo.getText());
        //url이 없을때 사이트 이동 버튼을 숨김
        if(memo.getUrl() == null){
            btn_url.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * url 클릭 시 브라우저를 띄움
     */
    @OnClick(R.id.btn_detail_url)
    void urlClick(){
        String url = memo.getUrl();//http가 없으면 연결이 안되므로 붙여준다
        if(!url.substring(0,6).equals("http://")){
            url = "http://"+url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     * 삭제 클릭 리스너
     */
    private class DeleteClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Log.e(TAG,"position :"+position);
            if(position > -1){
                Intent intent = new Intent();
                intent.putExtra("position",position);
                setResult(MemoListActivity.MEMODETAIL,intent);
                finish();
            }
        }
    }

    /**
     * 수정 클릭 리스너
     */
    private class EditClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MemoDetailActivity.this,MemoWriteActivity.class);
            intent.putExtra("memo",memo);
            intent.putExtra("position",position);
            intent.putExtra("flag","edit");
            startActivity(intent);
            finish();
        }
    }
}
