package com.tuodao.mvp_practice.net;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by hysea on 2018/3/15.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    public abstract void onSuccess(T t);

    public abstract void onFail(Throwable e);

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onFail(e);
    }

    @Override
    public void onComplete() {

    }
}
