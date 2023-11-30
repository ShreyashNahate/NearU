package com.example.nearucustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    TextView dataPrice, dataNumber, dataLocation ,dataTitle,dataDescription,status;
    ImageView dataImage;
    CardView recCard;
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
        recCard = findViewById(R.id.recCard);
        status = findViewById(R.id.status);

        ImageButton callnow = (ImageButton) findViewById(R.id.callnow);
        ImageButton chatnow = (ImageButton) findViewById(R.id.chatnow);
        Bundle bundle = getIntent().getExtras();


        if (bundle != null){
            dataPrice.setText(bundle.getString("Price"));
            dataLocation.setText(bundle.getString("Location"));
            dataNumber.setText(bundle.getString("Number"));
            dataTitle.setText(bundle.getString("Category"));
            dataDescription.setText(bundle.getString("Desc"));
            status.setText(bundle.getString("Type"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(dataImage);
        }
        String number = dataNumber.getText().toString();

        if (status.getText().toString().toLowerCase().contains("for rent"))
        {
            status.setTextColor(Color.RED);
        }else{
            status.setTextColor(Color.GREEN);
        }
        chatnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s", "+91"+number, "Hey ! I want to contact you for a product.")
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
    }
}