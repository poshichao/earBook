package com.hebut.earbook.view.widget.material;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hebut.earbook.R;
import com.hebut.earbook.controller.ArticleLoader;
import com.hebut.earbook.model.ArticleListItem;
import com.hebut.earbook.util.ResourceUtil;
import com.hebut.earbook.util.rx.OnlyNextObserver;
import com.hebut.earbook.view.activity.ArticleActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class TestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ArticleListItem> mArticles;
    private ArticleLoader mArticleLoader;
    private String category;
    private int updateBeginIndex;
    private int updateLength;

    /**
     *
     * @param cate 文章分类
     * @param index 文章列表的请求起始编号
     * @param length 文章列表的请求长度
     */
    public TestRecyclerViewAdapter(String cate, int index, int length) {
        category = cate;
        updateBeginIndex = index;
        updateLength = length;
        mArticles = new ArrayList<>();
        mArticleLoader = new ArticleLoader();
    }

    /**
     * 将新的文章列表项全部添加到当前的文章列表的后面
     * @param articles 文章列表
     */
    public void setArticles(List<ArticleListItem> articles) {
        mArticles.addAll(articles);
    }

    // 获取当前的列表项个数
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    // 每创建一个列表项时调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ArticleHolder(LayoutInflater.from(context).inflate(R.layout.list_item_card_big, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticleHolder articleHolder = (ArticleHolder) holder;

        // 绑定列表卡片的点击事件
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleActivity.class);
            // 在intent参数中添加文章编号aid的值
            intent.putExtra("aid", mArticles.get(position).aid);
            context.startActivity(intent);
        });

        // 填充卡片内容，根据当前页面的分类获取默认图片并传入到Viewholder的filldata方法中
        articleHolder.fillData(context, mArticles.get(position), ResourceUtil.getDefaultImage(context,category));

        // 当显示的为当前列表的最后一个元素时更新请求文章列表的开始序号，并使用新的
        // 参数再次请求文章列表，添加到原列表的后面
        if (position == getItemCount() - 1) {
            updateBeginIndex = getItemCount();
            getArticleList();
        }
    }


    // 使用当前成员变量向服务器请求文章列表
    public void getArticleList() {
        mArticleLoader.getArticle(category, updateBeginIndex, updateLength).subscribe(new OnlyNextObserver<List<ArticleListItem>>() {
            @Override
            public void onNext(List<ArticleListItem> articleListItems) {
                setArticles(articleListItems);
                notifyDataSetChanged();
            }
        });

    }


    public static class ArticleHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView title;
        public TextView subTitle;
        public TextView time;

        public ArticleHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.article_image);
            title = itemView.findViewById(R.id.article_title);
            subTitle = itemView.findViewById(R.id.article_cate);
            time = itemView.findViewById(R.id.article_time);
        }

        public void fillData(Context context, ArticleListItem article, Drawable defaultImg) {
            // 如果当前文章的img链接不为空就通过glide进行加载，否则设置为但当前类别的默认图片
            if (article.img != null && !article.img.trim().isEmpty()) {
                Glide.with(context).load(article.img).into(mImageView);
            } else {
                mImageView.setImageDrawable(defaultImg);
            }

            time.setText(article.time);
            title.setText(article.title);
            subTitle.setText(article.cate2);
        }
    }
}