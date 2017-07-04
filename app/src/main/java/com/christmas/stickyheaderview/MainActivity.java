package com.christmas.stickyheaderview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private LinearLayoutManager linearLayoutManager;
  private StickyExampleAdapter adapter;
  private Handler handler = new Handler();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.layout_main);
    
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
       startActivity(new Intent(MainActivity.this,Main2Activity.class));
      }
    });

    initRecyclerView();
  }

  private void initRecyclerView() {
    RecyclerView rvStickyExample = (RecyclerView) findViewById(R.id.rv_sticky_example);
    final TextView tvStickyHeaderView = (TextView) findViewById(R.id.tv_sticky_header_view);

    rvStickyExample.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
    rvStickyExample.setAdapter(adapter = new StickyExampleAdapter(this, DataUtil.getData()));
    rvStickyExample.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        View stickyInfoView = recyclerView.findChildViewUnder(
            tvStickyHeaderView.getMeasuredWidth() / 2, 5);

        if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
          tvStickyHeaderView.setText(String.valueOf(stickyInfoView.getContentDescription()));
        }

        View transInfoView = recyclerView.findChildViewUnder(
            tvStickyHeaderView.getMeasuredWidth() / 2, tvStickyHeaderView.getMeasuredHeight() + 1);

        if (transInfoView != null && transInfoView.getTag() != null) {
          
          int transViewStatus = (int) transInfoView.getTag();
          int dealtY = transInfoView.getTop() - tvStickyHeaderView.getMeasuredHeight();
          
          if (transViewStatus == StickyExampleAdapter.HAS_STICKY_VIEW) {
            if (transInfoView.getTop() > 0) {
              tvStickyHeaderView.setTranslationY(dealtY);
            } else {
              tvStickyHeaderView.setTranslationY(0);
            }
          } else if (transViewStatus == StickyExampleAdapter.NONE_STICKY_VIEW) {
            tvStickyHeaderView.setTranslationY(0);
          }

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
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
