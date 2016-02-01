package com.crazydude.android_arch_test.di.module;

import android.util.Log;

import com.crazydude.android_arch_test.MyApplication;
import com.crazydude.android_arch_test.di.components.ApplicationComponent;
import com.crazydude.android_arch_test.jobs.BaseJob;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.path.android.jobqueue.log.CustomLogger;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Crazy on 31.01.2016.
 */
@Module
public class AppModule {

    private MyApplication mMyApplication;

    public AppModule(MyApplication myApplication) {
        mMyApplication = myApplication;
    }

    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Provides
    @Singleton
    public JobManager provideJobManager(ApplicationComponent component) {
        return new JobManager(mMyApplication, new Configuration.Builder(mMyApplication)
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(component);
                        }
                    }
                })
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .build());
    }

    @Provides
    @Singleton
    public MyApplication provideMyApplication() {
        return mMyApplication;
    }
}
