package com.ltdung.friendlychat.domain.interactor;

import com.ltdung.friendlychat.domain.Messenger;
import com.ltdung.friendlychat.domain.repository.Repository;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;

public abstract class UseCase1<RESPONSE_DATA, REPOSITORY extends Repository>
        extends UseCase<Void, RESPONSE_DATA, REPOSITORY>  {

    public UseCase1(REPOSITORY repository,
                    Messenger messenger,
                    Scheduler threadScheduler,
                    Scheduler postExecutionScheduler) {
        super(repository, messenger, threadScheduler, postExecutionScheduler);
    }

    @Override
    protected Observable<RESPONSE_DATA> buildObservable(Void aVoid) {
      return buildObservable();
    }

    protected abstract Observable<RESPONSE_DATA> buildObservable();

    public void execute(Subscriber<RESPONSE_DATA> useCaseSubscriber){
        super.execute(null, useCaseSubscriber);
    }
}
