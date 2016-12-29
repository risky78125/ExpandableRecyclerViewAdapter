package lanou3g.com.expandableadapter.adapter;

import android.content.Context;
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
 * <p>基类的具体使用方式</p>
 */

public class TestExAdapter extends BaseExpandableRecyclerAdapter<TestExAdapter.GroupHolder,TestExAdapter.ChildHolder> {

    private List<GiftEntity.DataBean.CategoriesBean> categories;
    private LayoutInflater mInflater;
    private Context mContext;

    public TestExAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        categories = new ArrayList<>();
    }

    public void addAll(List<GiftEntity.DataBean.CategoriesBean> categories){
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    protected ChildHolder onCreateChildViewHolder(ViewGroup parent) {
        return new ChildHolder(mInflater.inflate(R.layout.item_child,null));
    }

    @Override
    protected GroupHolder onCreateGroupViewHolder(ViewGroup parent) {
        return new GroupHolder(mInflater.inflate(R.layout.item_group,null));
    }

    @Override
    protected void onBindGroupViewHolder(GroupHolder groupViewHolder, int groupPosition) {
        GiftEntity.DataBean.CategoriesBean groupBean = categories.get(groupPosition);
        groupViewHolder.textName.setText(groupBean.getName());
    }

    @Override
    protected void onBindChildViewHolder(ChildHolder childViewHolder, int groupPosition, int childPosition) {
        GiftEntity.DataBean.CategoriesBean.SubcategoriesBean childBean = categories.get(groupPosition).getSubcategories().get(childPosition);
        childViewHolder.textName.setText(childBean.getName());
        Glide.with(mContext).load(childBean.getIcon_url()).into(childViewHolder.imgIcon);
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return categories.get(groupPosition).getSubcategories().size();
    }

    @Override
    public Object getGroupItem(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChildItem(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getSubcategories().get(childPosition);
    }

    public static class GroupHolder extends BaseExpandableRecyclerAdapter.GroupViewHolder{

        private TextView textName;
        public GroupHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_group_name);
        }
    }

    public static class ChildHolder extends BaseExpandableRecyclerAdapter.ChildViewHolder{

        private TextView textName;
        private ImageView imgIcon;
        public ChildHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }
}
