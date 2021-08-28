package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlms.mobilelaundrymanagementsystemadmin.models.adminNemployeeModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class OpenNewBillActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore fireStore;
    FirebaseDatabase db;

    Button confirm_btn;
    ImageButton back_btn;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_new_bill);

        auth=FirebaseAuth.getInstance();
        fireStore=FirebaseFirestore.getInstance();
        db=FirebaseDatabase.getInstance();

        calendar=Calendar.getInstance();

        confirm_btn=findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defineCabang();
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

    public void defineCabang(){

        db.getReference().child("loginAdminNEmployee").child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        adminNemployeeModel adminNemployeeModel=snapshot.getValue(adminNemployeeModel.class);
                        assert adminNemployeeModel != null;

                        String cabang=adminNemployeeModel.getCabang();
                        Log.w("Cabang", cabang);

                        defineBillNo(cabang);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void defineBillNo(String cabangID){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentYear=new SimpleDateFormat("yy");

        fireStore.collection(cabangID)
                .document(currentYear.format(calendar.getTime()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if(document.get("countBill")!=null){
                                Log.i("LOGGER","Count: "+document.get("countBill"));

                                int countBill=((Long) Objects.requireNonNull(document.get("countBill"))).intValue();

                                Toast.makeText(OpenNewBillActivity.this, "Bill number: "+countBill,Toast.LENGTH_SHORT).show();

                                countBill++;
                                Toast.makeText(OpenNewBillActivity.this, "Bill number: "+countBill,Toast.LENGTH_SHORT).show();

                                HashMap<String, Object> updateBillCount=new HashMap<>();
                                updateBillCount.put("countBill", countBill);
                                fireStore.collection(cabangID)
                                        .document(currentYear.format(calendar.getTime()))
                                        .set(updateBillCount);

                                OpenBill(cabangID,countBill);

                            }else{
                                Log.d("LOGGER", "No such document");
                                Log.d("LOGGER", "Document count added!");
                                int countBill=1;

                                HashMap<String, Object> updateBillCount=new HashMap<>();
                                updateBillCount.put("countBill", countBill);

                                fireStore.collection(cabangID)
                                        .document(currentYear.format(calendar.getTime()))
                                        .set(updateBillCount);

                                OpenBill(cabangID,countBill);

                            }

                        }else{
                            Log.d("LOGGER", "No such document");
                        }
                    }
                });
    }

    public void OpenBill(String cabangID, int BillNo){

        Calendar calendar=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate=new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm a");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentYear=new SimpleDateFormat("yy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentMonth=new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDay=new SimpleDateFormat("dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat billNumGenDate=new SimpleDateFormat("yyMMdd");


        @SuppressLint("DefaultLocale") String lastDig = String.format("%04d", BillNo);
        String billNumber = cabangID+billNumGenDate.format(calendar.getTime()) + lastDig;
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
        int total_qty=1;
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
        cartMap.put("total_qty", total_qty);


        fireStore.collection(cabangID)
                .document(currentYear.format(calendar.getTime()))
                .collection(currentMonth.format(calendar.getTime()))
                .document(currentDay.format(calendar.getTime()))
                .collection(lastDig)
                .document(billNumber)
                .set(cartMap);

        Toast.makeText(this, "Bill created!", Toast.LENGTH_SHORT).show();

        finish();
    }
}