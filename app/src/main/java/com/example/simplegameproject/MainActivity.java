package com.example.simplegameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView face;
    private ImageView blackPie;
    private ImageView greenPie;
    private ImageView cloud;


    private int frameHeight;
    private int screenWidth;
    private int faceSize;
    private int screenHeight;

    private int blackX;
    private int blackY;

    private int greenX;
    private int greenY;

    private int cloudX;
    private int cloudY;

    private int score = 0;


    private int faceY;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private boolean action_flag = false;

    private boolean start_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        face = (ImageView) findViewById(R.id.face);
        blackPie = (ImageView) findViewById(R.id.blackpie);
        greenPie = (ImageView) findViewById (R.id.greenpie);
        cloud = (ImageView) findViewById(R.id.cloud);

        WindowManager wm = getWindowManager();
        Display dis = wm.getDefaultDisplay();
        Point size = new Point();
        dis.getSize(size);

        screenHeight = size.y;
        screenWidth = size.x;




        blackPie.setX(-80);
        blackPie.setY(-80);

        greenPie.setX(-80);
        greenPie.setY(-80);

        cloud.setX(-80);
        cloud.setY(-80);


        scoreLabel.setText("Score: 0" );


        //faceY = 200;



    }


    public void changePos() {


        hitCheck();

        blackX -=12;

        if (blackX < 0) {
            blackX = screenWidth + 20;
            blackY = (int)Math.floor(Math.random() * (frameHeight - blackPie.getHeight()));

        }

        blackPie.setX(blackX);
        blackPie.setY(blackY);

        greenX = -18;

        if (greenX < 0) {
            greenX = screenWidth + 10;
            greenY = (int)Math.floor(Math.random() * (frameHeight - greenPie.getHeight()));
        }


        greenPie.setX(greenX);
        greenPie.setY((greenY));

        cloudX -= 20;

        if ( cloudX < 0) {

            cloudX = screenWidth + 5000;
            cloudY = (int)Math.floor(Math.random() * (frameHeight - cloud.getHeight()));
        }

        cloud.setX(cloudX);
        cloud.setY(cloudY);


        if (action_flag == true) {

            faceY -=20;
        } else {

            faceY += 20;
        }

        if (faceY < 0 ){
            faceY = 0;
        }

        if (faceY > frameHeight - faceSize) {
            faceY = frameHeight - faceSize;
        }

        face.setY(faceY);

         scoreLabel.setText("Score: " + score);

    }

    public void hitCheck() {

        int blackCenterX = blackX + blackPie.getWidth()/2;
        int blackCenterY = blackY + blackPie.getHeight()/2;

        if (0 <= blackCenterX && blackCenterX <= faceSize && faceY <= blackCenterY && blackCenterY <= faceY + faceSize) {

            score += 10;
            blackX =-10;

        }


        int greenCenterX = greenX + greenPie.getWidth()/2;
        int greenCenterY = greenX + greenPie.getHeight()/2;


        if (0 <= greenCenterX && greenCenterX <= faceSize && faceY <= greenCenterY && greenCenterY <= faceY + faceSize) {

            score += 50;
            greenX = -10;
        }

        int cloudCenterX = cloudX + cloud.getWidth()/2;
        int cloudCenterY = cloudY + cloud.getHeight()/2;

        if (0 <= cloudCenterX && cloudCenterX <= faceSize && faceY <= cloudCenterY && cloudCenterY <= faceY + faceSize) {

            timer.cancel();
            timer = null;
        }

    }

    public boolean onTouchEvent (MotionEvent mEvent) {

        if (start_flag == false) {


            start_flag = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id. frame);
            frameHeight = frame.getHeight();

            faceY = (int) face.getY();

            faceSize = face.getHeight();


            startLabel.setVisibility((View.GONE));


            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });


                }
            } , 0, 20);




        } else {

            if(mEvent.getAction() == MotionEvent.ACTION_DOWN){
                action_flag = true;
            } else if (mEvent.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }

        }






        return true;
    }


}
