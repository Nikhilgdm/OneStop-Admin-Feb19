package com.example.onestop_admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    EditText email,password;
    Button login;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailS =   email.getText().toString();
                String passwordS = password.getText().toString();

                if(TextUtils.isEmpty(emailS)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwordS)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields", Toast.LENGTH_SHORT).show();
                }
                if(passwordS.length()<6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }
                firebaseAuth.signInWithEmailAndPassword(emailS,passwordS).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            SessionManager sessionManager=new SessionManager(getApplicationContext());
                            sessionManager.createLoginSession(email.getText().toString());
                            startActivity(new Intent(LoginPage.this, MainActivity.class));
                        }
                        else {
                            Toast.makeText(LoginPage.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}
