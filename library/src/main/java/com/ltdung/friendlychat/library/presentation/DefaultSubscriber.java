package com.ltdung.friendlychat.library.presentation;



import com.ltdung.friendlychat.library.presentation.mvp.view.View;

import rx.Subscriber;

public class DefaultSubscriber<T> extends Subscriber<T> {

    private View view;

    public DefaultSubscriber(View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {
        if(view != null){
            view.hideProgress();
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }
}
