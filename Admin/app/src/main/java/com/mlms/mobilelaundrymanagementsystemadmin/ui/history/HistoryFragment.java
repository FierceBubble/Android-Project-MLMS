package com.mlms.mobilelaundrymanagementsystemadmin.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mlms.mobilelaundrymanagementsystemadmin.R;
import com.mlms.mobilelaundrymanagementsystemadmin.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    SwipeRefreshLayout pullToRefresh;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pullToRefresh=root.findViewById(R.id.RefreshHistoryPage);
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(false);
            Log.d("HistoryFragment","Fragment refreshed!");
        });

        Log.d("HistoryFragment","Fragment created!");

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("HistoryFragment","Fragment started!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("HistoryFragment","Fragment stopped!");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("HistoryFragment","Fragment paused!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HistoryFragment","Fragment resumed!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.d("HistoryFragment","Fragment destroyed!");
    }
}