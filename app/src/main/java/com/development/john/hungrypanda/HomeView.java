package com.development.john.hungrypanda;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;



public class HomeView extends ImageView {

    private Paint p;
    private Handler h;
    private int alpha, alphaInc;
    private int frameRate = 30;
    private double pixelRatioX, pixelRatioY;
    private Bitmap startScreen, startScreenResized, tapAnywhere, tapAnywhereResized;
    private boolean initialized;

    public HomeView(Context context, AttributeSet attbs)
    {
        super(context, attbs);
        pixelRatioX = (double) getWidth()/ 1080.0;
        pixelRatioY = (double) getHeight()/ 1920.0;
        p = new Paint();
        h = new Handler();
        alphaInc = -5;
        alpha = 255;
        initialized = false;
    }

    public void init(){
        pixelRatioX = (double) getWidth()/ 1080.0;
        pixelRatioY = (double) getHeight()/ 1920.0;
        startScreen = BitmapFactory.decodeResource(getResources(), R.drawable.startscreen);
        startScreenResized = Bitmap.createScaledBitmap(startScreen, getWidth(), getHeight(), true);
        startScreen.recycle();
        startScreen = null;
        tapAnywhere = BitmapFactory.decodeResource(getResources(), R.drawable.tapanywhere);
        tapAnywhereResized = Bitmap.createScaledBitmap(tapAnywhere, (int) Math.round(1031 * pixelRatioX), (int) Math.round(174 * pixelRatioY), true);
        tapAnywhere.recycle();
        tapAnywhere = null;
        initialized = true;
    }

    private Runnable r = new Runnable() { //sets up runnable for refreshing of screen
        @Override
        public void run() {
            invalidate();
        }
    };

    public void onDraw(Canvas canvas){
            if(!initialized)
                init();
            p.setAlpha(255);
            canvas.drawBitmap(startScreenResized, 0, 0, p);
            p.setAlpha(alpha);
            alpha += alphaInc;
            if(alpha < 1 || alpha > 254)
                alphaInc*=-1;
            canvas.drawBitmap(tapAnywhereResized, (int)Math.round(20 * pixelRatioX), (int)Math.round(1695 * pixelRatioY), p);
            h.postDelayed(r, frameRate);
    }

}
