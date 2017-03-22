
package org.ddstar.hellorcyview;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ddstar.rcyclerview.OnLoadMoreListener;
import com.ddstar.rcyclerview.RcyAdapter;
import com.ddstar.rcyclerview.RcyItemClickerListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRcyView;
    private List<String> mData = new ArrayList<>();
    private int times = 0;

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
        mRcyView.setLayoutManager(new LinearLayoutManager(this));
//        mRcyView.setLayoutManager(new GridLayoutManager(this, 4));
        mRcyView.setItemAnimator(new DefaultItemAnimator());
        final RcyAdapter adapter = new RcyAdapter(this, mData);
        mRcyView.setAdapter(adapter);
        mRcyView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int curPage) {
                Log.e("addOnScrollListener", "onLoadMore");
                times++;
                mRcyView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> datas = new ArrayList<String>();
                        datas.add("我");
                        datas.add("们");
                        datas.add("是");
                        datas.add("后");
                        datas.add("面");
                        datas.add("来");
                        datas.add("的");
                        if (times <= 3) {
                            adapter.addData(datas);
                        } else {
                            adapter.addData(null);
                        }
                    }
                }, 1000);

            }
        });
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
}