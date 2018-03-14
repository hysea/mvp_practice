package com.tuodao.mvp_practice.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tuodao.mvp_practice.R;

/**
 * Created by hysea on 2018/3/14.
 */

public class MultiStateView extends FrameLayout {

    public static final int STATE_CONTENT = 10001;
    public static final int STATE_LOADING = 10002;
    public static final int STATE_EMPTY = 10003;
    public static final int STATE_FAILED = 10004;
    public static final int STATE_NO_NETWORK = 10005;

    private int mCurrentState = STATE_CONTENT;

    private View mContentView;
    private SparseArray<View> mStateViewArray = new SparseArray<>();
    private SparseIntArray mLayoutIDArray = new SparseIntArray();

    public MultiStateView(@NonNull Context context) {
        super(context);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        validContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        validContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        validContentView(child);
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        validContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        validContentView(child);
        super.addView(child, index, params);
    }

    /**
     * 检查当前View是否是content
     */
    private void validContentView(View view) {
        if (isValidContentView(view)) {
            mContentView = view;
            mStateViewArray.put(STATE_CONTENT, view);
        } else if (mCurrentState != STATE_CONTENT) {
            mContentView.setVisibility(GONE);
        }
    }

    private boolean isValidContentView(View view) {
        if (mContentView == null) {
            for (int i = 0; i < mStateViewArray.size(); i++) {
                if (mStateViewArray.indexOfValue(view) != -1) return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 改变视图状态
     *
     * @param state 状态类型
     */
    public void setViewState(int state) {
        if (getCurrentView() == null) return;
        if (state != mCurrentState) {
            View view = getView(state);
            getCurrentView().setVisibility(GONE);
            mCurrentState = state;
            if (view != null) {
                view.setVisibility(VISIBLE);
            } else {
                int resLayoutID = mLayoutIDArray.get(state);
                if (resLayoutID == 0) return;
                view = LayoutInflater.from(getContext()).inflate(resLayoutID, this, false);
                mStateViewArray.put(state, view);
                addView(view);

                if (state == STATE_FAILED) {
                    View btnRetry = view.findViewById(R.id.btn_retry);
                    if (btnRetry != null) {
                        btnRetry.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mOnReloadListener != null) {
                                    mOnReloadListener.onReload();
                                    setViewState(STATE_LOADING);
                                }
                            }
                        });
                    }
                }

                view.setVisibility(VISIBLE);
                if (mOnInflateListener != null) {
                    mOnInflateListener.onInflate(state, view);
                }
            }
        }
    }

    public void addViewForStatus(int status, int resLayoutID) {
        mLayoutIDArray.put(status, resLayoutID);
    }

    /**
     * 获取当前状态
     *
     * @return 状态
     */
    public int getViewState() {
        return mCurrentState;
    }


    /**
     * 获取当前状态的View
     *
     * @return 当前状态的View
     */
    public View getCurrentView() {
        if (mCurrentState == -1) return null;
        View view = getView(mCurrentState);
        if (view == null && mCurrentState == STATE_CONTENT) {
            throw new NullPointerException("content is null");
        } else if (view == null) {
            throw new NullPointerException("current state view is null, state = " + mCurrentState);
        }
        return getView(mCurrentState);
    }

    /**
     * 获取指定状态的View
     *
     * @param state 状态类型
     * @return 指定状态的View
     */
    public View getView(int state) {
        return mStateViewArray.get(state);
    }

    private OnInflateListener mOnInflateListener;
    private OnReloadListener mOnReloadListener;

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        mOnReloadListener = onReloadListener;
    }

    public void setOnInflateListener(OnInflateListener onInflateListener) {
        mOnInflateListener = onInflateListener;
    }

    public interface OnInflateListener {
        void onInflate(int state, View view);
    }

    public interface OnReloadListener {
        void onReload();
    }
}
