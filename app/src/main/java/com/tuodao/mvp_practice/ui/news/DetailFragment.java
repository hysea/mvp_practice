package com.tuodao.mvp_practice.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by hysea on 2018/3/14.
 */

public class DetailFragment extends Fragment {
    public static DetailFragment newInstance() {
        Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
