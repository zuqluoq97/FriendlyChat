package com.ltdung.friendlychat.presentation.mvp.view.impl;

import com.ltdung.friendlychat.library.presentation.mvp.view.ViewImpl;
import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.mvp.view.UsersView;

public abstract class UsersViewImpl extends ViewImpl implements UsersView {

    public UsersViewImpl(BaseActivity activity) {
        super(activity);
    }
}
