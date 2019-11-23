package com.ltdung.friendlychat.presentation.di.component;

import com.ltdung.friendlychat.presentation.ui.activity.DialogActivity;
import com.ltdung.friendlychat.presentation.ui.activity.EditProfileActivity;
import com.ltdung.friendlychat.presentation.ui.activity.SignInActivity;
import com.ltdung.friendlychat.presentation.ui.activity.SplashActivity;
import com.ltdung.friendlychat.presentation.di.module.ViewModule;
import com.ltdung.friendlychat.presentation.di.scope.ViewScope;
import com.ltdung.friendlychat.presentation.ui.activity.UsersActivity;
import com.ltdung.friendlychat.presentation.ui.service.BsFirebaseMessagingService;

import dagger.Subcomponent;

@ViewScope
@Subcomponent(modules = {ViewModule.class})
public interface ViewComponent {
    void inject(SplashActivity splashActivity);

    void inject(SignInActivity signInActivity);

    void inject(UsersActivity usersActivity);

    void inject(BsFirebaseMessagingService bsFirebaseMessagingService);

    void inject(DialogActivity dialogActivity);

    void inject(EditProfileActivity editProfileActivity);
}
