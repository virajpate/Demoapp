package com.example.demoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.demoapp.Adapter.UserdDataAdapter;
import com.example.demoapp.R;
import com.example.demoapp.model.UserForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHomepage extends AppCompatActivity {

    private RecyclerView recyclerViewUser;
    private UserdDataAdapter adapter;
    private List<UserForm> userFormsList;

    private FirebaseAuth mAuth;
    private FirebaseUser currentuser;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("UserForm");

        Initialization();

        setRecyclerview();
    }


    private void Initialization() {

        recyclerViewUser=findViewById(R.id.recyclerview_user);
        recyclerViewUser.setHasFixedSize(true);


    }

    private void setRecyclerview() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userFormsList=new ArrayList<>();

                for (DataSnapshot postsnap :snapshot.getChildren()){

                    UserForm userForm=postsnap.getValue(UserForm.class);

                    userFormsList.add(userForm);
                }
                adapter=new UserdDataAdapter(getApplicationContext(),userFormsList);
                recyclerViewUser.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
