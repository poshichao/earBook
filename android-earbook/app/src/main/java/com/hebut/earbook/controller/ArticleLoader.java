package com.hebut.earbook.controller;

import android.util.Log;

import com.hebut.earbook.model.Article;
import com.hebut.earbook.model.ArticleListItem;
import com.hebut.earbook.service.ArticleService;
import com.hebut.earbook.util.http.RetrofitServiceManager;

import java.util.List;

import io.reactivex.Observable;

public class ArticleLoader extends ObjectLoader {
    private ArticleService mArticleService;

    public ArticleLoader() {
        mArticleService = RetrofitServiceManager.getInstance().create(ArticleService.class);
    }

    /**
     * @param cate 文章分类
     * @param pn   分页起始位置
     * @param pl   分页长度
     * @return 文章列表
     */
    public Observable<List<ArticleListItem>> getArticle(String cate, int pn, int pl) {
        return observe(mArticleService.getArticleList(cate, pn, pl))
                .map(articleList -> {
                    Log.e("MOBSE", String.valueOf(articleList.status));
                    Log.e("MOBSE", String.valueOf(articleList.msg));

                    return articleList.data;
                });
    }


    /**
     *
     * @param aid 文章编号
     * @return 对应编号的文章
     */
    public Observable<Article> getArticleByAid(int aid) {
        return observe(mArticleService.getArticleByAid(aid))
                .map(articleData -> {
                    Log.e("MOBSE", String.valueOf(articleData.status));
                    Log.e("MOBSE", String.valueOf(articleData.msg));

                    return articleData.data;
                });
    }

}