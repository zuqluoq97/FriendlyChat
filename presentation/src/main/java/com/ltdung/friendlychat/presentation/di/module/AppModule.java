package com.ltdung.friendlychat.presentation.di.module;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ltdung.friendlychat.data.manager.AuthManager;
import com.ltdung.friendlychat.data.manager.AuthManagerImpl;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.data.manager.NetworkManagerImpl;
import com.ltdung.friendlychat.presentation.App;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.di.scope.AppScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class AppModule {

    private App app;

    public AppModule(App app){
        this.app = app;
    }

    @Provides
    @AppScope
    App provideAppContext(){
        return app;
    }

    @Provides
    @Named("Thread")
    @AppScope
    Scheduler providesThreadScheduler(){
        return Schedulers.io();
    }

    @Provides
    @Named("PostExecution")
    @AppScope
    Scheduler providesPostExecutionScheduler(){
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @AppScope
    GoogleApiClient providesGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(app.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return new GoogleApiClient.Builder(app)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Provides
    @AppScope
    NetworkManager providesNetworkManager(){
        return new NetworkManagerImpl(app);
    }

    @Provides
    @AppScope
    AuthManager providesAuthManager(GoogleApiClient googleApiClient) {
        return new AuthManagerImpl(googleApiClient);
    }
}
