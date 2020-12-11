package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.makcarscompany.AppClassess.Cars;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class SearchAct extends AppCompatActivity {

    RecyclerView myrv;
    CardView C;
    FirebaseDatabase fdb;
    DatabaseReference dbref;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Cars, CarsViewHolder> adapter;
    FirebaseRecyclerOptions<Cars> options;
    Intent i ;
    String seartxt;
    SharedPreferences spref;
    Button acc,ad;
    FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ad = findViewById(R.id.admin);
        acc = findViewById(R.id.account);
        C=findViewById(R.id.cv);
        spref = getSharedPreferences("Data", MODE_PRIVATE);
        if ((spref.getInt("log", 0) != 0)) {
            ad.setVisibility(View.INVISIBLE);
            acc.setText("Logout");
            fauth = FirebaseAuth.getInstance();
        }
        myrv = findViewById(R.id.myrec);
        i = getIntent();
        seartxt = i.getStringExtra("si");
        layoutManager = new LinearLayoutManager(this);
        fdb = FirebaseDatabase.getInstance();
        spref = getSharedPreferences("Data", MODE_PRIVATE);

        dbref = fdb.getReference().child("AllCars");

        options = new FirebaseRecyclerOptions.Builder<Cars>().setQuery(dbref, Cars.class).build();

        adapter = new FirebaseRecyclerAdapter<Cars, CarsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CarsViewHolder holder, int position, @NonNull final Cars model) {

                if ((model.getBrand().equals(seartxt)) || (model.getcolor().equals(seartxt)) || (model.getModel().equals(seartxt)) || (seartxt.equals(model.getMileage())) || (seartxt.equals(model.getPrice())) || (seartxt.equals(model.getyear()))) {
                    holder.Description.setText(model.tostring());
                    Picasso.get().load(model.getimage()).into(holder.imagec);
                    holder.status.setText(model.getStatus());
                    holder.CV.setVisibility(View.VISIBLE);


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if ((holder.status.getText().toString().equals("Available")) && (spref.getInt("log", 0) == 2)) {
                                Intent intent = new Intent(SearchAct.this, BuyOrAppointment.class);
                                intent.putExtra("car", model);
                                startActivity(intent);
                                finish();
                            } else if (!(spref.getInt("log", 0) == 2)) {
                                Toast.makeText(SearchAct.this, "To buy this car please login to your account or register ", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SearchAct.this, loginorregister.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });

            }else{
                    holder.CV.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) holder.CV.getLayoutParams();
                    params.height = 0;
                    holder.CV.setLayoutParams(params);


                }
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


        if(!myrv.isShown())
            Toast.makeText(SearchAct.this,"Not Found",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    public void home(View v) {

        Intent in = new Intent(this, Home.class);
        startActivity(in);

    }
    public void Admin(View v) {

            AlertDialog.Builder mybuilder = new AlertDialog.Builder(SearchAct.this);
            View myview = getLayoutInflater().inflate(R.layout.loginuser, null);
            final EditText email = myview.findViewById(R.id.f);
            final EditText password = myview.findViewById(R.id.c);
            final ProgressBar prb = myview.findViewById(R.id.p1);
            Button cancel = myview.findViewById(R.id.bc);
            final Button login = myview.findViewById(R.id.bl);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String Email = email.getText().toString().trim();
                    String Password = password.getText().toString().trim();
                    if (TextUtils.isEmpty(Email)) {
                        email.setError("Email is required");
                        return;
                    }
                    if (TextUtils.isEmpty(Password)) {
                        password.setError("Password is required");
                        return;
                    }
                    prb.setVisibility(View.VISIBLE);
                    dbref = FirebaseDatabase.getInstance().getReference("Admin");
                    fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(SearchAct.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            if (d.child("email").getValue().toString().equals(fauth.getCurrentUser().getEmail())) {
                                                prb.setVisibility(View.INVISIBLE);
                                                Intent i = new Intent(SearchAct.this, AdminHome.class);
                                                spref.edit().putInt("log", 1).apply();
                                                spref.edit().putString("admin",Email).apply();
                                                startActivity(i);
                                                finish();
                                            } else {
                                                prb.setVisibility(View.INVISIBLE);
                                                Toast.makeText(SearchAct.this, "User not found ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                prb.setVisibility(View.INVISIBLE);
                                Toast.makeText(SearchAct.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

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

    public void Account(View view){
        Intent inn;
        if(spref.getInt("log",0)!=0){

            inn= new Intent(SearchAct.this, Home.class);

        }else{
            inn= new Intent(SearchAct.this, loginorregister.class);
        }
        startActivity(inn);

    }

}

