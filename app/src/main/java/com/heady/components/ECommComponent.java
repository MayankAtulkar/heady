package com.heady.components;

import com.heady.activity.MainActivity;
import com.heady.modules.ECommModule;
import com.heady.scopes.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = NetComponent.class, modules = ECommModule.class)
public interface ECommComponent {
    void inject(MainActivity activity);
}
