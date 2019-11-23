package com.ltdung.friendlychat.library.presentation.ui.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.loader.content.Loader;

import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;

public abstract class PresenterLoader<PRESENTER extends BasePresenter> extends Loader<PRESENTER> {

    private PRESENTER presenter;

    public PresenterLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(presenter != null){
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        presenter = initPresenter();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        super.onReset();
        presenter.destroy();
        presenter = null;
    }

    protected abstract PRESENTER initPresenter();
}
