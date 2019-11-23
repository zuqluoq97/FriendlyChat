package com.ltdung.friendlychat.domain.interactor;

import com.ltdung.friendlychat.domain.repository.UserRepository;

import org.junit.Test;

import rx.functions.Action0;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GetUsersTest extends BaseUseCaseTest<GetUsers, UserRepository>{

    @Override
    public GetUsers createUseCase() {
        return new GetUsers(mockRepository, mockThreadExecutor, mockPostExecutionThread);
    }

    @Override
    public UserRepository createRepository() {
        return mock(UserRepository.class);
    }

    @Test
    @Override
    public void testBuildUseCaseObservable() {
        testBuildUseCaseObservable(new Action0() {
            @Override
            public void call() {
                verify(mockRepository).getUsers();
            }
        });
    }
}
