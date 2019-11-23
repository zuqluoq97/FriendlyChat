package com.ltdung.friendlychat.domain.interactor;

import com.ltdung.friendlychat.domain.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetUserTest extends BaseUseCaseTest<GetUser, UserRepository> {
    private static final String FAKE_USER_ID = "123";

    @Override
    public GetUser createUseCase() {
        return new GetUser(FAKE_USER_ID,
                mockRepository,
                mockThreadExecutor,
                mockPostExecutionThread);
    }

    @Override
    public UserRepository createRepository() {
        return mock(UserRepository.class);
    }

    @Test
    @Override
    public void testBuildUseCaseObservable() {
        testBuildUseCaseObservable(() ->
                Mockito.verify(mockRepository).getUser(FAKE_USER_ID));
    }
}
