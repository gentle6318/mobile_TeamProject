package com.jaedeuk.notepad.memo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaedeuk.notepad.R;
import com.jaedeuk.notepad.model.Memo;
import com.jaedeuk.notepad.util.BusProvider;
import com.jaedeuk.notepad.util.PushEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.CategoryViewHolder> {
    final static String TAG = MemoListAdapter.class.getSimpleName();
    private Context mContext;

    private List<Memo> mListData = new ArrayList<>();


    public MemoListAdapter(Context mContext) {
        this.mContext = mContext;
        BusProvider.getInstance().register(this); // 이벤트 버스 등록
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Memo data = mListData.get(position);

        holder.getTv_name().setText(data.getTitle());
        holder.getTv_name().setOnClickListener(new MemoListClickListener(data,position));

        if(data.isImportant()){
            holder.getTv_name().setTextColor(mContext.getResources().getColor(R.color.important));
            holder.getIv_star().setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if(mListData == null){
            return 0;
        }else{
            return mListData.size();
        }
    }

    public void setmListData(List<Memo> mListData){
        this.mListData = mListData;
        Log.e(TAG,"mListData :"+mListData);
        notifyDataSetChanged();//데이터가 변경되었음을 ui 스레드에 알림
    }

    /**
     * 메모 데이터를 받아 리스트에 추가
     * @param memo
     */
    public void addmListData(Memo memo){
        mListData.add(memo);
        notifyDataSetChanged();
    }

    /**
     * 해당하는 포지션의 값을 삭제
     * @param position
     */
    public void deleteData(int position){
        if(position > -1){
            mListData.remove(position);
            notifyDataSetChanged();
        }
    }

    /**
     * 수정은 이벤트 버스로 값을 받아옴
     */
    @Subscribe // 이벤트버스 구독
    public void editData(PushEvent mPushEvent){
        mListData.set(mPushEvent.getPosition(),mPushEvent.getMemo());
        notifyDataSetChanged();
    }

    public List<Memo> getmListData() {
        return mListData;
    }

    /**
     * 리사이클러 뷰 홀더
     */
    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_item_name)
        TextView tv_name;
        @Bind(R.id.iv_item_star)
        ImageView iv_star;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public TextView getTv_name() {
            return tv_name;
        }

        public ImageView getIv_star() {
            return iv_star;
        }
    }

    /**
     * 메모를 클릭했을 때 메모 데이터와 포지션값을 넘겨준다
     */
    private class MemoListClickListener implements View.OnClickListener{
        Memo memo;
        int position;

        public MemoListClickListener(Memo memo, int position) {
            this.memo = memo;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext,MemoDetailActivity.class);
            intent.putExtra("memo",memo); // 메모 객체는 직렬화를 하여 넘긴다
            intent.putExtra("position",position);
            ((MemoListActivity)mContext).startActivityForResult(intent,MemoListActivity.MEMODETAIL);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        BusProvider.getInstance().unregister(this); // 앱 종료 시 이벤트버스 해제
    }
}
