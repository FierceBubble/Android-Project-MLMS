package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mlms.mobilelaundrymanagementsystemadmin.adapters.ListOfItemsAdapter;
import com.mlms.mobilelaundrymanagementsystemadmin.models.CabangListModel;
import com.mlms.mobilelaundrymanagementsystemadmin.models.LaundryModel;
import com.mlms.mobilelaundrymanagementsystemadmin.models.ListOfItemsModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class LaundryDetailViewActivity extends AppCompatActivity {

    private static final String TAG ="LaundryDetailViewActivity";
    ImageButton back_btn;
    TextView billID, pic, pic_id, customerName, DoS, DoLU, status, totalWeight,
            totalPrice, paymentMethod, additional_item_label, customerPhone,
            paket_choice, totalQTY;
    RecyclerView listOfItem_qty_Rec;
    Button update_btn;

    List<ListOfItemsModel> listOfItemsModelsList_qty;
    ListOfItemsAdapter listOfItemsAdapter_qty;

    LaundryModel laundryModel = null;

    FirebaseAuth auth;
    FirebaseFirestore database;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_detail_view);

        auth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();

        billID=findViewById(R.id.billID_detail_label);
        pic=findViewById(R.id.pic_label);
        pic_id=findViewById(R.id.pic_id_label);
        customerName=findViewById(R.id.customerName_label);
        customerPhone=findViewById(R.id.customerPhone_label);
        DoS=findViewById(R.id.DoS_label);
        DoLU=findViewById(R.id.DoLU_label);
        status=findViewById(R.id.status_label);
        totalPrice=findViewById(R.id.totalPrice_label);
        totalWeight=findViewById(R.id.totalWeight_label);
        totalQTY=findViewById(R.id.totalqty_label);
        paymentMethod=findViewById(R.id.paymentMethod);
        paket_choice=findViewById(R.id.paket_choice_label);
        additional_item_label=findViewById(R.id.additional_item_label);

        listOfItem_qty_Rec=findViewById(R.id.listofItem_qty_Rec);

        update_btn=findViewById(R.id.update_btn);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialoForUpdate(laundryModel.getBillID(), laundryModel.getStatus());
            }
        });

        back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            finish();
            Log.d("Back button","Back button clicked!, returned to mainActivity");
        });

        final Object object=getIntent().getSerializableExtra("position");
        if(object instanceof LaundryModel){
            laundryModel=(LaundryModel) object;
        }

        if(laundryModel!=null){
            String LUD="Last updated:\n"+laundryModel.getStatus_date()+" "+laundryModel.getStatus_time();
            String SD="Last updated:\n"+laundryModel.getStart_date()+" "+laundryModel.getStart_time();
            String Status="Status: "+laundryModel.getStatus();

            DecimalFormat weightFormatter = new DecimalFormat("#0.00");
            String weight="Total berat: "+weightFormatter.format(laundryModel.getTotalWeight())+"kg";

            DecimalFormat priceFormatter = new DecimalFormat("#0,000.00");
            String price="Total harga:\nRp"+priceFormatter.format(laundryModel.getTotalPrice());

            String customerNo=String.valueOf(laundryModel.getCustomerPhone());

            if(laundryModel.getPaket_choice()!=null){
                StringBuilder paket= new StringBuilder("Pilihan paket:\n");
                for(int i=0; i<laundryModel.getPaket_choice().size(); i++){
                    paket.append(laundryModel.getPaket_choice().get(i)+"\n");
                }
                paket_choice.setText(paket);
            }else{
                paket_choice.setText("");
            }

            billID.setText(laundryModel.getBillID());
            pic.setText(laundryModel.getEmployeeName());
            pic_id.setText(laundryModel.getEmployeeID());
            customerName.setText(laundryModel.getCustomerName());
            customerPhone.setText(customerNo);
            DoS.setText(SD);
            DoLU.setText(LUD);
            status.setText(Status);
            totalPrice.setText(price);
            totalWeight.setText(weight);
            totalQTY.setText("Total qty: "+laundryModel.getTotal_qty());
            paymentMethod.setText("Bayar menggunakan:\n"+laundryModel.getPaymentMethod());

            String billID=laundryModel.getBillID();

            showListOfAdditionalItems(billID);

        }

        Log.i(TAG,"onCreate");
    }

    public void showListOfAdditionalItems(String billID){

        listOfItem_qty_Rec.setLayoutManager(new LinearLayoutManager(LaundryDetailViewActivity.this, RecyclerView.VERTICAL,false));
        listOfItemsModelsList_qty=new ArrayList<>();
        listOfItemsAdapter_qty=new ListOfItemsAdapter(listOfItemsModelsList_qty);
        listOfItem_qty_Rec.setAdapter(listOfItemsAdapter_qty);

        String cabangCode = Character.toString(billID.charAt(0))+ billID.charAt(1) + billID.charAt(2);
        String yearCode = Character.toString(billID.charAt(3))+ billID.charAt(4);
        String monthCode = Character.toString(billID.charAt(5))+ billID.charAt(6);
        String dayCode = Character.toString(billID.charAt(7))+ billID.charAt(8);
        String billCode = Character.toString(billID.charAt(9))+ billID.charAt(10) + billID.charAt(11) + billID.charAt(12);

        database.collection("Yearly Book")
                .document(cabangCode)
                .collection(yearCode)
                .document(monthCode)
                .collection(dayCode)
                .document(billCode)
                .collection("Additional Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int count=0;

                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                ListOfItemsModel listOfItemsModel=document.toObject(ListOfItemsModel.class);
                                listOfItemsModelsList_qty.add(listOfItemsModel);
                                listOfItemsAdapter_qty.notifyDataSetChanged();
                                count++;
                            }

                            if(count>0){
                                additional_item_label.setVisibility(View.VISIBLE);
                                listOfItem_qty_Rec.setVisibility(View.VISIBLE);
                            }
                        }else{
                            Toast.makeText(LaundryDetailViewActivity.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void alertDialoForUpdate(String billID, String status){

        AlertDialog.Builder builder= new AlertDialog.Builder(LaundryDetailViewActivity.this);
        builder.setTitle("Lanjutkan Update");
        builder.setCancelable(false);
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateStatus(billID, status);
                finish();
            }
        });


        builder.show();

    }

    public void updateStatus(String billID, String status){

        String updateStatusL;
        switch (status){
            case "Baru":
                updateStatusL="Proses";
                break;
            case "Proses":
                updateStatusL="Siap";
                break;
            case "Siap":
                updateStatusL="Sudah diambil";
                break;
            default:
                updateStatusL="Invalid status!";
        }

        Calendar calendar=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate=new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm a");

        String cabangCode = Character.toString(billID.charAt(0))+ billID.charAt(1) + billID.charAt(2);
        String yearCode = Character.toString(billID.charAt(3))+ billID.charAt(4);
        String monthCode = Character.toString(billID.charAt(5))+ billID.charAt(6);
        String dayCode = Character.toString(billID.charAt(7))+ billID.charAt(8);
        String billCode = Character.toString(billID.charAt(9))+ billID.charAt(10) + billID.charAt(11) + billID.charAt(12);

        database.collection("Yearly Book")
                .document(cabangCode)
                .collection(yearCode)
                .document(monthCode)
                .collection(dayCode)
                .document(billCode)
                .update("status", updateStatusL,
                        "status_date",currentDate.format(calendar.getTime()),
                        "status_time", currentTime.format(calendar.getTime()));

        if(!updateStatusL.equals("Sudah diambil")){
            database.collection("Cabang Information")
                    .document(cabangCode)
                    .collection("Active List")
                    .document(billID)
                    .update("status", updateStatusL,
                            "status_date",currentDate.format(calendar.getTime()),
                            "status_time", currentTime.format(calendar.getTime()));
        }else {
            database.collection("Cabang Information")
                    .document(cabangCode)
                    .collection("Active List")
                    .document(billID)
                    .collection("Additional Items")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                    String docID =document.getId();
                                    database.collection("Cabang Information")
                                            .document(cabangCode)
                                            .collection("Active List")
                                            .document(billID)
                                            .collection("Additional Items")
                                            .document(docID)
                                            .delete();
                                }

                            }else{
                                Toast.makeText(LaundryDetailViewActivity.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            database.collection("Cabang Information")
                    .document(cabangCode)
                    .collection("Active List")
                    .document(billID)
                    .delete();
        }

        Toast.makeText(this,"Status sudah diupdate!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }
}