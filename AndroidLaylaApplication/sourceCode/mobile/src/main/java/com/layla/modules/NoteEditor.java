package com.layla.modules;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.*;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.layla.*;
import com.layla.facetracker.FaceTracker;


public class NoteEditor extends AppCompatActivity
{
    private int noteId;
    private EditText editText;
    private boolean isEdited;
    private String previousMessage;

    //constants like defines in c++, we should move it to another class and just import it
    private static final int WRONG_NOTE_CODE = -1;
    private static final int IMAGE_VIEW_REQUEST_CODE = 1;

    //Image permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == IMAGE_VIEW_REQUEST_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //will be used for face recognition
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        setToolbar();

        editText = findViewById(R.id.editText);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_VIEW_REQUEST_CODE);
            }
        }
        editOrAddCheck();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BACK))
        {
            saveNote();
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_main_setting:
                saveNote();
                startActivity(new Intent(NoteEditor.this, HeartRateMonitor.class));
                return true;
            case R.id.menu_main_setting2:
                saveNote();
                startActivity(new Intent(NoteEditor.this, FaceTracker.class));
                return true;
            default:
                saveNote();
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.noteEditor_writeNote));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v ->
        {
            saveNote();
            finish();
        });
        myToolbar.inflateMenu(R.menu.main_menu);
    }

    public void editOrAddCheck()
    {
        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", WRONG_NOTE_CODE);

        if(noteId != WRONG_NOTE_CODE)
        {
            previousMessage = Notebook.notes.get(noteId);
            editText.setText(Notebook.notes.get(noteId));
            isEdited = true;
        }
        else
        {
            Notebook.notes.add("");
            noteId = Notebook.notes.size() - 1;
            Notebook.arrayAdapter.notifyDataSetChanged();
            isEdited = false;
        }
    }

    public void saveNote()
    {
        final EditText inputMessage = findViewById(R.id.editText);
        final String messageContent = inputMessage.getText().toString();

        if(isEdited)
        {
            ParseQuery<ParseUser> query = new ParseQuery<>("Note");
            query.whereMatches("sender", ParseUser.getCurrentUser().getUsername());
            query.whereMatches("recipient", ParseUser.getCurrentUser().getString("doctor"));
            query.whereMatches("message", previousMessage);
            query.setLimit(1);
            query.findInBackground((objects, e) ->
            {
                if(e == null)
                {
                    for(ParseObject user : objects)
                    {
                        user.put("message", messageContent);
                        user.saveInBackground();
                    }
                }
                else
                {
                    Log.e("LAYLA", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
        else
        {
            final ParseObject message = new ParseObject("Note");
            message.put("sender", ParseUser.getCurrentUser().getUsername());
            message.put("recipient", ParseUser.getCurrentUser().getString("doctor"));
            message.put("message", messageContent);
            message.saveInBackground();
        }

        Notebook.notes.set(noteId, messageContent);
        Notebook.arrayAdapter.notifyDataSetChanged();
    }
}
