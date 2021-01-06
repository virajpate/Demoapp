package com.example.demoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.R;

public class RecvFormActivity extends AppCompatActivity {

    private TextView tv_date, tv_gender, tv_message, tv_username;
    private Button allow_Btn, deny_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recv_form);

        Initialization();

        //get Post Data
        String date = getIntent().getExtras().getString("Date");
        tv_date.setText(date);

        String gender = getIntent().getExtras().getString("Gender");
        tv_gender.setText(gender);

        String message = getIntent().getExtras().getString("Message");
        tv_message.setText(message);

        String userId = getIntent().getExtras().getString("userId");

        String username = getIntent().getExtras().getString("username");
        tv_username.setText(username);

        //Btn Click Listner
        allow_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMessage("Allow");
            }
        });

        deny_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMessage("Deny");
            }
        });

    }

    private void Initialization() {
        tv_date = findViewById(R.id.tv_date);
        tv_gender = findViewById(R.id.tv_gender);
        tv_message = findViewById(R.id.tv_message);
        tv_username = findViewById(R.id.tv_usename);
        allow_Btn = findViewById(R.id.btn_Apv);
        deny_Btn = findViewById(R.id.btn_notApv);
    }

    private void ShowMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
