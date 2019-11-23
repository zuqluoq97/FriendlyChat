package com.ltdung.friendlychat.library.data.repository;

import com.ltdung.friendlychat.domain.interactor.UseCase;
import com.ltdung.friendlychat.domain.repository.Repository;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.data.store.cache.Cache;
import com.ltdung.friendlychat.library.data.mapper.BaseMapper;
import com.ltdung.friendlychat.library.data.store.EntityStore;

import java.util.HashMap;
import java.util.Map;

public class RepositoryImpl <ENTITY_STORE extends EntityStore,
        CACHE extends Cache, ENTITY_DTO_MAPPER extends BaseMapper> implements Repository {
    protected NetworkManager networkManager;

    protected ENTITY_STORE cloudStore;

    protected CACHE cache;

    protected ENTITY_DTO_MAPPER entityDtoMapper;

    protected final Map<String, UseCase> useCasesMap = new HashMap<>();

    public RepositoryImpl(NetworkManager networkManager,
                          ENTITY_STORE cloudStore,
                          CACHE cache,
                          ENTITY_DTO_MAPPER entityDtoMapper) {
        this.networkManager = networkManager;
        this.cloudStore = cloudStore;
        this.cache = cache;
        this.entityDtoMapper = entityDtoMapper;
    }


    @Override
    public void register(UseCase useCase) {
        if(useCase != null){
            useCasesMap.put(useCase.toString(), useCase);
        }
    }

    @Override
    public void unregister(UseCase useCase) {
        if(useCase != null){
            useCasesMap.remove(useCase.toString());
        }
    }
}
