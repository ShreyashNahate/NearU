package com.example.nearuseller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.SearchView searchView;
    private GridView gridView;

    private GridAdapter gridAdapter;
    private DatabaseReference databaseReference;
    TextView textViewx;

    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton scan;
    String   number ,whatsapp,  address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        searchView = findViewById(R.id.search);
        textViewx = findViewById(R.id.textView);




        scan = findViewById(R.id.scan);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent2 = new Intent(getApplicationContext(),  Login.class);
            startActivity(intent2);
            finish();
        }else {
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            if(user == null) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }else {
                showprofile(user);
            }
        }

        String uid = user.getUid();
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Seller");
                    databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            SellerReadData userReadData = snapshot.getValue(SellerReadData.class);
                            if (userReadData != null) {
                                number = userReadData.getDataNumber();
                                whatsapp = userReadData.getDataWhatsapp();
                                address = userReadData.getDataAddress();
                                Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
                                intent.putExtra("Location",address);
                                intent.putExtra("Number",number);
                                intent.putExtra("Whatsapp",whatsapp);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        textViewx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),profile.class);
                startActivity(intent);
            }
        });
        searchView.clearFocus();



        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
//        dialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Seller").child(uid).child(uid);
        readData("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                readData(newText);
                return false;
            }
        });
    }

    private void readData(final String query) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item.getDataPrice().toLowerCase().contains(query.toLowerCase())|| item.getDataCategory().toLowerCase().contains(query.toLowerCase()) || item.getDataDescription().toLowerCase().contains(query.toLowerCase()) || item.getDataNumber().toLowerCase().contains(query.toLowerCase()) || item.getDataLocation().toLowerCase().contains(query.toLowerCase())) {
                        itemList.add(item);
                    }
                }
                gridAdapter = new GridAdapter(MainActivity.this, itemList);
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void showprofile(FirebaseUser user) {
    String uid = user.getUid();

    textViewx = findViewById(R.id.welcomename);

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Seller");
    databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            SellerReadData userReadData = snapshot.getValue(SellerReadData.class);
            if (userReadData != null) {
                String name1 = " ";
                name1 = userReadData.getDataName();
                textViewx.setText(name1);
                Toast.makeText(MainActivity.this, "Welcome " +name1 , Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }
}
