package com.jaedeuk.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.jaedeuk.notepad.category.CategoryActivity;


public class IntroActivity  extends AppCompatActivity{
    final static String TAG = IntroActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                finish();
            }
        },2000);
    }

}
