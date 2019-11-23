package com.ltdung.friendlychat.data.repository;

import com.ltdung.friendlychat.data.entity.MessageEntity;
import com.ltdung.friendlychat.data.mapper.MessageEntityDtoMapper;
import com.ltdung.friendlychat.data.store.MessageEntityStore;
import com.ltdung.friendlychat.data.store.cache.MessageCache;
import com.ltdung.friendlychat.data.store.cache.UserCache;
import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.MessageDto;
import com.ltdung.friendlychat.domain.listener.OnUserChangedListener;
import com.ltdung.friendlychat.domain.repository.MessageRepository;
import com.ltdung.friendlychat.domain.repository.UserRepository;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.data.repository.RepositoryImpl;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class MessageRepositoryImpl extends RepositoryImpl<MessageEntityStore, MessageCache, MessageEntityDtoMapper>
        implements MessageRepository {

    @Inject
    public MessageRepositoryImpl(NetworkManager networkManager,
                                 MessageEntityStore cloudStore,
                                 MessageCache cache,
                                 MessageEntityDtoMapper messageEntityDtoMapper) {
        super(networkManager, cloudStore, cache, messageEntityDtoMapper);
    }

    @Override
    public Observable<List<MessageDto>> getMessages(String peerId, Messenger messenger) {
        Observable<List<MessageEntity>> observable;
        if (networkManager.isNetworkAvailable()) {
            observable = cloudStore.getMessages(peerId).doOnNext(messageEntities -> cache.saveMessages(messageEntities));
        } else {
            observable = cache.getMessages(peerId).doOnNext(messagesEntities -> messenger.showFromCacheMessage());
        }
        return observable.map(messageEntities -> entityDtoMapper.map2(messageEntities));
    }

    @Override
    public Observable<Void> postMessage(MessageDto message, Messenger messenger) {
        if(networkManager.isNetworkAvailable()){
            return cloudStore.postMessage(entityDtoMapper.map1(message));
        }else{
            return Observable.<Void>empty().doOnCompleted(() -> messenger.showNoNetworkMessage());
        }
    }

    @Override
    public Observable<Void> editMessage(MessageDto editedMessage, Messenger messenger) {
        if(networkManager.isNetworkAvailable()){
            return cloudStore.editMessage(entityDtoMapper.map1(editedMessage));
        }else{
            return Observable.<Void>empty().doOnCompleted(() -> messenger.showNoNetworkMessage());
        }
    }

    @Override
    public Observable<Void> deleteMessage(MessageDto message, Messenger messenger) {
        return null;
    }
}
