package com.sadwyn.iceandfire;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = ApiModule.class)
public interface ComponentDagger {

    void inject(CharacterModelImpl model);
}
