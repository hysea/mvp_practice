package com.tuodao.mvp_practice.ui.news;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.flyco.tablayout.SlidingTabLayout;
import com.tuodao.mvp_practice.R;
import com.tuodao.mvp_practice.bean.Channel;
import com.tuodao.mvp_practice.component.AppComponent;
import com.tuodao.mvp_practice.component.DaggerHttpComponent;
import com.tuodao.mvp_practice.event.ChannelSelectEvent;
import com.tuodao.mvp_practice.ui.adapter.ChannelPagerAdapter;
import com.tuodao.mvp_practice.ui.base.BaseFragment;
import com.tuodao.mvp_practice.ui.news.contract.NewsContract;
import com.tuodao.mvp_practice.ui.news.presenter.NewsPresenter;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hysea on 2018/3/14.
 */

public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsContract.View {

    @BindView(R.id.vp_news)
    ViewPager mVpNews;
    @BindView(R.id.ibtn_edit)
    ImageButton mIbtnEdit;
    @BindView(R.id.slide_tab)
    SlidingTabLayout mSlideTab;


    private List<Channel> mSelectedDatas;
    private List<Channel> mUnSelectedDatas;

    private ChannelPagerAdapter mChannelPagerAdapter;

    private int mSelectedIndex;
    private String mSelectedChannel;

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void initInjector(AppComponent appComponent) {
        DaggerHttpComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mVpNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSelectedIndex = position;
                mSelectedChannel = mSelectedDatas.get(position).getChannelName();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        mSelectedDatas = new ArrayList<>();
        mUnSelectedDatas = new ArrayList<>();
        mPresenter.getChannel();
    }



    @Override
    public void loadData(List<Channel> channels, List<Channel> otherChannels) {
        if (otherChannels != null) {
            mUnSelectedDatas.clear();
            mUnSelectedDatas.addAll(otherChannels);


        }

        if (channels != null) {
            mSelectedDatas.clear();
            mSelectedDatas.addAll(channels);
            mChannelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(), channels);
            mVpNews.setAdapter(mChannelPagerAdapter);
            mVpNews.setOffscreenPageLimit(2);
            mVpNews.setCurrentItem(0, false);
            mSlideTab.setViewPager(mVpNews);
        }
    }
    @OnClick(R.id.ibtn_edit)
    public void onClick(View view) {
        ChannelDialogFragment dialogFragment = ChannelDialogFragment.newInstance(mSelectedDatas, mUnSelectedDatas);
        dialogFragment.show(getChildFragmentManager(), "CHANNEL");
    }

    @Subscriber
    public void channelSelectEvent(ChannelSelectEvent event) {
        List<String> channelNames = new ArrayList<>();
        for (Channel channel : mSelectedDatas) {
            channelNames.add(channel.getChannelName());
        }
        setViewpagerPos(channelNames,event.getChannelName());
    }

    private void setViewpagerPos(List<String> channelNames, String channelName) {
        if (TextUtils.isEmpty(channelName) || channelNames == null) return;

        for (int i = 0; i < channelNames.size(); i++) {
            if (channelNames.get(i).equals(channelName)) {
                mSelectedChannel = channelNames.get(i);
                mSelectedIndex = i;
                return;
            }
        }

        mVpNews.postDelayed(new Runnable() {
            @Override
            public void run() {
                mVpNews.setCurrentItem(mSelectedIndex, false);
            }
        }, 100);
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
