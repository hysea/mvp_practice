package com.tuodao.mvp_practice.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.animation.SlideEnter.SlideRightEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.tuodao.mvp_practice.App;
import com.tuodao.mvp_practice.R;
import com.tuodao.mvp_practice.bean.NewsDetail;
import com.tuodao.mvp_practice.component.AppComponent;
import com.tuodao.mvp_practice.component.DaggerHttpComponent;
import com.tuodao.mvp_practice.net.NewsApi;
import com.tuodao.mvp_practice.ui.adapter.NewsDetailAdapter;
import com.tuodao.mvp_practice.ui.base.BaseFragment;
import com.tuodao.mvp_practice.ui.news.contract.NewsDetailContract;
import com.tuodao.mvp_practice.ui.news.presenter.NewsDetailPresenter;
import com.tuodao.mvp_practice.utils.ContextUtils;
import com.tuodao.mvp_practice.utils.ImageLoaderUtil;
import com.tuodao.mvp_practice.widget.CustomLoadMoreView;
import com.tuodao.mvp_practice.widget.NewsDelPop;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by hysea on 2018/3/14.
 */

public class NewsDetailFragment extends BaseFragment<NewsDetailPresenter> implements NewsDetailContract.View {
    @BindView(R.id.rv_news_detail)
    RecyclerView mRvNewsDetail;
    @BindView(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mRlTopToast;

    private static final String NEWS_ID = "news_id";
    private static final String POSITION = "position";

    private List<NewsDetail.ItemBean> mBannerList;
    private List<NewsDetail.ItemBean> mList;
    private NewsDetailAdapter mNewsDetailAdapter;

    private int upPullNum = 1;
    private int downPullNum = 1;

    private NewsDelPop newsDelPop;
    private View view_Focus;//顶部banner
    private Banner mBanner;

    private String newsId;


    private boolean isRemoveHeaderView = false;
    private int position;


    public static NewsDetailFragment newInstance(String newsId, int position) {
        Bundle args = new Bundle();
        args.putString(NEWS_ID,newsId);
        args.putInt(POSITION, position);
        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_news_detail;
    }

    @Override
    public void initInjector(AppComponent appComponent) {
        DaggerHttpComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRvNewsDetail, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRemoveHeaderView = true;
                mPresenter.getData(newsId, NewsApi.ACTION_DOWN, downPullNum);
            }
        });

        mList = new ArrayList<>();
        mBannerList = new ArrayList<>();
        mRvNewsDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsDetailAdapter = new NewsDetailAdapter(mList, getActivity());
        mRvNewsDetail.setAdapter(mNewsDetailAdapter);
        mNewsDetailAdapter.setEnableLoadMore(true);
        mNewsDetailAdapter.setLoadMoreView(new CustomLoadMoreView());
        mNewsDetailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mNewsDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getData(newsId, NewsApi.ACTION_UP, upPullNum);
            }
        }, mRvNewsDetail);

        mRvNewsDetail.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) baseQuickAdapter.getItem(i);
                toRead(itemBean);
            }
        });

        mRvNewsDetail.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) baseQuickAdapter.getItem(i);
                switch (view.getId()) {
                    case R.id.iv_close:
                        view.getHeight();
                        int[] location = new int[2];
                        view.getLocationInWindow(location);
                        if (itemBean.getStyle() == null) return;
                        if (ContextUtils.getSreenWidth(App.getContext()) - 50 - location[1] < ContextUtils.dip2px(App.getContext(), 80)) {
                            newsDelPop
                                    .anchorView(view)
                                    .gravity(Gravity.TOP)
                                    .setBackReason(itemBean.getStyle().getBackreason(), true, i)
                                    .show();
                        } else {
                            newsDelPop
                                    .anchorView(view)
                                    .gravity(Gravity.BOTTOM)
                                    .setBackReason(itemBean.getStyle().getBackreason(), false, i)
                                    .show();
                        }
                        break;
                }
            }
        });

        view_Focus = getView().inflate(getActivity(), R.layout.news_detail_headerview, null);
        mBanner = (Banner) view_Focus.findViewById(R.id.banner);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        //Glide 加载图片简单用法
                        ImageLoaderUtil.LoadImage(getActivity(), path, imageView);
                    }
                })
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mBannerList.size() < 1) return;
//                bannerToRead(mBannerList.get(position));
            }
        });

        newsDelPop = new NewsDelPop(getActivity())
                .alignCenter(false)
                .widthScale(0.95f)
//                                .showAnim(new FlipRightEnter())
//                                .dismissAnim(new FlipHorizontalExit())
                .showAnim(new SlideRightEnter())
                .dismissAnim(new SlideRightExit())
                .offset(-100, 0)
                .dimEnabled(true);
        newsDelPop.setClickListener(new NewsDelPop.onClickListener() {
            @Override
            public void onClick(int position) {
                newsDelPop.dismiss();
                mNewsDetailAdapter.remove(position);
                showToast(0, false);
            }
        });
    }

    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            mTvToast.setText(String.format(getResources().getString(R.string.news_toast), num + ""));
        } else {
            mTvToast.setText("将为你减少此类内容");
        }
        mRlTopToast.setVisibility(View.VISIBLE);
        ViewAnimator.animate(mRlTopToast)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        ViewAnimator.animate(mRlTopToast)
                                .bounceOut()
                                .duration(1000)
                                .start();
                    }
                });
    }

    private void toRead(NewsDetail.ItemBean itemBean) {
        if (itemBean == null) {
            return;
        }
        switch (itemBean.getItemType()) {
            case NewsDetail.ItemBean.TYPE_DOC_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG:
//                Intent intent = new Intent(getActivity(), ArticleReadActivity.class);
//                intent.putExtra("aid", itemBean.getDocumentId());
//                startActivity(intent);
                break;
            case NewsDetail.ItemBean.TYPE_SLIDE:
//                ImageBrowseActivity.launch(getActivity(), itemBean);
                break;
            case NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG:
//                AdvertActivity.launch(getActivity(), itemBean.getLink().getWeburl());
                break;
            case NewsDetail.ItemBean.TYPE_PHVIDEO:
//                T("TYPE_PHVIDEO");
                break;
        }
    }
    @Override
    public void onRetry() {
        initData();
    }

    @Override
    public void initData() {
        if (getArguments() == null) return;
        newsId = getArguments().getString(NEWS_ID);
        position = getArguments().getInt(POSITION);
        mPresenter.getData(newsId, NewsApi.ACTION_DEFAULT, 1);
    }

    @Override
    public void loadBannerData(NewsDetail newsDetail) {
        List<String> mTitleList = new ArrayList<>();
        List<String> mUrlList = new ArrayList<>();
        mBannerList.clear();
        for (NewsDetail.ItemBean bean : newsDetail.getItem()) {
            if (!TextUtils.isEmpty(bean.getThumbnail())) {
                mTitleList.add(bean.getTitle());
                mBannerList.add(bean);
                mUrlList.add(bean.getThumbnail());
            }
        }
        if (mUrlList.size() > 0) {
            mBanner.setImages(mUrlList);
            mBanner.setBannerTitles(mTitleList);
            mBanner.start();
            if (mNewsDetailAdapter.getHeaderLayoutCount() < 1) {
                mNewsDetailAdapter.addHeaderView(view_Focus);
            }
        }
    }

    @Override
    public void loadTopNewsData(NewsDetail newsDetail) {

    }

    @Override
    public void loadData(List<NewsDetail.ItemBean> items) {
        if (items == null || items.size() == 0) {
            showFailed();
            mPtrFrameLayout.refreshComplete();
        } else {
            downPullNum++;
            if (isRemoveHeaderView) {
                mNewsDetailAdapter.removeAllHeaderView();
            }
            mNewsDetailAdapter.setNewData(items);
            showToast(items.size(), true);
            mPtrFrameLayout.refreshComplete();
            showSuccess();
        }
    }

    @Override
    public void loadMoreData(List<NewsDetail.ItemBean> items) {
        if (items == null || items.size() == 0) {
            mNewsDetailAdapter.loadMoreFail();
        } else {
            upPullNum++;
            mNewsDetailAdapter.addData(items);
            mNewsDetailAdapter.loadMoreComplete();
        }
    }
}
