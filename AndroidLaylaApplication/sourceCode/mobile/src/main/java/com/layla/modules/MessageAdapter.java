package com.layla.modules;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.layla.*;

import java.util.List;

public class MessageAdapter extends BaseAdapter
{
    private Context context;
    private List<ChatMessage> messagesItems;

    public MessageAdapter(Context context, List<ChatMessage> messagesItems)
    {
        this.context = context;
        this.messagesItems = messagesItems;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ChatMessage m = messagesItems.get(i);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Identifying the message owner
        if(messagesItems.get(i).isSelf())
        {
            // message belongs to you, so load the right aligned layout
            view = mInflater.inflate(R.layout.list_item_message_right, null);
        }
        else
        {
            // message belongs to other person, load the left aligned layout
            view = mInflater.inflate(R.layout.list_item_message_left, null);
        }

        TextView txtMsg = view.findViewById(R.id.txtMsg);
        txtMsg.setText(m.getMessage());

        return view;
    }

    @Override
    public int getCount()
    {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int i)
    {
        return messagesItems.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }
}
