package com.ltdung.friendlychat.domain.repository;

import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.MessageDto;

import java.util.List;

import rx.Observable;

public interface MessageRepository extends Repository {

    Observable<List<MessageDto>> getMessages(String peerId, Messenger messenger);

    Observable<Void> postMessage(MessageDto message, Messenger messenger);

    Observable<Void> editMessage(MessageDto message, Messenger messenger);

    Observable<Void> deleteMessage(MessageDto message, Messenger messenger);
}
