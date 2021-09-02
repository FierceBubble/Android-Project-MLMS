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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mlms.mobilelaundrymanagementsystemadmin.models.ListOfItemsModel;
import com.mlms.mobilelaundrymanagementsystemadmin.models.ListOfPaketModel;
import com.mlms.mobilelaundrymanagementsystemadmin.models.adminNemployeeModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OpenNewBillActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore fireStore;
    FirebaseDatabase db;

    String cabang;
    String employeeName;

    EditText customerName_input ,customerPhoneNo_input, totalWeight_input, totalQty_input;
    TextView totalPrice_tv, total_price_lot_tv, total_price_additionalItem_tv;
    Button confirm_btn, check_btn;
    ImageButton back_btn, additionalItem_btn;
    AppCompatSpinner paymentMethod_spinner;

    LinearLayout addItem_layout, paket_layout;
    CheckBox[] paket_checkbox;
    ArrayList<ListOfPaketModel> listOfPaketModelsArrayList=new ArrayList<>();
    List<String> item_name_list=new ArrayList<>();
    List<Double> item_price_list=new ArrayList<>();
    Double total_price_per_item;
    Double total_price_all_item=0.0; // Additional Item total price
    Double total_price_lot=0.00; // Lot total price
    Double total_price_all=0.00;
    List<String> payment_method_list=new ArrayList<>();
    ArrayList<ListOfItemsModel> listOfItemsModelArrayList=new ArrayList<>();

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

        total_price_lot_tv=findViewById(R.id.total_price_lot_tv);
        total_price_additionalItem_tv=findViewById(R.id.total_price_additionalItem_tv);
        totalPrice_tv=findViewById(R.id.totalPrice);

        paymentMethod_spinner = findViewById(R.id.paymentMethodSpinner);
        paket_layout = findViewById(R.id.paket_list_layout);
        defineSpinnerChoice();

        additionalItem_btn=findViewById(R.id.addItem_btn);
        addItem_layout=findViewById(R.id.addItem_layout);
        additionalItem_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        check_btn=findViewById(R.id.check_btn);
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat priceFormatter = new DecimalFormat("#0,000.00");
                isAdditionalItemAdded();
                confirm_btn.setVisibility(View.VISIBLE);

                if(total_price_all_item==0.0){

                    if(listOfItemsModelArrayList.size()!=0){

                        for(ListOfItemsModel list: listOfItemsModelArrayList){
                            total_price_all_item=total_price_all_item+list.getItem_price();
                            total_price_additionalItem_tv.setText("Rp"+priceFormatter.format(total_price_all_item));
                        }
                        total_price_all=total_price_all+total_price_all_item;
                        totalPrice_tv.setText("Rp" + priceFormatter.format(total_price_all));
                    }
                }else if(total_price_all_item!=0.0){
                    total_price_all=total_price_all-total_price_all_item;
                    totalPrice_tv.setText("Rp" + priceFormatter.format(total_price_all));
                    total_price_all_item=0.0;
                    total_price_additionalItem_tv.setText("Rp"+priceFormatter.format(total_price_all_item));

                    if(listOfItemsModelArrayList.size()!=0){

                        for(ListOfItemsModel list: listOfItemsModelArrayList){
                            total_price_all_item=total_price_all_item+list.getItem_price();
                            total_price_additionalItem_tv.setText("Rp"+priceFormatter.format(total_price_all_item));
                        }
                        total_price_all=total_price_all+total_price_all_item;
                        totalPrice_tv.setText("Rp" + priceFormatter.format(total_price_all));
                    }
                }
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

    public void checkAllfields(){




    }

    public void defineSpinnerChoice(){

        //Payment Method choices
        payment_method_list.add("Pilih");
        payment_method_list.add("Cash");
        payment_method_list.add("ShopeePay");
        payment_method_list.add("GoPay");
        payment_method_list.add("Ovo");

        //List of additional items
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, payment_method_list);
        paymentMethod_spinner.setAdapter(arrayAdapter);


        db.getReference().child("loginAdminNEmployee").child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        adminNemployeeModel adminNemployeeModel=snapshot.getValue(adminNemployeeModel.class);
                        assert adminNemployeeModel != null;

                        String cabang=adminNemployeeModel.getCabang();

                        fireStore.collection("Cabang Information")
                                .document(cabang)
                                .collection("Daftar Harga")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            item_name_list.add("Pilih");
                                            item_price_list.add(0.00);
                                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                                ListOfItemsModel listOfItemsModel=document.toObject(ListOfItemsModel.class);
                                                item_name_list.add(listOfItemsModel.getItem_name());
                                                item_price_list.add(listOfItemsModel.getItem_price());
                                            }
                                            item_name_list.add("Other");
                                            item_price_list.add(0.00);
                                            Toast.makeText(OpenNewBillActivity.this,"Total list String: "+item_name_list.size(),Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(OpenNewBillActivity.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        fireStore.collection("Cabang Information")
                                .document(cabang)
                                .collection("Daftar Harga Paket Lot")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){

                                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                                ListOfPaketModel listOfPaketModel=document.toObject(ListOfPaketModel.class);
                                                listOfPaketModelsArrayList.add(listOfPaketModel);
                                            }
                                            paket_checkbox= new CheckBox[listOfPaketModelsArrayList.size()];
                                            //List of paket choices
                                            for(int i=0; i<listOfPaketModelsArrayList.size(); i++){
                                                createPaketChoices(i);
                                            }

                                            Toast.makeText(OpenNewBillActivity.this,"Total list String: "+item_name_list.size(),Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(OpenNewBillActivity.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    public void createPaketChoices(int index){

        DecimalFormat priceFormatter = new DecimalFormat("#0,000.00");
        paket_checkbox[index] = new CheckBox(this);
        paket_checkbox[index].setId(index);
        paket_checkbox[index].setText(listOfPaketModelsArrayList.get(index).getPaket_name());
        paket_checkbox[index].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(paket_checkbox[index].isChecked()){
                    total_price_lot=total_price_lot+listOfPaketModelsArrayList.get(index).getPaket_price();
                    total_price_all=total_price_all+listOfPaketModelsArrayList.get(index).getPaket_price();
                }else if (!paket_checkbox[index].isChecked()){
                    total_price_lot=total_price_lot-listOfPaketModelsArrayList.get(index).getPaket_price();
                    total_price_all=total_price_all-listOfPaketModelsArrayList.get(index).getPaket_price();
                }
                total_price_lot_tv.setText("Rp" + priceFormatter.format(total_price_lot));
                totalPrice_tv.setText("Rp" + priceFormatter.format(total_price_all));
            }
        });
        paket_layout.addView(paket_checkbox[index]);


    }


    public void addItem(){

        @SuppressLint("InflateParams") final View itemView =getLayoutInflater().inflate(R.layout.row_opennewbill_additionalitems,null,false);

        EditText item_qty = itemView.findViewById(R.id.additional_qty);
        EditText item_other = itemView.findViewById(R.id.additional_other_input);
        EditText item_other_price = itemView.findViewById(R.id.additional_other_price_input);
        TextView item_price = itemView.findViewById(R.id.additional_price);
        AppCompatSpinner item_type = itemView.findViewById(R.id.item_type_spinner);
        ImageView cancel_btn = itemView.findViewById(R.id.additional_item_cancel_btn);

        DecimalFormat priceFormatter = new DecimalFormat("#0,000.00");


        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, item_name_list);
        item_type.setAdapter(arrayAdapter);

        item_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_qty.restoreDefaultFocus();
                item_qty.getText().clear();
                item_other.getText().clear();
                item_other_price.getText().clear();

                Object choice=String.valueOf(parent.getItemAtPosition(position));
                item_price.setText("Rp"+priceFormatter.format(item_price_list.get(position)));
                total_price_per_item=item_price_list.get(position);

                if(choice=="Other"){
                    item_other.setVisibility(View.VISIBLE);
                    item_other_price.setVisibility(View.VISIBLE);

                    item_other_price.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void afterTextChanged(Editable s) {

                            if(!TextUtils.isEmpty(item_other_price.getText())){
                                total_price_per_item=Double.parseDouble(item_other_price.getText().toString());
                                item_price.setText("Rp" + priceFormatter.format(total_price_per_item));
                            }else{
                                total_price_per_item=0.0;
                                item_price.setText("Rp" + priceFormatter.format(0));
                            }

                        }
                    });

                }else{
                    item_other.setVisibility(View.INVISIBLE);
                    item_other_price.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        item_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {

                if(!TextUtils.isEmpty(item_qty.getText())){
                    Double total_price_per_qty=total_price_per_item*Integer.parseInt(item_qty.getText().toString());
                    item_price.setText("Rp" + priceFormatter.format(total_price_per_qty));
                }else{
                    item_price.setText("Rp" + priceFormatter.format(total_price_per_item));
                }
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

                                countBill++;

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
        String customerName = customerName_input.getText().toString() ;
        String employeeID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        String paymentMethod = paymentMethod_spinner.getSelectedItem().toString();
        String start_date = currentDate.format(calendar.getTime());
        String start_time = currentTime.format(calendar.getTime());
        String status = "Baru";
        String status_date = currentDate.format(calendar.getTime());
        String status_time = currentTime.format(calendar.getTime());
        Double totalWeight = Double.parseDouble(totalWeight_input.getText().toString());
        Double totalPrice = total_price_all;
        int total_qty=Integer.parseInt(totalQty_input.getText().toString());
        int total_additional_qty = listOfItemsModelArrayList.size();
        int customerPhone=Integer.parseInt(customerPhoneNo_input.getText().toString());

        String[] selected_paket=new String[listOfPaketModelsArrayList.size()];

        int j=0;
        for(int i=0; i<listOfPaketModelsArrayList.size();i++){
            if(paket_checkbox[i].isChecked()){
                String chosen=listOfPaketModelsArrayList.get(i).getPaket_name();
                selected_paket[j]=chosen;
                j++;
            }
        }

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
        cartMap.put("paket_choice", Arrays.asList(selected_paket));
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

        fireStore.collection("Cabang Information")
                .document(cabangID)
                .collection("Active List")
                .document(billNumber)
                .set(cartMap);


        if(listOfItemsModelArrayList.size()!=0){
            add_additionalItems(cabangID, BillNo, billNumber);
        }


        Toast.makeText(this, "Bill created!", Toast.LENGTH_SHORT).show();

        finish();
    }

    public void isAdditionalItemAdded(){

        listOfItemsModelArrayList.clear();

        for(int i=0; i<addItem_layout.getChildCount();i++){

            View itemView=addItem_layout.getChildAt(i);

            EditText item_qty= itemView.findViewById(R.id.additional_qty);
            EditText item_other= itemView.findViewById(R.id.additional_other_input);
            EditText item_other_price = itemView.findViewById(R.id.additional_other_price_input);
            TextView item_price = itemView.findViewById(R.id.additional_price);
            AppCompatSpinner item_type=itemView.findViewById(R.id.item_type_spinner);

            ListOfItemsModel listOfItemsModel=new ListOfItemsModel();

            if(!item_qty.getText().toString().equals("") && item_type.getSelectedItem()!="Other" && item_type.getSelectedItemPosition()!=0){
                int item_qty_total=Integer.parseInt(item_qty.getText().toString());

                String price=item_price.getText().toString();
                String remove_last_three=price.substring(0,price.length()-3);
                String remove_coma=remove_last_three.replace(",","");

                listOfItemsModel.setItem_name(item_name_list.get(item_type.getSelectedItemPosition()));
                listOfItemsModel.setItem_qty(item_qty_total) ;
                listOfItemsModel.setItem_price(Double.parseDouble(remove_coma.replace("Rp","")));

                listOfItemsModelArrayList.add(listOfItemsModel);

            }else if(item_type.getSelectedItem()=="Other" && !item_qty.getText().toString().equals("") && !item_other.getText().toString().equals("") && !item_other_price.getText().toString().equals("")){
                int item_qty_total=Integer.parseInt(item_qty.getText().toString());

                String price=item_price.getText().toString();
                String remove_last_three=price.substring(0,price.length()-3);
                String remove_coma=remove_last_three.replace(",","");

                listOfItemsModel.setItem_name(item_other.getText().toString());
                listOfItemsModel.setItem_qty(item_qty_total);
                listOfItemsModel.setItem_price(Double.parseDouble(remove_coma.replace("Rp","")));

                listOfItemsModelArrayList.add(listOfItemsModel);
            } else {
                Toast.makeText(this,"Please fill empty fields!", Toast.LENGTH_SHORT).show();
            }

        }

        Toast.makeText(this,"Additional List Size: "+listOfItemsModelArrayList.size(),Toast.LENGTH_SHORT).show();
    }

    public void add_additionalItems(String cabangID, int BillNo, String billNumber){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentYear=new SimpleDateFormat("yy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentMonth=new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDay=new SimpleDateFormat("dd");
        @SuppressLint("DefaultLocale") String lastDig = String.format("%04d", BillNo);

        for(ListOfItemsModel list: listOfItemsModelArrayList){

            HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("item_name",list.getItem_name());
            cartMap.put("item_qty",list.getItem_qty());
            cartMap.put("item_price", list.getItem_price());

            fireStore.collection("Yearly Book")
                    .document(cabangID)
                    .collection(currentYear.format(calendar.getTime()))
                    .document(currentMonth.format(calendar.getTime()))
                    .collection(currentDay.format(calendar.getTime()))
                    .document(lastDig)
                    .collection("Additional Items")
                    .document(list.getItem_name())
                    .set(cartMap);

            fireStore.collection("Cabang Information")
                    .document(cabangID)
                    .collection("Active List")
                    .document(billNumber)
                    .collection("Additional Items")
                    .document(list.getItem_name())
                    .set(cartMap);

        }

    }

}