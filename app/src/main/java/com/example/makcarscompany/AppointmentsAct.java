package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.makcarscompany.AppClassess.Appointments;
import com.example.makcarscompany.AppClassess.Cars;
import com.example.makcarscompany.AppClassess.Payments;
import com.example.makcarscompany.AppClassess.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppointmentsAct extends AppCompatActivity {
    FirebaseAuth fauth;
    SharedPreferences spref;
    RecyclerView myrv;
    FirebaseDatabase fdb;
    DatabaseReference dbref;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Appointments,ViewHolderr> adapter;
    FirebaseRecyclerOptions<Appointments> options;
    String un;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        un=spref.getString("usernamee","");
        myrv=findViewById(R.id.myrec);
        fdb=FirebaseDatabase.getInstance();
        fauth=FirebaseAuth.getInstance();
        layoutManager = new LinearLayoutManager(this);
        if(spref.getInt("log",0)==1) {
            dbref = fdb.getReference().child("Appointments");
            options = new FirebaseRecyclerOptions.Builder<Appointments>().setQuery(dbref, Appointments.class).build();

            adapter = new FirebaseRecyclerAdapter<Appointments, ViewHolderr>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ViewHolderr holder, final int position, @NonNull final Appointments model) {

                    holder.Description.setText(model.tostring());

                }

                @NonNull
                @Override
                public ViewHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
                    return new ViewHolderr(view);
                }
            };
            myrv.setLayoutManager(layoutManager);
            adapter.startListening();
            myrv.setAdapter(adapter);
        }

        else if(spref.getInt("log",0)==2) {

            if (!(un.equals(""))) {
                dbref = FirebaseDatabase.getInstance().getReference("Appointments");


                dbref = fdb.getReference().child("Appointments");
                options = new FirebaseRecyclerOptions.Builder<Appointments>().setQuery(dbref, Appointments.class).build();
                adapter = new FirebaseRecyclerAdapter<Appointments, ViewHolderr>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ViewHolderr holder, final int position, @NonNull final Appointments model) {

                        if (model.getUsername().equals(un)) {
                            holder.Description.setText(model.tostring());
                            holder.C.setVisibility(View.VISIBLE);
                            }
                        else
                            {holder.C.setVisibility(View.GONE);
                        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) holder.C.getLayoutParams();
                        params.height = 0;
                        holder.C.setLayoutParams(params);}

                    }

                    @NonNull
                    @Override
                    public ViewHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);

                        return new ViewHolderr(view);
                    }
                };

                myrv.setLayoutManager(layoutManager);
                adapter.startListening();
                myrv.setAdapter(adapter);
            }
        }
    }

    public void Logout(View view){
        fauth.signOut();
        Intent in= new Intent(this, Home.class);
        spref.edit().clear().apply();
        startActivity(in);
        finish();

    }

    public void home(View v){
        Intent in;
        String L=String.valueOf(spref.getInt("log",0));

        switch (L){

            case "1":
                in = new Intent(this, AdminHome.class);
                break;
            case "2":
                in = new Intent(this, UserHome.class);
                break;
            default:
                in = new Intent(this, Home.class);
                break;
        }
        startActivity(in);
        finish();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }

}
