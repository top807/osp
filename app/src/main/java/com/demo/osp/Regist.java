package com.demo.osp;

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

public class Regist extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private Button regist, cancel;
    private EditText add_account, add_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        findViews();
        setBtn();
    }

    private void findViews() {
        regist = findViewById(R.id.btn_reg);
        cancel = findViewById(R.id.btn_cancel);
        add_account = findViewById(R.id.ed_mail);
        add_password = findViewById(R.id.ed_pas);
    }

    private void setBtn() {

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    root = database.getReference("/account/" + add_account.getText().toString());
                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            HashMap accountName = (HashMap) dataSnapshot.getValue();

                            if (!(accountName == null)) {

                                Log.i("Wrong", "Already has this account !!!");
                                Regist.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Regist.this, "該帳號已被註冊。", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {

                                DatabaseReference input_account = database.getReference("account");
                                input_account.child(add_account.getText().toString()).child("password").setValue(add_password.getText().toString());

                                finish();

                                Regist.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Regist.this, "註冊成功。", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    Regist.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Regist.this, "不得為空!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        ///////////////////////////////////////////////////////

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_account.setText("");
                add_password.setText("");
            }
        });
    }
}
