package com.layla.modules;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.layla.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Chat extends AppCompatActivity
{
    private String secondUser;
    private List<ChatMessage> messages = new ArrayList<>();
    private MessageAdapter arrayAdapter;
    static final int POLL_INTERVAL = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getUsername();

        retainMessages();

        setContentView(R.layout.activity_chat);

        setToolbar();
        setMessageAdapter();
        refreshMessages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK) finish();

        return super.onKeyDown(keyCode, event);
    }

    public void refreshMessages()
    {
         timer = new Timer();
         timer.schedule(new TimerTask() {
            @Override
            public void run() {
                retainMessages();
            }
        }, 1000, 1000);

    }

    public void retainMessages()
    {
        ParseQuery<ParseObject> firstQuery = new ParseQuery<>("ChatMessage");
        firstQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        firstQuery.whereEqualTo("recipient", secondUser);

        ParseQuery<ParseObject> secondQuery = new ParseQuery<>("ChatMessage");
        secondQuery.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
        secondQuery.whereEqualTo("sender", secondUser);

        List<ParseQuery<ParseObject>> queries = new ArrayList<>();

        queries.add(firstQuery);
        queries.add(secondQuery);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);

        query.orderByAscending("createdAt");

        query.findInBackground((objects, e) ->
        {
            if(e == null)
            {
                if(objects.size() > 0)
                {
                    messages.clear();

                    for(ParseObject message : objects)
                    {
                        String messageContent = message.getString("message");

                        if(message.getString("sender").equals(ParseUser.getCurrentUser().getUsername()))
                        {
                            ChatMessage m = new ChatMessage(ParseUser.getCurrentUser().getUsername(), messageContent, true);
                            appendMessage(m);
                        }
                        else
                        {
                            ChatMessage m = new ChatMessage(secondUser, messageContent, false);
                            appendMessage(m);
                        }
                    }

                    arrayAdapter.notifyDataSetChanged();
                }
            }
            else
            {
                Log.e("LAYLA", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendChat(View view)
    {
        final EditText inputMessage = findViewById(R.id.inputMsg);
        final ParseObject message = new ParseObject("ChatMessage");
        final String messageContent = inputMessage.getText().toString();

        message.put("sender", ParseUser.getCurrentUser().getUsername());
        message.put("recipient", secondUser);
        message.put("message", messageContent);

        inputMessage.setText("");

        message.saveInBackground(e ->
        {
            if(e == null)
            {
                appendMessage(new ChatMessage(ParseUser.getCurrentUser().getUsername(), messageContent, true));
            }
            else
            {
                Log.e("LAYLA", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.chat_with)+" "+secondUser);
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    public void getUsername()
    {
        secondUser = getIntent().getStringExtra("username");
    }

    public void setMessageAdapter()
    {
        ListView listViewMessages = findViewById(R.id.list_view_messages);
        arrayAdapter = new MessageAdapter(this, messages);
        listViewMessages.setAdapter(arrayAdapter);
    }

    private void appendMessage(final ChatMessage message)
    {
        runOnUiThread(() ->
        {
            messages.add(message);
            arrayAdapter.notifyDataSetChanged();
        });
    }
}
