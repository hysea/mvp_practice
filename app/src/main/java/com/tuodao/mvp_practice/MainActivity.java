package com.tuodao.mvp_practice;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.jaeger.library.StatusBarUtil;
import com.tuodao.mvp_practice.component.AppComponent;
import com.tuodao.mvp_practice.ui.base.BaseActivity;
import com.tuodao.mvp_practice.ui.base.SupportFragment;
import com.tuodao.mvp_practice.ui.news.NewsFragment;
import com.tuodao.mvp_practice.widget.BottomBar;
import com.tuodao.mvp_practice.widget.BottomBarTab;

import butterknife.BindView;

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
            mFragments[0] = NewsFragment.newInstance();
            mFragments[1] = NewsFragment.newInstance();
            mFragments[2] = NewsFragment.newInstance();
            mFragments[3] = NewsFragment.newInstance();

            getSupportDelegate().loadMultipleRootFragment(R.id.fl_container, 0,
                    mFragments[0],
                    mFragments[1],
                    mFragments[2],
                    mFragments[3]);
        } else {
            mFragments[0] = findFragment(NewsFragment.class);
            mFragments[1] = findFragment(NewsFragment.class);
            mFragments[2] = findFragment(NewsFragment.class);
            mFragments[3] = findFragment(NewsFragment.class);
        }

        mBottomBar.addItem(new BottomBarTab(this, R.drawable.ic_news, "新闻"))
                .addItem(new BottomBarTab(this, R.drawable.ic_video, "视频"))
                .addItem(new BottomBarTab(this, R.drawable.ic_jiandan, "煎蛋"))
                .addItem(new BottomBarTab(this, R.drawable.ic_my, "我的"));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                getSupportDelegate().showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnSelected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
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
