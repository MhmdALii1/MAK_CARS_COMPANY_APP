package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
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
import java.io.IOException;
import java.util.ArrayList;

public class Home extends AppCompatActivity {
    TextView t,imagena;
    Button Ad, Ac, bmw,chev,ford,merc,nis,vols, nextt,prev;
    ImageView im;
    AssetManager assets;
    String [] images ;
    ArrayList<String>names=new ArrayList<>();
    int c=0;
    FirebaseAuth fauth;
    DatabaseReference dbref;
    Intent iii;
    SharedPreferences spref;
    int l;
    Intent i1;
    String Setxt;
    SearchView Svv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        fauth=FirebaseAuth.getInstance();
        l=spref.getInt("log",0);
        if(l!=0){

              if(l==1)
                 i1 = new Intent(Home.this, AdminHome.class);

              else
                 i1 = new Intent(Home.this, UserHome.class);

            startActivity(i1);
            finish();
        }
        try{
        assets = this.getAssets();
        images = assets.list("cars");
        }catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0;i<images.length;i++)
        names.add(images[i].replace(".jpg",""));

        im=findViewById(R.id.im1);
        t=findViewById(R.id.txt);
        t.setSelected(true);
        Ad=findViewById(R.id.admin);
        Ac=findViewById(R.id.account);
        bmw=findViewById(R.id.bm);
        chev=findViewById(R.id.ch);
        ford=findViewById(R.id.fd);
        merc=findViewById(R.id.me);
        vols=findViewById(R.id.vol);
        nis=findViewById(R.id.ni);
        prev=findViewById(R.id.back);
        nextt=findViewById(R.id.next);
        imagena=findViewById(R.id.imne);
        fauth=FirebaseAuth.getInstance();

        Svv=findViewById(R.id.search);

                Svv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        Setxt = Svv.getQuery().toString();
                        iii=new Intent(Home.this,SearchAct.class);
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

    public void BM(View view){
        Intent in= new Intent(this, Brandspage.class);
        in.putExtra("br","bmw");
        startActivity(in);
    }

    public void CH(View view){
        Intent in= new Intent(this,Brandspage.class);
        in.putExtra("br","chevrolet");
        startActivity(in);
    }

    public void FD(View view){
        Intent in= new Intent(this,Brandspage.class);
        in.putExtra("br","ford");
        startActivity(in);
    }

    public void MER(View view){
        Intent in= new Intent(this,Brandspage.class);
        in.putExtra("br","mercedes_benz");
        startActivity(in);
    }

    public void VOL(View view){
        Intent in= new Intent(this,Brandspage.class);
        in.putExtra("br","volswagen");
        startActivity(in);
    }

    public void NIS(View view){
        Intent in= new Intent(this,Brandspage.class);
        in.putExtra("br","nissan");
        startActivity(in);
    }

    public void Account(View view){
        Intent in= new Intent(this, loginorregister.class);
        startActivity(in);
        finish();
    }

     public void nimage(View v){

            if (c >0) {
                c--;
            } else {
                c=names.size()-1;
            }
            try {
                String imagename = "cars/" + images[c]; //name of the image
                Drawable image = Drawable.createFromStream(assets.open(imagename), imagename);
                im.setImageDrawable(image);
                imagena.setText(names.get(c));
            } catch (IOException e) {
                e.printStackTrace();
            }
}

     public void pimage(View v){
        if(c==names.size()-1){
            c=0;

        }else{
            c++;

        }
        try {
            String imagename    = "cars/" + images[c]; //name of the image
            Drawable image = Drawable.createFromStream(assets.open(imagename),imagename);
            im.setImageDrawable(image);
            imagena.setText(names.get(c));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void Admin(View v) {
            AlertDialog.Builder mybuilder = new AlertDialog.Builder(Home.this);
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
                            fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(Home.this, new OnCompleteListener<AuthResult>() {
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
                                                        Intent i = new Intent(Home.this, AdminHome.class);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        prb.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(Home.this, "User not found ", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        prb.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Home.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

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







