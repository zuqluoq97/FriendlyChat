package com.ltdung.friendlychat.presentation.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;
import com.ltdung.friendlychat.library.presentation.mvp.view.View;
import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.App;
import com.ltdung.friendlychat.presentation.di.component.AppComponent;
import com.ltdung.friendlychat.presentation.di.component.DaggerAppComponent;
import com.ltdung.friendlychat.presentation.di.component.ViewComponent;
import com.ltdung.friendlychat.presentation.di.module.AppModule;
import com.ltdung.friendlychat.presentation.di.module.ViewModule;

public abstract class BaseDaggerActivity<VIEW extends View,
        PRESENTER extends BasePresenter,
        BINDING extends ViewDataBinding>
        extends BaseActivity<VIEW, PRESENTER, BINDING> {

    private ViewComponent viewComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initViewComponent();
        super.onCreate(savedInstanceState);
    }

    protected abstract void injectViewComponent(ViewComponent viewComponent);

    private void initViewComponent() {
        viewComponent = ((App) getApplication()).getAppComponent()
                .plus(new ViewModule(view));
        injectViewComponent(viewComponent);
    }
}
