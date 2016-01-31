package com.crazydude.android_arch_test.events;

import com.crazydude.android_arch_test.models.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Crazy on 31.01.2016.
 */
@Data
@Accessors(prefix = "m")
@AllArgsConstructor
public class AddedNewMessageEvent {

    private Message mMessage;
}
