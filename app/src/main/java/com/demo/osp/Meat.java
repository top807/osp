package com.demo.osp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Meat extends AppCompatActivity {

    //TODO ref
    private TextView user;
    private String username;
    private boolean checkLoginState;
    private SharedPreferences share;
    private ListView listView;
    private HashMap<String, Bitmap> img = new HashMap<>();
    private int sub = 0;
    private int total = 0;
    private TextView meat_total;


    //TODO MyModel
    private ArrayList<DataModel> dataModels;

    //TODO MyListAdapter
    private MyListAdapter adapter;

    //TODO Firebase ref
    private FirebaseDatabase database;
    private DatabaseReference data;
    private FirebaseStorage storage;
    private ArrayList<String> arr;

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meat);

        share = getSharedPreferences("user", MODE_PRIVATE);
        findViews();
        checkLogin();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dataModels = new ArrayList<DataModel>();

        getMeatData();
        getPicture();
    }

    private void findViews() {
        user = findViewById(R.id.txt_user_login_meat);
        listView = findViewById(R.id.mylist_meat);
        meat_total = findViewById(R.id.txt_meat_total);
    }

    //TODO GET DATA FROM FIREBASE SEAFOOD & SET ADAPTER
    private void getMeatData() {
        data = database.getReference("/main_menu/main_dish/meat/");

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> s1 = dataSnapshot.getChildren();

                DataSnapshot sss = dataSnapshot.getChildren().iterator().next();
                ArrayList l = new ArrayList();

                for (DataSnapshot snap : s1) {
//                    Log.i("IT3", snap.getValue().toString());

                    String dish = snap.child("name").getValue().toString();
                    String price = snap.child("price").getValue().toString();
                    String stock = snap.child("stock_quantity").getValue().toString();
                    String order = snap.child("order_count").getValue().toString();

                    dataModels.add(new DataModel(dish, price, stock));
                }

                //TODO Set Adapter
                adapter = new MyListAdapter(dataModels, getApplicationContext()); //keep alive
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //TODO GET PICTURE
    private void getPicture() {

        DatabaseReference pic = database.getReference("/main_menu/main_dish/meat/");
        pic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, HashMap<String, Object>> hashMap = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
                HashMap hashMap1 = (HashMap) dataSnapshot.getValue();

                final Set<String> names = hashMap1.keySet();

                total = names.size();

                arr = new ArrayList<>();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        for (final String s : names) {

                            arr.add(s);

                            File localFile = null;
                            try {
                                localFile = File.createTempFile("images", "jpg");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            final File finalLocalFile = localFile;
                            FirebaseStorage.getInstance().getReference()
                                    .child("/abc")
                                    .child(s + ".jpg")
                                    .getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            // Successfully downloaded data to local file
                                            // ...

                                            Log.i("---------PATH---------", FirebaseStorage.getInstance().getReference()
                                                    .child("/abc")
                                                    .child(s + ".jpg").getPath());
                                            Bitmap bitbit = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                                            img.put(s, bitbit);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle failed download
                                    // ...
                                }
                            });
                        }
                        Log.i("-----arr.toString-----", arr.toString());

                        Meat.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getListData();
                            }
                        });
                    }
                }.start();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //TODO Get Total Count Changed
    private void getDataChange() {

        final int[] count = new int[total];


        for (int i = 0; i < total; i++) {


//            Log.i("COUNT", count + " 22222222222222");

            TextView price = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "price");
            final TextView order_number = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "order");
            Button btn_inc = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "inc");
            Button btn_dsc = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "dsc");
            final TextView stock = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "stock");

            final int current_item_price = Integer.parseInt(price.getText().toString());
            int order_amount = Integer.parseInt(order_number.getText().toString());
            final int[] numb = {0};
            final int[] num = {0};
            numb[0] = current_item_price * order_amount;

            final int finalI = i;
            btn_inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int quantity = Integer.parseInt(stock.getText().toString());

                    if (num[0] < quantity && num[0] >= 0) {
                        sub = 0;
                        ++num[0];
                        order_number.setText("" + num[0]);
                        count[finalI] = (num[0] * current_item_price);

                        for (int ii : count) {
                            sub += ii;
                        }
                        meat_total.setText(sub + "");

                    } else {
                        sub = 0;
                        order_number.setText(quantity + "");
                        count[finalI] = num[0] * current_item_price;

                        for (int ii : count) {
                            sub += ii;
                        }
                        meat_total.setText(sub + "");
                    }
                }
            });

            btn_dsc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (num[0] <= 0) {
                        sub = 0;
                        order_number.setText(0 + "");
                        count[finalI] = num[0] * current_item_price;

                        for (int ii : count) {
                            sub += ii;
                        }
                        meat_total.setText(sub + "");
                    } else {
                        sub = 0;
                        --num[0];
                        order_number.setText("" + num[0]);
                        count[finalI] = num[0] * current_item_price;

                        for (int ii : count) {
                            sub += ii;
                        }
                        meat_total.setText(sub + "");
                    }
                }
            });

        }
    }

    //TODO Toast Image With Corresponded DishName
    private void toastImg() {

        for (int i = 0; i < total; i++) {

//            Log.i("--------------", "!!!!!  total !!!!!!");


            final int finalI = i;
            Meat.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView dishname = ((RelativeLayout) getViewByPosition(finalI, listView)).findViewWithTag(finalI + "dishname");
//                    final TextView dishName = ((RelativeLayout) getViewByPosition(finalI, listView)).findViewById(R.id.model_txt_dishname);
                    dishname.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
//                            Log.i("--------------", "??????????? Click !!!!!!");
                            ImageView image = new ImageView(Meat.this);
                            image.setImageBitmap(img.get(arr.get(finalI)));
                            Toast toast = Toast.makeText(Meat.this, "AAA", Toast.LENGTH_SHORT);
                            toast.setView(image);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }
            });


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    private void checkLogin() {
        checkLoginState = share.getBoolean("loginState", false);
        username = share.getString("user", "訪客/登入");

        // TODO : Login Button & login state check
        if (!checkLoginState) {
            user.setText("訪客/登入");
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Meat.this.startActivityForResult(new Intent(Meat.this, Login.class), 11);
                }
            });
        } else {
            user.setText(username + "/登出");
            user.setOnClickListener(new View.OnClickListener() {
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

    //TODO Get ListView Items
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void main_menu(View view) {
        Intent goto_main_menu = new Intent(this, Order_Type.class);
        startActivity(goto_main_menu);
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        finish();
    }

    public void fin_ord(View view) {
        if (!checkLoginState) {
            new AlertDialog.Builder(Meat.this)
                    .setTitle("OAO !!!")
                    .setMessage("登入後方可結帳與購物   :)")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        } else {
            final Object o = this;
            if (Finalize_order.all_total > 0) {
                Intent fin = new Intent(this, Finalize_order.class);
                startActivity(fin);
                overridePendingTransition(R.anim.fadin, R.anim.fadout);
            } else {
//                if (Finalize_order.next_ord_flag == 1) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage("Are you sure you don't want to place another order?")
//                            .setCancelable(false)
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    Intent nextact = new Intent((Meat) o, thankyou.class);
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
            }
        }
    }

    private void getListData() {

        //TODO Get All Items In Each ListView Items
        /*------------------------------------------------------------------------------------------------------*/

        for (int i = 0; i < total; i++) {

            TextView dishname = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "dishname");
            TextView stock = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "stock");
            TextView price = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "price");
            TextView order_number = ((RelativeLayout) getViewByPosition(i, listView)).findViewWithTag(i + "order");

            Log.i("---------", dishname.getText().toString() + " " + price.getText().toString() + " " +
                    stock.getText().toString() + " " + order_number.getText().toString() + "!!!!!!!!!!!!");

            getDataChange();
            toastImg();
        }
    }
}


