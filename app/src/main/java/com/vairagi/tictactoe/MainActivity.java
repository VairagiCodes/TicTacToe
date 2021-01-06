package com.vairagi.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageButton ib = (ImageButton) findViewById(R.id.logoimage);
//        final Handler handler = new Handler();
//        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
//        final TextView timer = (TextView) findViewById(R.id.timer);
//        timer.startAnimation(animation);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                timer.setText("2");
//                timer.setTextColor(Color.BLUE);
//                timer.startAnimation(animation);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        timer.setText("1");
//                        timer.setTextColor(Color.BLUE);
//                        timer.startAnimation(animation);
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                startActivity(new Intent(getApplicationContext(),TicTacToe.class));
//                                finish();
//                            }
//                        }, 1000);
//                    }
//                }, 1000);
//            }
//        },1000);
    }

    public void Start(View view) {
        startActivity(new Intent(getApplicationContext(),TicTacToe.class));
        finish();
    }
}