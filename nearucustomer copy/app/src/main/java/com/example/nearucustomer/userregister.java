package com.example.nearucustomer;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class userregister extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    Uri uri;
    String imageURL;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregister);

        EditText editText1 = (EditText) findViewById(R.id.editTextText1);
        EditText editText2 = (EditText) findViewById(R.id.editTextText2);
        EditText editText3 = (EditText) findViewById(R.id.editTextText3);
        EditText editText4 = (EditText) findViewById(R.id.editTextText4);
        EditText editText5 = (EditText) findViewById(R.id.editTextText5);
        ImageView uploadImage = (ImageView) findViewById(R.id.imageView);

        ImageButton buttonn = (ImageButton) findViewById(R.id.registerbutton);
        ProgressBar pbar = (ProgressBar) findViewById(R.id.pbar);
        TextView textView2 = (TextView) findViewById(R.id.signuptextView2);
        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), userLogin.class);
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
                            Toast.makeText(userregister.this, "No Image Selected", Toast.LENGTH_SHORT).show();
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
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, pass, name, number, city;

                name = editText1.getText().toString();
                number = editText2.getText().toString();
                email = "U" + editText3.getText().toString();
                pass = editText4.getText().toString();
                city = editText5.getText().toString();

                if (uploadImage.getDrawable()==null) {
                    Toast.makeText(userregister.this, "Upload Image", Toast.LENGTH_SHORT).show();
                    pbar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(userregister.this, "Enter email", Toast.LENGTH_SHORT).show();
                    pbar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(userregister.this, "Enter password", Toast.LENGTH_SHORT).show();
                    pbar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(userregister.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    pbar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(userregister.this, "Enter number", Toast.LENGTH_SHORT).show();
                    pbar.setVisibility(View.GONE);
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    saveData(name, number, email, pass, city);
                                }
                            }
                        });


            }
        });
    }

    public void saveData(String name,String number,String email,String pass,String city){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Seller")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(userregister.this);
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
                uploadData(name, number, email, pass, city);
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void uploadData(String name,String number,String email,String pass,String city) {

        String uid = null;
        try {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            uid = firebaseUser.getUid();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
        }


        String NName = name;
        String NNumber = number;
        String EEmail = email;
        String PPass = pass;
        String CCity = city;

        userReadData dataClass = new userReadData(imageURL, NName, NNumber, EEmail, PPass, CCity);


        FirebaseDatabase.getInstance().getReference("Registered Users").child(uid)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(userregister.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}