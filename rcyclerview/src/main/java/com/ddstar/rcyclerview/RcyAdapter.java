
package com.ddstar.rcyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RcyAdapter extends RecyclerView.Adapter<RcyHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_FOOT = 3;
    public boolean hasMore = true;
    private Context context;
    private List<String> mData;

    public RcyAdapter(Context context, List<String> datas) {
        this.context = context;
        this.mData = datas;
    }

    @Override
    public RcyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = null;
        if (viewType == TYPE_HEADER) {
            layout = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
        } else if (viewType == TYPE_ITEM) {
            layout = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false);
        } else {
            layout = LayoutInflater.from(context).inflate(R.layout.progress_layout, parent, false);
        }
        return new RcyHolder(layout, viewType);
    }

    @Override
    public void onBindViewHolder(RcyHolder holder, int position) {
        if (position != 0 && position != mData.size() + 1) {
            holder.tvData.setText(mData.get(position - 1));
        }
        if (position == mData.size() + 1) {
            if (!hasMore) {
                holder.layoutLoading.setVisibility(View.GONE);
                holder.layoutNoMore.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == mData.size() + 1) {
            return TYPE_FOOT;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 2;
    }


    public void addData(List<String> datas) {
        if (datas == null || datas.size() <= 0) {
//没数据显示没有更多
            hasMore = false;
            notifyDataSetChanged();
        } else {
            //有数据添加进来
            mData.addAll(datas);
            notifyItemInserted(mData.size() + 1);
        }
    }

    public void addItem(RecyclerView.ViewHolder viewHolder) {
        mData.add(viewHolder.getLayoutPosition(), "我是新加的~~");
        notifyItemInserted(viewHolder.getLayoutPosition());
    }


    public void removeItem(RecyclerView.ViewHolder viewHolder) {
        mData.remove(viewHolder.getLayoutPosition() - 1);
        notifyItemRemoved(viewHolder.getLayoutPosition());
    }
}
