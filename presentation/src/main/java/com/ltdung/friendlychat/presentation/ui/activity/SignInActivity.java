package com.ltdung.friendlychat.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.databinding.ActivitySignInBinding;
import com.ltdung.friendlychat.presentation.di.component.ViewComponent;
import com.ltdung.friendlychat.presentation.mvp.presenter.SignInPresenter;
import com.ltdung.friendlychat.presentation.mvp.view.SignInView;
import com.ltdung.friendlychat.presentation.mvp.view.impl.SignInViewImpl;

import javax.inject.Inject;

import dagger.Lazy;

public class SignInActivity extends BaseDaggerActivity<SignInView, SignInPresenter, ActivitySignInBinding> {

    static final int RC_SIGN_IN = 9001;

    @Inject
    Lazy<SignInPresenter> signInPresenter;

    @Inject
    GoogleApiClient googleApiClient;

    public static void start(Context context){
        Intent intent = BaseActivity.getBaseStartIntent(context, SignInActivity.class, false);
        context.startActivity(intent);
    }

    @Override
    public void onLoadFinished() {
        super.onLoadFinished();
        googleApiClient.connect();
        binding.btnSignInGoogle.setOnClickListener((v) -> onClickSignInGoogle(v));
    }

    @Override
    public void onLoadReset() {
        super.onLoadReset();
        googleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            view.hideProgress();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                view.showProgress(getString(R.string.authenticating));
                GoogleSignInAccount account = result.getSignInAccount();
                presenter.signInWithGoogle(account);
            }
        }
    }

    @Override
    protected void injectViewComponent(ViewComponent viewComponent) {
        viewComponent.inject(this);
    }

    @Override
    protected SignInView initView() {
        return new SignInViewImpl(this) {
            @Override
            public void navigateUsers() {
                UsersActivity.start(SignInActivity.this);
                finish();
            }
        };
    }

    @Override
    protected Lazy<SignInPresenter> initPresenter() {
        return signInPresenter;
    }

    @Override
    protected ActivitySignInBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
    }

    public void onClickSignInGoogle(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        googleApiClientSignOut();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        view.showProgress();
    }

    private void googleApiClientSignOut() {
        try {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> Log.d("Google Api Client signed out", status.toString()));
        } catch (Exception e) {
        }
    }

}
