package com.tuodao.mvp_practice.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by hysea on 2018/3/14.
 */

public class Channel extends DataSupport implements Serializable, MultiItemEntity {
    public static final int TYPE_MY = 1;
    public static final int TYPE_OTHER = 2;
    public static final int TYPE_MY_CHANNEL = 3;
    public static final int TYPE_OTHER_CHANNEL = 4;

    @Column(ignore = true)
    private int itemType;

    private String channelId;
    private String channelName;

    /**
     * 0.可移除  1.不可移除
     */
    private int channelType;

    /**
     * 0.未选中 1.选中
     */
    private boolean isChannelSelected;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public boolean isChannelSelected() {
        return isChannelSelected;
    }

    public void setChannelSelected(boolean channelSelected) {
        isChannelSelected = channelSelected;
    }
}
