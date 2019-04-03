package com.hebut.earbook.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.hebut.earbook.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends TTSBaseActivity {

    private static final String TEXT = "You are my life, you are my wife.";

    protected Button mSpeak;
    protected Button mStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTTS();
    }

    @SuppressLint("HandlerLeak")
    private void initView() {
        mSpeak = findViewById(R.id.speak);
        mStop = findViewById(R.id.stop);


        View.OnClickListener listener = v -> {
            int id = v.getId();
            switch (id) {
                case R.id.speak:
                    speak(TEXT);
                    break;
                case R.id.stop:
                    stop();
                    break;
                default:
                    break;
            }
        };
        mSpeak.setOnClickListener(listener);
        mStop.setOnClickListener(listener);

        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    print(msg.obj.toString());
                }
            }

        };

        String filename = System.currentTimeMillis() + ".txt";
        String filepath = saveInternal(filename);
        showToast(filepath);
//        HwTxtPlayActivity.loadTxtFile(this, filepath);
    }

    public String saveInternal(String filename) {
        String fileContent = "最难受的思念，不是对方不知道你的思念，而是他知道却无所谓。有些人，无论你怎么对他好，他也不会留意，因为他的生命里，你显得是多么的微不足道.";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContent.getBytes("GBK"));
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getFilesDir().getAbsolutePath() + File.separator + filename;
    }

    public String getInternal(String filename) {
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream;

        try {
            fileInputStream = openFileInput(filename);
            fileInputStream.read(buffer);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(buffer);
    }


}
