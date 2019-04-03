package com.hebut.earbook.activity;

import android.media.AudioManager;
import android.os.Handler;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.hebut.earbook.common.Constants;
import com.hebut.earbook.listener.UiMessageListener;

public class TTSBaseActivity extends BaseActivity{
    private TtsMode TTS_MODE = TtsMode.ONLINE;
    private SpeechSynthesizer mSpeechSynthesizer;

    protected Handler mainHandler;

    /**
     * 注意此处为了说明流程，故意在UI线程中调用。
     * 实际集成中，该方法一定在新线程中调用，并且该线程不能结束。具体可以参考NonBlockSyntherizer的写法
     */
    protected void initTTS() {
        LoggerProxy.printable(true); // 日志打印在logcat中

        // 日志更新在UI中，可以换成MessageListener，在logcat中查看日志
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        // 1. 获取实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this);

        // 2. 设置listener
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);

        // 3. 设置appId，appKey.secretKey
        checkResult(mSpeechSynthesizer.setAppId(Constants.BAIDU_TTS_APP_ID), "setAppId");
        checkResult(mSpeechSynthesizer.setApiKey(Constants.BAIDU_TTS_APP_KEY,
                Constants.BAIDU_TTS_SECRET_KEY), "setApiKey");

        // 5. 以下setParam 参数选填。不填写则默认值生效
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        // 各种网络下优先在线，基本不打算使用离线功能
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE,
                SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);

        // 语音的播放方式，这里是按照音乐的方式来播放
        mSpeechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // 初始化语音合成模式
        checkResult(mSpeechSynthesizer.initTts(TTS_MODE), "initTts");

    }

    protected void speak(String text) {
        if (mSpeechSynthesizer == null) {
            print("[ERROR], 初始化失败");
            return;
        }
        int result = mSpeechSynthesizer.speak(text);
        print("合成并播放 按钮已经点击");
        checkResult(result, "speak");
    }

    protected void stop() {
        print("停止合成引擎 按钮已经点击");
        int result = mSpeechSynthesizer.stop();
        checkResult(result, "stop");
    }





    @Override
    protected void onDestroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
            mSpeechSynthesizer.release();
            mSpeechSynthesizer = null;
            print("释放资源成功");
        }
        super.onDestroy();
    }

}
