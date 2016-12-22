package com.jaedeuk.notepad.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jaedeuk.notepad.R;
import com.jaedeuk.notepad.memo.MemoDetailActivity;
import com.jaedeuk.notepad.model.Memo;


/**
 * 알림을 받는 리시버
 */
public class AlarmReceiver extends BroadcastReceiver {
    final static String TAG = AlarmReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,"리시브 진입");
        Memo memo = (Memo)intent.getSerializableExtra("memo");
        int position = intent.getIntExtra("position",-1);
        //메모 상세보기로 보내는 인텐트 생성
        Intent mIntent = new Intent(context, MemoDetailActivity.class);
        mIntent.putExtra("memo",memo);
        mIntent.putExtra("position",position);

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        //알림 세팅
        builder.setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis())
                .setContentTitle("메모 알림").setContentText(memo.getText())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);

        notificationmanager.notify(1, builder.build());
    }
}
