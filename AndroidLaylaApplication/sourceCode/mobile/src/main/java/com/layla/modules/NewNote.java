package com.layla.modules;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.layla.R;
import com.layla.facetracker.FaceTracker;


public class NewNote extends AppCompatActivity
{

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private Button buttonHeart;
    private Button buttonMood;
    private String currentMood;
    private String noteObjectId;
    private ParseObject noteObject;

    private int noteId;
    private EditText editText;
    private boolean isEdited;
    private String previousMessage;

    private static final int WRONG_NOTE_CODE = -1;
    private static final int IMAGE_VIEW_REQUEST_CODE = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_note);

        editText = findViewById(R.id.editText);

        editOrAddCheck();

        setToolbar();
        buttonHeart = findViewById(R.id.button1);
        buttonMood = findViewById(R.id.button2);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_VIEW_REQUEST_CODE);
                }
            }
        }

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);

        setLayouts();

        //mood and heart recognition
        onButtonClickListener();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void initializeNote()
    {
        noteObject = new ParseObject("Note");
        if(!isEdited)
        {
            noteObject.put("sender", ParseUser.getCurrentUser().getUsername());
            noteObject.put("recipient", ParseUser.getCurrentUser().getString("doctor"));
        }
        noteObject.saveInBackground(e -> {
            Toast.makeText(NewNote.this, "Successful initialized!",
                    Toast.LENGTH_SHORT).show();
            noteObjectId=noteObject.getObjectId();
        });

    }

    private void saveEmoticon()
    {
        noteObject.put("selfMood", currentMood);
        noteObject.saveInBackground(e -> Toast.makeText(NewNote.this, "Successful save!",
                Toast.LENGTH_SHORT).show());
    }

    // layouts of all welcome sliders
    private void setLayouts()
    {
        layouts = new int[]
        {
            R.layout.emoticon_slide1,
            R.layout.emoticon_slide2,
            R.layout.emoticon_slide3,
            R.layout.emoticon_slide4,
            R.layout.emoticon_slide5,
            R.layout.emoticon_slide6,
            R.layout.emoticon_slide7,
        };
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Determine your mood!");
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v ->
        {
            saveNote();
            finish();
        });
        myToolbar.inflateMenu(R.menu.main_menu);
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

    public void retainNote()
    {
        ParseQuery<ParseObject> query = new ParseQuery<>("Note");
        query.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("recipient", ParseUser.getCurrentUser().getString("doctor"));
        query.whereEqualTo("message", previousMessage);
        query.findInBackground((objects, e) ->
        {
            if(e == null)
            {
                if(objects.size() > 0)
                {
                    for(ParseObject note : objects)
                    {
                        noteObject=note;
                        noteObjectId=note.getObjectId();
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

    public void editOrAddCheck()
    {
        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", WRONG_NOTE_CODE);

        if(noteId != WRONG_NOTE_CODE)
        {
            previousMessage = Notebook.notes.get(noteId);
            retainNote();
            editText.setText(previousMessage);
            isEdited = true;
        }
        else
        {
            Notebook.notes.add("");
            noteId = Notebook.notes.size() - 1;
            Notebook.arrayAdapter.notifyDataSetChanged();
            initializeNote();
            isEdited = false;
        }
    }

    /**
     * viewpager change listener
     */
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageSelected(int position) {}

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            if(position == 0)
            {
                view.setOnClickListener(e ->
                {
                    currentMood = "1/7";
                    saveEmoticon();
                });
            }
            else if(position == 1)
            {
                view.setOnClickListener(e -> {
                    currentMood = "2/7";
                    saveEmoticon();
                });
            }
            else if (position == 2)
            {
                view.setOnClickListener(e -> {
                    currentMood = "3/7";
                    saveEmoticon();
                });
            }
            else if (position == 3)
            {
                view.setOnClickListener(e -> {
                    currentMood = "4/7";
                    saveEmoticon();
                });
            }
            else if (position == 4)
            {
                view.setOnClickListener(e -> {
                    currentMood = "5/7";
                    saveEmoticon();
                });
            }
            else if (position == 5)
            {
                view.setOnClickListener(e -> {
                    currentMood = "6/7";
                    saveEmoticon();
                });
            }
            else if (position == 6)
            {
                view.setOnClickListener(e -> {
                    currentMood = "7/7";
                    saveEmoticon();
                });
            }

            container.addView(view);
            return view;
        }

        @Override
        public int getCount()
        {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj)
        {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
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
            noteObject.put("message", messageContent);
            noteObject.saveInBackground();
        }

        Notebook.notes.set(noteId, messageContent);
        Notebook.arrayAdapter.notifyDataSetChanged();
    }

    public void onButtonClickListener()
    {
        buttonHeart.setOnClickListener(view ->
        {
            saveNote();
            Intent intent = new Intent(NewNote.this, HeartRateMonitor.class);
            intent.putExtra("ParseObject", noteObjectId);
            startActivity(intent);
        });
        buttonMood.setOnClickListener(view ->
        {
            saveNote();
            Intent intent = new Intent(NewNote.this, FaceTracker.class);
            intent.putExtra("ParseObject", noteObjectId);
            startActivity(intent);        });
    }

}
