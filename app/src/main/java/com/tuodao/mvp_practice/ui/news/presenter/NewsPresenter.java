package com.tuodao.mvp_practice.ui.news.presenter;

import com.tuodao.mvp_practice.App;
import com.tuodao.mvp_practice.R;
import com.tuodao.mvp_practice.bean.Channel;
import com.tuodao.mvp_practice.database.ChannelDao;
import com.tuodao.mvp_practice.ui.base.BasePresenter;
import com.tuodao.mvp_practice.ui.news.contract.NewsContract;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hysea on 2018/3/14.
 */

public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {

    @Inject
    public NewsPresenter() {

    }


    @Override
    public void getChannel() {
        List<Channel> channelList;
        List<Channel> myChannels = new ArrayList<>();
        List<Channel> otherChannels = new ArrayList<>();
        channelList = ChannelDao.getChannels();
        if (channelList.size() < 1) {
            List<String> channelName = Arrays.asList(App.getContext().getResources()
                    .getStringArray(R.array.news_channel));
            List<String> channelId = Arrays.asList(App.getContext().getResources()
                    .getStringArray(R.array.news_channel_id));
            List<Channel> channels = new ArrayList<>();

            for (int i = 0; i < channelName.size(); i++) {
                Channel channel = new Channel();
                channel.setChannelId(channelId.get(i));
                channel.setChannelName(channelName.get(i));
                channel.setChannelType(i < 1 ? 1 : 0);
                channel.setChannelSelected(i < channelId.size() - 3);
                // 将最后3个频道作为频道推荐，其他频道作为我的频道
                if (i < channelId.size() - 3) {
                    myChannels.add(channel);
                } else {
                    otherChannels.add(channel);
                }
                channels.add(channel);
            }

            // 异步保存所有频道
            DataSupport.saveAllAsync(channels).listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
                }
            });

            channelList = new ArrayList<>();
            channelList.addAll(channels);
        } else {
            Iterator<Channel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                Channel channel = iterator.next();
                if (!channel.isChannelSelected()) {
                    otherChannels.add(channel);
                    iterator.remove();
                }
            }
            myChannels.addAll(channelList);
        }
        mView.loadData(myChannels, otherChannels);
    }

}
