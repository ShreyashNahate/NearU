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
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class register extends AppCompatActivity {



    ImageView uploadImage;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;

    ImageButton imageButton;
    EditText Name, Mobile, Whatsapp, Email, Pass, Address, City;
    TextView button;
    String imageURL;
    Uri uri;


    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = findViewById(R.id.name);
        Mobile = findViewById(R.id.mobile);
        Whatsapp = findViewById(R.id.whatsapp);
        Email = findViewById(R.id.email);
        Pass = findViewById(R.id.pass);
        Address = findViewById(R.id.address);
        City = findViewById(R.id.city);
        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        uploadImage = findViewById(R.id.imageView);
        imageButton = findViewById(R.id.registerbutton);

        ProgressBar pbar = (ProgressBar) findViewById(R.id.pbar);


        mAuth = FirebaseAuth.getInstance();

        button = findViewById(R.id.signuptextView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(register.this, "No Image Selected", Toast.LENGTH_SHORT).show();
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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        String email, pass;

                        pass = Pass.getText().toString();
                        email = "S" + Email.getText().toString();
                        String NName = Name.getText().toString();
                        String MMobile = Mobile.getText().toString();
                        String WWhatsapp = Whatsapp.getText().toString();
                        String AAddress = Address.getText().toString();
                        String CCity = City.getText().toString();

                        if (uploadImage.getDrawable()==null) {
                            Toast.makeText(register.this, "Upload Image", Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.GONE);
                            return;
                        }

                        if (TextUtils.isEmpty(pass)) {
                            Toast.makeText(register.this, "Enter password", Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.GONE);
                            return;
                        }

                        if (TextUtils.isEmpty(NName)) {
                            Toast.makeText(register.this, "Enter name", Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.GONE);
                            return;
                        }

                        if (TextUtils.isEmpty(MMobile)) {
                            Toast.makeText(register.this, "Enter mobile", Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.GONE);
                            return;
                        }

                        if (TextUtils.isEmpty(WWhatsapp)) {
                            Toast.makeText(register.this, "Enter whatsapp no", Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.GONE);
                            return;
                        }

                        if (TextUtils.isEmpty(AAddress)) {
                            Toast.makeText(register.this, "Enter address", Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.GONE);
                            return;
                        }

                        if (TextUtils.isEmpty(CCity)) {
                            Toast.makeText(register.this, "Enter city", Toast.LENGTH_SHORT).show();
                            pbar.setVisibility(View.GONE);
                            return;
                        }
                        mAuth.createUserWithEmailAndPassword(email, pass)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {
                                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                                    saveData();
                                                } else {
                                                    Toast.makeText(register.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                            }

                });


        }


    public void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Seller")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
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
                dialog.dismiss();
            }
        });
    }

    public void uploadData() {

        String uid = null;
        try {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            uid = firebaseUser.getUid();
        } catch (Exception e) {
        }
        String NName = Name.getText().toString();
        String MMobile = Mobile.getText().toString();
        String WWhatsapp = Whatsapp.getText().toString();
        String EEmail = Email.getText().toString();
        String PPass = Pass.getText().toString();
        String AAddress = Address.getText().toString();
        String CCity = City.getText().toString();

        SellerReadData dataClass = new SellerReadData(imageURL,NName, MMobile, WWhatsapp,EEmail,PPass,AAddress,CCity);


        FirebaseDatabase.getInstance().getReference("Registered Seller").child(uid)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(register.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

