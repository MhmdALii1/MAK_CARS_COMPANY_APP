package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginorregister extends AppCompatActivity {

    FirebaseAuth fauth;
    DatabaseReference dbref;
    SharedPreferences spref;
    Intent iii;
    String Setxt;
    SearchView Svv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginorregister);
        fauth=FirebaseAuth.getInstance();
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        Svv=findViewById(R.id.search);

        Svv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Setxt = Svv.getQuery().toString();
                iii=new Intent(loginorregister.this,SearchAct.class);
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

        public void Registeru(View v){
            Intent in= new Intent(this, Register.class);
            startActivity(in);
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
        public void Loginu (View view) {

            AlertDialog.Builder mybuilder = new AlertDialog.Builder(loginorregister.this);
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
                            dbref = FirebaseDatabase.getInstance().getReference("Users");
                            fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(loginorregister.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                    if (d.child("email").getValue().toString().equals(fauth.getCurrentUser().getEmail())) {
                                                        Intent i = new Intent(loginorregister.this, UserHome.class);
                                                        prb.setVisibility(View.INVISIBLE);
                                                        spref.edit().putInt("log",2).apply();
                                                        spref.edit().putString("useremail",Email).apply();
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        prb.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(loginorregister.this, "User not found ", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        prb.setVisibility(View.INVISIBLE);
                                        Toast.makeText(loginorregister.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
}
