package com.example.nearucustomer;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// MainActivity.java
public class scan extends AppCompatActivity {
    private GridView gridView;
    private GridAdapter gridAdapter;
    private DatabaseReference databaseReference;

    TextView  fake1,fake2,fake3,fake4,fake5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);

        gridView = findViewById(R.id.gridView);
        fake1 = findViewById(R.id.fake1);
        fake2 = findViewById(R.id.fake2);
        fake3 = findViewById(R.id.fake3);
//        fake4 = findViewById(R.id.fake4);
//        fake5 = findViewById(R.id.fake5);
        Bundle bundle = getIntent().getExtras();
        String s1,s2,s3,s4,s5 = null;
        if (bundle != null){
            fake1.setText(bundle.getString("que1"));
            fake2.setText(bundle.getString("que2"));
            fake3.setText(bundle.getString("que3"));
//            fake4.setText(bundle.getString("que4"));
//            fake5.setText(bundle.getString("que5"));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        s1 = fake1.getText().toString();
        s2 = fake2.getText().toString();
        s3 = fake3.getText().toString();
//        s4 = fake4.getText().toString();
//        s5 = fake5.getText().toString();

        String arr1[] = s1.split("2F",15);
        for (String w : arr1) {
            fake1.setText(w);
        }
        String arr2[] = s2.split("2F",15);
        for (String w : arr2) {
            fake2.setText(w);
        }
        String arr3[] = s3.split("2F",15);
        for (String w : arr3) {
            fake3.setText(w);
        }

//        String arr4[] = s4.split("2F",15);
//        for (String w : arr4) {
//            fake4.setText(w);
//        }
//
//        String arr5[] = s5.split("2F",15);
//        for (String w : arr5) {
//            fake5.setText(w);
//        }

        int index1 = fake1.getText().toString().indexOf("?alt");
        int index2 = fake2.getText().toString().indexOf("?alt");
        int index3 = fake3.getText().toString().indexOf("?alt");
//        int index4 = fake4.getText().toString().indexOf("?alt");
//        int index5 = fake5.getText().toString().indexOf("?alt");

        String s11=fake1.getText().toString().substring(0,index1);
        String s22=fake2.getText().toString().substring(0,index2);
        String s33=fake3.getText().toString().substring(0,index3);
//        String s44=fake4.getText().toString().substring(0,index4);
//        String s55=fake5.getText().toString().substring(0,index5);


        readData(s11,s22,s33  );
    }

    private void readData(final String query1,String query2,String query3  ) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);

                    if ( item.getDataImage().toLowerCase().contains(query1.toLowerCase()) ||
                            item.getDataImage().toLowerCase().contains(query2.toLowerCase()) ||
                            item.getDataImage().toLowerCase().contains(query3.toLowerCase()))   {
                        itemList.add(item);
                    }
                }

                gridAdapter = new GridAdapter(scan.this, itemList);
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
