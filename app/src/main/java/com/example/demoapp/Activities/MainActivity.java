package com.example.demoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.demoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText login_email,login_pass;
    private Button user_btn,admin_btn,signup_btn;
    FirebaseAuth mAuth;
    ProgressBar mprogressBar;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        Initialization();

        //user btn
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SigninAccount();
            }
        });

        //admin btn
        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(login_email.getText().toString().equals("admin@gmail.com") && login_pass.getText().toString().equals("password")){
                  startActivity(new Intent(getApplicationContext(),AdminHomepage.class));
              }
              else
              {
                  ShowMessage("Please Field valid Email address & Password!!");
              }
            }
        });

      //signup btn
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));

            }
        });

    }


    private void Initialization() {
        login_email=findViewById(R.id.et_email);
        login_pass=findViewById(R.id.et_pass);
        user_btn=findViewById(R.id.user_Login_Btn);
        admin_btn=findViewById(R.id.admin_Login_Btn);
        signup_btn=findViewById(R.id.signUp_btn);
        mprogressBar=findViewById(R.id.signin_progress);
    }

    private void SigninAccount() {
        user_btn.setVisibility(View.INVISIBLE);
        mprogressBar.setVisibility(View.VISIBLE);

        final String email=login_email.getText().toString();
        final String password=login_pass.getText().toString();

        if (email.isEmpty() || password.isEmpty()){
            ShowMessage("Please Verify All Field");
            mprogressBar.setVisibility(View.INVISIBLE);
            user_btn.setVisibility(View.VISIBLE);
        }
        else
        {
            SigninSuccessful(email,password);
        }
    }

    private void SigninSuccessful(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

               if (task.isSuccessful()){
                   if (mAuth.getCurrentUser().isEmailVerified()){
                       assert currentUser!=null;
                       mprogressBar.setVisibility(View.INVISIBLE);
                       user_btn.setVisibility(View.VISIBLE);
                       ShowMessage("Signin Successful..!!!");
                       gotoHomeActivity();
                       finish();
                   }
                   else
                   {
                       ShowMessage("Plese Verify Email...");
                       mprogressBar.setVisibility(View.INVISIBLE);
                       user_btn.setVisibility(View.VISIBLE);
                       mAuth.signOut();

                   }

               }
               else
               {
                   ShowMessage("Signin Failed"+task.getException().getMessage());
                   mprogressBar.setVisibility(View.INVISIBLE);
                   user_btn.setVisibility(View.VISIBLE);
               }
            }
        });
    }

    private void gotoHomeActivity() {
        startActivity(new Intent(getApplicationContext(),UserHomepage.class));
    }

    private void ShowMessage(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
