package com.tuodao.mvp_practice.ui.base;

/**
 * Created by hysea on 2018/3/14.
 */

public class BasePresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {
    protected T mView;

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}
