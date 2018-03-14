package com.tuodao.mvp_practice;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.jaeger.library.StatusBarUtil;
import com.tuodao.mvp_practice.component.AppComponent;
import com.tuodao.mvp_practice.ui.base.BaseActivity;
import com.tuodao.mvp_practice.widget.BottomBar;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity {
    @BindView(R.id.bottom_bar)
    BottomBar mBottomBar;
    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;

    private SupportFragment[] mFragments = new SupportFragment[4];

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
        if (savedInstanceState == null) {

        }
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void initInjector(AppComponent appComponent) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {

    }
}
