package com.search.wiki.di.component;

import com.search.wiki.di.module.ActivityModule;
import com.search.wiki.di.scope.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {


}
