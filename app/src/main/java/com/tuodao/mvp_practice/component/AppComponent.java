package com.tuodao.mvp_practice.component;

import android.content.Context;

import com.tuodao.mvp_practice.App;
import com.tuodao.mvp_practice.module.AppModule;
import com.tuodao.mvp_practice.module.HttpModule;
import com.tuodao.mvp_practice.net.NewsApi;

import dagger.Component;

/**
 * Created by hysea on 2018/3/14.
 */

@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {
    App getApplication();

    NewsApi getNetEaseApi();

    Context getContext();
}
