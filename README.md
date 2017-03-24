# 这是什么
这是一个针对Recycleview的研究Demo，可能有一些很简单的示例，也可能会有复杂的定制

那我们后面再来慢慢定制吧

###  功能1 实现基本数据列表显示

   基本步骤

   * 设置layoutManager

   ```
   mRcyView.setLayoutManager(new LinearLayoutManager(this));
   ```

   * 设置adapter，不过adapter要求很多
   ```
 mRcyView.setAdapter(new RcyAdapter());
   ```
    * 首先要继承recyclerView的Adapter

    * 要有一个实现recyclerView的ViewHolder的ViewHolder

```
 class RcyHolder extends RecyclerView.ViewHolder
 class RcyAdapter extends RecyclerView.Adapter<RcyHolder>
```
就是酱紫~~

* 重写此方法返回recyclerView的itemType，这边有几个item类型就可以写几个
```
  @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }
```

* 创建ViewHolder时实例化Item的View
```
@Override
        public RcyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View viewHeader = getLayoutInflater().inflate(R.layout.item_header, parent,false);
                return new RcyHolder(viewHeader, viewType);
            } else {
                View viewData = getLayoutInflater().inflate(R.layout.item_data, parent,false);
                return new RcyHolder(viewData, viewType);
            }
        }
```

*小插曲一首*

实例化Item的View时以前都是
```
View viewHeader = getLayoutInflater().inflate(R.layout.item_header, null,false);
```
酱紫的话Item宽度是没办法充满整个屏幕
所以要这么搞
```
View viewHeader = getLayoutInflater().inflate(R.layout.item_header, parent,false);
```
patent要传进去才行

### 功能2 自定义给RecyclerView添加itemClick和ItemLongClick

怎么定制呢？我们从RecyclerView的addOnItemToucherListener这个方法入手
此方法传入的OnItemTouchListener是RecyclerView的一个接口
```
 public static interface OnItemTouchListener {
        /**
         * Silently observe and/or take over touch events sent to the RecyclerView
         * before they are handled by either the RecyclerView itself or its child views.
         *
         * <p>The onInterceptTouchEvent methods of each attached OnItemTouchListener will be run
         * in the order in which each listener was added, before any other touch processing
         * by the RecyclerView itself or child views occurs.</p>
         *
         * @param e MotionEvent describing the touch event. All coordinates are in
         *          the RecyclerView's coordinate system.
         * @return true if this OnItemTouchListener wishes to begin intercepting touch events, false
         *         to continue with the current behavior and continue observing future events in
         *         the gesture.
         */
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e);

        /**
         * Process a touch event as part of a gesture that was claimed by returning true from
         * a previous call to {@link #onInterceptTouchEvent}.
         *
         * @param e MotionEvent describing the touch event. All coordinates are in
         *          the RecyclerView's coordinate system.
         */
        public void onTouchEvent(RecyclerView rv, MotionEvent e);

        /**
         * Called when a child of RecyclerView does not want RecyclerView and its ancestors to
         * intercept touch events with
         * {@link ViewGroup#onInterceptTouchEvent(MotionEvent)}.
         *
         * @param disallowIntercept True if the child does not want the parent to
         *            intercept touch events.
         * @see ViewParent#requestDisallowInterceptTouchEvent(boolean)
         */
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept);
    }
```

当然我们如果自定义一个监听的话可以直接实现这个接口，并全部实现所有的抽象方法，但是我们实际上需要实现的方法就一个
这样我们就得有一个思考，怎样才能做到只要实现其中的一两个方法就好了呢？其实谷歌工程师已经帮我们想好了。

RecyclerView内部有一个实现该接口的类，叫

```
 public static class SimpleOnItemTouchListener implements RecyclerView.OnItemTouchListener {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
```
它就实现了这个OnTouchListener，但是所有的方法都是空实现，所以我们自己定义的类只要继承这个类，并且重写我们需要的方法就可以了

先贴代码再说明
```
public abstract class RcyItemClickerListener extends RecyclerView.SimpleOnItemTouchListener {

    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;

    public RcyItemClickerListener(final RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            //接触到的屏幕的坐标返回给 MotionEvent
            //通过MotionEvent获取到当前接触到的屏幕的坐标
            //Recyvlerview根据坐标获取到当前的childview
            //再根据childview获取到当前childView的holder，然后就可以处理了
            @Override
            public void onLongPress(MotionEvent e) {//长按

                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(childView);
                    onItemLongClick(viewHolder);
                }
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {//单击
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(childView);
                    onItemClick(viewHolder);
                }
                return true;
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh);

    public abstract void onItemLongClick(RecyclerView.ViewHolder vh);
}
```

代码也不长，简单看一下

首先继承RecyclerView的SimpleOnItemTouchListener，并重写我们需要的方法，那我们需要重写那个方法呢？
我们重写的方法是要接管用户点击到屏幕时的处理，SimpleOnItemTouchListener对此方法是空处理，所以我们需要的就是重写此方法
即
```
@Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }
```
怎么处理是单击还是长按呢？我们就想到用GestureDetectorCompat类来解析用户的操作，创建该类的实例需要穿一个OnGestureListener，同样的，该方法里面我们需要实现的方法就两个，但又有这么多方法，怎么办呢？
没错，他也有一个空的实现类SimpleOnGestureListener，所以我们只需要新建一个这个类的实例，然后重写我们需要的方法就好了
在我们自定义类的构造方法里面实例化mGestureDetectorCompat，在构造方法里面传入GestureDetector.SimpleOnGestureListener
并且重写他的两个方法
```
 @Override
            public void onLongPress(MotionEvent e) {//长按


            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {//单击

                return true;
            }
```
注意到我们的自定义类是抽象类，里面有两个抽象方法
```
 public abstract void onItemClick(RecyclerView.ViewHolder vh);

    public abstract void onItemLongClick(RecyclerView.ViewHolder vh);
```
这两个方法是在Activity使用到Item的点击时的回调，那这两个方法在哪里调用呢？就是在mGestureDetectorCompat的onLongPress和onSingleTapUp方法
最后，通过用户点击屏幕回调的MotionEvent获取到点击的坐标，再让RecyclerView根据坐标获取到点击到的Item，最后调用相应的点击事件就ok了

MainActivity的使用

```
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
```

### 功能3 RecyclerView添加Item的增加删除动画
### 功能4 RecyclerView添加上拉加载更多
先上几张图吧
![展示图1](https://github.com/DDstar/HelloRcyView/Pictures/p1.jpg)
