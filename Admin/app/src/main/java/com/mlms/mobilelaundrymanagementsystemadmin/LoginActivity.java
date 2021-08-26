package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlms.mobilelaundrymanagementsystemadmin.models.adminNemployeeModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG ="LoginActivity";
    FirebaseAuth auth;
    FirebaseDatabase database;

    EditText email_input;
    EditText password_input;
    Button login_btn;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        setContentView(R.layout.activity_login);
        email_input=findViewById(R.id.email_input);
        password_input=findViewById(R.id.password_input);
        login_btn=findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields(email_input.getText().toString(), password_input.getText().toString());
            }
        });

    }

    public void checkFields(String email, String password){
        if(email.equals("")||password.equals("")){
            Toast.makeText(LoginActivity.this, "Please fill in empty fields",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<6){
            Toast.makeText(LoginActivity.this, "Password should be more than 6 characters",Toast.LENGTH_SHORT).show();
            return;
        }
        authenticate(email, password);
    }

    public void authenticate(String email, String password){

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("Authentication", "Success");

                }else{
                    Log.w("Authentication", "Failed",task.getException());
                    Toast.makeText(LoginActivity.this,"Account does not exist!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirebaseUser user=auth.getCurrentUser();
        if (user!=null) {
            Log.i("Login", "Success");
            // User is signed in
            defineRole(user.getUid());
        } else {
            // No user is signed in
            Log.i("Login", "Failed");
        }
    }



    public void defineRole(String accountID){

        database.getReference().child("loginAdminNEmployee").child(accountID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        adminNemployeeModel adminNemployeeModel=snapshot.getValue(adminNemployeeModel.class);
                        assert adminNemployeeModel != null;

                        role=adminNemployeeModel.getRole();
                        Log.i("Role", role);

                        if(role.equals("admin")){
                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        }else if(role.equals("employee")){
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        email_input.setText("");
        password_input.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

    /**
    private void reload(FirebaseUser currentUser){
        FirebaseUser User=auth.getCurrentUser();
        if(User!=null){
            reload(User);
        }
        defineRole(currentUser.getUid());
    }
     **/

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
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
        auth.signOut();
    }

}