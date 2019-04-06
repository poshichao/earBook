package com.hebut.earbook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.dd.processbutton.iml.ActionProcessButton;
import com.hebut.earbook.R;
import com.qmuiteam.qmui.util.QMUIViewHelper;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ActionProcessButton btnSignIn;
    private BootstrapButton btnSignUp;

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

    }

    private void initViews() {
        mTopBar = findViewById(R.id.topbar);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        mTopBar.setTitle("用户登录");
        mTopBar.addRightTextButton("刷脸登录", QMUIViewHelper.generateViewId())
                .setOnClickListener(v -> {
                    activeEngine();
                    Intent intent = new Intent(LoginActivity.this, RecognizeActivity.class);
                    startActivity(intent);
                });

        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);



        etUsername.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                signIn();
                break;
            case R.id.btnSignUp:
                switchActivityTo(new SignupActivity());
                break;
        }
    }

    public void signIn() {
        btnSignIn.setProgress(50);
        btnSignIn.setLoadingText("正在登录...");

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(username.isEmpty()){
            btnSignIn.setErrorText("请输入账号");
            btnSignIn.setProgress(-1);
            return;
        }

        if(password.isEmpty()){
            btnSignIn.setErrorText("请输入密码");
            btnSignIn.setProgress(-1);
            return;
        }



    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btnSignIn.setProgress(0);
    }
}
