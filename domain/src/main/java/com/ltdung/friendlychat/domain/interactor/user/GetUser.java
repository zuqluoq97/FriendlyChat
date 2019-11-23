package com.ltdung.friendlychat.domain.interactor.user;

import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.UserDto;
import com.ltdung.friendlychat.domain.interactor.UseCase;
import com.ltdung.friendlychat.domain.listener.OnUserChangedListener;
import com.ltdung.friendlychat.domain.repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Setter;
import rx.Observable;
import rx.Scheduler;

public class GetUser extends UseCase<String, UserDto, UserRepository>
        implements OnUserChangedListener{

    @Setter
    private OnUserChangedListener onUserChangedListener;

    @Inject
    public GetUser(UserRepository repository,
                   Messenger messenger,
                   @Named("Thread") Scheduler threadScheduler,
                   @Named("PostExecution") Scheduler postExecutionScheduler) {
        super(repository, messenger, threadScheduler, postExecutionScheduler);
    }

    @Override
    protected Observable<UserDto> buildObservable(String userId) {
        return repository.getUser(userId, messenger);
    }

    @Override
    public void onDataChanged(String userId) {
        if(onUserChangedListener != null){
            onUserChangedListener.onDataChanged(userId);
        }
    }
}
