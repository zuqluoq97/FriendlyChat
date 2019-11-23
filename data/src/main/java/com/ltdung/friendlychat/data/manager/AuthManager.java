package com.ltdung.friendlychat.data.manager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ltdung.friendlychat.domain.interactor.user.CreateUser;

import rx.Subscriber;

public interface AuthManager {

    void signInGoogle(GoogleSignInAccount account, Subscriber<String> signInSubscriber, CreateUser createUser);

    void signOut(Subscriber<String> signOutSubscriber);

    boolean isSignedIn();

    String getCurrentUserId();
}
