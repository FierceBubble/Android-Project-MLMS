package com.mlms.mobilelaundrymanagementsystemadmin.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mlms.mobilelaundrymanagementsystemadmin.LoginActivity;
import com.mlms.mobilelaundrymanagementsystemadmin.MainActivity;
import com.mlms.mobilelaundrymanagementsystemadmin.OpenNewBillActivity;
import com.mlms.mobilelaundrymanagementsystemadmin.R;
import com.mlms.mobilelaundrymanagementsystemadmin.adapters.ActiveListAdapter_Admin;
import com.mlms.mobilelaundrymanagementsystemadmin.databinding.FragmentHomeBinding;
import com.mlms.mobilelaundrymanagementsystemadmin.models.CabangListModel;
import com.mlms.mobilelaundrymanagementsystemadmin.models.LaundryModel;
import com.mlms.mobilelaundrymanagementsystemadmin.models.adminNemployeeModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements ActiveListAdapter_Admin.OnShowInfoListener{

    private static final String TAG ="HomeFragment";
    private FragmentHomeBinding binding;
    SwipeRefreshLayout pullToRefresh;

    FirebaseAuth auth;
    FirebaseFirestore fireStore;
    FirebaseDatabase database;

    RecyclerView recView;
    List<LaundryModel> laundryModelList;
    ActiveListAdapter_Admin activeListAdapter_admin;
    String name,role,cabang;
    Calendar calendar;

    Button OpenNewBill_btn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fireStore=FirebaseFirestore.getInstance();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        calendar=Calendar.getInstance();

        recView=root.findViewById(R.id.listOfActive);

        defineRole();

        OpenNewBill_btn=root.findViewById(R.id.open_bill);
        OpenNewBill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), OpenNewBillActivity.class);
                startActivity(intent);
            }
        });

        pullToRefresh=root.findViewById(R.id.RefreshHomePage);
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(false);
            Log.i(TAG,"onRefresh");
            //listAllActiveStatus();
            defineRole();
        });



        Log.i(TAG,"onCreate");
        return root;
    }

    public void defineRole(){

        database.getReference().child("loginAdminNEmployee").child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        adminNemployeeModel adminNemployeeModel=snapshot.getValue(adminNemployeeModel.class);
                        assert adminNemployeeModel != null;

                        role=adminNemployeeModel.getRole();
                        cabang=adminNemployeeModel.getCabang();
                        name=adminNemployeeModel.getName();

                        if(role.equals("Admin")){
                            listAllActiveStatus_admin();
                        }else if(role.equals("Employee")){
                            listAllActiveStatus_employee(cabang);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void listAllActiveStatus_employee(String cabang){
        // Initiate RecycleView
        recView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false ));
        laundryModelList=new ArrayList<>();
        activeListAdapter_admin= new ActiveListAdapter_Admin(getActivity(),laundryModelList,this );
        recView.setAdapter(activeListAdapter_admin);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentYear=new SimpleDateFormat("yy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentMonth=new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDay=new SimpleDateFormat("dd");

        fireStore.collection("Cabang Information")
                .document(cabang)
                .collection("Active List")
                .whereNotEqualTo("status","Sudah diambil")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        LaundryModel laundryModel=document.toObject(LaundryModel.class);
                        laundryModelList.add(laundryModel);
                        activeListAdapter_admin.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void listAllActiveStatus_admin(){
        List<CabangListModel> cabangListModelList=new ArrayList<>();

        fireStore.collection("Cabang Information")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                CabangListModel cabangListModel=document.toObject(CabangListModel.class);
                                cabangListModelList.add(cabangListModel);
                            }
                            for(CabangListModel list: cabangListModelList){
                                String cabangL=list.getCabangName();
                                listAllActiveStatus(cabangL);
                            }
                        }else{
                            Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void listAllActiveStatus(String cabangName){

        // Initiate RecycleView
        recView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false ));
        laundryModelList=new ArrayList<>();
        activeListAdapter_admin= new ActiveListAdapter_Admin(getActivity(),laundryModelList,this );
        recView.setAdapter(activeListAdapter_admin);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentYear=new SimpleDateFormat("yy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentMonth=new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDay=new SimpleDateFormat("dd");

        fireStore.collection("Cabang Information")
                .document(cabangName)
                .collection("Active List")
                .whereNotEqualTo("status","Sudah diambil")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        LaundryModel laundryModel=document.toObject(LaundryModel.class);

                        laundryModelList.add(laundryModel);
                        activeListAdapter_admin.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void OnShowInfoClick(int position){ }

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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.i(TAG,"onDestroy");
    }
}