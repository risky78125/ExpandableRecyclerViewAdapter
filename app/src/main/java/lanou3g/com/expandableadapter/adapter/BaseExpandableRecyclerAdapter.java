package lanou3g.com.expandableadapter.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Risky57 on 2016/12/28.
 * 显示展开二级列表形式的适配器基类
 * <p>很多属性和方法都与{@link TestMultiAdapter}类似</p>
 *
 * @param <GVH> 组布局的ViewHolder,由子类完成
 * @param <CVH> 子布局的ViewHolder,由子类完成
 */
public abstract class BaseExpandableRecyclerAdapter<GVH extends BaseExpandableRecyclerAdapter.GroupViewHolder,CVH extends BaseExpandableRecyclerAdapter.ChildViewHolder> extends RecyclerView.Adapter implements View.OnClickListener {

    private static final int TYPE_GROUP = 1;
    private static final int TYPE_CHILD = 2;

    private SparseIntArray mTypes;
    private SparseIntArray mGroupPositions;
    private SparseIntArray mChildPositions;
    private int mCount;

    private OnExpandableItemClickListener mOnExpandableItemClickListener;

    public BaseExpandableRecyclerAdapter() {
        mTypes = new SparseIntArray();
        mGroupPositions = new SparseIntArray();
        mChildPositions = new SparseIntArray();
//        calculateCount();
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                calculateCount();
            }
        });

    }

    /**
     * 设置点击监听
     * @param onExpandableItemClickListener
     */
    public void setOnExpandableItemClickListener(OnExpandableItemClickListener onExpandableItemClickListener) {
        mOnExpandableItemClickListener = onExpandableItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mTypes.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GROUP:
//                View itemGroup = mInflater.inflate(R.layout.item_group,null);
//                TestMultiAdapter.GroupViewHolder groupHolder = new TestMultiAdapter.GroupViewHolder(itemGroup);
                return onCreateGroupViewHolder(parent);
//                break;
            case TYPE_CHILD:
//                View itemChild = mInflater.inflate(R.layout.item_child,null);
//                return new TestMultiAdapter.ChildViewHolder(itemChild);
                return onCreateChildViewHolder(parent);
//                break;
        }
        return null;
    }

    /**
     * 基类不清楚该创建什么样式的子布局,定义抽象方法,由子类完成
     * @param parent 同{@link #onCreateViewHolder(ViewGroup, int)}
     * @return 子布局的ViewHolder
     */
    protected abstract CVH onCreateChildViewHolder(ViewGroup parent);

    /**
     * 组布局,参考{@link #onCreateChildViewHolder(ViewGroup)}
     */
    protected abstract GVH onCreateGroupViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        // 根据position来确定当前条数据,在组的集合中的位置,和在子的集合中的位置
        int groupPosition = mGroupPositions.get(position);
        int childPosition = mChildPositions.get(position);

        if (mOnExpandableItemClickListener != null) {
            // 点击事件的设置可以挪到这里
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);
        }

        switch (viewType) {
            case TYPE_GROUP:
                GVH groupViewHolder = (GVH) holder;
//                if (mOnExpandableItemClickListener != null) {
//                    groupViewHolder.itemView.setTag(position);
//                    groupViewHolder.itemView.setOnClickListener(this);
//                }
                onBindGroupViewHolder(groupViewHolder,groupPosition);
                break;
            case TYPE_CHILD:
                CVH childViewHolder = (CVH) holder;
//                if (mOnExpandableItemClickListener != null) {
//                    childViewHolder.itemView.setTag(position);
//                    childViewHolder.itemView.setOnClickListener(this);
//                }
                onBindChildViewHolder(childViewHolder,groupPosition,childPosition);
                break;
        }
    }

    /**
     * 为组布局绑定数据
     * @param groupViewHolder 组布局的ViewHolder,由子类创建
     * @param groupPosition 组布局所对应的集合中的groupPosition
     */
    protected abstract void onBindGroupViewHolder(GVH groupViewHolder, int groupPosition);

    /**
     * 为子布局绑定数据
     * @param childViewHolder 子布局的ViewHolder,子类创建
     * @param groupPosition 子布局所对应集合中的groupPosition
     * @param childPosition 子布局所对应集合中的childPosition
     */
    protected abstract void onBindChildViewHolder(CVH childViewHolder, int groupPosition, int childPosition);


    @Override
    public int getItemCount() {
//        mCount = calculateCount();
        return mCount;
    }

    private void calculateCount() {
        mCount = 0;
        int position = 0;
        for (int i = 0,groupSize = getGroupCount(); i < groupSize; i++) {
            mTypes.put(position,TYPE_GROUP);
            mGroupPositions.put(position,i);
            position++;

            mCount++;
            int childSize = getChildCount(i);
            mCount += childSize;

            // 遍历外层对应i位置的内层集合
            for (int j = 0; j < childSize; j++) {
                mTypes.put(position,TYPE_CHILD);
                mGroupPositions.put(position,i);
                mChildPositions.put(position,j);
                position++;
            }

        }
        Log.d("TestMultiAdapter", "mTypes:" + mTypes);
        Log.d("TestMultiAdapter", "mGroupPositions:" + mGroupPositions);
        Log.d("TestMultiAdapter", "mChildPositions:" + mChildPositions);
    }

    /**
     * 组布局的数量
     * @return
     */
    public abstract int getGroupCount();

    /**
     * 对应groupPosition的子布局的数量
     * @param groupPosition
     * @return
     */
    public abstract int getChildCount(int groupPosition);

    /**
     * 与groupPosition对应的数据元素
     * @param groupPosition
     * @return
     */
    public abstract Object getGroupItem(int groupPosition);

    /**
     * 第groupPosition个组布局中第childPosition个item所对应的集合中的元素
     * @param groupPosition
     * @param childPosition
     * @return
     */
    public abstract Object getChildItem(int groupPosition,int childPosition);

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager){
                final GridLayoutManager manager = (GridLayoutManager) layoutManager;
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return getItemViewType(position) == TYPE_GROUP ? manager.getSpanCount() : 1;
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        // 取出该View对象所对应的位置
        int position = (int) v.getTag();
        // 根据位置判断出来布局类型
        int viewType = getItemViewType(position);
        // 获取集合中对应的组位置和子位置
        int groupPosition = mGroupPositions.get(position);
        int childPosition = mChildPositions.get(position);
        // 调用接口方法
        switch (viewType) {
            case TYPE_GROUP:
                mOnExpandableItemClickListener.onGroupClick(groupPosition);
                break;
            case TYPE_CHILD:
                mOnExpandableItemClickListener.onChildClick(groupPosition,childPosition);
                break;
        }
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{
        public GroupViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder{
        public ChildViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnExpandableItemClickListener{
        void onGroupClick(int groupPosition);
        void onChildClick(int groupPosition,int childPosition);
    }
}
