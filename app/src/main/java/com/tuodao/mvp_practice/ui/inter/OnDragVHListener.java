package com.tuodao.mvp_practice.ui.inter;

/**
 * ViewHolder被选中 以及 拖拽释放触发监听
 */
public interface OnDragVHListener {
    /**
     * Item被选中时触发
     */
    void onItemSelected();

    /**
     * Item在拖拽/滑动结束后触发
     */
    void onItemFinish();
}
