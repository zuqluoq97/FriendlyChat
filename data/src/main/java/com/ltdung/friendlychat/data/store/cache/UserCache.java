package com.ltdung.friendlychat.data.store.cache;

import com.ltdung.friendlychat.data.entity.UserEntity;
import com.ltdung.friendlychat.data.store.UserEntityStore;
import com.ltdung.friendlychat.library.data.store.cache.Cache;

import java.util.List;

public interface UserCache extends UserEntityStore, Cache {

    void saveUser(String userId, UserEntity userEntity);

    void saveUsers(List<UserEntity> userEntityList);
}
