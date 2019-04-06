package com.hebut.earbook.view.activity;

import android.os.Bundle;

import com.hebut.earbook.R;

public class BindActivity extends ARCBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        // 初始化人脸注册的相关view
        initArcViews();

        mTopBar = findViewById(R.id.topbar);
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mTopBar.setTitle("绑定人脸");

        btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(v -> {
            btnFinish.setProgress(50);
            register();
        });
    }


}
