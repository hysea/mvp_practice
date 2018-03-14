package com.tuodao.mvp_practice.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuodao.mvp_practice.R;
import com.tuodao.mvp_practice.utils.ContextUtils;

/**
 * Created by hysea on 2018/3/14.
 */

public class BottomBarTab extends LinearLayout {
    private ImageView mIcon;
    private TextView mTitle;
    private Context mContext;
    private int mTabPosition = -1;
    private int icon;

    public BottomBarTab(Context context, @DrawableRes int icon, String title) {
        this(context, null, icon, title);
    }

    public BottomBarTab(Context context, @Nullable AttributeSet attrs, @DrawableRes int icon, String title) {
        this(context, attrs, 0, icon, title);
    }

    public BottomBarTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @DrawableRes int icon, String title) {
        super(context, attrs, defStyleAttr);
        init(context, icon, title);
    }

    private void init(Context context, int icon, String title) {
        mContext = context;
        this.icon = icon;
        setOrientation(VERTICAL);
        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        LayoutParams params = new LayoutParams(size, size);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = ContextUtils.dip2px(context, 3);
        mIcon.setImageResource(icon);
        mIcon.setLayoutParams(params);

        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_HORIZONTAL;
        textParams.topMargin = ContextUtils.dip2px(context, 3);
        textParams.bottomMargin = ContextUtils.dip2px(context, 3);
        mTitle = new TextView(context);
        mTitle.setText(title);
        mTitle.setTextSize(ContextUtils.sp2px(context, 13));
        mTitle.setTextColor(ContextCompat.getColor(context, R.color.tab_un_select));
        mTitle.setLayoutParams(textParams);

        addView(mIcon);
        addView(mTitle);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            mIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
            mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        } else {
            mIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.tab_un_select));
            mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.tab_un_select));
        }
    }

    public void setTabPosition(int position) {
        mTabPosition = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    public int getTabPosition() {
        return mTabPosition;
    }
}
