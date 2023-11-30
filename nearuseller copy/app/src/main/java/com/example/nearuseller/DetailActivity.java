package com.example.nearuseller;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DetailActivity extends AppCompatActivity {

    TextView dataPrice, dataNumber, dataLocation ,dataTitle,dataDescription,dataWhatsappNumber,dataTime;
    ImageView dataImage;
    CardView recCard;
    FirebaseAuth auth;
    FirebaseUser user;
    String imageUrl = "";
    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dataImage = findViewById(R.id.recImage);
        dataPrice = findViewById(R.id.Price);
        dataLocation = findViewById(R.id.Location);
        dataNumber = findViewById(R.id.Number);
        dataTitle = findViewById(R.id.title);
        dataDescription = findViewById(R.id.Description);
        dataWhatsappNumber = findViewById(R.id.whats);
        dataTime = findViewById(R.id.time);
        recCard = findViewById(R.id.recCard);
        ImageButton callnow = (ImageButton) findViewById(R.id.callnow);
        ImageButton chatnow = (ImageButton) findViewById(R.id.chatnow);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            dataPrice.setText(bundle.getString("Price"));
            dataLocation.setText(bundle.getString("Location"));
            dataNumber.setText(bundle.getString("Number"));
            dataWhatsappNumber.setText(bundle.getString("Whatsapp"));
            dataTitle.setText(bundle.getString("Category"));
            dataDescription.setText(bundle.getString("Desc"));
            dataTime.setText(bundle.getString("Time"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");//firebase uri

            Glide.with(this).load(bundle.getString("Image")).into(dataImage);
        }
        String number = dataNumber.getText().toString();
        String time = dataTime.getText().toString();
        String whatsapp = dataWhatsappNumber.getText().toString();
        chatnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s", "+91"+whatsapp, "Hey ! I want to contact you for a product.")
                        ));
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        callnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);
            }
        });
        recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), zoom.class);
                intent.putExtra("Image",imageUrl);
                startActivity(intent);
            }
        });

        ImageButton deleteButton = (ImageButton) findViewById(R.id.delete);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String uid = user.getUid();

        DatabaseReference myref1 = FirebaseDatabase.getInstance().getReference("Registered Seller").child(uid).child(uid).child(time);
        DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Products").child(time);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(DetailActivity.this, imageUrl, Toast.LENGTH_SHORT).show();
                try{
                    //new serverthread().delete(imageUrl);
                }
                catch (Exception e)
                {
//                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(DetailActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                myref1.removeValue()
                        .addOnSuccessListener(aVoid -> {

                        })
                        .addOnFailureListener(e -> {
                        });
                myref2.removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firebase", "Product deleted successfully");
                        })
                        .addOnFailureListener(e -> {
                            // Handle the failure to delete
                            Log.e("Firebase", "Error deleting Product", e);
                        });
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });
    }
    class serverthread implements Runnable{
        Socket client;
        OutputStream out;
        InputStream in;
        String uri;
        @Override
        public void run() {
            try {
                client = new Socket("34.100.220.209",6769);
                out = client.getOutputStream();
                in = client.getInputStream();
                out.write("remove_product".getBytes(StandardCharsets.UTF_8));
                in.read();
                out.write(uri.getBytes(StandardCharsets.UTF_8));
//                in.read();
                out.flush();
                out.close();
                client.close();
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailActivity.this, "Error"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        public void delete(String uri){
            this.uri = uri;
            run();
        }
    }
}