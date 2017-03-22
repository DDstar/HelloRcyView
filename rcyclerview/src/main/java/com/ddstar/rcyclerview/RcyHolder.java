package com.ddstar.rcyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RcyHolder extends RecyclerView.ViewHolder {
    public TextView tvData;
    public View layoutLoading;
    public View layoutNoMore;

    public RcyHolder(View itemView, int viewType) {
        super(itemView);
        if (viewType == RcyAdapter.TYPE_HEADER) {
            tvData = (TextView) itemView.findViewById(R.id.tv_header);
        } else if (viewType == RcyAdapter.TYPE_ITEM) {
            tvData = (TextView) itemView.findViewById(R.id.tv_item);
        } else {
            layoutLoading = itemView.findViewById(R.id.lay_has_more);
            layoutNoMore = itemView.findViewById(R.id.lay_nomore);
        }
    }
}
