package com.example.simplegameproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


//Proste menu startowe, dzięki któremu gracz może poznać zasady punktacji oraz końca gry
//Odpowiednia kolejność ekranów gry została ustawiona w AndroidManifest.xml
public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }


    public void startGame (View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }



    }

