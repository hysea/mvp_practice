package com.tuodao.mvp_practice.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tuodao.mvp_practice.bean.Channel;
import com.tuodao.mvp_practice.ui.news.NewsDetailFragment;

import java.util.List;

/**
 * Created by hysea on 2018/3/14.
 */

public class ChannelPagerAdapter extends FragmentStatePagerAdapter {
    private List<Channel> mChannels;

    public ChannelPagerAdapter(FragmentManager fm, List<Channel> channels) {
        super(fm);
        this.mChannels = channels;
    }

    @Override
    public Fragment getItem(int position) {
        return NewsDetailFragment.newInstance(mChannels.get(position).getChannelId(), position);
    }

    @Override
    public int getCount() {
        return mChannels != null ? mChannels.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).getChannelName();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
