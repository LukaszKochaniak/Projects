package com.layla.modules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.widget.*;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.layla.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Notebook extends AppCompatActivity
{
    static List<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    private ListView listView;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        retainNotes();
        SystemClock.sleep(400);

        setContentView(R.layout.activity_notebook);

        imageButton = findViewById(R.id.imageButton);
        listView = findViewById(R.id.listView);

        setToolbar();
        noteAddEdit();
        deleteNote();
        setAdapter();
    }

    public void retainNotes()
    {
        ParseQuery<ParseObject> query = new ParseQuery<>("Note");
        query.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("recipient", ParseUser.getCurrentUser().getString("doctor"));

        query.orderByAscending("createdAt");

        notes.clear();

        query.findInBackground((objects, e) ->
        {
            if(e == null)
            {
                if(objects.size() > 0)
                {
                    for(ParseObject message : objects)
                    {
                        String messageContent = message.getString("message");
                        notes.add(messageContent);
                    }
                }
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
        myToolbar.setTitle(getString(R.string.notebook_title));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    public void setAdapter()
    {
        arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);
    }

    public void noteAddEdit()
    {
        listView.setOnItemClickListener((adapterView, view, i, l) ->
        {
            Intent intent = new Intent(Notebook.this, NewNote.class);
            intent.putExtra("noteId", i);
            startActivity(intent);
        });

        imageButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(Notebook.this, NewNote.class);
            startActivity(intent);
        });
    }

    public void deleteNote()
    {
        listView.setOnItemLongClickListener((adapterView, view, item, l) ->
        {
            //item can make mistake
            new AlertDialog.Builder(Notebook.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want do delete this note?")
                    .setPositiveButton("Yes", (dialogInterface, i) ->
                    {
                        notes.remove(item);
                        arrayAdapter.notifyDataSetChanged();

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.projects.lukas.myapp", Context.MODE_PRIVATE);
                        HashSet<String> set = new HashSet<>(Notebook.notes);
                        sharedPreferences.edit().putStringSet("notes", set).apply();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }
}
