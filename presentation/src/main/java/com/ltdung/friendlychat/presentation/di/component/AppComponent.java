package com.ltdung.friendlychat.presentation.di.component;


import com.ltdung.friendlychat.presentation.App;
import com.ltdung.friendlychat.presentation.di.module.AppModule;
import com.ltdung.friendlychat.presentation.di.module.CacheModule;
import com.ltdung.friendlychat.presentation.di.module.EntityStoreModule;
import com.ltdung.friendlychat.presentation.di.module.RepositoryModule;
import com.ltdung.friendlychat.presentation.di.module.ViewModule;
import com.ltdung.friendlychat.presentation.di.scope.AppScope;

import dagger.Component;

@AppScope
@Component(modules = {AppModule.class,
        RepositoryModule.class,
        EntityStoreModule.class,
        CacheModule.class})
public interface AppComponent {

    // Get Children component
    ViewComponent plus(ViewModule viewModule);

    void inject(App app);
}
