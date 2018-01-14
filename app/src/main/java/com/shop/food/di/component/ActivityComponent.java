package com.shop.food.di.component;

import com.shop.food.auth.view.LoginActivity;
import com.shop.food.di.module.ActivityModule;
import com.shop.food.di.scope.PerActivity;

import dagger.Component;
/**
 * Created by Krishna on 1/2/2018.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity loginActivity);

}
