package com.christmas.stickyheaderview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/6/26 0026.
 */


public abstract class LoadMoreAdapter extends RecyclerView.Adapter<CommonRcViewHolder> {
    public static final int ITEM_TYPE_FOOTER = 0;

    protected String loadMoreText = "加载更多";


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1){
            return ITEM_TYPE_FOOTER;
        }else {
            return 1;
        }
    }

    @Override
    public CommonRcViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(getFooterViewResId(),parent,false);
            return new CommonRcViewHolder(view);
        }else {
            return getViewHolder(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(CommonRcViewHolder holder, int position) {
        if (getItemViewType(position) != ITEM_TYPE_FOOTER){
            loadData(holder, position);
        }else {
            TextView tv = holder.getView(getFooterTextViewResId());
            tv.setText(loadMoreText);
        }
    }

    @Override
    public int getItemCount() {
        return getCount()+1;
    }

    public void setLoadMoreText(String loadMoreText) {
        this.loadMoreText = loadMoreText;
        notifyItemChanged(getItemCount()-1);
    }

    public abstract int getFooterViewResId();
    public abstract int getFooterTextViewResId();
    public abstract int getCount();
    public abstract CommonRcViewHolder getViewHolder(ViewGroup parent, int viewType);
    public abstract void loadData(CommonRcViewHolder holder, int position);
    public abstract void loadMore();
}
