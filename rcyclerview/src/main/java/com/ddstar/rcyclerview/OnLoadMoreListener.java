package com.ddstar.rcyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/3/22.
 */

public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = true;
    private int tempTotalCount = 0;
    private int curPage = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RcyAdapter adapter = (RcyAdapter) recyclerView.getAdapter();
        if (!adapter.hasMore)//跟Adapter那边联动，当没有更多数据时就不在调用loadMore方法
            return;
        RecyclerView.LayoutManager rcyManager = recyclerView.getLayoutManager();
        if (rcyManager instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) rcyManager;
        }
        int visiableItemCount = recyclerView.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisiableItem = linearLayoutManager.findFirstVisibleItemPosition();
        if (isLoading) {//已经在loading了
            if (totalItemCount > tempTotalCount) {
                isLoading = false;
                tempTotalCount = totalItemCount;
            }
        }
        if (!isLoading && totalItemCount - visiableItemCount <= firstVisiableItem) {
            curPage++;
            onLoadMore(curPage);
            isLoading = true;
        }

    }

    public abstract void onLoadMore(int curPage);
}
