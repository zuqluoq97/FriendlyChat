package com.ltdung.friendlychat.domain.interactor;

import com.ltdung.friendlychat.domain.executor.PostExecutionThread;
import com.ltdung.friendlychat.domain.executor.ThreadExecutor;
import com.ltdung.friendlychat.domain.repository.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.functions.Action0;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UseCaseTest extends BaseUseCaseTest<UseCaseTest.TestUseCase, UseCaseTest.TestRepository>{

    private TestSubscriber<Integer> testSubscriber;

    @Before
    public void setUp(){
        super.setUp();
        testSubscriber = new TestSubscriber();
    }

    @Override
    public TestUseCase createUseCase() {
        return new TestUseCase(mockRepository, mockThreadExecutor, mockPostExecutionThread);
    }

    @Override
    public TestRepository createRepository() {
        TestRepository testRepository = mock(TestRepository.class);
        when(testRepository.getData()).thenReturn(Observable.empty());
        return testRepository;
    }

    @Test
    @Override
    public void testBuildUseCaseObservable() {
        testBuildUseCaseObservable(new Action0() {
            @Override
            public void call() {
                verify(mockRepository).getData();
            }
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void buildUseCaseObservable_AsCorrectResult(){
        TestScheduler testScheduler = new TestScheduler();
        given(mockPostExecutionThread.getScheduler()).willReturn(testScheduler);
        useCase.execute(testSubscriber);
        assertThat(testSubscriber.getOnNextEvents().size(), is(0));
    }

    static class TestUseCase extends UseCase<Integer, TestRepository> {

        TestUseCase(TestRepository repository,
                    ThreadExecutor threadExecutor,
                    PostExecutionThread postExecutionThread){
            super(repository, threadExecutor, postExecutionThread);
        }

        @Override
        protected Observable buildUseCaseObservable() {
            return repository.getData();
        }
    }

    interface TestRepository extends Repository{
        Observable getData();
    }
}
