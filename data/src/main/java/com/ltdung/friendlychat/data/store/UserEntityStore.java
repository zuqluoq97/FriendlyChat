package com.ltdung.friendlychat.data.store;

import com.ltdung.friendlychat.data.entity.UserEntity;
import com.ltdung.friendlychat.library.data.store.EntityStore;

import java.util.List;

import rx.Observable;

public interface UserEntityStore extends EntityStore {

    Observable<String> createUserIfNotExists(UserEntity userEntity);

    Observable<String> editUser(UserEntity userEntity);

    Observable<List<UserEntity>> getUsers();

    Observable<UserEntity> getUser(String userId);
}
