package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.makcarscompany.AppClassess.Cars;
import com.example.makcarscompany.AppClassess.Feedbacks;
import com.example.makcarscompany.AppClassess.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserCart extends AppCompatActivity {
    FirebaseAuth fauth;
    RecyclerView myrv;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Cars,CarsViewHolder> adapter;
    FirebaseRecyclerOptions<Cars> options;
    SharedPreferences spref;
    FirebaseDatabase fdb;
    DatabaseReference dbref;
    ArrayList<Long> cd=new ArrayList<>();
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        spref = getSharedPreferences("Data", MODE_PRIVATE);
        fauth = FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance();
        myrv=findViewById(R.id.myrec);
        layoutManager = new LinearLayoutManager(this);

        user = spref.getString("usernamee", "");

        dbref = fdb.getReference().child("Cart").child(user).child("UserCars");

        options = new FirebaseRecyclerOptions.Builder<Cars>().setQuery(dbref, Cars.class).build();

        adapter = new FirebaseRecyclerAdapter<Cars, CarsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CarsViewHolder holder, final int position, @NonNull final Cars model) {

                holder.Description.setText(model.tostring());
                Picasso.get().load(model.getimage()).into(holder.imagec);
                holder.status.setText(model.getStatus());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mybuilder = new AlertDialog.Builder(UserCart.this);
                        View myview = getLayoutInflater().inflate(R.layout.fe ,null);
                        final EditText feedback = myview.findViewById(R.id.fee);
                        Button cancel = myview.findViewById(R.id.bc);
                        final Button send= myview.findViewById(R.id.bs);

                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String di = feedback.getText().toString().trim();

                                if (TextUtils.isEmpty(di)) {

                                    feedback.setError("your feedback is required");
                                    return;
                                }
                                insertfeedback(user,di,model.getCid());
                                Toast.makeText(UserCart.this,"Thank you for your feedback mr."+user,Toast.LENGTH_SHORT).show();
                            }
                        });
                        mybuilder.setView(myview);
                        final AlertDialog dialog = mybuilder.create();
                        dialog.show();
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                    }
                });
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
    public void Logout(View view){
        fauth.signOut();
        Intent in= new Intent(this, Home.class);
        spref.edit().clear().apply();
        startActivity(in);
        finish();
    }

    public void insertfeedback(String username,String comment,long cid){
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("Feedbacks").child(String.valueOf(cid)).setValue(new Feedbacks(username,comment,cid));

    }

    public void home(View v){

        Intent in = new Intent(this, UserHome.class);
        startActivity(in);
        finish();

    }
}