package com.ltdung.friendlychat.presentation.di.module;

import com.ltdung.friendlychat.data.mapper.MessageEntityDtoMapper;
import com.ltdung.friendlychat.data.mapper.UserEntityDtoMapper;
import com.ltdung.friendlychat.data.repository.MessageRepositoryImpl;
import com.ltdung.friendlychat.data.repository.UserRepositoryImpl;
import com.ltdung.friendlychat.data.store.MessageEntityStore;
import com.ltdung.friendlychat.data.store.UserEntityStore;
import com.ltdung.friendlychat.data.store.cache.MessageCache;
import com.ltdung.friendlychat.data.store.cache.UserCache;
import com.ltdung.friendlychat.domain.repository.MessageRepository;
import com.ltdung.friendlychat.domain.repository.UserRepository;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.presentation.di.scope.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @AppScope
    UserRepository providesUserRepository(NetworkManager networkManager,
                                          UserEntityStore userEntityStore,
                                          UserCache userCache,
                                          UserEntityDtoMapper userEntityDtoMapper){
        return new UserRepositoryImpl(networkManager, userEntityStore, userCache, userEntityDtoMapper);
    }

    @Provides
    @AppScope
    MessageRepository providesMessageRepository(NetworkManager networkManager,
                                                MessageEntityStore messageEntityStore,
                                                MessageCache messageCache,
                                                MessageEntityDtoMapper messageEntityDtoMapper) {
        return new MessageRepositoryImpl(networkManager, messageEntityStore, messageCache, messageEntityDtoMapper);
    }
}
