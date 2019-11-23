package com.ltdung.friendlychat.domain.interactor.user;

import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.UserDto;
import com.ltdung.friendlychat.domain.interactor.UseCase1;
import com.ltdung.friendlychat.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

public class GetUsers extends UseCase1<List<UserDto>, UserRepository> {

    @Inject
    public GetUsers(UserRepository repository,
                    Messenger messenger,
                    @Named("Thread")Scheduler threadScheduler,
                    @Named("PostExecution") Scheduler postExecutionScheduler) {
        super(repository, messenger, threadScheduler, postExecutionScheduler);
    }

    @Override
    protected Observable<List<UserDto>> buildObservable() {
        return repository.getUsers(messenger);
    }
}
