package com.mlms.mobilelaundrymanagementsystemadmin.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mlms.mobilelaundrymanagementsystemadmin.LaundryDetailViewActivity;
import com.mlms.mobilelaundrymanagementsystemadmin.R;
import com.mlms.mobilelaundrymanagementsystemadmin.models.LaundryModel;

import java.util.List;

public class ActiveListAdapter_Admin extends RecyclerView.Adapter<ActiveListAdapter_Admin.ViewHolder> {

    private final List<LaundryModel> laundryModelList;
    private final OnShowInfoListener mOnShowInfoListener;
    private final Context context;

    public ActiveListAdapter_Admin(Context context, List<LaundryModel> laundryModelList, OnShowInfoListener OnShowInfoListener){
        this.context=context;
        this.laundryModelList=laundryModelList;
        this.mOnShowInfoListener=OnShowInfoListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_activelaundrylist,parent,false);
        return new ViewHolder(view, mOnShowInfoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String LUD="Last updated:\n"+laundryModelList.get(position).getStatus_date()+"\n"+laundryModelList.get(position).getStatus_time();
        String Status="Status: "+laundryModelList.get(position).getStatus();
        // Display list in CardView
        holder.billID.setText(laundryModelList.get(position).getBillID());
        holder.last_update_date.setText(LUD);
        holder.status.setText(Status);

        holder.info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, LaundryDetailViewActivity.class);
                intent.putExtra("position",laundryModelList.get(position));
                context.startActivity(intent);
                //Toast.makeText(context,"Detail view of billID: "+laundryModelList.get(position).getBillID(),Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("ActiveListAdapter","RecycleView created successfully! Position: "+position);
    }

    @Override
    public int getItemCount() {
        return laundryModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView billID, last_update_date, status;
        Button info_btn;

        OnShowInfoListener onShowInfoListener;

        public ViewHolder(@NonNull View itemView, OnShowInfoListener OnShowInfoListener) {
            super(itemView);

            this.onShowInfoListener=OnShowInfoListener;

            billID=itemView.findViewById(R.id.billID_label);
            last_update_date=itemView.findViewById(R.id.last_update_date_label);
            status=itemView.findViewById(R.id.status_label);

            info_btn=itemView.findViewById(R.id.info_btn);
            info_btn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onShowInfoListener.OnShowInfoClick(getAdapterPosition());
        }
    }

    public interface OnShowInfoListener{
        void OnShowInfoClick(int position);
    }

}
