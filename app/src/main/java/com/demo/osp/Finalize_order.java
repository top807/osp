package com.demo.osp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Finalize_order extends AppCompatActivity {

    public static Socket client;
    public static PrintWriter printwriter;
    public static BufferedReader bb;
    public static String messsage;
    public static String m1;
    static int old_all_total;
    static int all_total;
    static int next_ord_flag;
    static String order_string;
    static String old_ord_string = "";
    public String S;
    Finalize_order a = this;
    String personal_preferances;
    Object oo;
    String fin_order_string = "";

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_order);
        show_Order();
        tot_v();
        oo = this;
    }


    public void show_Order() {

    }

    public void main_menu(View view) {
        Intent goto_main_menu = new Intent(this, Order_Type.class);
        startActivity(goto_main_menu);
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
    }

    public void tot_v() {
        all_total = all_total + old_all_total;
        TextView tv = (TextView) findViewById(R.id.tot_p);
        tv.setText("total price:" + "₹" + (Finalize_order.all_total));
    }

    public void send_ord(View v) {
        order_string = fin_order_string;

        EditText Ed = (EditText) findViewById(R.id.personalp);

        personal_preferances = Ed.getText().toString();

        final Object o = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to confirm this order?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        messsage = "Order:" + SlimpleTextClientActivity.tablex + "|" + fin_order_string + "|" + Integer.toString(all_total) + "|" + personal_preferances;
                        ; // get the text message on the text field
                        // messsage = "Order:" + SlimpleTextClientActivity.tablex + "|" + fin_order_string + "|" + Integer.toString(all_total);
                        SendMessage sendMessageTask = new SendMessage();
                        sendMessageTask.execute();
                        Intent nextact = new Intent((Finalize_order) o, thankyou.class);
                        startActivity(nextact);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class SendMessage extends AsyncTask<Void, Void, Void> {
        BufferedReader bb;
        Socket client;
        PrintWriter printwriter;


        @Override
        protected Void doInBackground(Void... params) {
            try {
                client = new Socket("192.168.1.105", 4444); // connect to the server
                printwriter = new PrintWriter(client.getOutputStream(), true);
                bb = new BufferedReader(new InputStreamReader(client.getInputStream()));
                printwriter.println(Finalize_order.messsage); // write the message to output stream
                printwriter.flush();
                m1 = bb.readLine();
                //System.out.println("dd:"+m1);
                //m1="hello";
                bb.close();
                client.close(); // closing the connection
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}