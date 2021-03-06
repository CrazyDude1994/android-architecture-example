package com.crazydude.android_arch_test.jobs;

import com.crazydude.android_arch_test.di.components.ApplicationComponent;
import com.crazydude.android_arch_test.events.AddedNewMessageEvent;
import com.crazydude.android_arch_test.events.DeliveredMessageEvent;
import com.crazydude.android_arch_test.events.RemovedMessageEvent;
import com.crazydude.android_arch_test.models.Message;
import com.path.android.jobqueue.Params;
import com.squareup.otto.Bus;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import javax.inject.Inject;


/**
 * Created by Crazy on 29.01.2016.
 */
public class SendMessageJob extends BaseJob {

    @Inject
    transient Bus mBus;

    private String mText;
    private long mLocalId;

    public SendMessageJob(String text) {
        super(new Params(1).requireNetwork().persist().groupBy("messages"));
        mText = text;
        Message message = new Message(mText, System.currentTimeMillis(), false);
        message.save();
        mLocalId = message.getId();
    }

    @Override
    public void onAdded() {
        mBus.post(new AddedNewMessageEvent(null));
    }

    @Override
    public void onRun() throws Throwable {
        if (VKSdk.isLoggedIn()) {
            VKRequest vkRequest = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID,
                    "13829742", VKApiConst.MESSAGE, mText));

            vkRequest.executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    Message lastMessage = Message.load(Message.class, mLocalId);
                    lastMessage.setSent(true);
                    lastMessage.save();
                    mBus.post(new DeliveredMessageEvent(lastMessage));
                }

                @Override
                public void onError(VKError error) {
                    Message.load(Message.class, mLocalId).delete();
                    mBus.post(new RemovedMessageEvent(null));
                }
            });
        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void inject(ApplicationComponent component) {
        component.inject(this);
    }
}
