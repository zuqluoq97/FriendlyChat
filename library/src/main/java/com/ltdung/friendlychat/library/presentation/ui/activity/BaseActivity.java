package com.ltdung.friendlychat.library.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;
import com.ltdung.friendlychat.library.presentation.mvp.view.View;
import com.ltdung.friendlychat.library.presentation.ui.loader.PresenterLoader;

import java.lang.ref.WeakReference;

import dagger.Lazy;

public abstract class BaseActivity<VIEW extends View,
        PRESENTER extends BasePresenter,
        BINDING extends ViewDataBinding> extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<PRESENTER> {
    private static final int LOADER_ID = 101;

    protected PRESENTER presenter;

    protected VIEW view;

    protected BINDING binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = initBinding();
        view = initView();
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    protected void onPause() {
        presenter.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @NonNull
    @Override
    public Loader<PRESENTER> onCreateLoader(int id, @Nullable Bundle args) {
        return new PresenterLoader<PRESENTER>(this) {
            @Override
            protected PRESENTER initPresenter() {
                return BaseActivity.this.initPresenter().get();
            }
        };
    }

    @Override
    public final void onLoadFinished(Loader<PRESENTER> loader, PRESENTER presenter) {
        this.presenter = presenter;
        onLoadFinished();
        presenter.attachView(view);
    }

    /**
     * Prepare stuffs after view loaded
     */
    public void onLoadFinished() {
    }

    @Override
    public final void onLoaderReset(Loader<PRESENTER> loader) {
        onLoadReset();
    }

    public void onLoadReset() {
    }

    public VIEW getView() {
        return view;
    }

    protected abstract VIEW initView();

    protected abstract Lazy<PRESENTER> initPresenter();

    protected abstract BINDING initBinding();

    protected static Intent getBaseStartIntent(Context context, Class<? extends BaseActivity> activityClass, boolean clearStack) {
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (clearStack) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        return intent;
    }
}

