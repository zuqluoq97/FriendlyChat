package com.ltdung.friendlychat.presentation.mvp.view.impl;

import android.app.Activity;

import com.ltdung.friendlychat.library.presentation.mvp.view.ViewImpl;
import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.mvp.view.SplashView;

public abstract class SplashViewImpl extends ViewImpl implements SplashView {

    public SplashViewImpl(BaseActivity activity) {
        super(activity);
    }
}
