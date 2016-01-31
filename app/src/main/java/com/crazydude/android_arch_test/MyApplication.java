package com.crazydude.android_arch_test;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.crazydude.android_arch_test.di.components.ApplicationComponent;
import com.crazydude.android_arch_test.di.components.DaggerApplicationComponent;
import com.crazydude.android_arch_test.di.module.AppModule;
import com.vk.sdk.VKSdk;


/**
 * Created by Crazy on 29.01.2016.
 */
public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
        mApplicationComponent = DaggerApplicationComponent.builder().appModule(new AppModule(this)).build();
        ActiveAndroid.initialize(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
