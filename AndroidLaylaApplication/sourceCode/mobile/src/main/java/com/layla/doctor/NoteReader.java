package com.layla.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.layla.R;
import com.layla.modules.ChatMessage;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class NoteReader extends AppCompatActivity
{
    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    private String secondUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        retainMessages();

        setContentView(R.layout.activity_note_reader);

        setToolbar();

        recyclerView = findViewById(R.id.recycler_view);

        recyclerViewSettings();
    }

    public void recyclerViewSettings()
    {
        mAdapter = new NoteAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.readNote));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    public void getUsername()
    {
        Intent intent = getIntent();
        secondUser = intent.getStringExtra("username");
    }

    public void retainMessages()
    {
        getUsername();

        ParseQuery<ParseObject> query = new ParseQuery<>("Note");
        query.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("sender", secondUser);

        query.orderByAscending("createdAt");

        query.findInBackground((objects, e) ->
        {
            if(e == null)
            {
                if(objects.size() > 0)
                {
                    noteList.clear();

                    for(ParseObject message : objects)
                    {
                        String comment = message.getString("message");
                        String mood = "in range (-1, 1) - " + message.getString("autoMood");
                        String selfMood = message.getString("selfMood");
                        String heartRate = String.valueOf(message.getInt("heartRate"));
                        String date = String.valueOf(message.getUpdatedAt());

                        Note m = new Note("Auto mood: "+ mood,"Self evaluation: " + selfMood, "Heart rate: " + heartRate,"Thoughts: " + comment ,date);
                        appendMessage(m);

                    }

                    mAdapter.notifyDataSetChanged();
                }
            }
            else
            {
                Log.e("LAYLA", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void appendMessage(final Note note)
    {
        runOnUiThread(() ->
        {
            noteList.add(note);
            mAdapter.notifyDataSetChanged();
        });
    }
}
