package com.example.nearuseller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String name , number ,whatsapp, email,pass , address , city,ImageId;

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
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showprofile(FirebaseUser user) {
        String uid = user.getUid();
        TextView namex = (TextView) findViewById(R.id.textView) ;
        TextView numberx = (TextView) findViewById(R.id.text1Vie1w4) ;
        TextView whatsappx = (TextView) findViewById(R.id.textVie1w4) ;
        TextView emailx = (TextView) findViewById(R.id.textView2) ;
        TextView passx = (TextView) findViewById(R.id.textView4) ;
        TextView addressx = (TextView) findViewById(R.id.textVi3ew4) ;
        TextView cityx = (TextView) findViewById(R.id.textVi3ewyy) ;

        ImageView imageView = (ImageView) findViewById(R.id.circularImageView);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Seller");
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SellerReadData userReadData = snapshot.getValue(SellerReadData.class);
                if (userReadData != null) {
                    name = userReadData.getDataName();
                    number = userReadData.getDataNumber();
                    whatsapp = userReadData.getDataWhatsapp();
                    email = userReadData.getDataEmail();
                    pass = userReadData.getDataPassword();
                    address = userReadData.getDataAddress();
                    city = userReadData.getDataCity();
                    ImageId = userReadData.getDataShopImage();

                    namex.setText(name);
                    numberx.setText(number);
                    whatsappx.setText(whatsapp);
                    emailx.setText(email);
                    passx.setText(pass);
                    addressx.setText(address);
                    cityx.setText(city);


                    Glide.with(getApplicationContext())
                            .load(ImageId)
                            .into(imageView);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}
