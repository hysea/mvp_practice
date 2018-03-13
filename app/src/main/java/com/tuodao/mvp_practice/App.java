package com.tuodao.mvp_practice;

import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by hysea on 2018/3/13.
 */

public class App extends LitePalApplication {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        BGASwipeBackHelper.init(this,null);
    }

    public static App getInstance(){
        return sApp;
    }
}
