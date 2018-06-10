package com.layla.modules;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.*;
import android.view.*;

import com.layla.R;

public class HeartbeatView extends View
{
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static Bitmap greenBitmap = null;
    private static Bitmap redBitmap = null;

    private static int parentWidth = 0;
    private static int parentHeight = 0;

    public HeartbeatView(Context context, AttributeSet attr)
    {
        super(context, attr);

        greenBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_heart);
        redBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.new_red_heart);
    }

    public HeartbeatView(Context context)
    {
        super(context);

        greenBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_heart);
        redBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.new_red_heart);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(canvas == null) throw new NullPointerException();

        Bitmap bitmap;
        if (HeartRateMonitor.getCurrent() == HeartRateMonitor.TYPE.GREEN) bitmap = greenBitmap;
        else bitmap = redBitmap;

        float centerX = (parentWidth - bitmap.getWidth()) / 2.0f;
        float centerY = (parentHeight - bitmap.getHeight()) / 2.0f;
        
        Matrix matrix = new Matrix();
        matrix.setTranslate(centerX, centerY);
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
