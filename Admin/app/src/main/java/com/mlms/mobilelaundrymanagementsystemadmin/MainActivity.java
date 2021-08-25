package com.mlms.mobilelaundrymanagementsystemadmin;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
// import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.mlms.mobilelaundrymanagementsystemadmin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        authenticate();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        // Old version of fragment NavControl
        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;
        NavController navController=navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Log.d("MainActivity","Activity created!");
    }

    public void authenticate(){
        String userName="admin@adminemail.com";
        String Password="admin123";
        //Sign In User
        auth.signInWithEmailAndPassword(userName,Password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        Log.d("Authenticate","Success");
                    }else{
                        Log.d("Authenticate","Failed");
                    }

                });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MainActivity","Activity started!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("MainActivity","Activity stopped!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","Activity paused!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","Activity resumed!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity","Activity destroyed!");
    }

}