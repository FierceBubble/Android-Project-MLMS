package com.mlms.mobilelaundrymanagementsystemadmin.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mlms.mobilelaundrymanagementsystemadmin.R;
import com.mlms.mobilelaundrymanagementsystemadmin.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    SwipeRefreshLayout pullToRefresh;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pullToRefresh=root.findViewById(R.id.RefreshHomePage);
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(false);
            Log.d("HomeFragment","Fragment refreshed!");
        });

        Log.d("HomeFragment","Fragment created!");

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("HomeFragment","Fragment started!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("HomeFragment","Fragment stopped!");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("HomeFragment","Fragment paused!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeFragment","Fragment resumed!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.d("HomeFragment","Fragment destroyed!");
    }
}