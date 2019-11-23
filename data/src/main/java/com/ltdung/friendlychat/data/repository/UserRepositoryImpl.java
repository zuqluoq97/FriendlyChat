package com.ltdung.friendlychat.data.repository;

import com.ltdung.friendlychat.data.entity.UserEntity;
import com.ltdung.friendlychat.data.mapper.UserEntityDtoMapper;
import com.ltdung.friendlychat.data.store.UserEntityStore;
import com.ltdung.friendlychat.data.store.cache.UserCache;
import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.UserDto;
import com.ltdung.friendlychat.domain.interactor.UseCase;
import com.ltdung.friendlychat.domain.listener.OnUserChangedListener;
import com.ltdung.friendlychat.domain.repository.UserRepository;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.data.repository.RepositoryImpl;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class UserRepositoryImpl extends RepositoryImpl<UserEntityStore, UserCache, UserEntityDtoMapper>
        implements UserRepository, OnUserChangedListener {

    @Inject
    public UserRepositoryImpl(NetworkManager networkManager,
                              UserEntityStore cloudStore,
                              UserCache cache,
                              UserEntityDtoMapper userEntityDtoMapper) {
        super(networkManager, cloudStore, cache, userEntityDtoMapper);
    }

    @Override
    public void onDataChanged(String userId) {
        Collection<UseCase> useCaseList = useCasesMap.values();
        for(UseCase useCase: useCaseList){
            if(useCase instanceof OnUserChangedListener){
                ((OnUserChangedListener) useCase).onDataChanged(userId);
            }
        }
    }

    @Override
    public Observable<String> createUserIfNotExists(UserDto user, Messenger messenger) {
        if(networkManager.isNetworkAvailable()) {
            return cloudStore.createUserIfNotExists(entityDtoMapper.map1(user)).doOnNext(this::onDataChanged);
        }else{
            return Observable.<String>empty().doOnCompleted(messenger::showNoNetworkMessage);
        }
    }

    @Override
    public Observable<String> editUser(UserDto userDto, Messenger messenger) {
        if(networkManager.isNetworkAvailable()){
            return cloudStore.editUser(entityDtoMapper.map1(userDto)).doOnNext(this::onDataChanged);
        }else{
            return Observable.<String>empty().doOnCompleted(messenger::showNoNetworkMessage);
        }
    }

    @Override
    public Observable<List<UserDto>> getUsers(Messenger messenger) {
        Observable<List<UserEntity>> entityObservable;
        if(networkManager.isNetworkAvailable()){
            entityObservable = cloudStore.getUsers().doOnNext(userEntities -> cache.saveUsers(userEntities));
        }else{
            entityObservable = cache.getUsers().doOnNext(userEntities -> messenger.showFromCacheMessage());
        }
        return entityObservable.map(userEntities -> entityDtoMapper.map2(userEntities));
    }

    @Override
    public Observable<UserDto> getUser(String userId, Messenger messenger) {
        Observable<UserEntity> entityObservable;
        if(networkManager.isNetworkAvailable()){
            entityObservable = cloudStore.getUser(userId)
                    .doOnNext(userEntity -> cache.saveUser(userId, userEntity));
        }else{
            entityObservable = cache.getUser(userId).doOnNext(userEntity -> messenger.showFromCacheMessage());
        }
        return entityObservable.map(userEntity -> entityDtoMapper.map2(userEntity));
    }
}
