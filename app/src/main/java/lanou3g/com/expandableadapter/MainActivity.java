package lanou3g.com.expandableadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import lanou3g.com.expandableadapter.adapter.BaseExpandableRecyclerAdapter;
import lanou3g.com.expandableadapter.adapter.TestExAdapter;
import lanou3g.com.expandableadapter.entity.GiftEntity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String URL_GIFT = "http://192.168.31.195/code/gift.json";

    private RecyclerView mRecyclerView;
//    private TestMultiAdapter mAdapter;
    private TestExAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // 布局管理器
        final GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);

//        TestAdapter adapter = new TestAdapter(this);
//        mRecyclerView.setAdapter(adapter);
//        mAdapter = new TestMultiAdapter(this);
        mAdapter = new TestExAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnExpandableItemClickListener(new BaseExpandableRecyclerAdapter.OnExpandableItemClickListener() {
            @Override
            public void onGroupClick(int groupPosition) {
                Toast.makeText(MainActivity.this, "groupPosition:" + mAdapter.getGroupItem(groupPosition), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildClick(int groupPosition, int childPosition) {
                Toast.makeText(MainActivity.this, "(groupPosition,childPosition):" + mAdapter.getChildItem(groupPosition, childPosition), Toast.LENGTH_SHORT).show();
            }
        });


        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(URL_GIFT).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                final GiftEntity gift = gson.fromJson(response.body().string(), GiftEntity.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addAll(gift.getData().getCategories());
                    }
                });

            }
        });


    }
}
