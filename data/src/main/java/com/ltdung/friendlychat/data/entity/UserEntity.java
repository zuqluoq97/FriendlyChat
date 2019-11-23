package com.ltdung.friendlychat.data.entity;

import com.ltdung.friendlychat.library.data.entity.Entity;

import lombok.Data;

@Data
public class UserEntity implements Entity {

    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

    private String id;

    private String firstName;

    private String lastName;

    private int age;

    private String gender;

    private float latitude;

    private float longitude;
}
