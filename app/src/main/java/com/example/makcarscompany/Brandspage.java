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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
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


public class Brandspage extends AppCompatActivity {

    RecyclerView myrv;
    FirebaseDatabase fdb;
    DatabaseReference dbref;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Cars,CarsViewHolder> adapter;
    FirebaseRecyclerOptions<Cars> options;
    int dis;
    String brandd,Setxt,of;
    FirebaseAuth fauth;
    SharedPreferences spref;
    Button acc,ad;
    TextView t;
    Intent iii,i;
    SearchView Svv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandspage);
        fauth=FirebaseAuth.getInstance();
        myrv=findViewById(R.id.myrec);
        t=findViewById(R.id.txt);
        Svv=findViewById(R.id.search);
        t.setSelected(true);
        i=getIntent();
        brandd=i.getStringExtra("br");
        layoutManager = new LinearLayoutManager(this);
        fdb=FirebaseDatabase.getInstance();
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        spref.edit().putInt("m",1).apply();

        dis=spref.getInt("discount",0);
        of= spref.getString("offer","");

        if(dis!=0 && !(of.equals("")) ){

            t.setText(dis+"% discount "+of);

        }

        ad=findViewById(R.id.admin);
        acc=findViewById(R.id.account);

        if((spref.getInt("log",0)!=0)){
            ad.setVisibility(View.INVISIBLE);
            acc.setText("Logout");

        }

           switch (brandd) {
               case "bmw":
                   dbref = fdb.getReference().child("Cars").child("bmw");

                   break;
               case "ford":
                   dbref = fdb.getReference().child("Cars").child("ford");
                   break;
               case "volswagen":
                   dbref = fdb.getReference().child("Cars").child("volswagen");
                   break;
               case "chevrolet":
                   dbref = fdb.getReference().child("Cars").child("chevrolet");
                   break;
               case "mercedes_benz":
                   dbref = fdb.getReference().child("Cars").child("mercedes_benz");
                   break;
               case "nissan":
                   dbref = fdb.getReference().child("Cars").child("nissan");
                   break;
           }

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

                           if ((holder.status.getText().toString().equals("Available")) && (spref.getInt("log", 0) == 2)) {
                               Intent intent = new Intent(Brandspage.this, BuyOrAppointment.class);
                               intent.putExtra("car", model);
                               startActivity(intent);
                               finish();
                           } else if (!(spref.getInt("log", 0) == 2)) {
                               Toast.makeText(Brandspage.this, "To buy this car please login to your account or register ", Toast.LENGTH_SHORT).show();
                               Intent i = new Intent(Brandspage.this, loginorregister.class);
                               startActivity(i);
                               finish();
                           }
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

        Svv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Setxt = Svv.getQuery().toString();
                iii=new Intent(Brandspage.this,SearchAct.class);
                iii.putExtra("si",Setxt);
                startActivity(iii);
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


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

    public void home(View v){
        Intent in;
        String L=String.valueOf(spref.getInt("log",0));
       switch (L ){

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


    public void Admin(View v) {
        AlertDialog.Builder mybuilder = new AlertDialog.Builder(Brandspage.this);
        View myview = getLayoutInflater().inflate(R.layout.loginuser, null);
        final EditText email = myview.findViewById(R.id.f);
        final EditText password = myview.findViewById(R.id.c);
        final ProgressBar prb = myview.findViewById(R.id.p1);
        final Button login = myview.findViewById(R.id.bl);
        Button cancel = myview.findViewById(R.id.bc);

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
                        fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(Brandspage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                if (d.child("email").getValue().toString().equals(fauth.getCurrentUser().getEmail())) {
                                                    prb.setVisibility(View.INVISIBLE);
                                                    spref.edit().putInt("log",1).apply();
                                                    spref.edit().putString("admin",Email).apply();
                                                    Intent i = new Intent(Brandspage.this, AdminHome.class);
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    prb.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(Brandspage.this, "User not found ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    prb.setVisibility(View.INVISIBLE);
                                    Toast.makeText(Brandspage.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

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

            inn= new Intent(Brandspage.this, Home.class);


        }else{
            inn= new Intent(Brandspage.this, loginorregister.class);

        }
        startActivity(inn);

        finish();

    }

}