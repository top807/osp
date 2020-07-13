package com.demo.osp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class thankyou extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        // do nothing.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

    }


    public void main_menu(View view) {
        Finalize_order.next_ord_flag = 1;
        Finalize_order.old_ord_string = Finalize_order.order_string + Finalize_order.old_ord_string;
        Finalize_order.old_all_total = Finalize_order.all_total;

        Intent menu = new Intent(this, Order_Type.class);
        startActivity(menu);

    }


}
