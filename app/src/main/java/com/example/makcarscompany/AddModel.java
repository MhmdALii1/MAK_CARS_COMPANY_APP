package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.makcarscompany.AppClassess.Cars;
import com.example.makcarscompany.AppClassess.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddModel extends AppCompatActivity {
    FirebaseAuth fauth;
    SharedPreferences spref;
    ImageView img;
    Button ad,si;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseDatabase fdb;
    DatabaseReference dbref;
    EditText cii,bra,mdl,cor,yar,mil,pri;
    StorageTask mUploadTask;
    String brand,cid,color,model,mileage,year,price,status,image,furi;
    Task<Uri> downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_model);
        fauth=FirebaseAuth.getInstance();
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        img = findViewById(R.id.ima);
        si = findViewById(R.id.sim);
        ad=findViewById(R.id.ba);
        cii=findViewById(R.id.ci);
        bra=findViewById(R.id.br);
        mdl=findViewById(R.id.ml);
        cor=findViewById(R.id.cr);
        yar=findViewById(R.id.yr);
        mil=findViewById(R.id.mi);
        pri=findViewById(R.id.pr);
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://mak-cars-company-ca97e.appspot.com/");

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

    public void Add(View v){

          brand = bra.getText().toString().trim();
          cid = cii.getText().toString().trim();
          color = cor.getText().toString().trim();
          model = mdl.getText().toString().trim();
          mileage = mil.getText().toString().trim();
          price= pri.getText().toString().trim();
          year = yar.getText().toString().trim();
          status = "Available";

        if (TextUtils.isEmpty(cid)) {

            cii.setError("Cid is required");
            return;
        }

        else if (TextUtils.isEmpty(brand)) {

            bra.setError("Brand is required");
            return;
        }

        else if (TextUtils.isEmpty(model)) {

            mdl.setError("Model is required");
            return;
        }
        else if (TextUtils.isEmpty(color)) {

            cor.setError("Color is required");
            return;
        }
        else if (TextUtils.isEmpty(year)) {

            yar.setError("Year is required");
            return;
        }
        else if (TextUtils.isEmpty(mileage)) {

           mil.setError("Mileage is required");
            return;

        }else if (TextUtils.isEmpty(price)) {

           pri.setError("Price is required");
            return;
        }
        else{
            uploadFile();

            furi="https://firebasestorage.googleapis.com/v0/b/mak-cars-company-ca97e.appspot.com/o/"+cid+"."+getFileExtension(imageUri)+"?alt=media&token=ceb10b9b-6185-4818-a003-1bbb9ec07634";
            insertCar(brand,Long.valueOf(cid),color,furi,Long.valueOf(mileage),model,Long.valueOf(price),status,Long.valueOf(year));
            insertACar(brand,Long.valueOf(cid),color,furi,Long.valueOf(mileage),model,Long.valueOf(price),status,Long.valueOf(year));
            Toast.makeText(AddModel.this,"Car Added successfully",Toast.LENGTH_SHORT).show();

        }

    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data!=null){
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }
    public void insertCar(String brand,long cid, String color, String image, long mileage, String model, long price,String status,long year) {
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("Cars").child(brand).child(String.valueOf(cid)).setValue(new Cars(brand,cid, color,  image, mileage, model, price,status, year));

    }

    public void insertACar(String brand,long cid, String color, String image, long mileage, String model, long price,String status,long year) {
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("AllCars").child(String.valueOf(cid)).setValue(new Cars(brand,cid, color,  image, mileage, model, price,status, year));

    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {

             StorageReference fileReference = storageRef.child(cid+ "." + getFileExtension(imageUri));

            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddModel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });


        } else {
            Toast.makeText(this, "No photo selected", Toast.LENGTH_SHORT).show();
        }
    }
}






