package com.demo.osp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Finalize_Order_My extends AppCompatActivity {

    private Button keepShopping;
    private Button checkOut;
    private ListView orderList;

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_order);

        findViews();

        keepShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Finalize_Order_My.this, Order_Type.class));
                overridePendingTransition(R.anim.fadin, R.anim.fadout);
                overridePendingTransition(R.anim.fadin, R.anim.fadout);
                finish();
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Finalize_Order_My.this, "尚未完工", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews() {
        checkOut = findViewById(R.id.btn_checkout);
        keepShopping = findViewById(R.id.btn_keepshopping);
        orderList = findViewById(R.id.final_list_view);
    }

}
