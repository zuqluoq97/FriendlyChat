package com.ltdung.friendlychat.data.entity;

import com.ltdung.friendlychat.library.data.entity.Entity;

import lombok.Data;

@Data
public class MessageEntity implements Entity {

    private String id;

    private String senderId;

    private String receiverId;

    private String text;

    private long timestamp;
}
