package com.sadwyn.iceandfire;
import com.sadwyn.iceandfire.network.Api;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {
    @Provides
    @Singleton
    Api provideApi() {
        return InstanceApi.getApi();
    }
}
