package com.ltdung.friendlychat.presentation.mvp.view.impl;

import android.app.Activity;

import com.ltdung.friendlychat.library.presentation.mvp.view.ViewImpl;
import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.mvp.view.SignInView;

public abstract class SignInViewImpl extends ViewImpl implements SignInView {

    public SignInViewImpl(BaseActivity activity) {
        super(activity);
    }
}
