package com.ltdung.friendlychat.domain.interactor.message;

import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.dto.MessageDto;
import com.ltdung.friendlychat.domain.interactor.UseCase;
import com.ltdung.friendlychat.domain.repository.MessageRepository;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

public class DeleteMessage extends UseCase<MessageDto, Void, MessageRepository> {

    @Inject
    public DeleteMessage(MessageRepository repository,
                         Messenger messenger,
                         @Named("Thread") Scheduler threadScheduler,
                         @Named("PostExecution") Scheduler postExecutionScheduler) {
        super(repository, messenger, threadScheduler, postExecutionScheduler);
    }

    @Override
    protected Observable<Void> buildObservable(MessageDto messageDto) {
        return repository.deleteMessage(messageDto, messenger);
    }
}
