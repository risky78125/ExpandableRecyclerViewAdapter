package lanou3g.com.expandableadapter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Risky57 on 2016/12/28.
 * <p>
 *     基本适配器,用于演示所使用
 * </p>
 */

public class TestAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;

    public TestAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(android.R.layout.simple_list_item_1,null);
        // 这里没有单独创建自定义的ViewHolder类,系统的ViewHolder为抽象类,
        // 必须使用子类对象,所以后面需要跟上一对{},代表匿名类,
        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(itemView) {};
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText("当前是第" + position + "个Item");
    }

    @Override
    public int getItemCount() {
        return 100;
    }
}
