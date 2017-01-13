package org.ddstar.hellorcyview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRcyView;
    private List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRcyView = (RecyclerView) findViewById(R.id.rcy_view);
        for (int i = 0; i < 20; i++) {
            mData.add("我是第" + i + "个 item");
        }
        initRcyView();
    }

    private void initRcyView() {
//        mRcyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRcyView.setLayoutManager(new GridLayoutManager(this, 4));
        mRcyView.setItemAnimator(new DefaultItemAnimator());
        final RcyAdapter adapter = new RcyAdapter();
        mRcyView.setAdapter(adapter);
        mRcyView.addOnItemTouchListener(new RcyItemClickerListener(mRcyView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() == 0) {
                    adapter.addItem(vh);
                } else {
                    adapter.removeItem(vh);
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                adapter.removeItem(vh);
            }
        });
    }

    class RcyHolder extends RecyclerView.ViewHolder {
        public TextView tvData;

        public RcyHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == RcyAdapter.TYPE_HEADER) {
                tvData = (TextView) itemView.findViewById(R.id.tv_header);
            } else {
                tvData = (TextView) itemView.findViewById(R.id.tv_item);
            }
        }
    }


    class RcyAdapter extends RecyclerView.Adapter<RcyHolder> {
        public static final int TYPE_HEADER = 1;
        public static final int TYPE_ITEM = 2;

        @Override
        public RcyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View viewHeader = getLayoutInflater().inflate(R.layout.item_header, parent, false);
                return new RcyHolder(viewHeader, viewType);
            } else {
                View viewData = getLayoutInflater().inflate(R.layout.item_data, parent, false);
                return new RcyHolder(viewData, viewType);
            }
        }

        @Override
        public void onBindViewHolder(RcyHolder holder, int position) {
            if (position != 0) {
                holder.tvData.setText(mData.get(position - 1));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getItemCount() {
            return mData.size() + 1;
        }

        public void addItem(int posotion) {
            mData.add(posotion, "我是新加的~~");
            notifyItemInserted(posotion);
        }

        public void addItem(RecyclerView.ViewHolder viewHolder) {
            mData.add(viewHolder.getLayoutPosition(), "我是新加的~~");
//            notifyItemChanged(viewHolder.getLayoutPosition());
            notifyItemInserted(viewHolder.getLayoutPosition());
        }

        public void removeItem(int position) {
            mData.remove(position);
            notifyItemRemoved(position);
        }

        public void removeItem(RecyclerView.ViewHolder viewHolder) {
            mData.remove(viewHolder.getLayoutPosition() - 1);
            notifyItemRemoved(viewHolder.getLayoutPosition());
        }
    }


}
