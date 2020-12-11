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

import com.example.makcarscompany.AppClassess.Appointments;
import com.example.makcarscompany.AppClassess.Payments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentsAct extends AppCompatActivity {
    SharedPreferences spref;
    FirebaseAuth fauth;
    RecyclerView myrv;
    FirebaseDatabase fdb;
    DatabaseReference dbref;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Payments,ViewHolderr> adapter;
    FirebaseRecyclerOptions<Payments> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        fauth=FirebaseAuth.getInstance();
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        spref.edit().putInt("log",1).apply();
        myrv=findViewById(R.id.myrec);
        fdb=FirebaseDatabase.getInstance();
        layoutManager = new LinearLayoutManager(this);
        dbref = fdb.getReference().child("Payments");
        options = new FirebaseRecyclerOptions.Builder<Payments>().setQuery(dbref, Payments.class).build();

        adapter = new FirebaseRecyclerAdapter<Payments, ViewHolderr>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderr holder, final int position, @NonNull final Payments model) {

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

    public void Logout(View view){
        fauth.signOut();
        Intent in= new Intent(this, Home.class);
        spref.edit().clear().apply();
        startActivity(in);
        finish();
    }

    public void home(View v){

        Intent in = new Intent(this, AdminHome.class);
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
