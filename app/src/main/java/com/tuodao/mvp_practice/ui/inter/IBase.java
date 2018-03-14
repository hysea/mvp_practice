package com.tuodao.mvp_practice.ui.inter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuodao.mvp_practice.component.AppComponent;

/**
 * Created by hysea on 2018/3/13.
 */

public interface IBase {
    View createView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState);

    View getView();

    int getContentLayout();

    void initInjector(AppComponent appComponent);

    void bindView(View view, Bundle savedInstanceState);

    void initData();
}
