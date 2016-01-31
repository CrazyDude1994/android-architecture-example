package com.crazydude.android_arch_test.di.components;

import com.crazydude.android_arch_test.MainActivity;
import com.crazydude.android_arch_test.di.module.AppModule;
import com.crazydude.android_arch_test.jobs.SendMessageJob;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Crazy on 31.01.2016.
 */
@Singleton
@Component(modules = AppModule.class)
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);

    void inject(SendMessageJob sendMessageJob);

}
