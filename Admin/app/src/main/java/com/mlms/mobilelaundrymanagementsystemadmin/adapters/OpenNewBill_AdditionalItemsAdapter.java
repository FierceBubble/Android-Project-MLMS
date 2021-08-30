package com.mlms.mobilelaundrymanagementsystemadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mlms.mobilelaundrymanagementsystemadmin.R;
import com.mlms.mobilelaundrymanagementsystemadmin.models.ListOfItemsModel;

import java.util.List;

public class OpenNewBill_AdditionalItemsAdapter extends RecyclerView.Adapter<OpenNewBill_AdditionalItemsAdapter.ViewHolder> {

    List<ListOfItemsModel> listOfItemsModelList;

    public OpenNewBill_AdditionalItemsAdapter(){ }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_opennewbill_additionalitems,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public List<ListOfItemsModel> getAdditionalItems(){
        return listOfItemsModelList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView item_price;
        EditText item_qty;
        Spinner item_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name=itemView.findViewById(R.id.spinner);
            item_qty=itemView.findViewById(R.id.additional_qty);
            item_price=itemView.findViewById(R.id.additional_price);


        }
    }


}
