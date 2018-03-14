package com.tuodao.mvp_practice.module;

import android.content.Context;

import com.tuodao.mvp_practice.App;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hysea on 2018/3/14.
 */

@Module
public class AppModule {
    private Context mContext;

    public AppModule(Context context) {
        this.mContext = context;
    }

    @Provides
    App provideApp() {
        return (App) mContext.getApplicationContext();
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
