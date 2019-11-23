package com.ltdung.friendlychat.domain.repository;

import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.UserDto;

import java.util.List;

import rx.Observable;

public interface UserRepository extends Repository{

    Observable<String> createUserIfNotExists(UserDto user, Messenger messenger);

    Observable<String> editUser(UserDto userDto, Messenger messenger);

    Observable<List<UserDto>> getUsers(Messenger messenger);

    Observable<UserDto> getUser(final String userId, Messenger messenger);
}