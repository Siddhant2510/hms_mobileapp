package com.healthapp.mainpage;

import com.healthapp.homepage.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.Display;
import android.view.WindowManager;
import android.util.Log;

public class Element extends View {
    private Context context;
    private Bitmap image;
    private String text;
    private Paint textPaint;
    private int winwidth;
    private int winheight;

    public Element(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        setWidthAndHeight();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Element,
                0, 0);

        try {
            int imageResId = a.getResourceId(R.styleable.Element_image, 0);
            image = BitmapFactory.decodeResource(getResources(), imageResId);
            text = a.getString(R.styleable.Element_text);
        } finally {
            a.recycle();
        }

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25 * getResources().getDisplayMetrics().density);
        textPaint.setFakeBoldText(true);
        textPaint.setStrokeWidth(5.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (image != null) {
            int scaledHeight = winheight/5;
            float aspectRatio = (float) image.getWidth() / image.getHeight();
            int scaledWidth = Math.round(scaledHeight * aspectRatio);
            Bitmap scaledImage = Bitmap.createScaledBitmap(image, scaledWidth, scaledHeight, true);
            canvas.drawBitmap(scaledImage, 10, winheight/25, null);
            if (text != null) {
                canvas.drawText(text, scaledImage.getWidth()+40,10+(scaledImage.getHeight()/2), textPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = winwidth;
        int desiredHeight = (winheight/5)+15;

        int width = resolveSize(desiredWidth, widthMeasureSpec);
        int height = resolveSize(desiredHeight, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    public void setText(String newtext){
        this.text = newtext;
        invalidate();
    }

    private void setWidthAndHeight(){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        winwidth = size.x;
        winheight = size.y;
    }
}