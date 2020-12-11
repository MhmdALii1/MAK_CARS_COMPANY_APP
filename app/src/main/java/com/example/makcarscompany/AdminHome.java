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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makcarscompany.AppClassess.Feedbacks;
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

public class AdminHome extends AppCompatActivity {
    TextView t,imagena;
    Button log,bmw,chev,ford,merc,nis,vols, nextt,prev;
    ImageView im;
    AssetManager assets;
    String [] images ;
    ArrayList<String> names=new ArrayList<>();
    int c=0;
    FirebaseAuth fauth;
    SharedPreferences spref;
    Spinner spn2,spn3;
    Intent i5,iii;
    SearchView Svv;
    String Setxt;
    int check=0,check1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        spref.edit().putInt("log",1);
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
        spn2=findViewById(R.id.spp2);
        spn3=findViewById(R.id.spp3);
        fauth=FirebaseAuth.getInstance();
        spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1) {
                String op=spn2.getSelectedItem().toString();

                switch (op){

                    case "Add Models":
                        i5=new Intent(AdminHome.this,AddModel.class);
                        startActivity(i5);
                        finish();
                        break;

                    case "View Models":
                        i5=new Intent(AdminHome.this,ViewModels.class);
                        startActivity(i5);
                        finish();
                        break;

                    case "Add Discount":
                        AlertDialog.Builder mybuilder = new AlertDialog.Builder(AdminHome.this);
                        View myview = getLayoutInflater().inflate(R.layout.disc, null);
                        final EditText discount = myview.findViewById(R.id.d);
                        final EditText description  = myview.findViewById(R.id.ds);
                        Button cancel = myview.findViewById(R.id.bc);
                        final Button add = myview.findViewById(R.id.bl);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String di = discount.getText().toString().trim();
                                final String descc=description.getText().toString().trim();
                                if (TextUtils.isEmpty(di)) {
                                    discount.setError("Discount is required");
                                    return;
                                } else if (TextUtils.isEmpty(descc)) {
                                    discount.setError("offer is required");
                                    return;
                                }
                                spref.edit().putInt("discount",Integer.valueOf(di)).apply();
                                spref.edit().putString("offer",descc).apply();
                                Toast.makeText(AdminHome.this,"Discount added successfully",Toast.LENGTH_SHORT).show();
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

          }
        });

        spn3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check1 > 1) {
                String op=spn3.getSelectedItem().toString();
                switch (op){

                    case "View Appointments":
                        i5=new Intent(AdminHome.this,AppointmentsAct.class);
                        break;

                    case "View Feedbacks":
                        i5=new Intent(AdminHome.this, FeedbacksAct.class);
                        break;

                    case "View Payments":
                        i5=new Intent(AdminHome.this,PaymentsAct.class);
                        break;

                }
                startActivity(i5);
                finish();
              }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Svv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Setxt = Svv.getQuery().toString();
                iii=new Intent(AdminHome.this,SearchAct.class);
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

    public void Logout(View view){

        Intent in= new Intent(this, Home.class);
        spref.edit().clear().apply();
        fauth.signOut();
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
