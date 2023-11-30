//package com.example.seller;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.text.DateFormat;
//import java.util.Calendar;
//
//public class UploadActivity extends AppCompatActivity {
//
//    ImageView uploadImage;
//    Button saveButton;
//    FirebaseAuth auth;
//    FirebaseUser user;
//    EditText dataPrice ,dataLocation,dataNumber,dataDescription , dataCategory;
//    String imageURL;
//    Uri uri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload);
//
//        Intent intent = getIntent();
//        Bundle bundle = getIntent().getExtras();
//
//        if (bundle != null){
//            String x = bundle.getString("Number");
//            String y = bundle.getString("Whatsapp");
//            String z = bundle.getString("Location");
//        }
//
//        dataPrice = findViewById(R.id.Price);
//        dataCategory = findViewById(R.id.Category);
//        dataDescription = findViewById(R.id.Description);
//        uploadImage = findViewById(R.id.uploadImage);
//        saveButton = findViewById(R.id.saveButton);
//
//        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK){
//                            Intent data = result.getData();
//                            uri = data.getData();
//                            uploadImage.setImageURI(uri);
//                        } else {
//                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//        );
//
//        uploadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent photoPicker = new Intent(Intent.ACTION_PICK);
//                photoPicker.setType("image/*");
//                activityResultLauncher.launch(photoPicker);
//            }
//        });
//
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveData();
//            }
//        });
//    }
//
//    public void saveData(){
//
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Seller")
//                .child(uri.getLastPathSegment());
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!uriTask.isComplete());
//                Uri urlImage = uriTask.getResult();
//                imageURL = urlImage.toString();
//                uploadData();
//                dialog.dismiss();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(UploadActivity.this, "failed", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//    }
//
//    public void uploadData() {
//
//        Intent intent = getIntent();
//        Bundle bundle = getIntent().getExtras();
//
//        String location = null;
//        String number = null;
//        String whatsapp = null;
//        if (bundle != null) {
//            number = bundle.getString("Number");
//            whatsapp = bundle.getString("Whatsapp");
//            location = bundle.getString("Location");
//        }
//        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//
//        String price = dataPrice.getText().toString();
//        String description = dataDescription.getText().toString();
//        String category = dataCategory.getText().toString();
//
//        DataClass dataClass = new DataClass(imageURL, price, location, number, whatsapp, description, category,currentDate);
//
//
//
//
//
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//
//        String uid = user.getUid();
//        DatabaseReference myref1 = FirebaseDatabase.getInstance().getReference("Registered Seller").child(uid).child(uid).child(currentDate);
//        DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Products").child(currentDate);
//
//
//
//        myref1.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//        myref2.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                   Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//            Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//        }
//    });
//}
//
//}


package com.example.nearuseller;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {
    Bitmap bitmap;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte [] file_bytes;
    ImageView uploadImage;
    Button saveButton;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText dataPrice ,dataDescription , dataCategory;
    String imageURL;
    Uri uri;
    TextView rent,sell,sellicon,renticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            String x = bundle.getString("Number");
            String y = bundle.getString("Whatsapp");
            String z = bundle.getString("Location");
        }

        dataPrice = findViewById(R.id.Price);
        dataCategory = findViewById(R.id.Category);
        dataDescription = findViewById(R.id.Description);
        uploadImage = findViewById(R.id.uploadImage);
        saveButton = findViewById(R.id.saveButton);
        sell = findViewById(R.id.sell);
        sellicon = findViewById(R.id.sellicon);
        rent = findViewById(R.id.rent);
        renticon = findViewById(R.id.renttick);

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renticon.setText("✅");
                sellicon.setText("");
                rent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                sell.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renticon.setText("");
                sell.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                rent.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                sellicon.setText("✅");
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            try {
                                InputStream inputStream=getContentResolver().openInputStream(uri);
                                bitmap = BitmapFactory.decodeStream(inputStream);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                file_bytes = byteArrayOutputStream.toByteArray();
                            } catch (Exception e) {
                            }
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Seller")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "failed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void uploadData() {

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();

        String location = null;
        String number = null;
        String whatsapp = null;
        if (bundle != null) {
            number = bundle.getString("Number");
            whatsapp = bundle.getString("Whatsapp");
            location = bundle.getString("Location");
        }
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        String price = dataPrice.getText().toString();
        String description = dataDescription.getText().toString();
        String category = dataCategory.getText().toString();

        sell = findViewById(R.id.sell);
        sellicon = findViewById(R.id.sellicon);
        rent = findViewById(R.id.rent);
        renticon = findViewById(R.id.renttick);
        String datatype = "Sell";

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renticon.setText("");
                sell.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                rent.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                sellicon.setText("✅");
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellicon.setText("");
                rent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                sell.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                renticon.setText("✅");
            }
        });

        if (renticon.getText().toString()=="✅"){
            datatype=rent.getText().toString();
        }else {
            datatype=sell.getText().toString();
        }

        DataClass dataClass = new DataClass(imageURL, price, location, number, whatsapp, description, category,currentDate, datatype);





        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String uid = user.getUid();
        DatabaseReference myref1 = FirebaseDatabase.getInstance().getReference("Registered Seller").child(uid).child(uid).child(currentDate);
        DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Products").child(currentDate);

        new serverthread().send(imageURL);

        myref1.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        myref2.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
                client = new Socket("34.100.220.209",5555);
                out = client.getOutputStream();
                in = client.getInputStream();
                out.write("upload_product".getBytes(StandardCharsets.UTF_8));
                in.read();
                out.write(uri.getBytes(StandardCharsets.UTF_8));
                in.read();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                     }
                });
                out.write(file_bytes);
                out.write("<END>".getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.close();
                client.close();
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadActivity.this, "error"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        public void send(String uri){
            this.uri = uri;
            run();
        }
    }
}