package com.jaedeuk.notepad.memo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jaedeuk.notepad.R;
import com.jaedeuk.notepad.model.Memo;
import com.jaedeuk.notepad.util.BaseActivity;
import com.jaedeuk.notepad.util.Preference;

public class MemoListActivity extends BaseActivity {
    final static String TAG = MemoListActivity.class.getSimpleName();
    final static int MEMOWRITE = 1011;
    final static int MEMODETAIL = 1022;
    private MemoListAdapter adapter;
    private Preference pref;
    private View containView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        container.setLayoutResource(R.layout.activity_memolist);
        containView = container.inflate();
        pref = new Preference(this);

        setAdapter();
        setData();
    }

    /**
     * 리사이클러뷰 어댑터를 붙임
     */
    private void setAdapter() {
        fab = (FloatingActionButton) containView.findViewById(R.id.fab_memolist);
        recyclerView = (RecyclerView) containView.findViewById(R.id.rv_memolist);
        name = getIntent().getStringExtra("name"); // 액션바 이름
        actionBarTitleSet(name, Color.WHITE);

        adapter = new MemoListAdapter(this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        //메모 작성 버튼
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MemoListActivity.this,MemoWriteActivity.class), MEMOWRITE);
            }
        });
    }

    /**
     * 캐시 데이터를 세팅
     */
    private void setData() {
        if(pref.getMemoList(name) != null){
            adapter.setmListData(pref.getMemoList(name)); // 이름을 key로 목록을 가져옴
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            //메모 쓰기 결과
            if (requestCode == MEMOWRITE) {
                Memo memo = (Memo) data.getSerializableExtra("memo");
                Log.e(TAG, "새글쓰기 진입");
                adapter.addmListData(memo);// 새 글 쓰기
                pref.putMemoList(adapter.getmListData(), name); // 이름을 key로 목록을 저장함
                //메모 상세보기 결과
            } else if (requestCode == MEMODETAIL) {
                if (data != null) {
                    adapter.deleteData(data.getIntExtra("position", -1)); //메모 삭제를 눌렀을때 어댑터에서 삭제
                    pref.putMemoList(adapter.getmListData(), name);
                }
            }
        }catch (Exception e){}
    }
}
