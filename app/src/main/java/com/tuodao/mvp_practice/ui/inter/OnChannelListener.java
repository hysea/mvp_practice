package com.tuodao.mvp_practice.ui.inter;

/**
 * Created by hysea on 2018/3/15.
 */

public interface OnChannelListener {
    void onItemMove(int startPos, int endPos);

    void onMoveToMyChannel(int startPos, int endPos);

    void onMoveToOtherChannel(int startPos, int endPos);

    void onFinish(String selectedChannelName);
}
