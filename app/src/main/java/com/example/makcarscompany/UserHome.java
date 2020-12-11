package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.makcarscompany.AppClassess.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class UserHome extends AppCompatActivity {
    TextView t,imagena;
    DatabaseReference dbref;
    Button log,bmw,chev,ford,merc,nis,vols, nextt,prev;
    SearchView Svv;
    ImageView im;
    AssetManager assets;
    String [] images ;
    ArrayList<String> names=new ArrayList<>();
    int c=0;
    FirebaseAuth fauth;
    String Setxt,em,user;
    Intent iii;
    SharedPreferences spref;
    Spinner spn;
    Intent i4;
    int check = 0;
    FirebaseDatabase fdb;
    User u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        fdb=FirebaseDatabase.getInstance();
        em=spref.getString("useremail","");

        if(!em.equals("")) {
            fdb.getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        u = d.getValue(User.class);
                        if(u.getEmail().equals(em)){
                            user=u.getUsername();
                            spref.edit().putString("usernamee",user).apply();}

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        spref.edit().putInt("log",2).apply();
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
        log=findViewById(R.id.account);
        bmw=findViewById(R.id.bm);
        chev=findViewById(R.id.ch);
        ford=findViewById(R.id.fd);
        merc=findViewById(R.id.me);
        vols=findViewById(R.id.vol);
        nis=findViewById(R.id.ni);
        prev=findViewById(R.id.back);
        nextt=findViewById(R.id.next);
        Svv=findViewById(R.id.search);
        imagena=findViewById(R.id.imne);
        fauth=FirebaseAuth.getInstance();
        spn=findViewById(R.id.spp);
        fauth=FirebaseAuth.getInstance();


        Svv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Setxt = Svv.getQuery().toString();
                iii=new Intent(UserHome.this,SearchAct.class);
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

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1) {
                    if (spn.getSelectedItem().toString().trim().equals("Cart")) {

                        i4 = new Intent(UserHome.this, UserCart.class);
                    }
                     if (spn.getSelectedItem().toString().trim().equals("Appointments")) {

                        i4 = new Intent(UserHome.this, AppointmentsAct.class);

                    }
                     if (spn.getSelectedItem().toString().trim().equals("Edit Info")) {

                        i4 = new Intent(UserHome.this, Editprofile.class);
                    }
                    startActivity(i4);
                    finish();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    public void Logout(View view){
        fauth.signOut();
        Intent in= new Intent(this, Home.class);
        spref.edit().clear().apply();
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


}
