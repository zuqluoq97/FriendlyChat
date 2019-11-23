package com.ltdung.friendlychat.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;


import androidx.databinding.DataBindingUtil;

import com.ltdung.friendlychat.library.presentation.mvp.view.ViewImpl;
import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.databinding.ActivitySplashBinding;
import com.ltdung.friendlychat.presentation.di.component.ViewComponent;
import com.ltdung.friendlychat.presentation.mvp.presenter.SplashPresenter;
import com.ltdung.friendlychat.presentation.mvp.view.SplashView;
import com.ltdung.friendlychat.presentation.mvp.view.impl.SplashViewImpl;

import javax.inject.Inject;

import dagger.Lazy;

public class SplashActivity extends BaseDaggerActivity<SplashView, SplashPresenter, ActivitySplashBinding> {

    @Inject
    Lazy<SplashPresenter> splashPresenter;

    public static void start(Context context){
        Intent intent = BaseActivity.getBaseStartIntent(context, SplashActivity.class, true);
        context.startActivity(intent);
    }

    @Override
    public void onLoadFinished() {
        super.onLoadFinished();
        initUI();
    }

    @Override
    protected void injectViewComponent(ViewComponent viewComponent) {
        viewComponent.inject(this);
    }

    @Override
    protected SplashView initView() {
        return new SplashViewImpl(this) {
            @Override
            public void navigateToLogin() {
                SignInActivity.start(SplashActivity.this);
                finish();
            }

            @Override
            public void navigateToUsers() {
                UsersActivity.start(SplashActivity.this);
                finish();
            }
        };
    }

    @Override
    protected Lazy<SplashPresenter> initPresenter() {
        return splashPresenter;
    }

    @Override
    protected ActivitySplashBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_splash);
    }

    private void initUI(){
        initSwipeToRefresh();
    }

    private void initSwipeToRefresh() {
        ((ViewImpl) view).initSwipeToRefresh(binding.swipeToRefresh, presenter);
    }
}
