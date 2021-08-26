package com.mlms.mobilelaundrymanagementsystemadmin.ui.employee_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mlms.mobilelaundrymanagementsystemadmin.NewEmployeeActivity;
import com.mlms.mobilelaundrymanagementsystemadmin.R;
import com.mlms.mobilelaundrymanagementsystemadmin.databinding.FragmentEmployeelistBinding;

public class EmployeeListFragment extends Fragment {

    private static final String TAG ="EmployeeListFragment";
    private FragmentEmployeelistBinding binding;
    RecyclerView listOfEmployee_Rec;
    SwipeRefreshLayout pullToRefresh;
    Button addNewEmployee;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEmployeelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listOfEmployee_Rec=root.findViewById(R.id.listOfEmployee);

        pullToRefresh=root.findViewById(R.id.RefreshEmployeePage);
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(false);
            Log.i(TAG,"onRefresh");
        });

        addNewEmployee=root.findViewById(R.id.NewEmployee_btn);
        addNewEmployee.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), NewEmployeeActivity.class);
            startActivity(intent);
        });

        Log.i(TAG,"onCreate");
        return root;
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