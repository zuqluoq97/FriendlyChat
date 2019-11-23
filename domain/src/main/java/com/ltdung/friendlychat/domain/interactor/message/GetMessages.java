package com.ltdung.friendlychat.domain.interactor.message;

import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.MessageDto;
import com.ltdung.friendlychat.domain.interactor.UseCase;
import com.ltdung.friendlychat.domain.repository.MessageRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

public class GetMessages extends UseCase<String, List<MessageDto>, MessageRepository> {

    @Inject
    public GetMessages(MessageRepository repository,
                       Messenger messenger,
                       @Named("Thread") Scheduler threadScheduler,
                       @Named("PostExecution") Scheduler postExecutionScheduler) {
        super(repository, messenger, threadScheduler, postExecutionScheduler);
    }

    @Override
    protected Observable<List<MessageDto>> buildObservable(String peerId) {
        return repository.getMessages(peerId, messenger);
    }
}