package lanou3g.com.expandableadapter.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import lanou3g.com.expandableadapter.R;
import lanou3g.com.expandableadapter.entity.GiftEntity;

/**
 * Created by Risky57 on 2016/12/28.
 * 显示不同行布局的适配器,未封装
 */

public class TestMultiAdapter extends RecyclerView.Adapter {

    /**
     * 组的布局类型
     */
    private static final int TYPE_GROUP = 1;
    /**
     * 子的布局类型
     */
    private static final int TYPE_CHILD = 2;

    /**
     * 适配器中需要显示的数据集合
     */
    private List<GiftEntity.DataBean.CategoriesBean> categories;
    /**
     * 布局加载器,用来将布局文件加载成View对象
     */
    private LayoutInflater mInflater;
    private Context mContext;

    /**
     * 用来存储第几个item是哪种数据类型
     * key为position,value为type
     */
    private SparseIntArray mTypes;
    /**
     * 用来记录第几个item所对应数据集合外层数据中第几项
     */
    private SparseIntArray mGroupPositions;
    /**
     * 用来记录第几个item所对应数据集合内层数据中第几项
     */
    private SparseIntArray mChildPositions;
    /**
     * RecyclerView需要显示的item总数量
     */
    private int mCount;

    public TestMultiAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        categories = new ArrayList<>();
        mTypes = new SparseIntArray();
        mGroupPositions = new SparseIntArray();
        mChildPositions = new SparseIntArray();
//        calculateCount();
        // 将计算总数量的方法注册成观察者,
        // 这样调用notifyDataSetChanged()方法时就会重新计算item总数量
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                calculateCount();
            }
        });

    }

    /**
     * 向Adapter中添加数据,并显示
     * @param categories
     */
    public void addAll(List<GiftEntity.DataBean.CategoriesBean> categories){
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // 用来确定第position个item属于哪种布局类型
        return mTypes.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GROUP:
                // 加载组的布局
                View itemGroup = mInflater.inflate(R.layout.item_group,null);
                GroupViewHolder groupHolder = new GroupViewHolder(itemGroup);
                return groupHolder;
//                break;
            case TYPE_CHILD:
                // 加载子的布局
                View itemChild = mInflater.inflate(R.layout.item_child,null);
                return new ChildViewHolder(itemChild);
//                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 先判断第position位置的布局类型是什么
        int viewType = getItemViewType(position);
        // 根据position来确定当前条数据,在组的集合中的位置,和在子的集合中的位置
        int groupPosition = mGroupPositions.get(position);
        int childPosition = mChildPositions.get(position);
        switch (viewType) {
            case TYPE_GROUP:
                // 为组布局绑定数据
                GiftEntity.DataBean.CategoriesBean groupBean = categories.get(groupPosition);
                GroupViewHolder groupHolder = (GroupViewHolder) holder;
                groupHolder.textName.setText(groupBean.getName());
                break;
            case TYPE_CHILD:
                // 为子布局绑定数据
                GiftEntity.DataBean.CategoriesBean.SubcategoriesBean childBean = categories.get(groupPosition).getSubcategories().get(childPosition);
                ChildViewHolder childHolder = (ChildViewHolder) holder;
                childHolder.textName.setText(childBean.getName());
                Glide.with(mContext).load(childBean.getIcon_url()).into(childHolder.imgIcon);
                break;
        }
    }

    @Override
    public int getItemCount() {
//        mCount = calculateCount();
        return mCount;
    }

    /**
     * 用来计算总的item数量为多少,
     * 并初始化好三个集合
     */
    private void calculateCount() {
        mCount = 0;
        int position = 0;
        for (int i = 0,groupSize = categories.size(); i < groupSize; i++) {
            mTypes.put(position,TYPE_GROUP);
            mGroupPositions.put(position,i);
            position++;

            mCount++;
            GiftEntity.DataBean.CategoriesBean categoriesBean = categories.get(i);
            int childSize = categoriesBean.getSubcategories().size();
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
     * 当为RecyclerView设置适配器时该方法会调用
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // 获取RecyclerView的LayoutManager
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager){
                // 将LayoutManger强转为GridLayoutManager
                final GridLayoutManager manager = (GridLayoutManager) layoutManager;
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        // 让组的布局类型跨列显示
                        return getItemViewType(position) == TYPE_GROUP ? manager.getSpanCount() : 1;
                    }
                });
            }
        }
    }

    /**
     * 组布局的ViewHolder
     */
    private class GroupViewHolder extends RecyclerView.ViewHolder{
        private TextView textName;
        public GroupViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_group_name);
        }
    }

    /**
     * 子布局的ViewHolder
     */
    private class ChildViewHolder extends RecyclerView.ViewHolder{
        private TextView textName;
        private ImageView imgIcon;
        public ChildViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }
}
