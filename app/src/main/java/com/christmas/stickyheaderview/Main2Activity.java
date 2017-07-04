package com.christmas.stickyheaderview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class Main2Activity extends Activity {

    RecyclerView recyclerView;
    MyAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.loadMore();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = (RecyclerView) this.findViewById(R.id.rv_demo);
        recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //拿到最后一条的position
                int endCompletelyPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (endCompletelyPosition ==adapter.getItemCount()-1){
                    //执行加载更多的方法，无论是用接口还是别的方式都行
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1);
                        }
                    },1000);
                }
            }
        });
    }

    class MyAdapter extends LoadMoreAdapter {

        private LinkedList<String> mData;

        public MyAdapter() {
            mData = new LinkedList<>();
            for (int i = 0; i < 20; i++) {
                mData.add("item " + i + "");
            }
        }

        public void addDate() {
            for (int i = 0; i < 3; i++) {
                mData.addFirst("refresh " + i);
            }
            recyclerView.getLayoutManager().scrollToPosition(0);
            notifyItemRangeInserted(0, 3);
        }

        @Override
        public CommonRcViewHolder getViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_recycler, parent, false);
            return new CommonRcViewHolder(view);
        }

        @Override
        public void loadData(CommonRcViewHolder holder, int position) {
            TextView tv = holder.getView(R.id.tv);
            tv.setText(mData.get(position));
        }

        @Override
        public void loadMore() {
            int startPosition = getCount();
            if (mData.size() < 100) {
                for (int i = 0; i < 20; i++) {
                    mData.add("more " + i + "");
                }
                int endPosition = getCount() - 1;
                notifyItemRangeInserted(startPosition, 20);
            } else {
                setLoadMoreText("没有更多了");
            }
        }

        @Override
        public int getFooterViewResId() {
            return R.layout.item_footer;
        }

        @Override
        public int getFooterTextViewResId() {
            return R.id.tv;
        }

        @Override
        public int getCount() {
            return mData.size();
        }
    }
}