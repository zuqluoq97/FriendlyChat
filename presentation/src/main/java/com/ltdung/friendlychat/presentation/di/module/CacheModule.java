package com.ltdung.friendlychat.presentation.di.module;

import com.ltdung.friendlychat.data.mapper.realm.RealmMessageEntityMapper;
import com.ltdung.friendlychat.data.mapper.realm.RealmUserEntityMapper;
import com.ltdung.friendlychat.data.store.cache.MessageCache;
import com.ltdung.friendlychat.data.store.cache.UserCache;
import com.ltdung.friendlychat.data.store.cache.realm.RealmMessageCache;
import com.ltdung.friendlychat.data.store.cache.realm.RealmUserCache;
import com.ltdung.friendlychat.presentation.di.scope.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class CacheModule {

    @Provides
    @AppScope
    UserCache provideUserCache(RealmUserEntityMapper realmUserEntityMapper){
        return new RealmUserCache(realmUserEntityMapper);
    }

    @Provides
    @AppScope
    MessageCache provideMessageCache(RealmMessageEntityMapper realmMessageEntityMapper){
        return new RealmMessageCache(realmMessageEntityMapper);
    }
}
