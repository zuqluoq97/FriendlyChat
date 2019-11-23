package com.ltdung.friendlychat.data.store.cache;

import com.ltdung.friendlychat.data.entity.MessageEntity;
import com.ltdung.friendlychat.data.store.MessageEntityStore;
import com.ltdung.friendlychat.library.data.store.cache.Cache;

import java.util.List;

public interface MessageCache extends MessageEntityStore, Cache {
    void saveMessages(List<MessageEntity> messageEntities);
}
