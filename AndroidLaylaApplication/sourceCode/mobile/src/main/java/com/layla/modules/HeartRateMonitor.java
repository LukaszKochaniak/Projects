package com.layla.modules;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.layla.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HeartRateMonitor extends Activity
{
    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static View image = null;
    private static TextView text = null;

    private static WakeLock wakeLock = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    private String noteObjectId;
    private ParseObject note;
    private static float heartRate;
    private Button button;
    
    public enum TYPE
    {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent()
    {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getObjectToSave();

        setContentView(R.layout.activity_heart_rate);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(HeartRateMonitor.this, new String[]{android.Manifest.permission.CAMERA}, 50);
        }

        preview = findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        image = findViewById(R.id.image);
        text = findViewById(R.id.text);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

        button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            saveHeartRate();
            finish();
        });
    }

    public void getObjectToSave()
    {
        Intent intent = getIntent();
        noteObjectId = intent.getStringExtra("ParseObject");

        ParseQuery<ParseObject> query = new ParseQuery<>("Note");
        query.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("recipient", ParseUser.getCurrentUser().getString("doctor"));
        query.whereEqualTo("objectId", noteObjectId);
        query.findInBackground((objects, e) ->
        {
            if(e == null)
            {
                if(objects.size() > 0)
                {
                    for(ParseObject noteObject : objects)
                    {
                        note = noteObject;
                        noteObjectId = noteObject.getObjectId();
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

    /** TODO add R.string*/
    public void saveHeartRate()
    {
        note.put("heartRate", heartRate);
        note.saveInBackground(e -> Toast.makeText(HeartRateMonitor.this, "Successful save!", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            saveHeartRate();
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        wakeLock.acquire();

        camera = Camera.open();

        startTime = System.currentTimeMillis();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private static PreviewCallback previewCallback = new PreviewCallback()
    {
        @Override
        public void onPreviewFrame(byte[] data, Camera cam)
        {
            if(data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if(size == null) throw new NullPointerException();

            if(!processing.compareAndSet(false, true)) return;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), size.height, size.width);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if(imgAvg == 0 || imgAvg == 255)
            {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for(int i = 0; i < averageArray.length; i++)
            {
                if(averageArray[i] > 0)
                {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if(imgAvg < rollingAverage)
            {
                newType = TYPE.RED;
                if(newType != currentType)
                {
                    beats++;
                }
            }
            else if(imgAvg > rollingAverage)
            {
                newType = TYPE.GREEN;
            }

            if(averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if(newType != currentType)
            {
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if(totalTimeInSecs >= 5)
            {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if(dpm < 30 || dpm > 180)
                {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                if(beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for(int i = 0; i < beatsArray.length; i++)
                {
                    if(beatsArray[i] > 0)
                    {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = beatsArrayAvg / beatsArrayCnt;
                heartRate = beatsAvg;
                text.setText(String.valueOf(beatsAvg));
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback()
    {
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            try
            {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            }
            catch(Throwable t)
            {
                Log.e("java", "Exception in setPreviewDisplay()", t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if(size != null)
            {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }
        
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {}
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters)
    {
        Camera.Size result = null;

        for(Camera.Size size : parameters.getSupportedPreviewSizes())
        {
            if(size.width <= width && size.height <= height)
            {
                if(result == null)
                {
                    result = size;
                }
                else
                {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if(newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }
}
