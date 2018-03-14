package com.tuodao.mvp_practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuodao.mvp_practice.component.AppComponent;
import com.tuodao.mvp_practice.ui.base.BaseActivity;
import com.tuodao.mvp_practice.utils.ImageLoaderUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author hysea 启动页
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.giv_logo)
    GifImageView mGivLogo;
    @BindView(R.id.iv_ad)
    ImageView mIvAd;
    @BindView(R.id.tv_skip)
    TextView mTvSkip;

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }


    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        final GifDrawable gifDrawable = (GifDrawable) mGivLogo.getDrawable();
        gifDrawable.setLoopCount(1);
        mGivLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                gifDrawable.start();
            }
        }, 100);

        ImageLoaderUtil.LoadImage(this, "http://api.dujin.org/bing/1920.php", mIvAd);

        final int countTime = 4;
        Observable<Integer> observable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                }).take(countTime + 1);

        mCompositeDisposable.add(observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                mTvSkip.setText("跳过 5");
            }
        }).subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                mTvSkip.setText("跳过 " + (integer + 1));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                toMain();
            }
        }));
    }

    private void toMain() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @OnClick(R.id.fl_ad)
    public void skip() {
        toMain();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInjector(AppComponent appComponent) {

    }

    @Override
    public void onRetry() {

    }
}
