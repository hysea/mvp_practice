package com.tuodao.mvp_practice.ui.base;

/**
 * Created by hysea on 2018/3/13.
 */

public interface BaseContract {
    interface BaseView {
        // 显示请求成功
        void showSuceess();

        // 显示请求失败
        void showFailed();

        // 显示网络不可用
        void showNotNetwork();

        // 重试
        void onRetry();
    }

    interface BasePresenter<T extends BaseView> {
        void attachView(T view);
        void detachView();
    }
}
