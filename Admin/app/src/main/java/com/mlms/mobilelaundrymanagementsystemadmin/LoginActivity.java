package com.mlms.mobilelaundrymanagementsystemadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        setContentView(R.layout.activity_login);
        email_input=findViewById(R.id.email_input);
        password_input=findViewById(R.id.password_input);
        login_btn=findViewById(R.id.login_btn);

        login_btn.setOnClickListener(v -> checkFields());

    }

    public void checkFields(){
        String email=email_input.getText().toString();
        String password=password_input.getText().toString();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Please fill in empty fields",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email)){
            email_input.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            password_input.requestFocus();
        }else if(password.length()<6){
            Toast.makeText(LoginActivity.this, "Password should be more than 6 characters",Toast.LENGTH_SHORT).show();
        }else{
            // Continue to Authentication
            authenticate(email, password);
        }

    }

    public void authenticate(String email, String password){

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.w("Authentication", "Success");
                        email_input.setText("");
                        password_input.setText("");
                        defineRole(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                    }else{
                        Log.w("Authentication", "Failed",task.getException());
                        Toast.makeText(LoginActivity.this,"Login failed: "+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        email_input.requestFocus();
                        password_input.requestFocus();
                    }
                });

    }

    public void defineRole(String accountID){

        database.getReference().child("loginAdminNEmployee").child(accountID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        adminNemployeeModel adminNemployeeModel=snapshot.getValue(adminNemployeeModel.class);
                        assert adminNemployeeModel != null;

                        String role=adminNemployeeModel.getRole();
                        String cabang=adminNemployeeModel.getCabang();
                        Log.w("Role", role);
                        Log.w("Cabang", cabang);

                        if(role.equals("admin")){

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        }else if(role.equals("employee")){

                            Toast.makeText(LoginActivity.this,"Employee logged in!",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }

}