package com.ltdung.friendlychat.presentation.mvp.presenter;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ltdung.friendlychat.data.manager.AuthManager;
import com.ltdung.friendlychat.domain.interactor.user.CreateUser;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.presentation.DefaultSubscriber;
import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.di.scope.ViewScope;
import com.ltdung.friendlychat.presentation.mvp.view.SignInView;

import javax.inject.Inject;

import rx.Subscriber;

@ViewScope
public class SignInPresenter extends BasePresenter<SignInView> {

    private AuthManager authManager;

    private Subscriber<String> signInSubscriber;

    private CreateUser createUser;

    @Inject
    public SignInPresenter(NetworkManager networkManager,
                           AuthManager authManager,
                           CreateUser createUser) {
        super(networkManager);
        this.authManager = authManager;
        this.createUser = createUser;
    }

    @Override
    public void refreshData() {

    }

    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        if(signInSubscriber != null){
            signInSubscriber.unsubscribe();
            signInSubscriber = null;
        }
        createUser.unsubscribe();
    }

    public void signInWithGoogle(GoogleSignInAccount googleSignInAccount){
        signInSubscriber = new DefaultSubscriber<String>(view){
            @Override
            public void onNext(String userId) {
                super.onNext(userId);
                view.navigateUsers();
                view.hideProgress();

                FirebaseMessaging.getInstance().subscribeToTopic("user_" + userId);
                Log.d("Google Sign Account", userId);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(R.string.authentication_failed);
                view.hideProgress();
            }
        };
        authManager.signInGoogle(googleSignInAccount, signInSubscriber, createUser);
        Log.d("Google Sign in ID Token", googleSignInAccount.getIdToken());
    }
}
