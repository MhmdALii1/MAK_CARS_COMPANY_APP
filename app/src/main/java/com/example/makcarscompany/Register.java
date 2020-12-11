package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.makcarscompany.AppClassess.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    Button re;
    EditText fn, un, em, phn, pd;
    ProgressBar pb;
    FirebaseAuth fauth;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fn = findViewById(R.id.n);
        un = findViewById(R.id.em);
        em = findViewById(R.id.c);
        phn = findViewById(R.id.ci);
        pd = findViewById(R.id.adr);
        re = findViewById(R.id.bp);
        pb = findViewById(R.id.p1);
        fauth = FirebaseAuth.getInstance();

    }

   public void CreateAccount(View v){
        final String flname = fn.getText().toString().trim();
        final String usern = un.getText().toString().trim();
        final String Email = em.getText().toString().trim();
        final String phone = phn.getText().toString().trim();
        final String Password = pd.getText().toString().trim();


        if (TextUtils.isEmpty(flname)) {

            fn.setError("FullName is required");
            return;
        }

        else if (TextUtils.isEmpty(usern)) {

            un.setError("Username is required");
            return;
        }


        else if (TextUtils.isEmpty(Email)) {

            em.setError("Email is required");
            return;
        }
        else if (TextUtils.isEmpty(phone)) {

            phn.setError("password is required");
            return;
        }

       else if (TextUtils.isEmpty(Password)) {

            pd.setError("password is required");
            return;
        }
        else if (pd.length() < 8) {

            pd.setError("password must be at least 8 characters");
            return;
        }
       else{ pb.setVisibility(View.VISIBLE);


            RegisterUser(flname,usern,Email, Long.valueOf(phone),Password);
        }

    }
    public void RegisterUser(final String FullName, final String username,final String email,final long Phonenb,final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(username).exists())){

                    fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                insertCustomer(FullName, username, email, Phonenb, password);
                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(Register.this, "you are registered now", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Register.this, loginorregister.class);
                                startActivity(i);
                                finish();

                            } else {

                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(Register.this, username + " already exists.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Register.this, "Please try again using another username.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    
    public void insertCustomer(String FullName, String username, String email, long Phonenb, String password) {
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("Users").child(username).setValue(new User(FullName, username, email, Phonenb, password));

    }
    public void home(View v){

        Intent in = new Intent(this, Home.class);
        startActivity(in);
        finish();

    }
}