package com.example.simplegameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


    //Zmienne przechowywują wymiary ekranu

    private int frameHeight;
    private int screenWidth;


    private int faceSize;
    private int screenHeight;


    //Zmienne przechowywujące szczegółowe dane odnośnie pozycji oraz prędkości poruszania się
    private int blackX;
    private int blackY;

    private int greenX;
    private int greenY;

    private int cloudX;
    private int cloudY;

    private int score = 0;


    //Pozycja pojazdu gracza
    private int faceY;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private boolean action_flag = false;

    private boolean start_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Przypisywanie po ustalonym id
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        face = (ImageView) findViewById(R.id.face);
        blackPie = (ImageView) findViewById(R.id.blackpie);
        greenPie = (ImageView) findViewById (R.id.greenpie);
        cloud = (ImageView) findViewById(R.id.cloud);

        //Pozyskuje całkowity wymiar ekranu
        WindowManager wm = getWindowManager();
        Display dis = wm.getDefaultDisplay();
        Point size = new Point();
        dis.getSize(size);

        screenHeight = size.y;
        screenWidth = size.x;




        //Początkowe ustawnie współrzędnych tak, aby obiekty pojawiały się poza ekranem.

        blackPie.setX(-80);
        blackPie.setY(-80);

        greenPie.setX(-80);
        greenPie.setY(-80);

        cloud.setX(-80);
        cloud.setY(-80);


        //Początkowy wynik punktowy, przypisywany na starcie
        scoreLabel.setText("Score: 0" );


        //faceY = 200;



    }


    public void changePos() {


        hitCheck();

        //Odpowiednie ustalanie pozycji obiektów, aby nie kolidowały/przenikały z górą częścią ekranu, gdzie widnieje wynik
        //Pojazd gracza została zrobiona tak, że utrzymuje się w ekranie - nie opuszcza go ani górą, ani dołem

        blackX -=12;

        if (blackX < 0) {
            blackX = screenWidth + 20;
            blackY = (int)Math.floor(Math.random() * (frameHeight - blackPie.getHeight()));

        }

        blackPie.setX(blackX);
        blackPie.setY(blackY);

        greenX = -16;

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


        //True oznacza, że dotykamy ekranu - wtedy pojazd wznosi się do góry
        if (action_flag == true) {

            faceY -=20;
        } else {

            //Nie dotykamy ekranu, więc pojazd opada
            faceY += 20;
        }

        //Sprawdzanie pozycji pojazdu
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

        //Kolizje zostały zrobione tak, aby zachodzić wtedy, kiedy środek danego obiektu styka się z pojazdem
        //W innym wypadku lecą dalej

        int blackCenterX = blackX + blackPie.getWidth()/2;
        int blackCenterY = blackY + blackPie.getHeight()/2;

        //Przykładowo
        //0 <= blackCenterX <= faceWidth
        //faceY <= blackCenterY <= faceY + faceHeight

        if (0 <= blackCenterX && blackCenterX <= faceSize && faceY <= blackCenterY && blackCenterY <= faceY + faceSize) {


            //Przyznawane punkty
            score += 10;
            blackX =-10;

        }


        //Podobne zasady w przypadku pozostałych obiektów

        int greenCenterX = greenX + greenPie.getWidth()/2;
        int greenCenterY = greenX + greenPie.getHeight()/2;


        if (0 <= greenCenterX && greenCenterX <= faceSize && faceY <= greenCenterY && greenCenterY <= faceY + faceSize) {

            score += 50;
            greenX = -10;
        }

        int cloudCenterX = cloudX + cloud.getWidth()/2;
        int cloudCenterY = cloudY + cloud.getHeight()/2;

        if (0 <= cloudCenterX && cloudCenterX <= faceSize && faceY <= cloudCenterY && cloudCenterY <= faceY + faceSize) {


            //W przypadku kolizji z chmurką, zatrzymywana jest gra i gracz jest przenoszony na ekran wyników.
            timer.cancel();
            timer = null;

            Intent intent = new Intent (getApplicationContext(), Score.class);
            intent.putExtra("SCORE",score);
            startActivity(intent);


        }

    }

    public boolean onTouchEvent (MotionEvent mEvent) {

        if (start_flag == false) {


            start_flag = true;

            //Pozyskiwanie wymiarów
            //Dane są pozyskiwane w tej metodzie, ponieważ UI nie było ustawiane na scene w OnCreate()
            FrameLayout frame = (FrameLayout) findViewById(R.id. frame);
            frameHeight = frame.getHeight();

            faceY = (int) face.getY();

            //Pozyskiwanie wymiarów pojazdu (obrazek ma wymiary 300 na 300, więc jest kwadratem - wystarczy tylko wysokość)
            faceSize = face.getHeight();


            startLabel.setVisibility((View.GONE));


            timer.schedule(new TimerTask() {
                @Override
                public void run() {

             //Określa, jak często wywoływany jest changepos ( w tym przypadku - co 20 milisekund)

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });


                }
            } , 0, 20);




        } else {


            //
            if(mEvent.getAction() == MotionEvent.ACTION_DOWN){
                action_flag = true;
            } else if (mEvent.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }

        }






        return true;
    }


}
