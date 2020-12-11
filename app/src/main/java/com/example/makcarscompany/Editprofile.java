package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Editprofile extends AppCompatActivity {
    FirebaseAuth fauth;
    SharedPreferences spref;
    Button re;
    EditText fn, un, em, phn, pd;
    DatabaseReference dbref;
    String uemail;
    User u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        fauth=FirebaseAuth.getInstance();
        fn = findViewById(R.id.n);
        un = findViewById(R.id.em);
        em = findViewById(R.id.c);
        phn = findViewById(R.id.ci);
        pd = findViewById(R.id.adr);
        re = findViewById(R.id.bp);
        un.setEnabled(false);
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        uemail=spref.getString("useremail","");

       if( !(uemail.equals("") )) {
           dbref = FirebaseDatabase.getInstance().getReference("Users");
           dbref.addValueEventListener(new ValueEventListener() {

               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                       u = snapshot.getValue(User.class);

                       if (u.getEmail().equals(uemail)) {
                           fn.setText(u.getFullName());
                           un.setText(u.getUsername());
                           em.setText(u.getEmail());
                           phn.setText(String.valueOf(u.getPhonenb()));
                           pd.setText(u.getPassword());
                       }
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
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

        Intent in = new Intent(this, UserHome.class);
        startActivity(in);
        finish();

    }

    public void Edit(View v) {
        final String flname = fn.getText().toString().trim();
        final String usern = un.getText().toString().trim();
        final String Email = em.getText().toString().trim();
        final String phone = phn.getText().toString().trim();
        final String Password = pd.getText().toString().trim();

        if (TextUtils.isEmpty(flname)) {

            fn.setError("FullName is required");
            return;
        } else if (TextUtils.isEmpty(usern)) {

            un.setError("Username is required");
            return;
        } else if (TextUtils.isEmpty(Email)) {

            em.setError("Email is required");
            return;
        } else if (TextUtils.isEmpty(phone)) {

            phn.setError("password is required");
            return;
        } else if (TextUtils.isEmpty(Password)) {

            pd.setError("password is required");
            return;
        } else if (pd.length() < 8) {

            pd.setError("password must be at least 8 characters");
            return;

        } else {

            EditUser(flname, usern, Email, Long.valueOf(phone), Password);
        }
    }
    public void EditUser(String FullName, String username, String email, long Phonenb, String password) {
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("Users").child(username).setValue(new User(FullName, username, email, Phonenb, password));

    }
}
