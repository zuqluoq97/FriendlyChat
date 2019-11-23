package com.ltdung.friendlychat.presentation.mvp.view.impl;

import android.app.Activity;

import com.ltdung.friendlychat.library.presentation.mvp.view.ViewImpl;
import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.mvp.view.EditProfileView;

public abstract class EditProfileViewImpl extends ViewImpl implements EditProfileView {

    public EditProfileViewImpl(BaseActivity activity) {
        super(activity);
    }
}
