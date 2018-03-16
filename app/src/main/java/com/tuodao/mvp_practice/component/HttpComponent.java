package com.tuodao.mvp_practice.component;

import com.tuodao.mvp_practice.ui.news.NewsDetailFragment;
import com.tuodao.mvp_practice.ui.news.NewsFragment;

import dagger.Component;

/**
 * Created by hysea on 2018/3/14.
 */
@Component(dependencies = AppComponent.class)
public interface HttpComponent {
    void inject(NewsFragment newsFragment);
    void inject(NewsDetailFragment newsDetailFragment);
}
