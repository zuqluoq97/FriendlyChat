package com.ltdung.friendlychat.data.mapper.realm;

import com.ltdung.friendlychat.data.entity.UserEntity;
import com.ltdung.friendlychat.data.entity.realm.RealmUserEntity;
import com.ltdung.friendlychat.library.data.mapper.BaseMapper;

import javax.inject.Inject;

public class RealmUserEntityMapper extends BaseMapper<RealmUserEntity, UserEntity> {

    @Inject
    public RealmUserEntityMapper() {
    }

    @Override
    public RealmUserEntity map1(UserEntity o2) {
        if (o2 == null) {
            return null;
        }
        RealmUserEntity realmUserEntity = new RealmUserEntity();
        realmUserEntity.setId(o2.getId());
        realmUserEntity.setFirstName(o2.getFirstName());
        realmUserEntity.setLastName(o2.getLastName());
        realmUserEntity.setAge(o2.getAge());
        realmUserEntity.setGender(o2.getGender());
        realmUserEntity.setLatitude(o2.getLatitude());
        realmUserEntity.setLongitude(o2.getLongitude());
        return realmUserEntity;
    }

    @Override
    public UserEntity map2(RealmUserEntity o1) {
        if (o1 == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(o1.getId());
        userEntity.setFirstName(o1.getFirstName());
        userEntity.setLastName(o1.getLastName());
        userEntity.setAge(o1.getAge());
        userEntity.setGender(o1.getGender());
        userEntity.setLatitude(o1.getLatitude());
        userEntity.setLongitude(o1.getLongitude());
        return userEntity;
    }
}
