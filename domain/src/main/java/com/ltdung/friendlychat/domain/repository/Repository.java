package com.ltdung.friendlychat.domain.repository;

import com.ltdung.friendlychat.domain.interactor.UseCase;

public interface Repository {

    void register(UseCase useCase);

    void unregister(UseCase useCase);
}
