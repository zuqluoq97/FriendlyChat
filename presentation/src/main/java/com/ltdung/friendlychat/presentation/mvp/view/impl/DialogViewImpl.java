package com.ltdung.friendlychat.presentation.mvp.view.impl;

import android.app.Activity;

import com.ltdung.friendlychat.library.presentation.mvp.view.ViewImpl;
import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.mvp.view.DialogView;

public abstract class DialogViewImpl extends ViewImpl implements DialogView {

    public DialogViewImpl(BaseActivity activity) {
        super(activity);
    }

}
