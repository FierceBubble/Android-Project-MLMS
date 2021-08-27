package com.mlms.mobilelaundrymanagementsystemadmin.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mlms.mobilelaundrymanagementsystemadmin.R;
import com.mlms.mobilelaundrymanagementsystemadmin.adapters.ActiveListAdapter_Admin;
import com.mlms.mobilelaundrymanagementsystemadmin.databinding.FragmentHomeBinding;
import com.mlms.mobilelaundrymanagementsystemadmin.models.LaundryModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements ActiveListAdapter_Admin.OnShowInfoListener{

    private static final String TAG ="HomeFragment";
    private FragmentHomeBinding binding;
    SwipeRefreshLayout pullToRefresh;

    FirebaseAuth auth;
    FirebaseFirestore database;
    RecyclerView recView;
    List<LaundryModel> laundryModelList;
    ActiveListAdapter_Admin activeListAdapter_admin;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        database=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        recView=root.findViewById(R.id.listOfActive);
        // List all active status for Admin
        listAllActiveStatus();

        pullToRefresh=root.findViewById(R.id.RefreshHomePage);
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(false);
            Log.i(TAG,"onRefresh");
        });



        Log.i(TAG,"onCreate");
        return root;
    }

    public void listAllActiveStatus(){
        // Initiate RecycleView
        recView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false ));
        laundryModelList=new ArrayList<>();
        activeListAdapter_admin= new ActiveListAdapter_Admin(getActivity(),laundryModelList,this );
        recView.setAdapter(activeListAdapter_admin);

        Calendar calendar=Calendar.getInstance();


        database.collection("2021").document("February")
                .collection("18")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
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
    public void OnShowInfoClick(int position){
        Toast.makeText(getActivity(),String.valueOf(position)+" is clicked",Toast.LENGTH_SHORT).show();
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.i(TAG,"onDestroy");
    }
}