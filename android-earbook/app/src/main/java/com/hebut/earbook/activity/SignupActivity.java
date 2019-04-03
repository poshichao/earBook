package com.hebut.earbook.activity;

import android.os.Bundle;

import com.dd.processbutton.iml.ActionProcessButton;
import com.hebut.earbook.R;

public class SignupActivity extends BaseActivity {

    private ActionProcessButton btnContinue;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();

    }

    private void initViews() {
        mTopBar = findViewById(R.id.topbar);
        btnContinue = findViewById(R.id.btnContinue);
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mTopBar.setTitle("用户注册");

        btnContinue.setMode(ActionProcessButton.Mode.ENDLESS);

        btnContinue.setOnClickListener(v -> switchActivityTo(new BindActivity()));
    }

}
