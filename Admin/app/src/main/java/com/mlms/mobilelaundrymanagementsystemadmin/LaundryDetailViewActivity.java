package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mlms.mobilelaundrymanagementsystemadmin.models.LaundryModel;

import java.text.DecimalFormat;

public class LaundryDetailViewActivity extends AppCompatActivity {

    private static final String TAG ="LaundryDetailViewActivity";
    ImageButton back_btn;
    TextView billID, pic, customerName, DoS, DoLU, status, totalWeight, totalPrice;
    RecyclerView listOfItems, listOfItems_qty;
    Button update_btn;

    LaundryModel laundryModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_detail_view);

        billID=findViewById(R.id.billID_detail_label);
        pic=findViewById(R.id.pic_label);
        customerName=findViewById(R.id.customerName_label);
        DoS=findViewById(R.id.DoS_label);
        DoLU=findViewById(R.id.DoLU_label);
        status=findViewById(R.id.status_label);
        totalPrice=findViewById(R.id.totalPrice_label);
        totalWeight=findViewById(R.id.totalWeight_label);

        listOfItems=findViewById(R.id.listofItem_Rec);
        listOfItems_qty=findViewById(R.id.listofItem_qty_Rec);

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
        }
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