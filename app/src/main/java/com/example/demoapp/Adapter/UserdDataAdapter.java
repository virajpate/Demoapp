package com.example.demoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.Activities.RecvFormActivity;
import com.example.demoapp.R;
import com.example.demoapp.model.UserForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserdDataAdapter extends RecyclerView.Adapter<UserdDataAdapter.MyViewHolder> {

    Context context;
    List<UserForm> userFormsList;
    FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();

    public UserdDataAdapter(Context context, List<UserForm> userFormsList) {
        this.context = context;
        this.userFormsList = userFormsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(context).inflate(R.layout.userdata_container_layout,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date.setText(userFormsList.get(position).getDate());
        holder.useremail.setText(userFormsList.get(position).getUseremail());
        holder.username.setText(userFormsList.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        return userFormsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username,useremail,date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.user_name);
            useremail=itemView.findViewById(R.id.user_email);
            date=itemView.findViewById(R.id.tv_date);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, RecvFormActivity.class);
                    int position=getAdapterPosition();

                    intent.putExtra("Date",userFormsList.get(position).getDate());
                    intent.putExtra("Gender",userFormsList.get(position).getGender());
                    intent.putExtra("Message",userFormsList.get(position).getMessage());
                    intent.putExtra("userId",userFormsList.get(position).getUserid());
                    intent.putExtra("username",userFormsList.get(position).getUsername());

                    context.startActivity(intent);
                }
            });
        }
    }
}
