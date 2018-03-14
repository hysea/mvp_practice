package com.tuodao.mvp_practice.event;

/**
 * Created by hysea on 2018/3/14.
 */

public class ChannelSelectEvent {

    private String channelName;

    public ChannelSelectEvent(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
}
