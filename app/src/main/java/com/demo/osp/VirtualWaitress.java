package com.demo.osp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class VirtualWaitress extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_waitress);
        Thread startTimer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Intent intent0 = new Intent(VirtualWaitress.this, Order_Type.class);
                    startActivity(intent0);
                    overridePendingTransition(R.anim.fadin, R.anim.fadout);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        startTimer.start();
    }
}
