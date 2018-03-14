package com.tuodao.mvp_practice.net;

import com.tuodao.mvp_practice.bean.NewsArticleBean;
import com.tuodao.mvp_practice.bean.NewsCmppVideoBean;
import com.tuodao.mvp_practice.bean.NewsDetail;
import com.tuodao.mvp_practice.bean.NewsImagesBean;
import com.tuodao.mvp_practice.bean.VideoChannelBean;
import com.tuodao.mvp_practice.bean.VideoDetailBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
public interface NewsApiService {

    @GET("ClientNews")
    Observable<List<NewsDetail>> getNewsDetail(@Query("id") String id,
                                               @Query("action") String action,
                                               @Query("pullNum") int pullNum
    );

    @GET("api_vampire_article_detail")
    Observable<NewsArticleBean> getNewsArticleWithSub(@Query("aid") String aid);

    @GET
    Observable<NewsArticleBean> getNewsArticleWithCmpp(@Url String url,
                                                       @Query("aid") String aid);

    @GET
    Observable<NewsImagesBean> getNewsImagesWithCmpp(@Url String url,
                                                     @Query("aid") String aid);

    @GET("NewRelativeVideoList")
    Observable<NewsCmppVideoBean> getNewsVideoWithCmpp(@Url String url,
                                                       @Query("guid") String guid);

    @GET("ifengvideoList")
    Observable<List<VideoChannelBean>> getVideoChannel(@Query("page") int page);

    @GET("ifengvideoList")
    Observable<List<VideoDetailBean>> getVideoDetail(@Query("page") int page,
                                                     @Query("listtype") String listtype,
                                                     @Query("typeid") String typeid);


}
