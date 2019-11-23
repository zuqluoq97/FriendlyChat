package com.ltdung.friendlychat.data.store.cache.realm;

import com.ltdung.friendlychat.data.entity.MessageEntity;
import com.ltdung.friendlychat.data.entity.realm.RealmMessageEntity;
import com.ltdung.friendlychat.data.mapper.realm.RealmMessageEntityMapper;
import com.ltdung.friendlychat.data.store.cache.MessageCache;

import java.util.List;

import io.realm.Realm;
import rx.Observable;

public class RealmMessageCache implements MessageCache {

    private Realm realm;

    private RealmMessageEntityMapper realmMessageEntityMapper;

    public RealmMessageCache(RealmMessageEntityMapper realmMessageEntityMapper) {
        this.realm = Realm.getDefaultInstance();
        this.realmMessageEntityMapper = realmMessageEntityMapper;
    }

    @Override
    public void saveMessages(List<MessageEntity> messageEntities) {
        realm.executeTransaction(realm1 -> {
            realm1.delete(RealmMessageEntity.class);
            realm1.copyToRealm(realmMessageEntityMapper.map1(messageEntities));
        });
    }

    @Override
    public Observable<List<MessageEntity>> getMessages(String peerId) {
        List<MessageEntity> messageEntities = realmMessageEntityMapper
                .map2(realm.where(RealmMessageEntity.class).findAll());
        return Observable.just(messageEntities);
    }

    @Override
    public Observable<Void> postMessage(MessageEntity message) {
        // cache does not support this method
        return null;
    }

    @Override
    public Observable<Void> editMessage(MessageEntity editedMessage) {
        // Cache does not support this method
        return null;
    }

    @Override
    public Observable<Void> deleteMessage(MessageEntity message) {
        // Cache does not support this method
        return null;
    }
}
