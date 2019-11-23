package com.ltdung.friendlychat.data.store;

import com.ltdung.friendlychat.data.entity.MessageEntity;
import com.ltdung.friendlychat.library.data.store.EntityStore;

import java.util.List;

import rx.Observable;

public interface MessageEntityStore extends EntityStore {
    Observable<List<MessageEntity>> getMessages(String peerId);

    Observable<Void> postMessage(MessageEntity message);

    Observable<Void> editMessage(MessageEntity editedMessage);

    Observable<Void> deleteMessage(MessageEntity message);
}
