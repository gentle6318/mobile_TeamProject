package com.jaedeuk.notepad.category;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jaedeuk.notepad.R;
import com.jaedeuk.notepad.model.Category;
import com.jaedeuk.notepad.util.BaseActivity;
import com.jaedeuk.notepad.util.Preference;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity {
    final static String TAG = CategoryActivity.class.getSimpleName();
    private CategoryAdapter adapter;

    @Bind(R.id.fab_category)
    FloatingActionButton fab;
    @Bind(R.id.rv_category)
    RecyclerView recyclerView;
    private View containView;
    Preference pref = new Preference(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        actionBarTitleSet("카테고리", Color.WHITE);
        container.setLayoutResource(R.layout.activity_category);
        containView = container.inflate();
        actionBar.setDisplayHomeAsUpEnabled(false);// 로그인 화면에선 뒤로가기 키를 보여주지 않는다

        ButterKnife.bind(this);
        setAdapter();
        setData();
    }

    private void setAdapter() {
        adapter = new CategoryAdapter(this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CategoryActivity.this);
                alert.setMessage("카테고리 이름을 입력해주세요");
                final EditText input = new EditText(CategoryActivity.this);
                alert.setView(input);
                //키보드 올리기
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = input.getText().toString();
                        adapter.addmListData(new Category(name));
                        pref.putCategoryList(adapter.getmListData());
                        //키보드 내리기
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                });
                alert.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int whichButton) {}});
                alert.show();
            }
        });
    }

    /**
     * 저장된 데이터 세팅
     */
    private void setData() {
        if(pref.getCategoryList() != null){
            List<Category> data = pref.getCategoryList();
            adapter.setmListData(data);
        }
    }

}
