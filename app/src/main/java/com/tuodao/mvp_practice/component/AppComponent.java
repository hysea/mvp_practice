package com.tuodao.mvp_practice.component;

import android.content.Context;

import com.tuodao.mvp_practice.App;
import com.tuodao.mvp_practice.module.AppModule;

import dagger.Component;

/**
 * Created by hysea on 2018/3/14.
 */

@Component(modules = {AppModule.class})
public interface AppComponent {
    App getApplication();

    Context getContext();
}
