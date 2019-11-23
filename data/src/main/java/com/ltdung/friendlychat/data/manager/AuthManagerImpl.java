package com.ltdung.friendlychat.data.manager;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ltdung.friendlychat.domain.dto.UserDto;
import com.ltdung.friendlychat.domain.interactor.user.CreateUser;

import io.realm.Realm;
import rx.Subscriber;

public class AuthManagerImpl implements AuthManager {

    private FirebaseAuth auth;

    private GoogleApiClient googleApiClient;

    public AuthManagerImpl(GoogleApiClient googleApiClient){
        this.googleApiClient = googleApiClient;
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signInGoogle(GoogleSignInAccount account, Subscriber<String> subscriber, CreateUser createUser) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(!subscriber.isUnsubscribed()){
                        if(task.isSuccessful()){
                            saveUser(task.getResult().getUser(), subscriber, createUser);
                        }
                    }
                });
    }

    @Override
    public void signOut(Subscriber<String> subscriber) {
        String id = getCurrentUserId();
        auth.signOut();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                        status -> {
                            googleApiClient.disconnect();
                            googleApiClient.unregisterConnectionCallbacks(this);
                            if(!subscriber.isUnsubscribed()){
                                if(status.isSuccess()){
                                    deleteCache();
                                    subscriber.onNext(id);
                                }else{
                                    subscriber.onError(new AuthException());
                                }
                            }
                });
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });

        googleApiClient.connect();
    }

    @Override
    public boolean isSignedIn() {
        return auth.getCurrentUser() != null;
    }

    @Override
    public String getCurrentUserId() {
        String id = null;
        if(!isSignedIn()){
            id = auth.getCurrentUser().getUid();
        }
        return id;
    }

    private void saveUser(FirebaseUser user, Subscriber<String> subscriber, CreateUser createUser){
        UserDto userDto = new UserDto();
        userDto.setId(user.getUid());
        if(!TextUtils.isEmpty(user.getDisplayName())){
            String[] name = user.getDisplayName().split("");
            userDto.setFirstName(name[0]);
            userDto.setLastName(name[1]);
        }

        createUser.execute(userDto, subscriber);
    }

    private void deleteCache(){
        Realm.getDefaultInstance().executeTransaction(realm -> realm.deleteAll());
    }
}
