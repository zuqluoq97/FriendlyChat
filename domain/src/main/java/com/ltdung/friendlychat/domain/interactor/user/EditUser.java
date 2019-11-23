package com.ltdung.friendlychat.domain.interactor.user;

import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.UserDto;
import com.ltdung.friendlychat.domain.interactor.UseCase;
import com.ltdung.friendlychat.domain.repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

public class EditUser extends UseCase<UserDto, String, UserRepository> {

    @Inject
    public EditUser(UserRepository repository,
                    Messenger messenger,
                    @Named("Thread") Scheduler threadScheduler,
                    @Named("PostExecution") Scheduler postExecutionScheduler) {
        super(repository, messenger, threadScheduler, postExecutionScheduler);
    }

    @Override
    protected Observable<String> buildObservable(UserDto userDto) {
        return repository.editUser(userDto, messenger);
    }
}
