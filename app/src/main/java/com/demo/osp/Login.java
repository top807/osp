package com.demo.osp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    public static boolean checkingLoginState = false;
    public SharedPreferences share;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private Button login, regist;
    private EditText account, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        share = getSharedPreferences("user", MODE_PRIVATE);
        findViews();
        setBtn();
    }

    private void findViews() {
        login = findViewById(R.id.btn_log);
        regist = findViewById(R.id.btn_regg);
        account = findViewById(R.id.ed_acc);
        password = findViewById(R.id.ed_pass);
    }

    private void setBtn() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    root = database.getReference("/account/" + account.getText().toString());
                    root.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            HashMap accountName = (HashMap) dataSnapshot.getValue();

                            if (!(accountName == null)) {
                                String pass = accountName.get("password").toString();

                                if (pass.contains(password.getText().toString())) {
                                    /*-------------------------------------------------------------------------------*/
                                    share.edit().putBoolean("loginState", true)
                                            .putString("user", account.getText().toString())
                                            .commit();
                                    checkingLoginState = true;
                                    /*-------------------------------------------------------------------------------*/
                                    /*-------------------------------------------------------------------------------*/

                                    Login.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Login.this, "歡迎!" + account.getText().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();

                                } else
                                    Log.i("Wrong", "YOU ARE NOT THE MEMBER !!!");
                                Login.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Login.this, "請註冊為會員", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.i("Wrong", "YOU ARE NOT THE MEMBER !!!");
                                Login.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Login.this, "請註冊為會員", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    Login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login.this, "不得為空!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        ///////////////////////////////////////////////////////

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.this.startActivityForResult(new Intent(Login.this, Regist.class), 33);
            }
        });
    }

}
