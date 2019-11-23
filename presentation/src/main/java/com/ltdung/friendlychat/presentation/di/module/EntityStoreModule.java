package com.ltdung.friendlychat.presentation.di.module;

import com.ltdung.friendlychat.data.manager.AuthManager;
import com.ltdung.friendlychat.data.repository.UserRepositoryImpl;
import com.ltdung.friendlychat.data.store.MessageEntityStore;
import com.ltdung.friendlychat.data.store.UserEntityStore;
import com.ltdung.friendlychat.data.store.firebase.FirebaseMessageEntityStore;
import com.ltdung.friendlychat.data.store.firebase.FirebaseUserEntityStore;
import com.ltdung.friendlychat.presentation.App;
import com.ltdung.friendlychat.presentation.di.scope.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class EntityStoreModule {
    @Provides
    @AppScope
    UserEntityStore providesUserEntityStore(){
        return new FirebaseUserEntityStore();
    }

    @Provides
    @AppScope
    MessageEntityStore providesMessageEntityStore(AuthManager authManager, App app){
        return new FirebaseMessageEntityStore(authManager, app);
    }
}
