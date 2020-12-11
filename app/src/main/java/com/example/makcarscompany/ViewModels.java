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

import com.example.makcarscompany.AppClassess.Cars;
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
import java.util.Arrays;

public class ViewModels extends AppCompatActivity {
    RecyclerView myrv;
    FirebaseDatabase fdb;
    DatabaseReference dbref;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Cars,CarsViewHolder> adapter;
    FirebaseRecyclerOptions<Cars> options;
    FirebaseAuth fauth;
    SharedPreferences spref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_models);
        fauth=FirebaseAuth.getInstance();
        myrv=findViewById(R.id.myrec);
        layoutManager = new LinearLayoutManager(this);
        fdb=FirebaseDatabase.getInstance();

        dbref = fdb.getReference("AllCars");

        options = new FirebaseRecyclerOptions.Builder<Cars>().setQuery(dbref, Cars.class).build();

        adapter = new FirebaseRecyclerAdapter<Cars,CarsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CarsViewHolder holder, final int position, @NonNull final Cars model) {

                holder.Description.setText(model.tostring());
                Picasso.get().load(model.getimage()).into(holder.imagec);
                holder.status.setText(model.getStatus());

        }
            @NonNull
            @Override
            public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
                return new CarsViewHolder(view);
            }
        };

        myrv.setLayoutManager(layoutManager);
        adapter.startListening();
        myrv.setAdapter(adapter);

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

}
