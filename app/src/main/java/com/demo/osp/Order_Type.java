package com.demo.osp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Order_Type extends AppCompatActivity {


    static int new_ord_flag = 0;
    private Button login;
    private Button myorder;

    private boolean checkLoginState;
    private String username;
    private SharedPreferences share;

    private TextView user;

    @Override
    public void onBackPressed() {
        // do nothing.
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__type);

        findViews();

        share = getSharedPreferences("user", MODE_PRIVATE);

        if (Finalize_order.all_total > 0) {
            myorder.setText("" + "₹" + Finalize_order.all_total);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (checkLoginState) {
//            share.edit().putBoolean("loginState", false).remove("user").commit();
//            checkLoginState = false;
//        }
    }

    private void checkLogin() {
        checkLoginState = share.getBoolean("loginState", false);
        username = share.getString("user", "訪客");

        // TODO : Login Button & login state check
        if (!checkLoginState) {
            login.setText("登入");
            user.setText("訪客");
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order_Type.this.startActivityForResult(new Intent(Order_Type.this, Login.class), 11);
                }
            });
        } else {
            login.setText("登出");
            user.setText(username);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    share.edit().putBoolean("loginState", false)
                            .putString("user", "").apply();
                    checkLoginState = false;
                    finish();
                    startActivity(getIntent());
                }
            });
        }
    }

    private void findViews() {
        myorder = findViewById(R.id.btn_my_order);
        login = findViewById(R.id.btn_login_main);
        user = findViewById(R.id.txt_username);
    }

    //------------------------------Button---------------------------------------------------------------


    public void starter(View view) {
        Intent nextact = new Intent(this, Seafood.class);
        startActivity(nextact);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    public void veg(View view) {
        Intent nextact = new Intent(this, Vegeta.class);
        startActivity(nextact);
        overridePendingTransition(R.anim.right_left, R.anim.left_right);
    }

    public void nveg(View view) {
        Intent nextact = new Intent(this, Meat.class);
        startActivity(nextact);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    public void dessert(View view) {
        Intent nextact = new Intent(this, Dessert.class);
        startActivity(nextact);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    public void fin_ord(View view) {

        if (!checkLoginState) {
            Log.i("TEST", "iiii");
            new AlertDialog.Builder(Order_Type.this)
                    .setTitle("OAO !!!")
                    .setMessage("登入後方可結帳與購物   :)")
                    .setCancelable(false)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        } else {

            startActivity(new Intent(Order_Type.this, Finalize_Order_My.class));
            overridePendingTransition(R.anim.fadin, R.anim.fadout);
            finish();


            //todo 原始程式碼，用判斷已點的數量來進行後續步驟。
//            final Object o = this;
//            if (Finalize_order.all_total > 0) {
//                Intent fin = new Intent(this, Finalize_order.class);
//                startActivity(fin);
//                overridePendingTransition(R.anim.fadin, R.anim.fadout);
//            } else {
//                if (Finalize_order.next_ord_flag == 1) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage("Are you sure you don't want to place another order?")
//                            .setCancelable(false)
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    Intent nextact = new Intent((Order_Type) o, thankyou.class);
//                                    startActivity(nextact);
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Please select your order", Toast.LENGTH_SHORT).show();
//                }
//            }


        }

    }
}

