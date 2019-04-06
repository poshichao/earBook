package com.hebut.earbook.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.hebut.earbook.R;
import com.hebut.earbook.controller.ArticleLoader;
import com.hebut.earbook.model.Article;
import com.hebut.earbook.util.ResourceUtil;
import com.hebut.earbook.util.StringUtil;
import com.hebut.earbook.util.rx.OnlyNextObserver;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.Objects;


public class ArticleActivity extends BaseActivity implements ObservableScrollViewCallbacks,Player.EventListener {
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private TextView mContentView;
    private ImageView mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private TextView mTitleView;
    private FloatingActionButton mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;
    private ArticleLoader mArticleLoader;
    private String articleContent;

    private ConcatenatingMediaSource concatenatedSource;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        initViews();

        // 获取文章
        int aid = Objects.requireNonNull(getIntent().getExtras()).getInt("aid");
        getArticleByAid(aid);
    }

    /**
     * 通过文章id获取文章内容
     * 并设置
     * @param aid 文章id
     */
    public void getArticleByAid(int aid) {
        mArticleLoader.getArticleByAid(aid).subscribe(new OnlyNextObserver<Article>() {
            @Override
            public void onNext(Article article) {
                articleContent = article.content;
                initPlayer();

                mContentView.setText(articleContent);
                mTitleView.setText(article.title);
                if (article.img != null && !article.img.trim().isEmpty()) {
                    Glide.with(ArticleActivity.this).load(article.img).into(mImageView);
                } else {
                    mImageView.setImageDrawable(ResourceUtil.getDefaultImage(
                            ArticleActivity.this, article.cate1));
                }

            }
        });
    }

    // 初始化播放器
    public void initPlayer() {
        concatenatedSource = new ConcatenatingMediaSource();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(ArticleActivity.this,
                Util.getUserAgent(ArticleActivity.this, "EarBook"));

        // 按512字节分割待朗读文本，并分别请求接口转换为MP3格式，
        // 并通过concatenatedSource对象加入无缝播放列表
        for (String str : StringUtil.getStrList(articleContent, 512)) {
            concatenatedSource.addMediaSource(new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse("http://pre.mingdocs.cn/tts.php?text=" + str)));
        }

        player = ExoPlayerFactory.newSimpleInstance(ArticleActivity.this);
    }


    public void initViews() {

        mArticleLoader = new ArticleLoader();
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();

        mContentView = findViewById(R.id.tv_content);
        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        mScrollView = findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView = findViewById(R.id.title);
        mTitleView.setText(getTitle());
        setTitle(null);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(v -> {
            // 获取当前播放状态，如果正在播放那么就停止播放
            Log.e("TAG", String.valueOf(player.getPlaybackState()));

            if (player.getPlayWhenReady()) {
                player.setPlayWhenReady(false);
                mFab.setImageResource(R.drawable.play);
            } else {
                // 如果未处于播放状态则判断播放器是否有文件可以播放或者是否已经播放结束
                if (player.getPlaybackState() == Player.STATE_IDLE || player.getPlaybackState() == Player.STATE_ENDED) {
                    // 没有就准备资源
                    player.prepare(concatenatedSource);
                }
                // 开始播放
                player.setPlayWhenReady(true);
                mFab.setImageResource(R.drawable.pause);
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, () -> {
            mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);
        });

    }

    // 监听页面滚动事件
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);

        ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
        ViewHelper.setTranslationY(mFab, fabTranslationY);

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    // 监听播放事件
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // 当播放结束时将浮动按钮的图标设置为播放图标
        if(playbackState == Player.STATE_ENDED || playbackState == Player.STATE_IDLE){
            mFab.setImageResource(R.drawable.play);
        }
    }

    // 释放播放器对象
    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }
}
