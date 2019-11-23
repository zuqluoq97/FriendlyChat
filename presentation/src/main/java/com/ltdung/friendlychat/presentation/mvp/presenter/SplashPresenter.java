package com.ltdung.friendlychat.presentation.mvp.presenter;

import com.ltdung.friendlychat.data.manager.AuthManager;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;
import com.ltdung.friendlychat.presentation.di.scope.ViewScope;
import com.ltdung.friendlychat.presentation.mvp.view.SplashView;

import javax.inject.Inject;

@ViewScope
public class SplashPresenter extends BasePresenter<SplashView> {

    private AuthManager authManager;

    @Inject
    public SplashPresenter(NetworkManager networkManager, AuthManager authManager) {
        super(networkManager);
        this.authManager = authManager;
    }

    @Override
    protected void onViewAttached() {
        super.onViewDetached();
        refreshData();
    }

    @Override
    public void refreshData() {
        chooseNavigation();
    }

    private void chooseNavigation(){
        if(authManager.isSignedIn()){
            view.navigateToUsers();
        }else{
            view.navigateToLogin();
        }
    }
}
