package com.example.demoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.demoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserHomepage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homepage);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();

        popouMethod();


    }

    private void popouMethod() {
        //Form Dialog
        final Dialog dialog=new Dialog(this);

        //set contentview
        dialog.setContentView(R.layout.popup_form_layout);

        //set outside touch
        dialog.setCanceledOnTouchOutside(false);

        //set dialog height & width
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        //set transparent backgroun
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //set Animation
        dialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;

        //Initial dialog variable
        final EditText date=dialog.findViewById(R.id.tv_date);
        final EditText message=dialog.findViewById(R.id.et_message);
        Button submitBtn=dialog.findViewById(R.id.submit_btn);
        RadioGroup radioGroup=dialog.findViewById(R.id.radioGroup);
        final RadioButton male=dialog.findViewById(R.id.male_btn);
        final RadioButton female=dialog.findViewById(R.id.female_btn);

        //create code
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String datetime=simpleDateFormat.format(calendar.getTime());
        date.setText(datetime);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mdate=date.getText().toString().trim();
                String mmessage=message.getText().toString().trim();
                String gender="";

                if (male.isChecked()){

                    gender="Male";
                }

                if (female.isChecked()){
                    gender="Female";
                }


                if (mdate.isEmpty() || mmessage.isEmpty()){
                    ShowMessage("Please verify all field");
                }
                else {
                    submitFormtofirebase(mdate,mmessage,gender,dialog);
                }



            }
        });



        //show dialog
        dialog.show();
    }

    private void submitFormtofirebase(String mdate, String mmessage, String gender, final Dialog dialog) {

        HashMap<String,Object> map=new HashMap<>();
        map.put("userid",currentUser.getUid());
        map.put("username",currentUser.getDisplayName());
        map.put("useremail",currentUser.getEmail());
        map.put("date",mdate);
        map.put("gender",gender);
        map.put("message",mmessage);


        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("UserForm").push();

        String id=mRef.getKey();

        mRef.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    ShowMessage("Form Updated Succesfully..");
                    dialog.dismiss();
                }
                else {
                    ShowMessage("Error"+task.getException().getMessage());
                }
            }
        });



    }


    private void ShowMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
