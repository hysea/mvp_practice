package com.tuodao.mvp_practice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;

import com.tuodao.mvp_practice.R;

/**
 * Created by hysea on 2018/3/14.
 */

public class SimpleMultiStateView extends MultiStateView {
    private static final int MIN_SHOW_TIME = 400; // ms
    private static final int MIN_DELAY = 600; // ms

    private int mTargetState = -1;
    private long mLoadingStartTime = -1;


    int resIdEmpty;
    int resIdLoading;
    int resIdFailed;
    int resIdNoNetwork;

    private final Runnable mLoadingHide = new Runnable() {
        @Override
        public void run() {
            setViewState(mTargetState);
            mLoadingStartTime = -1;
            mTargetState = -1;
        }
    };

    public SimpleMultiStateView(Context context) {
        this(context, null);
    }

    public SimpleMultiStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleMultiStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.msv_SimpleMultiStateView);
        resIdEmpty = typedArray.getResourceId(R.styleable.msv_SimpleMultiStateView_msv_emptyView, -1);
        resIdLoading = typedArray.getResourceId(R.styleable.msv_SimpleMultiStateView_msv_loadingView, -1);
        resIdFailed = typedArray.getResourceId(R.styleable.msv_SimpleMultiStateView_msv_failView, -1);
        resIdNoNetwork = typedArray.getResourceId(R.styleable.msv_SimpleMultiStateView_msv_noNetworkView, -1);
        typedArray.recycle();
        if (typedArray != null) {
            if (resIdEmpty != -1) {
                addViewForStatus(MultiStateView.STATE_EMPTY, resIdEmpty);
            }
            if (resIdFailed != -1) {
                addViewForStatus(MultiStateView.STATE_FAILED, resIdFailed);
            }
            if (resIdLoading != -1) {
                addViewForStatus(MultiStateView.STATE_LOADING, resIdLoading);
            }
            if (resIdNoNetwork != -1) {
                addViewForStatus(MultiStateView.STATE_NO_NETWORK, resIdNoNetwork);
            }
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks(mLoadingHide);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mLoadingHide);
    }

    /**
     * 显示进度页
     */
    public void showLoadingView() {
        setViewState(MultiStateView.STATE_LOADING);
    }

    /**
     * 显示错误页
     */
    public void showErrorView() {
        if (getViewState() != MultiStateView.STATE_CONTENT) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewState(MultiStateView.STATE_FAILED);
                }
            }, 100);
        }

    }

    /**
     * 无数据时
     */
    public void showEmptyView() {
        if (getViewState() != MultiStateView.STATE_CONTENT) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewState(MultiStateView.STATE_EMPTY);
                }
            }, 100);
        }

    }

    /**
     * 无数据时
     */
    public void showNoNetworkView() {
        if (getViewState() != MultiStateView.STATE_CONTENT) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewState(MultiStateView.STATE_NO_NETWORK);
                }
            }, 100);

        }

    }

    /**
     * 显示内容
     */
    public void showContent() {

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewState(MultiStateView.STATE_CONTENT);
            }
        }, 100);
    }

    @Override
    public void setViewState(int state) {
        if (getViewState() == STATE_LOADING && state != STATE_LOADING) {
            long diff = System.currentTimeMillis() - mLoadingStartTime;
            if (diff < MIN_SHOW_TIME) {
                mTargetState = state;
                postDelayed(mLoadingHide, MIN_DELAY);
            } else {
                super.setViewState(state);
            }
            return;
        } else if (state == STATE_LOADING) {
            mLoadingStartTime = System.currentTimeMillis();
        }
        super.setViewState(state);
    }


    /**
     * 设置emptyView的自定义Layout
     *
     * @param emptyResource emptyView的layoutResource
     */
    public SimpleMultiStateView setEmptyResource(@LayoutRes int emptyResource) {
        this.resIdEmpty = emptyResource;
        addViewForStatus(MultiStateView.STATE_EMPTY, resIdEmpty);
        return this;
    }

    /**
     * 设置retryView的自定义Layout
     *
     * @param retryResource retryView的layoutResource
     */
    public SimpleMultiStateView setRetryResource(@LayoutRes int retryResource) {
        this.resIdFailed = retryResource;
        addViewForStatus(MultiStateView.STATE_FAILED, resIdFailed);
        return this;
    }

    /**
     * 设置loadingView的自定义Layout
     *
     * @param loadingResource loadingView的layoutResource
     */
    public SimpleMultiStateView setLoadingResource(@LayoutRes int loadingResource) {
        resIdLoading = loadingResource;
        addViewForStatus(MultiStateView.STATE_LOADING, resIdLoading);
        return this;
    }

    /**
     * 设置NoNetView的自定义Layout
     *
     * @param noNetworkResource loadingView的layoutResource
     */
    public SimpleMultiStateView setNoNetworkResource(@LayoutRes int noNetworkResource) {
        resIdNoNetwork = noNetworkResource;
        addViewForStatus(MultiStateView.STATE_NO_NETWORK, resIdNoNetwork);
        return this;
    }

    public SimpleMultiStateView build() {
        showLoadingView();
        return this;
    }
}
