package com.crazydude.android_arch_test.events;

import com.crazydude.android_arch_test.models.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Crazy on 01.02.2016.
 */
@AllArgsConstructor
@Data
@Accessors(prefix = "m")
public class RemovedMessageEvent {

    private Message mMessage;
}
