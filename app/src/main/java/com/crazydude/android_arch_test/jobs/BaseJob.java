package com.crazydude.android_arch_test.jobs;

import com.crazydude.android_arch_test.di.components.ApplicationComponent;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

/**
 * Created by Crazy on 01.02.2016.
 */
public abstract class BaseJob extends Job {

    public BaseJob(Params params) {
        super(params);
    }

    abstract public void inject(ApplicationComponent component);
}
