package com.tuodao.mvp_practice;

import com.tuodao.mvp_practice.component.AppComponent;
import com.tuodao.mvp_practice.component.DaggerAppComponent;
import com.tuodao.mvp_practice.module.AppModule;

import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by hysea on 2018/3/13.
 */

public class App extends LitePalApplication {

    private static App sApp;
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        BGASwipeBackHelper.init(this, null);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static App getInstance() {
        return sApp;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
