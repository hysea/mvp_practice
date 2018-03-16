package com.tuodao.mvp_practice.ui.news.contract;

import com.tuodao.mvp_practice.bean.NewsDetail;
import com.tuodao.mvp_practice.ui.base.BaseContract;

import java.util.List;

/**
 * Created by hysea on 2018/3/15.
 */

public interface NewsDetailContract {
    public interface View extends BaseContract.BaseView {
        /**
         * 加载顶部Banner数据
         */
        void loadBannerData(NewsDetail newsDetail);

        /**
         * 加载置顶新闻数据
         */
        void loadTopNewsData(NewsDetail newsDetail);

        /**
         * 加载新闻数据
         */
        void loadData(List<NewsDetail.ItemBean> items);

        /**
         * 加载更多数据
         */
        void loadMoreData(List<NewsDetail.ItemBean> items);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取新闻详细信息
         *
         * @param id      频道ID值
         * @param action  用户操作方式
         *                1：下拉 down
         *                2：上拉 up
         *                3：默认 default
         * @param pullNum 操作次数 累加
         */
        void getData(String id, String action, int pullNum);

    }
}
