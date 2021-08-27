package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlms.mobilelaundrymanagementsystemadmin.models.LaundryModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OpenNewBillActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore fireStore;
    FirebaseDatabase db;

    Button confirm_btn;
    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_new_bill);

        auth=FirebaseAuth.getInstance();
        fireStore=FirebaseFirestore.getInstance();
        db=FirebaseDatabase.getInstance();

        confirm_btn=findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewBill();
            }
        });

        back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.d("Back button","Back button clicked!, returned to mainActivity");
            }
        });
    }

    public void openNewBill(){

        Calendar calendar=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate=new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm a");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentYear=new SimpleDateFormat("yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentMonth=new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDay=new SimpleDateFormat("dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat billNumGenDate=new SimpleDateFormat("yyyyMMdd");

        for(int count=1;count<=5;count++) {
            @SuppressLint("DefaultLocale") String lastDig = String.format("%04d", count);
            String billNumber = billNumGenDate.format(calendar.getTime()) + lastDig;
            String customerName = "John Doe#3";
            String employeeID = "whS9n4tJBWb2VikpmSpT0Y9AMQj2";
            String paymentMethod = "GrabPay";
            String start_date = currentDate.format(calendar.getTime());
            String start_time = currentTime.format(calendar.getTime());
            String status = "Mulai";
            String status_date = currentDate.format(calendar.getTime());
            String status_time = currentTime.format(calendar.getTime());
            Double totalWeight = 20.0;
            Double totalPrice = 100000.00;
            int total_additional_qty = 1;

            HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("billID", billNumber);
            cartMap.put("customerName", customerName);
            cartMap.put("employeeID", employeeID);
            cartMap.put("paymentMethod", paymentMethod);
            cartMap.put("start_date", start_date);
            cartMap.put("start_time", start_time);
            cartMap.put("status", status);
            cartMap.put("status_date", status_date);
            cartMap.put("status_time", status_time);
            cartMap.put("totalPrice", totalPrice);
            cartMap.put("totalWeight", totalWeight);
            cartMap.put("total_additional_qty", total_additional_qty);


            fireStore.collection(currentYear.format(calendar.getTime())).document(currentMonth.format(calendar.getTime()))
                    .collection("31").document(billNumber).set(cartMap);

            Toast.makeText(this, "Bill created!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}