package com.ltdung.friendlychat.domain.interactor;

import com.ltdung.friendlychat.domain.executor.PostExecutionThread;
import com.ltdung.friendlychat.domain.executor.ThreadExecutor;
import com.ltdung.friendlychat.domain.interactor.UseCase;
import com.ltdung.friendlychat.domain.repository.Repository;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.functions.Action0;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseUseCaseTest<USECASE extends UseCase, REPOSITORY extends Repository> {

    public USECASE useCase;

    public REPOSITORY mockRepository;

    @Mock
    public ThreadExecutor mockThreadExecutor;

    @Mock
    public PostExecutionThread mockPostExecutionThread;

    @Before
    public void setUp(){
        mockRepository = createRepository();
        useCase = createUseCase();
    }

    public abstract USECASE createUseCase();

    public abstract REPOSITORY createRepository();

    public abstract void testBuildUseCaseObservable();

    public void testBuildUseCaseObservable(Action0 action){
        useCase.buildUseCaseObservable(); // Call method in UseCase
        action.call(); // Verify method is run
        verifyNoMoreInteractions(mockRepository);
        verifyZeroInteractions(mockPostExecutionThread);
        verifyZeroInteractions(mockThreadExecutor);
    }
}
