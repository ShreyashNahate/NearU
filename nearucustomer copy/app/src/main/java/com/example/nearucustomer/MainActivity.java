package com.example.nearucustomer;

import android.content.Intent;
import android.graphics.Typeface;
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

    TextView textViewx,rent,shop,fake;

    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton scan ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewx = findViewById(R.id.welcomename);
        gridView = findViewById(R.id.gridView);
        searchView = findViewById(R.id.search);
        fake = findViewById(R.id.fake);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            textViewx.setText(bundle.getString("User"));
            fake.setText(bundle.getString("que"));
        }


        scan = findViewById(R.id.scan);
        rent = findViewById(R.id.rent);
        shop = findViewById(R.id.shop);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent2 = new Intent(getApplicationContext(), userLogin.class);
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

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData("for rent");
                rent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                shop.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                rent.setTextSize(25);
                shop.setTextSize(20);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData("for sell");
                rent.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                shop.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                shop.setTextSize(25);
                rent.setTextSize(20);
            }
        });



        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
                startActivity(intent);
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



        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        readData("for sell");



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

                    if (  item.getDataType().toLowerCase().contains(query.toLowerCase())||item.getDataPrice().toLowerCase().contains(query.toLowerCase())|| item.getDataCategory().toLowerCase().contains(query.toLowerCase()) || item.getDataDescription().toLowerCase().contains(query.toLowerCase()) || item.getDataNumber().toLowerCase().contains(query.toLowerCase()) || item.getDataLocation().toLowerCase().contains(query.toLowerCase())) {
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
    private void showprofile(FirebaseUser user){
            String uid = user.getUid();

            textViewx = findViewById(R.id.welcomename);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
            databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userReadData userReadData = snapshot.getValue(userReadData.class);
                    if (userReadData != null) {
                        String name1 = " ";
                        name1 = userReadData.name;
                        textViewx.setText(name1);
                        Toast.makeText(MainActivity.this, "Welcome " + name1, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
}
