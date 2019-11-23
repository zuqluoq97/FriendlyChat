package com.ltdung.friendlychat.presentation.mvp.model;

import com.ltdung.friendlychat.domain.dto.Gender;

import org.parceler.Parcel;

import lombok.Data;

@Data
@Parcel
public class UserModel {

    String id;

    String firstName;

    String lastName;

    int age;

    Gender gender;

    float latitude;

    float longitude;
}
