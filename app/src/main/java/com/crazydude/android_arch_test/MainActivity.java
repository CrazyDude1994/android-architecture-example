package com.crazydude.android_arch_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.crazydude.android_arch_test.adapters.MessageAdapter;
import com.crazydude.android_arch_test.di.components.ApplicationComponent;
import com.crazydude.android_arch_test.events.AddedNewMessageEvent;
import com.crazydude.android_arch_test.events.DeliveredMessageEvent;
import com.crazydude.android_arch_test.events.RemovedMessageEvent;
import com.crazydude.android_arch_test.jobs.SendMessageJob;
import com.crazydude.android_arch_test.models.Message;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements VKCallback<VKAccessToken>, View.OnClickListener {

    private Button mLoginButton;
    private Button mSendMessageButton;
    private RecyclerView mMessagesRecycler;
    private MessageAdapter mMessageAdapter;

    @Inject
    JobManager mJobManager;

    @Inject
    Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = (Button) findViewById(R.id.activity_main_login_button);
        mSendMessageButton = (Button) findViewById(R.id.activity_main_send_message_button);
        mMessagesRecycler = (RecyclerView) findViewById(R.id.activity_main_messages_recycler);

        mLoginButton.setOnClickListener(this);
        mSendMessageButton.setOnClickListener(this);

        getApplicationComponent().inject(this);

        initRecyclerView();

        refresh(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKSdk.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onResult(VKAccessToken res) {

    }

    @Override
    public void onError(VKError error) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_login_button:
                VKSdk.login(this, "messages");
                break;
            case R.id.activity_main_send_message_button:
                mJobManager.addJobInBackground(new SendMessageJob("HELLO YOU FUCK XDDD" + new Random().nextInt()));
                break;
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return ((MyApplication) getApplication()).getApplicationComponent();
    }

    @Subscribe
    public void onAddedMessage(AddedNewMessageEvent event) {
        refresh(event.getMessage());
    }

    @Subscribe
    public void onDeliveredMessage(DeliveredMessageEvent event) {
        refresh(event.getMessage());
    }

    @Subscribe
    public void onRemovedMessage(RemovedMessageEvent event) {
        refresh(event.getMessage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfLoggedIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBus.unregister(this);
    }

    private void refresh(Message lastMessage) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Message> messages = new ArrayList(Message.getAllMessages());
                mMessageAdapter.setData(new ArrayList<>(messages));
            }
        });
    }

    private void initRecyclerView() {
        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageAdapter = new MessageAdapter();
        mMessagesRecycler.setAdapter(mMessageAdapter);
    }

    private void checkIfLoggedIn() {
        if (VKSdk.isLoggedIn()) {
            getSupportActionBar().setTitle("Logged in as ...");
            VKApi.users().get().executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    String name = ((VKList<VKApiUserFull>) response.parsedModel).get(0).first_name;
                    getSupportActionBar().setTitle("Logged in as " + name);
                }
            });
            mLoginButton.setVisibility(View.GONE);
        }
    }
}
