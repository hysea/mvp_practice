package com.tuodao.mvp_practice.ui.news;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tuodao.mvp_practice.R;
import com.tuodao.mvp_practice.bean.Channel;
import com.tuodao.mvp_practice.event.ChannelSelectEvent;
import com.tuodao.mvp_practice.ui.adapter.ChannelAdapter;
import com.tuodao.mvp_practice.ui.inter.OnChannelListener;
import com.tuodao.mvp_practice.utils.ItemDragHelperCallBack;

import org.simple.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hysea on 2018/3/15.
 */

public class ChannelDialogFragment extends DialogFragment implements OnChannelListener {

    @BindView(R.id.rv_channel)
    RecyclerView mRvChannel;
    @BindView(R.id.iv_channel_close)
    ImageView mIvChannelClose;

    private static final String SELECTED = "selected";
    private static final String UNSELECTED = "unselected";

    private ChannelAdapter mChannelAdapter;
    private OnChannelListener mOnChannelListener;
    private List<Channel> mDatas = new ArrayList<>();
    private List<Channel> mSelectedDatas;
    private List<Channel> mUnselectedDatas;
    private Unbinder mUnbinder;

    private boolean isUpdate = false;

    public static ChannelDialogFragment newInstance(List<Channel> selectedDatas, List<Channel> unselectedDatas) {
        Bundle bundle = new Bundle();
        ChannelDialogFragment fragment = new ChannelDialogFragment();
        bundle.putSerializable("selected", (Serializable) selectedDatas);
        bundle.putSerializable("unselected", (Serializable) unselectedDatas);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setWindowAnimations(R.style.dialogSlideAnim);
        }
        View view = inflater.inflate(R.layout.fragment_channal_dialog, null);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIvChannelClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Bundle bundle = getArguments();
        mSelectedDatas = (List<Channel>) bundle.getSerializable(SELECTED);
        mUnselectedDatas = (List<Channel>) bundle.getSerializable(UNSELECTED);
        processLogic();
    }

    private void processLogic() {
        Channel channel = new Channel();
        channel.setItemType(Channel.TYPE_MY);
        channel.setChannelName("我的频道");
        mDatas.add(channel);

        setDataType(mSelectedDatas, Channel.TYPE_MY_CHANNEL);
        setDataType(mUnselectedDatas, Channel.TYPE_OTHER_CHANNEL);

        mDatas.addAll(mSelectedDatas);

        Channel otherChannel = new Channel();
        otherChannel.setItemType(Channel.TYPE_OTHER);
        otherChannel.setChannelName("频道推荐");
        mDatas.add(otherChannel);
        mDatas.addAll(mUnselectedDatas);

        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack(this);
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(mRvChannel);

        mChannelAdapter = new ChannelAdapter(mDatas,helper);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        mRvChannel.setLayoutManager(manager);
        mRvChannel.setAdapter(mChannelAdapter);
        mChannelAdapter.setOnChannelListener(this);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mChannelAdapter.getItemViewType(position);
                return itemViewType == Channel.TYPE_MY_CHANNEL || itemViewType == Channel.TYPE_OTHER_CHANNEL ? 1 : 4;
            }
        });
    }

    private void setDataType(List<Channel> datas, int type) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setItemType(type);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onItemMove(int startPos, int endPos) {
        if (startPos < 0 || endPos < 0) return;
        if (mDatas.get(endPos).getChannelName().equals("头条")) return;
        //我的频道之间移动
        if (mOnChannelListener != null)
            mOnChannelListener.onItemMove(startPos - 1, endPos - 1);//去除标题所占的一个index
        onMove(startPos, endPos, false);
    }

    private String firstAddChannelName = "";
    private void onMove(int starPos, int endPos, boolean isAdd) {
        isUpdate = true;
        Channel startChannel = mDatas.get(starPos);
        //先删除之前的位置
        mDatas.remove(starPos);
        //添加到现在的位置
        mDatas.add(endPos, startChannel);
        mChannelAdapter.notifyItemMoved(starPos, endPos);
        if (isAdd) {
            if (TextUtils.isEmpty(firstAddChannelName)) {
                firstAddChannelName = startChannel.getChannelName();
            }
        } else {
            if (startChannel.getChannelName().equals(firstAddChannelName)) {
                firstAddChannelName = "";
            }
        }
    }

    @Override
    public void onMoveToMyChannel(int startPos, int endPos) {
        onMove(startPos, endPos, true);
    }

    @Override
    public void onMoveToOtherChannel(int startPos, int endPos) {
        onMove(startPos, endPos, false);
    }

    @Override
    public void onFinish(String selectedChannelName) {
        EventBus.getDefault().post(new ChannelSelectEvent(selectedChannelName));
        dismiss();
    }

    @Override
    public void onPause() {
        if (isUpdate) {
//            EventBus.getDefault().post(new ChannelSelectEvent(mChannelAdapter.getData(), firstAddChannelName));
        }
        super.onPause();
    }
}
