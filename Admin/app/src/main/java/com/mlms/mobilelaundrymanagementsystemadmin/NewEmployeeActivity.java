package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

public class NewEmployeeActivity extends AppCompatActivity {

    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_employee);

        back_btn=findViewById(R.id.back_btn);

        back_btn.setOnClickListener(v -> {
            finish();
            Log.d("Back button","Back button clicked!, returned to mainActivity");
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("NewEmployeeActivity","Activity started!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("NewEmployeeActivity","Activity stopped!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("NewEmployeeActivity","Activity paused!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NewEmployeeActivity","Activity resumed!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("NewEmployeeActivity","Activity destroyed!");
    }
}