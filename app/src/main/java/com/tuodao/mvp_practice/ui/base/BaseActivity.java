package com.tuodao.mvp_practice.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;
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
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by hysea on 2018/3/13.
 */

@SuppressLint("Registered")
public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends SupportActivity implements BGASwipeBackHelper.Delegate, IBase, BaseContract.BaseView {

    @Nullable
    @BindView(R.id.multi_state_view)
    SimpleMultiStateView mMultiStateView;

    @Nullable
    @Inject
    protected T mPresenter;
    protected View mRootView;
    private Unbinder mUnbinder;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mRootView = createView(null, null, savedInstanceState);
        setContentView(mRootView);
        initInjector(App.getInstance().getAppComponent());
        attachView();
        bindView(mRootView, savedInstanceState);
        initMultiStateView();
        initData();
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(getContentLayout(), container);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public View getView() {
        return mRootView;
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
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
        return this.bindToLife();
    }

    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(@ColorInt int color) {
        setStatusBarColor(color, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    public void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }
}
