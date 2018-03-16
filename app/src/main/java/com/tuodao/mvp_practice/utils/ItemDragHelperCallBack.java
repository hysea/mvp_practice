package com.tuodao.mvp_practice.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.tuodao.mvp_practice.ui.inter.OnChannelListener;
import com.tuodao.mvp_practice.ui.inter.OnDragVHListener;

/**
 * Created by hysea on 2018/3/15.
 */

public class ItemDragHelperCallBack extends ItemTouchHelper.Callback {

    private OnChannelListener mOnChannelListener;

    public ItemDragHelperCallBack(OnChannelListener onChannelListener) {
        mOnChannelListener = onChannelListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 用于设置是否处理拖拽事件和滑动事件，以及拖拽和滑动的方向
        // 若RecyclerView是列表类型，拖拽只有UP、DOWN这两个方向
        // 若RecyclerView是网格或者瀑布流类型，有UP、DOWN、LEFT、RIGHT四个方向
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int dragFlag;
        if (layoutManager instanceof GridLayoutManager ||
                layoutManager instanceof StaggeredGridLayoutManager) {
            dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        // return makeMovementFlags(dragFlags, swipeFlags);
        // dragFlags是拖拽标志，swipeFlags是滑动标志
        // 若swipeFlags为0，表示不处理滑动操作
        return makeMovementFlags(dragFlag,0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 当我们长按Item就会进入拖拽事件，并在拖拽过程中不断回调onMove方法
        // 可以在这个方法获取当前拖拽的item和已经被拖拽到所处位置的item的ViewHolder

        // 不断type之间，不可移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        if (mOnChannelListener != null) {
            mOnChannelListener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // 如果设置了非0的swipeFlags，那么在滑动item的时候就会调用onSwiped方法
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // 当长按选中item的时候（拖拽开始的时候）调用

        // 不在闲置状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof OnDragVHListener) {
                OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 当手指松开的时候（拖拽完成）调用

        if (viewHolder instanceof OnDragVHListener) {
            OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
            itemViewHolder.onItemFinish();
        }

        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        //不需要长按拖动，因为我们的标题和 频道推荐 是不需要拖动的，所以手动控制
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        //不需要侧滑
        return false;
    }
}
