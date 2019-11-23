package com.ltdung.friendlychat.data.store.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.ltdung.friendlychat.data.entity.UserEntity;
import com.ltdung.friendlychat.data.store.UserEntityStore;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class FirebaseUserEntityStore extends FirebaseEntityStore implements UserEntityStore {

    private final String CHILD_USERS = "users";

    @Inject
    public FirebaseUserEntityStore(){
    }

    @Override
    public Observable<String> createUserIfNotExists(UserEntity userEntity) {
        DatabaseReference reference = database.child(CHILD_USERS).child(userEntity.getId());
        return createIfNotExists(reference, userEntity, userEntity.getId());
    }

    @Override
    public Observable<String> editUser(UserEntity userEntity) {
        DatabaseReference reference = database.child(CHILD_USERS).child(userEntity.getId());
        return update(reference, userEntity, userEntity.getId());
    }

    @Override
    public Observable<List<UserEntity>> getUsers() {
        Query query = database.child(CHILD_USERS).orderByKey();
        return getList(query, UserEntity.class, true);
    }

    @Override
    public Observable<UserEntity> getUser(String userId) {
        Query query = database.child(CHILD_USERS).child(userId);
        return get(query, UserEntity.class, true);
    }
}
