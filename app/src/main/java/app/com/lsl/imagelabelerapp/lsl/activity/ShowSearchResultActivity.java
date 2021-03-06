package app.com.lsl.imagelabelerapp.lsl.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;

import app.com.lsl.imagelabelerapp.R;
import app.com.lsl.imagelabelerapp.lsl.activity.menu.TopMenuHeader;
import app.com.lsl.imagelabelerapp.lsl.adapter.PicAdapter;
import app.com.lsl.imagelabelerapp.lsl.utils.StrUtils;

/** 显示搜索图片结果
 * Created by M1308_000 on 2017/6/10.
 */

public class ShowSearchResultActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ShowSearchResultActivity";

    private RecyclerView search_recyclerView;
    private PicAdapter adapter;
    private String filePath;
    private String fileName;
    private int picNum;

    private ArrayList<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");
        fileName = intent.getStringExtra("fileName");
        picNum = intent.getIntExtra("picNum",0);

        thread.start(); // 启动线程加载数据

        initViews();

        initMenu();
    }

    private void initMenu() {
        TopMenuHeader topMenuHeader = new TopMenuHeader(getWindow().getDecorView());
        topMenuHeader.topMenuTitle.setText(fileName);
        topMenuHeader.topMenuTitle.setTextSize(20);
        topMenuHeader.topMenuTitle.setTextColor(Color.parseColor("#33CCB6"));
        topMenuHeader.topMenuLeft.setText("返回");
        topMenuHeader.topMenuLeft.setTextColor(Color.parseColor("#33CCB6"));
        topMenuHeader.topMenuRight.setVisibility(View.GONE);
        topMenuHeader.topMenuLeft.setOnClickListener(this);
        topMenuHeader.ivTopMenuLeft.setOnClickListener(this);
    }

    private void initViews() {
        search_recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        search_recyclerView.setHasFixedSize(true);
        search_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
             do {
                 list = StrUtils.GetPicUrl(fileName, filePath);
                 if (list.size() >= picNum) {
                     Message msg = new Message();
                     msg.arg1 = 1;
                     handler.sendMessage(msg);
                     break;
                 }
            }while(true);

        }
    });

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
                adapter = new PicAdapter();
                search_recyclerView.setAdapter(adapter);
                adapter.replaceAll(list);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_menu_left:
                finish();
                break;
            case R.id.iv_icon:
                finish();
                break;
        }
    }
}
