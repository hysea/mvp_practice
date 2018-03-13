package com.tuodao.mvp_practice.ui.inter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hysea on 2018/3/13.
 */

public interface IBase {
    View createView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState);

    View getView();

    int getContentLayout();

    void initInjector();

    void bindView();

    void initData();
}
