package com.mlms.mobilelaundrymanagementsystemadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
// import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlms.mobilelaundrymanagementsystemadmin.databinding.ActivityMainBinding;
import com.mlms.mobilelaundrymanagementsystemadmin.databinding.FragmentHomeBinding;
import com.mlms.mobilelaundrymanagementsystemadmin.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {



    private static final String TAG ="MainActivity";
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firestore;

    String name,role,cabang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        firestore=FirebaseFirestore.getInstance();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_employeelist)
                .build();

        NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;
        NavController navController=navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Intent intent=getIntent();
        name= intent.getStringExtra("name");
        role=intent.getStringExtra("role");
        cabang=intent.getStringExtra("cabang");

        Toast.makeText(MainActivity.this,name +" "+role+" "+cabang, Toast.LENGTH_SHORT).show();

        Log.i(TAG,"onCreate");
    }

    @Override
    public void onStart() {
        super. onStart();
        Log.i(TAG,"onStart");

        // Check if the user logged in or have not logged out
        FirebaseUser user=auth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
        auth.signOut();
    }

}