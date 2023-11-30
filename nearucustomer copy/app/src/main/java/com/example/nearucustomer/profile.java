package com.example.nearucustomer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; 
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String name , number , pass , email , city,ImageId;

    private FirebaseUser user;  


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        Button button = (Button) findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }else {
            showprofile(user);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), userLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showprofile(FirebaseUser user) {
        String uid = user.getUid();
        TextView namex = (TextView) findViewById(R.id.textView) ;
        TextView emailx = (TextView) findViewById(R.id.textView2) ;
        TextView numberx = (TextView) findViewById(R.id.text1Vie1w4) ;
        TextView passx = (TextView) findViewById(R.id.textView4) ;
        TextView cityx = (TextView) findViewById(R.id.textVi3ew4) ;

        ImageView imageView = (ImageView) findViewById(R.id.circularImageView);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userReadData userReadData = snapshot.getValue(userReadData.class);
                if (userReadData != null) {
                    name = userReadData.name;
                    number = userReadData.number;
                    email = userReadData.email;
                    city = userReadData.city;
                    pass = userReadData.pass;

                    namex.setText(name);
                    numberx.setText(number);
                    emailx.setText(email);
                    passx.setText(pass);
                    cityx.setText(city);

                    ImageId = userReadData.getImageURL();

                    Glide.with(getApplicationContext())
                            .load(ImageId) // Optional error image if loading fails
                            .into(imageView);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
