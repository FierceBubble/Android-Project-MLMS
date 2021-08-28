package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mlms.mobilelaundrymanagementsystemadmin.adapters.ListOfItemsAdapter;
import com.mlms.mobilelaundrymanagementsystemadmin.models.LaundryModel;
import com.mlms.mobilelaundrymanagementsystemadmin.models.ListOfItemsModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LaundryDetailViewActivity extends AppCompatActivity {

    private static final String TAG ="LaundryDetailViewActivity";
    ImageButton back_btn;
    TextView billID, pic, customerName, DoS, DoLU, status, totalWeight, totalPrice, paymentMethod, additionalItem_label;
    RecyclerView listOfItems_Rec, listOfItems_qty_Rec;
    Button update_btn;

    List<ListOfItemsModel> listOfItemsModelsList;
    ListOfItemsAdapter listOfItemsAdapter;

    List<ListOfItemsModel> listOfItemsModelsList_qty;
    ListOfItemsAdapter listOfItemsAdapter_qty;

    LaundryModel laundryModel = null;

    FirebaseAuth auth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_detail_view);

        auth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();

        billID=findViewById(R.id.billID_detail_label);
        pic=findViewById(R.id.pic_label);
        customerName=findViewById(R.id.customerName_label);
        DoS=findViewById(R.id.DoS_label);
        DoLU=findViewById(R.id.DoLU_label);
        status=findViewById(R.id.status_label);
        totalPrice=findViewById(R.id.totalPrice_label);
        totalWeight=findViewById(R.id.totalWeight_label);
        paymentMethod=findViewById(R.id.paymentMethod);
        additionalItem_label=findViewById(R.id.additional_item_label);

        listOfItems_Rec=findViewById(R.id.listofItem_Rec);
        listOfItems_qty_Rec=findViewById(R.id.listofItem_qty_Rec);

        update_btn=findViewById(R.id.update_btn);

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
            String weight="kg"+weightFormatter.format(laundryModel.getTotalWeight());

            DecimalFormat priceFormatter = new DecimalFormat("#0,000.00");
            String price="Rp"+priceFormatter.format(laundryModel.getTotalPrice());

            billID.setText(laundryModel.getBillID());
            pic.setText(laundryModel.getEmployeeID());
            customerName.setText(laundryModel.getCustomerName());
            DoS.setText(SD);
            DoLU.setText(LUD);
            status.setText(Status);
            totalPrice.setText(price);
            totalWeight.setText(weight);
            paymentMethod.setText(laundryModel.getPaymentMethod());

            showListOfItems(laundryModel.getBillID());
            showLidtOfAdditionalItems(laundryModel.getBillID());
        }

        Log.i(TAG,"onCreate");
    }

    public void showListOfItems(String billID){
        listOfItems_Rec.setLayoutManager(new LinearLayoutManager(LaundryDetailViewActivity.this, RecyclerView.VERTICAL,false));
        listOfItemsModelsList=new ArrayList<>();
        listOfItemsAdapter=new ListOfItemsAdapter(listOfItemsModelsList);
        listOfItems_Rec.setAdapter(listOfItemsAdapter);

        database.collection("2021")
                .document("February")
                .collection("18")
                .document(billID)
                .collection("listofItems")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                ListOfItemsModel listOfItemsModel=document.toObject(ListOfItemsModel.class);
                                listOfItemsModelsList.add(listOfItemsModel);
                                listOfItemsAdapter.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(LaundryDetailViewActivity.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showLidtOfAdditionalItems(String billID){

        listOfItems_qty_Rec.setLayoutManager(new LinearLayoutManager(LaundryDetailViewActivity.this, RecyclerView.VERTICAL,false));
        listOfItemsModelsList_qty=new ArrayList<>();
        listOfItemsAdapter_qty=new ListOfItemsAdapter(listOfItemsModelsList_qty);
        listOfItems_qty_Rec.setAdapter(listOfItemsAdapter_qty);

        database.collection("2021")
                .document("February")
                .collection("18")
                .document(billID)
                .collection("additional_listofItems")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            additionalItem_label.setVisibility(View.VISIBLE);
                            listOfItems_qty_Rec.setVisibility(View.VISIBLE);

                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                ListOfItemsModel listOfItemsModel=document.toObject(ListOfItemsModel.class);
                                listOfItemsModelsList_qty.add(listOfItemsModel);
                                listOfItemsAdapter_qty.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(LaundryDetailViewActivity.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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