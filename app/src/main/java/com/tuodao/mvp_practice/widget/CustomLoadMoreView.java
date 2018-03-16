package com.tuodao.mvp_practice.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.tuodao.mvp_practice.R;

/**
 * Created by hysea on 2018/3/15.
 */

public class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.layout_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }

    @Override
    public boolean isLoadEndGone() {
        return true;
    }
}
