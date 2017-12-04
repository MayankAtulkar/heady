package com.heady.modules;


import com.heady.network.RetrofitInterface;
import com.heady.scopes.UserScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


@Module
public class ECommModule {

    @Provides
    @UserScope
    public RetrofitInterface providesGitHubInterface(Retrofit retrofit) {
        return retrofit.create(RetrofitInterface.class);
    }
}
