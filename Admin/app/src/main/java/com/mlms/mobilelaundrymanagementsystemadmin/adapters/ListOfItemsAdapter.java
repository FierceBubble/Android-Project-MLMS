package com.mlms.mobilelaundrymanagementsystemadmin.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mlms.mobilelaundrymanagementsystemadmin.R;
import com.mlms.mobilelaundrymanagementsystemadmin.models.ListOfItemsModel;

import java.util.List;

public class ListOfItemsAdapter extends RecyclerView.Adapter<ListOfItemsAdapter.ViewHolder> {

    List<ListOfItemsModel> listOfItemsModels;

    public ListOfItemsAdapter (List<ListOfItemsModel> listOfItemsModels){
        this.listOfItemsModels=listOfItemsModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_listofitems,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_name.setText(listOfItemsModels.get(position).getItem_name());
        holder.item_qty.setText("x"+ listOfItemsModels.get(position).getItem_qty());
    }

    @Override
    public int getItemCount() {
        return listOfItemsModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_name, item_qty;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            item_name=itemView.findViewById(R.id.item_name);
            item_qty=itemView.findViewById(R.id.item_qty);
        }
    }
}
