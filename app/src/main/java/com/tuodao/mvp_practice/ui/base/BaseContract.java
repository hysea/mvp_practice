package com.tuodao.mvp_practice.ui.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface BaseContract {
    interface BaseView {
        // 显示加载进度
        void showLoading();

        // 显示请求成功
        void showSuccess();

        // 显示请求失败
        void showFailed();

        // 显示网络不可用
        void showNotNetwork();

        // 重试
        void onRetry();

        // 绑定生命周期
        <T> LifecycleTransformer<T> bindToLife();
    }

    interface BasePresenter<T extends BaseView> {
        void attachView(T view);

        void detachView();
    }
}
