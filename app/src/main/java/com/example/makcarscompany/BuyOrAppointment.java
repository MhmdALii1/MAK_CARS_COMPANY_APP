package com.example.makcarscompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.makcarscompany.AppClassess.Appointments;
import com.example.makcarscompany.AppClassess.Cars;
import com.example.makcarscompany.AppClassess.Payments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyOrAppointment extends AppCompatActivity {
    FirebaseAuth fauth;
    Button t;
    TextView tp;
    Cars car;
    DatabaseReference dbref,dbreff;
    FirebaseDatabase db;
    EditText Usern,eml ,cty,cy,addr,nameoc,cardn;
    Intent i;
    long carcid;
    long shipmentPrice=450;
    long totalprice=0;
    int mYear, mMonth, mDay, mHour, mMinute;
    String userr,em;
    SharedPreferences spref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_or_appointment);
        db=FirebaseDatabase.getInstance();
        t=findViewById(R.id.tap);
        Usern=findViewById(R.id.n);
        eml=findViewById(R.id.em);
        cty=findViewById(R.id.c);
        cy=findViewById(R.id.ci);
        addr=findViewById(R.id.adr);
        nameoc=findViewById(R.id.ncn);
        cardn=findViewById(R.id.cn);
        tp=findViewById(R.id.prtt);
        fauth= FirebaseAuth.getInstance();
        spref=getSharedPreferences("Data",MODE_PRIVATE);
        userr=spref.getString("usernamee", "");
        em=spref.getString("useremail","");
        Usern.setEnabled(false);
        Usern.setText(userr);
        eml.setEnabled(false);
        eml.setText(em);
        i=getIntent();
        car=(Cars)i.getSerializableExtra("car");
        carcid=car.getCid();

        totalprice=car.getPrice();
        totalprice+=shipmentPrice;
        tp.setText(totalprice+" $");

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mybuilder = new AlertDialog.Builder(BuyOrAppointment.this);
                View myview = getLayoutInflater().inflate(R.layout.takeapp, null);
                final EditText Usern = myview.findViewById(R.id.u);
                Usern.setText(userr);
                Usern.setEnabled(false);
                final EditText Date = myview.findViewById(R.id.dt);
                final EditText Time =myview.findViewById(R.id.dti);
                final Button submit = myview.findViewById(R.id.bs);
                Button cancel = myview.findViewById(R.id.bc);

                Date.setOnClickListener(new View.OnClickListener() {
                  @Override
                 public void onClick(View v) {

                      final Calendar c = Calendar.getInstance();

                      mYear = c.get(Calendar.YEAR);
                      mMonth = c.get(Calendar.MONTH);
                      mDay = c.get(Calendar.DAY_OF_MONTH);


                      DatePickerDialog datePickerDialog = new DatePickerDialog(BuyOrAppointment.this,R.style.DialogTheme ,new DatePickerDialog.OnDateSetListener() {
                                  @Override
                                  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                      Date.setText(year+"/"+(monthOfYear + 1)+"/"+ dayOfMonth);

                                  }
                              }, mYear, mMonth, mDay);
                      datePickerDialog.show();

                  }
                });

                Time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(BuyOrAppointment.this,R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String AM_PM ;
                                        if(hourOfDay < 12) {
                                            AM_PM = "am";
                                        } else {
                                            AM_PM = "pm";
                                        }

                                        if(minute<10){

                                        Time.setText(hourOfDay + ":0" + minute+" "+AM_PM);}
                                        else{
                                            Time.setText(hourOfDay + ":" + minute+" "+AM_PM);}
                                        }

                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String userna = Usern.getText().toString().trim();
                        String datee = Date.getText().toString().trim();
                        String timee=Time.getText().toString().trim();
                        if (TextUtils.isEmpty(userna)) {
                            Usern.setError("UserName is required");
                            return;

                        }
                        else if (TextUtils.isEmpty(datee)) {
                            Date.setError("Date is required");
                            return;

                        }
                        else if (TextUtils.isEmpty(timee)) {
                            Time.setError("Time is required");
                            return;

                        } else {

                            insertAppointment(userna, car.getCid(), datee,timee);
                            Toast.makeText(BuyOrAppointment.this,"Your appointment is placed , you can make a test drive when you come , we will be waiting for you mr."+userna,Toast.LENGTH_SHORT).show();

                        }
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

    public void brand(View v){

        Intent in= new Intent(BuyOrAppointment.this, Brandspage.class);
        in.putExtra("br",car.getBrand());
        startActivity(in);

    }

    public void Pay(View v) {

        final String usern = Usern.getText().toString().trim();
        final String email = eml.getText().toString().trim();
        final String cardNb = cardn.getText().toString().trim();
        final String Nameonc = nameoc.getText().toString().trim();
        final String country = cty.getText().toString().trim();
        final String city = cy.getText().toString().trim();
        final String address = addr.getText().toString().trim();

        if (TextUtils.isEmpty(usern)) {

            Usern.setError("UserName is required");
            return;

        } else if (TextUtils.isEmpty(email)) {

            eml.setError("Email is required");
            return;

        }else if(!isEmailValid(email)){

            eml.setError("Email is not valid");
            return;

        } else if (TextUtils.isEmpty(country)) {

            cty.setError("Country is required");
            return;

        } else if (TextUtils.isEmpty(city)) {

            cy.setError("City is required");
            return;

        } else if (TextUtils.isEmpty(address)) {

            addr.setError("Address is required");
            return;

        } else if (TextUtils.isEmpty(Nameonc)) {

            nameoc.setError("Name on Credit Card is required");
            return;

        } else if (TextUtils.isEmpty(cardNb)) {

            cardn.setError("Card Number is required");
            return;

        } else {

            insertPayment( country, address, city, Nameonc, Long.parseLong(cardNb), usern, carcid, totalprice);
            String brandd=car.getBrand();
            dbref=db.getReference("Cars").child(brandd);
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                         car.setStatus("Sold");
                                                         String cid=String.valueOf(car.getCid());

                                                         dataSnapshot.child(cid).getRef().setValue(car);
                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                     }
                                                 });
            dbref=db.getReference("AllCars");
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    car.setStatus("Sold");
                    String cid=String.valueOf(car.getCid());
                    dataSnapshot.child(cid).getRef().setValue(car);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            dbreff=FirebaseDatabase.getInstance().getReference("Cart").child(userr).child("UserCars");
            car.setStatus("Sold");
            dbreff.child(String.valueOf(car.getCid())).setValue(car);
            Toast.makeText(this, "Your order is placed mr." + usern + ", the car will be shipped within 4 days ", Toast.LENGTH_LONG).show();
        }
    }

    public void insertPayment(String Country,String Address,String City,String NameOnCreditCard,long CardNumber,String username, long cid, long price) {
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("Payments").child(String.valueOf(cid)).setValue(new Payments(Country,Address,City,NameOnCreditCard,CardNumber,username, cid, price));

    }
    public void insertAppointment(String username, long cid, String date,String time) {
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("Appointments").child(String.valueOf(cid)).setValue(new Appointments(username,cid,date,time));

    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void Logout(View view){
        fauth.signOut();
        Intent in= new Intent(this, Home.class);
        spref.edit().clear().apply();
        startActivity(in);
        finish();
    }
}
