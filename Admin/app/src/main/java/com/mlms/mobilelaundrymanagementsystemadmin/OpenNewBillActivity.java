package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OpenNewBillActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore fireStore;
    FirebaseDatabase db;

    EditText customerName_input ,customerPhoneNo_input, totalWeight_input, totalQty_input;
    TextView totalPrice_tv;
    Button confirm_btn;
    ImageButton back_btn, additionalItem_btn;

    LinearLayout addItem_layout;
    List<String> item_types_list=new ArrayList<>();

    Calendar calendar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_new_bill);

        auth=FirebaseAuth.getInstance();
        fireStore=FirebaseFirestore.getInstance();
        db=FirebaseDatabase.getInstance();

        calendar=Calendar.getInstance();


        customerName_input=findViewById(R.id.customerName_input);
        customerPhoneNo_input=findViewById(R.id.customerPhone_input);
        totalQty_input=findViewById(R.id.totalqty_input);
        totalWeight_input=findViewById(R.id.totalWeight_input);

        totalPrice_tv=findViewById(R.id.totalPrice);


        totalWeight_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                DecimalFormat priceFormatter = new DecimalFormat("#0,000.00");
                totalPrice_tv.setText("Rp" + priceFormatter.format(0));
                if(!TextUtils.isEmpty(totalWeight_input.getText())) {
                    double totalWeight = Double.parseDouble(String.valueOf(totalWeight_input.getText()));
                    Double totalPrice = totalWeight * 2900;
                    totalPrice_tv.setText("Rp" + priceFormatter.format(totalPrice));
                }
            }
        });



        additionalItem_btn=findViewById(R.id.addItem_btn);
        addItem_layout=findViewById(R.id.addItem_layout);
        item_types_list.add("Jacket");
        item_types_list.add("Spre");
        item_types_list.add("Bantal");
        item_types_list.add("Selimut");
        item_types_list.add("Gorden");
        item_types_list.add("Other");

        additionalItem_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                addItem();
            }
        });



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
            }
        });
    }

    public void addItem(){

        @SuppressLint("InflateParams") final View itemView =getLayoutInflater().inflate(R.layout.row_opennewbill_additionalitems,null,false);

        EditText item_qty= itemView.findViewById(R.id.additional_qty);
        EditText item_other= itemView.findViewById(R.id.additional_other_input);
        TextView item_price=itemView.findViewById(R.id.additional_price);
        AppCompatSpinner item_type=itemView.findViewById(R.id.item_type_spinner);
        ImageView cancel_btn=itemView.findViewById(R.id.additional_item_cancel_btn);

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, item_types_list);
        item_type.setAdapter(arrayAdapter);

        item_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object choice=String.valueOf(parent.getItemAtPosition(position));

                if(choice=="Other"){
                    item_other.setVisibility(View.VISIBLE);
                }else{
                    item_other.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(itemView);
            }
        });

        addItem_layout.addView(itemView);
    }

    public void removeView(View view){

        addItem_layout.removeView(view);

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

                        String employeeName= adminNemployeeModel.getName();

                        defineBillNo(cabang, employeeName);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void defineBillNo(String cabangID, String employeeName){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentYear=new SimpleDateFormat("yy");

        fireStore.collection("Yearly Book")
                .document(cabangID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if(document.get("countBill_"+currentYear.format(calendar.getTime()))!=null){
                                Log.i("LOGGER","Count: "+document.get("countBill"));

                                int countBill=((Long) Objects.requireNonNull(document.get("countBill_"+currentYear.format(calendar.getTime())))).intValue();

                                Toast.makeText(OpenNewBillActivity.this, "Bill number: "+countBill,Toast.LENGTH_SHORT).show();

                                countBill++;
                                Toast.makeText(OpenNewBillActivity.this, "Bill number: "+countBill,Toast.LENGTH_SHORT).show();

                                HashMap<String, Object> updateBillCount=new HashMap<>();
                                updateBillCount.put("countBill_"+currentYear.format(calendar.getTime()), countBill);
                                fireStore.collection("Yearly Book")
                                        .document(cabangID)
                                        .set(updateBillCount);

                                OpenBill(cabangID,countBill, employeeName);

                            }else{
                                Log.d("LOGGER", "No such document");
                                Log.d("LOGGER", "Document count added!");
                                int countBill=1;

                                HashMap<String, Object> updateBillCount=new HashMap<>();
                                updateBillCount.put("countBill_"+currentYear.format(calendar.getTime()), countBill);

                                fireStore.collection("Yearly Book")
                                        .document(cabangID)
                                        .set(updateBillCount);

                                OpenBill(cabangID,countBill, employeeName);
                            }

                        }else{
                            Log.d("LOGGER", "No such document");
                        }
                    }
                });
    }

    public void OpenBill(String cabangID, int BillNo, String employeeName){

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
        int customerPhone=1234567;


        HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("billID", billNumber);
        cartMap.put("customerName", customerName);
        cartMap.put("employeeID", employeeID);
        cartMap.put("employeeName", employeeName);
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
        cartMap.put("customerPhone", customerPhone);


        fireStore.collection("Yearly Book")
                .document(cabangID)
                .collection(currentYear.format(calendar.getTime()))
                .document(currentMonth.format(calendar.getTime()))
                .collection(currentDay.format(calendar.getTime()))
                .document(lastDig)
                .set(cartMap);

        Toast.makeText(this, "Bill created!", Toast.LENGTH_SHORT).show();

        finish();
    }
}