package com.tuodao.mvp_practice.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.tuodao.mvp_practice.App;
import com.tuodao.mvp_practice.R;
import com.tuodao.mvp_practice.ui.inter.IBase;
import com.tuodao.mvp_practice.widget.MultiStateView;
import com.tuodao.mvp_practice.widget.SimpleMultiStateView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hysea on 2018/3/14.
 */

public abstract class BaseFragment<T extends BaseContract.BasePresenter> extends SupportFragment implements IBase, BaseContract.BaseView {
    @Nullable
    @BindView(R.id.multi_state_view)
    SimpleMultiStateView mMultiStateView;

    @Nullable
    @Inject
    protected T mPresenter;

    protected View mRootView;
    protected Context mContext;
    private Unbinder mUnbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = createView(inflater, container, savedInstanceState);
        }
        return mRootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentLayout(), viewGroup, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector(App.getInstance().getAppComponent());
        attachView();
        bindView(view, savedInstanceState);
        initMultiStateView();
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Nullable
    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();
    }

    protected void initMultiStateView() {
        if (mMultiStateView == null) return;
        mMultiStateView.setEmptyResource(R.layout.layout_view_empty)
                .setRetryResource(R.layout.layout_view_retry)
                .setLoadingResource(R.layout.layout_view_loading)
                .setNoNetworkResource(R.layout.layout_view_no_network)
                .build()
                .setOnReloadListener(new MultiStateView.OnReloadListener() {
                    @Override
                    public void onReload() {
                        onRetry();
                    }
                });
    }

    public SimpleMultiStateView getMultiStateView() {
        return mMultiStateView;
    }

    @Override
    public void showSuccess() {
        if (mMultiStateView != null) {
            mMultiStateView.showContent();
        }
    }

    @Override
    public void showLoading() {
        if (mMultiStateView != null) {
            mMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showFailed() {
        if (mMultiStateView != null) {
            mMultiStateView.showErrorView();
        }
    }

    @Override
    public void showNotNetwork() {
        if (mMultiStateView != null) {
            mMultiStateView.showNoNetworkView();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

}
