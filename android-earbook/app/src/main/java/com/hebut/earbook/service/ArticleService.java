package com.hebut.earbook.service;

import com.hebut.earbook.model.ArticleData;
import com.hebut.earbook.model.ArticleList;
import com.hebut.earbook.model.ArticleListItem;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("getArticleList.php")
    Observable<ArticleList> getArticleList(@Query("cate1") String cate1, @Query("pn")int pn, @Query("pl")int pl);

    @GET("getArticle.php")
    Observable<ArticleData> getArticleByAid(@Query("aid") int aid);


//    @FormUrlEncoded
//    @POST("/x3/weather")
//    Call<String> getWeather(@Field("cityId") String cityId, @Field("key") String key);
}
