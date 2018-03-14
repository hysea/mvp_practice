package com.tuodao.mvp_practice.ui.news.contract;

import com.tuodao.mvp_practice.bean.Channel;
import com.tuodao.mvp_practice.ui.base.BaseContract;

import java.util.List;

/**
 * Created by hysea on 2018/3/14.
 */

public class NewsContract {
    public interface View extends BaseContract.BaseView {
        void loadData(List<Channel> channels, List<Channel> otherChannels);
    }

    public interface Presenter extends BaseContract.BasePresenter<View> {
        /**
         * 初始化频道
         */
        void getChannel();
    }
}
