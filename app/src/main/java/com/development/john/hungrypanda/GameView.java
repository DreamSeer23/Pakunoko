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
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class GameView extends ImageView {


    private int frameRate = 30;
    private int water, startingWater, score, alpha, alphaInc;
    private Handler h;
    private int[][] bambooLoc;
    private ArrayList<Bamboo> bambooPlot;
    private Paint p;
    private Bitmap winScreen, winScreenResized, portraitBackground, portraitResized, loseScreen, loseScreenResized,
            bamboo, bambooResized, panda1, panda1resized, panda2, panda2resized,
            upgradeBar, upgradeBarResized, waterBar, waterBarResized,
            sunny, sunnyResized, cloudy, cloudyResized, stormy, stormyResized, selector, selectorResized,
            tapAnywhere, tapAnywhereResized;
    private boolean initialized, pandaMode, lost, sunnyMode, cloudyMode, stormyMode, rained, won;
    private double pixelRatioX, pixelRatioY, upgradePoints;
    private long lastUpdate;
    private Panda panda;

    public GameView(Context context, AttributeSet attbs)
    {
        super(context, attbs);
        bambooPlot = new ArrayList<Bamboo>();
        for(int i = 0; i < 5; i++)
        {
            bambooPlot.add(new Bamboo());
        }
        p = new Paint();
        h = new Handler();
        initialized = false;
        reset();
    }

    public void initialize()
    {
        portraitBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        portraitResized = Bitmap.createScaledBitmap(portraitBackground, getWidth(), getHeight(), true);
        portraitBackground.recycle();
        portraitBackground = null;
        loseScreen = BitmapFactory.decodeResource(getResources(), R.drawable.endscreen);
        loseScreenResized = Bitmap.createScaledBitmap(loseScreen, getWidth(), getHeight(), true);
        loseScreen.recycle();
        loseScreen = null;
        winScreen = BitmapFactory.decodeResource(getResources(), R.drawable.winscreen);
        winScreenResized = Bitmap.createScaledBitmap(winScreen, getWidth(), getHeight(), true);
        winScreen.recycle();
        winScreen = null;
        bamboo = BitmapFactory.decodeResource(getResources(), R.drawable.bamboo);
        bambooResized = Bitmap.createScaledBitmap(bamboo, getWidth() / 10, (int) Math.round(52 * pixelRatioY), true);
        bamboo.recycle();
        bamboo = null;
        panda1 = BitmapFactory.decodeResource(getResources(), R.drawable.panda1);
        panda1resized = Bitmap.createScaledBitmap(panda1, (int) Math.round(61 * pixelRatioX), (int) Math.round(88 * pixelRatioY), true);
        panda1.recycle();
        panda1 = null;
        panda2 = BitmapFactory.decodeResource(getResources(), R.drawable.panda2);
        panda2resized = Bitmap.createScaledBitmap(panda2, (int) Math.round(61 * pixelRatioX), (int) Math.round(88 * pixelRatioY), true);
        panda2.recycle();
        panda2 = null;
        upgradeBar = BitmapFactory.decodeResource(getResources(), R.drawable.upgradebar);
        upgradeBarResized = Bitmap.createScaledBitmap(upgradeBar, (int) Math.round(66 * pixelRatioX), (int) Math.round(31 * pixelRatioY), true);
        upgradeBar.recycle();
        upgradeBar = null;
        waterBar = BitmapFactory.decodeResource(getResources(), R.drawable.waterbar);
        waterBarResized = Bitmap.createScaledBitmap(waterBar, (int) Math.round(31 * pixelRatioX), (int) Math.round(66 * pixelRatioY), true);
        waterBar.recycle();
        waterBar = null;
        sunny = BitmapFactory.decodeResource(getResources(), R.drawable.sunny);
        sunnyResized = Bitmap.createScaledBitmap(sunny, (int) Math.round(1080 * pixelRatioX), (int) Math.round(283 * pixelRatioY), true);
        sunny.recycle();
        sunny = null;
        cloudy = BitmapFactory.decodeResource(getResources(), R.drawable.cloudy);
        cloudyResized = Bitmap.createScaledBitmap(cloudy, (int) Math.round(1080 * pixelRatioX), (int) Math.round(283 * pixelRatioY), true);
        cloudy.recycle();
        cloudy = null;
        stormy = BitmapFactory.decodeResource(getResources(), R.drawable.rainy);
        stormyResized = Bitmap.createScaledBitmap(stormy, (int) Math.round(1080 * pixelRatioX), (int) Math.round(283 * pixelRatioY), true);
        stormy.recycle();
        stormy = null;
        selector = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        selectorResized = Bitmap.createScaledBitmap(selector, (int) Math.round(126 * pixelRatioX), (int) Math.round(128 * pixelRatioY), true);
        selector.recycle();
        selector = null;
        tapAnywhere = BitmapFactory.decodeResource(getResources(), R.drawable.tapanywhere);
        tapAnywhereResized = Bitmap.createScaledBitmap(tapAnywhere, (int) Math.round(1031 * pixelRatioX), (int) Math.round(174 * pixelRatioY), true);
        tapAnywhere.recycle();
        tapAnywhere = null;
        initialized = true;

    }

    public void setPandaMode(boolean i)
    {
        pandaMode = i;
    }

    public boolean isPandaMode()
    {
        return pandaMode;
    }

    public boolean isWon(){
        return won;
    }

    public double getUpgradePoints(){
        return upgradePoints;
    }

    public void upgrade(){
        upgradePoints--;
        if(startingWater < 15) {
            startingWater++;
            water++;
        }
    }

    public int getWater()
    {
        return water;
    }

    public void water(){
        if(water > 0)
            water--;
    }

    private Runnable r = new Runnable() { //sets up runnable for refreshing of screen
        @Override
        public void run() {
            invalidate();
        }
    };

    /**
     * Standard onDraw method
     * @param canvas the canvas where this is being drawn on
     */
    @Override
    public void onDraw(Canvas canvas)
    {
        //get aspect ratio
        pixelRatioY = (double) getHeight() /1920.0;
        pixelRatioX = (double) getWidth() /1080.0;
        //initialize bit maps
        if(!initialized)
            initialize();
        else if(!lost && !won) {
            canvas.drawBitmap(portraitResized, 0, 0, p);
            p.setColor(Color.BLACK);
            p.setTextSize((int) (30 * pixelRatioX));
            canvas.drawText("Score:", (int) Math.round(415 * pixelRatioX), (int) Math.round(80 * pixelRatioY), p);
            p.setTextSize((int) (60 * pixelRatioX));
            canvas.drawText("" + score, (int) Math.round(415 * pixelRatioX), (int) Math.round(150 * pixelRatioY), p);
            p.setTextSize((int) (75 * pixelRatioX));
            canvas.drawText("" + (int) upgradePoints, (int) Math.round(462 * pixelRatioX), (int) Math.round(1800 * pixelRatioY), p);


            if(sunnyMode) {
                canvas.drawBitmap(sunnyResized, (int) Math.round(0 * pixelRatioX), (int) Math.round(230 * pixelRatioY), p);
                canvas.drawBitmap(selectorResized, (int) Math.round(545 * pixelRatioX), (int) Math.round(1725 * pixelRatioY), p);

            }
            if(cloudyMode) {
                canvas.drawBitmap(cloudyResized, (int) Math.round(0 * pixelRatioX), (int) Math.round(230 * pixelRatioY), p);
                canvas.drawBitmap(selectorResized, (int) Math.round(705 * pixelRatioX), (int) Math.round(1725 * pixelRatioY), p);

            }
            if(stormyMode) {
                canvas.drawBitmap(stormyResized, (int) Math.round(0 * pixelRatioX), (int) Math.round(230 * pixelRatioY), p);
                canvas.drawBitmap(selectorResized, (int) Math.round(865 * pixelRatioX), (int) Math.round(1725 * pixelRatioY), p);
                if(!rained){
                    for(Bamboo b : bambooPlot)
                    {
                        if(b.getSize() < 2)
                            b.setSize(b.getSize()+1);
                    }
                    rained = true;
                }
            }

            //draw the bamboo
            for (int i = 0; i < bambooPlot.size(); i++) {
                int j = bambooPlot.get(i).getSize();
                while (j - 1 > 0) {
                    canvas.drawBitmap(bambooResized, (int) Math.round((bambooLoc[i][0] + 20) * pixelRatioX), (int) Math.round((bambooLoc[i][1] - 55 * (j-1)) * pixelRatioY), p);
                    j--;
                }
            }

            int numberWater = (int)Math.ceil((water/(startingWater*1.0) * 100) / 11.11);
            if(numberWater > 9)
                numberWater = 9;
            for (int i = 0; i < numberWater; i++){
                canvas.drawBitmap(waterBarResized, (int) Math.round((685 + (41 * i)) * pixelRatioX), (int) Math.round(30 * pixelRatioY), p);
            }

            int numberHunger = (int)Math.ceil((panda.hunger/(panda.startingHunger*1.0) * 100) / 11.11);
            if(numberHunger > 9)
                numberHunger = 9;
            for (int i = 0; i < numberHunger; i++){
                canvas.drawBitmap(waterBarResized, (int) Math.round((685 + (41 * i)) * pixelRatioX), (int) Math.round(133 * pixelRatioY), p);
            }

            int numberUpgrade = startingWater - 10;
            for (int i = 0; i < numberUpgrade; i++){
                canvas.drawBitmap(upgradeBarResized, (int) Math.round(365 * pixelRatioX), (int) Math.round((1841 - (33 * i)) * pixelRatioY), p);
            }

            //animate panda as necessary
            if (pandaMode) {
                //draw based on phase
                int i = panda.getCurrentBamboo();
                if (panda.getPhase() == 0)
                    canvas.drawBitmap(panda1resized, (int) ((bambooLoc[i][0] + 70) * pixelRatioX), (int) ((bambooLoc[i][1]) * pixelRatioY), p);
                else if (panda.getPhase() == 1)
                    canvas.drawBitmap(panda2resized, (int) ((bambooLoc[i][0] + 120) * pixelRatioX), (int) (bambooLoc[i][1] * pixelRatioY), p);

                //update every .5s for the animation
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdate > 500) {
                    if (panda.getPhase() == 0)
                        panda.setPhase(1);
                    else {
                        panda.setPhase(0);
                        Bamboo temp = bambooPlot.get(panda.getCurrentBamboo());
                        panda.hunger -= (int) Math.pow(2, temp.getSize());
                        score += (int) (Math.round(Math.pow(2, temp.getSize()) / 2));
                        temp.setSize(1);
                        panda.setBamboo(panda.getCurrentBamboo() + 1);
                        //Once done, reset and roll to see if weather should change
                        if (panda.getCurrentBamboo() >= bambooPlot.size()) {
                            pandaMode = false;
                            panda.setBamboo(0);
                            if (panda.hunger > 0)
                                lost = true;
                            upgradePoints += (panda.hunger - water) / -112.0;
                            score += water;
                            if(score > 1000)
                                won = true;
                            panda.startingHunger += 2;
                            panda.hunger = panda.startingHunger;
                            water = startingWater;
                            if(sunnyMode) {
                                int weatherChance = (int) (Math.random() * 100);
                                if (weatherChance > 65) {
                                    sunnyMode = false;
                                    cloudyMode = true;
                                }
                            }
                            else if(cloudyMode){
                                cloudyMode = false;
                                stormyMode = true;
                            }
                            else if(stormyMode){
                                sunnyMode = true;
                                stormyMode = false;
                                rained = false;
                            }
                        }
                    }
                    lastUpdate = currentTime;
                }
            }
        }
        else if(lost){
            p.setAlpha(255);
            canvas.drawBitmap(loseScreenResized, 0, 0, p);
            canvas.drawText("Score: " + score, (int) Math.round(340 * pixelRatioX), (int) Math.round(1255 * pixelRatioY), p);
            p.setAlpha(alpha);
            alpha += alphaInc;
            if(alpha < 1 || alpha > 254)
                alphaInc*=-1;
            canvas.drawBitmap(tapAnywhereResized, (int)Math.round(20 * pixelRatioX), (int)Math.round(1715 * pixelRatioY), p);
        }

        else if(won){
            p.setAlpha(255);
            canvas.drawBitmap(winScreenResized, 0, 0, p);
            p.setAlpha(alpha);
            alpha += alphaInc;
            if(alpha < 1 || alpha > 254)
                alphaInc*=-1;
            canvas.drawBitmap(tapAnywhereResized, (int)Math.round(23 * pixelRatioX), (int)Math.round(1700 * pixelRatioY), p);
        }

        h.postDelayed(r, frameRate);
    }

    public void reset()
    {
        panda = new Panda();
        alpha = 255;
        p.setAlpha(alpha);
        alphaInc = -5;
        startingWater = water = 10;
        upgradePoints = score = 0;
        lastUpdate = System.currentTimeMillis();
        pixelRatioX = (double) getHeight() /1920.0;
        pixelRatioY = (double) getWidth() /1080.0;
        bambooLoc = new int[][]{
                {445, 830},
                {707, 1000},
                {577, 1170},
                {335, 1170},
                {222, 995}
        };
        pandaMode = lost = cloudyMode = stormyMode = rained = won = false;
        sunnyMode = true;

    }

    public boolean isLost(){
        return lost;
    }
    public ArrayList<Bamboo> getBambooPlot()
    {
        return bambooPlot;
    }

    //Class for Bamboo object, very basic
    public class Bamboo{

        public int size = 1;

        public void setSize(int i)
        {
            size = i;
        }

        public int getSize()
        {
            return size;
        }

    }

    //Class for Panda object, keeps track of it's animation phase and current bamboo
    public class Panda{

        public int phase = 0;
        public int currentBamboo = 0;
        public int hunger = 20;
        public int startingHunger = 20;

        public void setBamboo(int i){
            currentBamboo = i;
        }

        public void setPhase(int i){
            phase = i;
        }

        public int getPhase(){
            return phase;
        }

        public int getCurrentBamboo(){
            return currentBamboo;
        }
    }
}
