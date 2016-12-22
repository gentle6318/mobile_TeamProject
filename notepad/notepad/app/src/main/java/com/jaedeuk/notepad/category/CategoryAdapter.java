package com.jaedeuk.notepad.category;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaedeuk.notepad.memo.MemoListActivity;
import com.jaedeuk.notepad.R;
import com.jaedeuk.notepad.model.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    final static String TAG = CategoryAdapter.class.getSimpleName();
    private Context mContext;

    private List<Category> mListData = new ArrayList<>();


    public CategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category data = mListData.get(position);

        holder.getTv_name().setText(data.getName());
        holder.getTv_name().setOnClickListener(new CategoryClickListener(data));

    }

    @Override
    public int getItemCount() {
        if(mListData == null){
            return 0;
        }else{
            return mListData.size();
        }
    }

    public void setmListData(List<Category> mListData){
        this.mListData = mListData;
        Log.e(TAG,"mListData :"+mListData);
        notifyDataSetChanged();
    }

    public void addmListData(Category category){
        mListData.add(category);
        notifyDataSetChanged();
    }

    public List<Category> getmListData() {
        return mListData;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_item_name)
        TextView tv_name;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public TextView getTv_name() {
            return tv_name;
        }
    }

    private class CategoryClickListener implements View.OnClickListener{
        Category data;
        public CategoryClickListener(Category data) {
            this.data = data;
        }
        @Override
        public void onClick(View view) {
            String name = data.getName();
            Intent intent = new Intent(mContext, MemoListActivity.class);
            intent.putExtra("name",name);
            mContext.startActivity(intent);
        }
    }
}
