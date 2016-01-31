package com.crazydude.android_arch_test.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Crazy on 29.01.2016.
 */
@Table(name = "Messages")
public class Message extends Model {

    @Column
    private String message;
    @Column
    private long timestamp;
    @Column
    private boolean isSent;
    @Column
    private long serverId;

    public Message() {
        super();
    }

    public Message(String message, long timestamp, boolean isSent) {
        super();
        this.message = message;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }

    public static List<Message> getAllMessages() {
        return new Select().from(Message.class).execute();
    }

    public static Message getFirstNotSentMessage() {
        return new Select().from(Message.class).where("isSent = ?", false).orderBy("timestamp ASC").executeSingle();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }
}
