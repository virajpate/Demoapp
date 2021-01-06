package com.example.demoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Register extends AppCompatActivity implements LocationListener {

    private EditText et_Name,et_add,et_Phone,et_email,et_password;
    private Button Btn_reg,Btn_log;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private DatabaseReference reference;

    //location
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //firebase initialization
        mAuth = FirebaseAuth.getInstance();
        CurrentUser=mAuth.getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference();

        Initialization();

        //get address
        getAddressmethod();
        checkLocationEnableorNot();
        getLocation();

        //click on register btn
        Btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationMethod();
            }
        });

        //click on signin btn
        Btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


    }


    private void Initialization() {

        et_Name=findViewById(R.id.et_name);
        et_add=findViewById(R.id.et_add);
        et_Phone=findViewById(R.id.et_phone);
        et_email=findViewById(R.id.et_reg_email);
        et_password=findViewById(R.id.et_password);
        Btn_reg=findViewById(R.id.btn_Reg);
        Btn_log=findViewById(R.id.sigin_btn);
        progressBar=findViewById(R.id.progressBar3);
    }

    private void getLocation() {
        try {
            locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,500,5,(LocationListener)this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void checkLocationEnableorNot() {
        LocationManager lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsdenable=false;
        boolean networkenable= false;

        try {
            gpsdenable=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            networkenable=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!gpsdenable && !networkenable){
            new AlertDialog.Builder(Register.this)
                            .setTitle("Enable Gps Service")
                             .setCancelable(false)
                              .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                  }
                              }).setNegativeButton("Cancel",null).show();

        }
    }

    private void getAddressmethod() {
       if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
           &&ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
       {
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                       Manifest.permission.ACCESS_COARSE_LOCATION},100);
       }
    }



    private void RegistrationMethod() {

        Btn_reg.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String name= et_Name.getText().toString().trim();
        String add=et_add.getText().toString().trim();
        String phone=et_Phone.getText().toString().trim();
        String email=et_email.getText().toString().trim();
        String pass=et_password.getText().toString().trim();

        String MobilePattern = "[0-9]{10}";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (name.isEmpty() || add.isEmpty()|| phone.isEmpty() || email.isEmpty() || pass.isEmpty()){
            Showmessage("Please verify All Fields...!!!");
            progressBar.setVisibility(View.INVISIBLE);
            Btn_reg.setVisibility(View.VISIBLE);
        }
        else if (!email.matches(emailPattern)){
            Showmessage("Please Enter Valid Email Address");
            progressBar.setVisibility(View.INVISIBLE);
            Btn_reg.setVisibility(View.VISIBLE);
        }
        else if (!phone.matches(MobilePattern)){
            Showmessage("Please enter valid 10 digit phone number");
            progressBar.setVisibility(View.INVISIBLE);
            Btn_reg.setVisibility(View.VISIBLE);
        }
        else {
            createAccount(name,email,pass);
        }

    }

    private void createAccount(String name, final String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


              if (task.isSuccessful()){
                  mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          SetUsername();
                          updateUi(CurrentUser,email);
                      }
                  });
              }
              else {

                  Showmessage("Account Created Failed.."+ task.getException().getMessage());
                  progressBar.setVisibility(View.INVISIBLE);
                  Btn_reg.setVisibility(View.VISIBLE);
              }


            }
        });

    }

    private void updateUi(FirebaseUser currentUser, String email) {


        HashMap<String,Object> map=new HashMap<>();
        map.put("User Name",et_Name.getText().toString());
        map.put("Address",et_add.getText().toString());
        map.put("Phone",et_Phone.getText().toString());
        map.put("Email",et_email.getText().toString());
        map.put("Password",et_password.getText().toString());

        reference.child("UserInfo").child(CurrentUser.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

               if (task.isSuccessful()){

                   Showmessage("Register Complete");
                   progressBar.setVisibility(View.INVISIBLE);
                   Btn_reg.setVisibility(View.VISIBLE);
                   Showmessage("Plese Verify Email...");
                   mAuth.signOut();
                   gotoSiginAct();
                   finish();
               }
               else{

                   Showmessage("Error"+task.getException().getMessage());
                   progressBar.setVisibility(View.INVISIBLE);
                   Btn_reg.setVisibility(View.VISIBLE);
               }
            }
        });

    }

    private void SetUsername() {
        String username=et_Name.getText().toString().trim();
        UserProfileChangeRequest profileUpdate=new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();

        CurrentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Showmessage("");
                }
                else {
                    Showmessage("UserName Fail");
                }
            }

        });


    }

    private void gotoSiginAct() {

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    private void Showmessage(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }















    //location

    @Override
    public void onLocationChanged(Location location) {


        try {
            Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            String address=addresses.get(0).getAddressLine(0);
            String area=addresses.get(0).getLocality();
            String city=addresses.get(0).getAdminArea();
            String country=addresses.get(0).getCountryName();
            String postalcode=addresses.get(0).getPostalCode();

            String fullAddress= address+", "+area+", "+city+", "+country+", "+postalcode;

            et_add.setText(fullAddress);

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
