package com.development.john.hungrypanda;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class GameScreen extends Activity {

    private int upgrade;
    private ArrayList<GameView.Bamboo> bamboo;
    private double pixelRatioX, pixelRatioY;
    private GameView game;
    private boolean pandaMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        upgrade = 0;
        game = (GameView) findViewById(R.id.GameCanvas);
        bamboo = game.getBambooPlot();
        pandaMode = false;
        View.OnTouchListener touchListen = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                pixelRatioY = (double) game.getHeight() /1920.0;
                pixelRatioX = (double) game.getWidth() /1080.0;
                if(!game.isLost() && !game.isWon()) {
                    if (!game.isPandaMode()) {
                        //check if touching Bamboo #1
                        if (e.getAction() == MotionEvent.ACTION_UP &&
                                e.getX() >= 455 * pixelRatioX &&
                                e.getX() <= 575 * pixelRatioX &&
                                e.getY() >= 825 * pixelRatioY &&
                                e.getY() <= 940 * pixelRatioY) {
                            GameView.Bamboo temp = bamboo.get(0);
                            if (temp.getSize() < 4 && game.getWater() > 0) {
                                temp.setSize(temp.getSize() + 1);
                                game.water();
                            }
                        }

                        //Bamboo #2
                        else if (e.getAction() == MotionEvent.ACTION_UP &&
                                e.getX() >= 714 * pixelRatioX &&
                                e.getX() <= 840 * pixelRatioX &&
                                e.getY() >= 1000 * pixelRatioY &&
                                e.getY() <= 1090 * pixelRatioY) {
                            GameView.Bamboo temp = bamboo.get(1);
                            if (temp.getSize() < 4 && game.getWater() > 0) {
                                temp.setSize(temp.getSize() + 1);
                                game.water();
                            }

                        }

                        //Bamboo #3
                        else if (e.getAction() == MotionEvent.ACTION_UP &&
                                e.getX() >= 575 * pixelRatioX &&
                                e.getX() <= 710 * pixelRatioX &&
                                e.getY() >= 1165 * pixelRatioY &&
                                e.getY() <= 1270 * pixelRatioY) {
                            GameView.Bamboo temp = bamboo.get(2);
                            if (temp.getSize() < 4 && game.getWater() > 0) {
                                temp.setSize(temp.getSize() + 1);
                                game.water();
                            }
                        }

                        //Bamboo #4
                        else if (e.getAction() == MotionEvent.ACTION_UP &&
                                e.getX() >= 350 * pixelRatioX &&
                                e.getX() <= 480 * pixelRatioX &&
                                e.getY() >= 1170 * pixelRatioY &&
                                e.getY() <= 1280 * pixelRatioY) {
                            GameView.Bamboo temp = bamboo.get(3);
                            if (temp.getSize() < 4 && game.getWater() > 0) {
                                temp.setSize(temp.getSize() + 1);
                                game.water();
                            }
                        }

                        //Bamboo #5
                        else if (e.getAction() == MotionEvent.ACTION_UP &&
                                e.getX() >= 235 * pixelRatioX &&
                                e.getX() <= 355 * pixelRatioX &&
                                e.getY() >= 990 * pixelRatioY &&
                                e.getY() <= 1100 * pixelRatioY) {
                            GameView.Bamboo temp = bamboo.get(4);
                            if (temp.getSize() < 4 && game.getWater() > 0) {
                                temp.setSize(temp.getSize() + 1);
                                game.water();
                            }
                        }

                        //Upgrade Button
                        else if (e.getAction() == MotionEvent.ACTION_UP &&
                                e.getX() >= 36 * pixelRatioX &&
                                e.getX() <= 145 * pixelRatioX &&
                                e.getY() >= 1737 * pixelRatioY &&
                                e.getY() <= 1845 * pixelRatioY) {
                            if(game.getUpgradePoints() >= 1 && upgrade < 5) {
                                game.upgrade();
                                upgrade++;
                            }
                        }

                        //Play Button
                        else if (e.getAction() == MotionEvent.ACTION_UP &&
                                e.getX() >= 210 * pixelRatioX &&
                                e.getX() <= 315 * pixelRatioX &&
                                e.getY() >= 110 * pixelRatioY &&
                                e.getY() <= 185 * pixelRatioY) {
                            game.setPandaMode(true);
                        }
                    }
                }
                else
                    game.reset();

                return true;
            }
        };
        game.setOnTouchListener(touchListen);
    }
}
