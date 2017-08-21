package com.boilerplate.injection.component;

import com.boilerplate.injection.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {
}