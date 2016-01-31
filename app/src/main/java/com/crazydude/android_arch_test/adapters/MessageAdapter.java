package com.crazydude.android_arch_test.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazydude.android_arch_test.models.Message;

import java.util.ArrayList;

/**
 * Created by Crazy on 31.01.2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ArrayList<Message> mMessages = new ArrayList<>();

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        ((TextView) holder.itemView).setText(message.getMessage());
        if (message.isSent()) {
            ((TextView) holder.itemView).setTextColor(0xff000000);
        } else {
            ((TextView) holder.itemView).setTextColor(0xffaaaaaa);
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setData(ArrayList<Message> data) {
        mMessages = data;
        notifyDataSetChanged();
    }

    public void addData(Message data) {
        mMessages.add(data);
        notifyItemInserted(mMessages.size() - 1);
    }
}
