package com.psycheval.testapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends ArrayAdapter<Message> {

    private Context mContext;
    private int mResource;

    public MessageListAdapter(Context context, int resource,  ArrayList<Message> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        String text = getItem(position).getMessage();
        String sentByWho = getItem(position).getSentByWho();

        Message message = new Message(text,sentByWho);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvMessage = convertView.findViewById(R.id.tvMessage);
        TextView tvSentBy = convertView.findViewById(R.id.tvSentBy);

        tvMessage.setText(text);
        tvSentBy.setText(sentByWho);

        return convertView;
    }
}
