package com.ltdung.friendlychat.presentation;

import android.app.Application;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ltdung.friendlychat.data.manager.AuthManager;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.presentation.di.component.AppComponent;
import com.ltdung.friendlychat.presentation.di.component.DaggerAppComponent;
import com.ltdung.friendlychat.presentation.di.module.AppModule;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    @Inject
    NetworkManager networkManager;

    @Inject
    AuthManager authManager;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        initializeInjector();
        initializeLeakDetector();
        networkManager.start();

        if (authManager.isSignedIn()) {
            Log.d("LoginInUser", authManager.getCurrentUserId());
            FirebaseMessaging.getInstance().subscribeToTopic("user_" + authManager.getCurrentUserId());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        networkManager.stop();
        if (authManager.isSignedIn()) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + authManager.getCurrentUserId());
        }
    }

    private void initRealm() {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    private void initializeLeakDetector() {
        LeakCanary.install(this);
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }

}
