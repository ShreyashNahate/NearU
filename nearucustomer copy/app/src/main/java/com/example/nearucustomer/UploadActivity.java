package com.example.nearucustomer;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class UploadActivity extends AppCompatActivity {
    int finalNRead;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Bitmap bitmap;
    ImageView uploadImage  ;
    Button saveButton;

    ProgressBar pbar;
    String [] uriall = new String[3];
    Uri uri;
    int lock=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        pbar = findViewById(R.id.progressBar);
        uploadImage = findViewById(R.id.uploadImage);

        saveButton = findViewById(R.id.saveButton);

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
//                                img.setImageBitmap(bitmap); swaraj code
                                uploadImage.setImageBitmap(bitmap);
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

                            } catch (Exception e) {
                                Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
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
                Toast.makeText(UploadActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                saveData();
            }
        });
    }
    private void saveData(){
        new search_thread().start_send_img();
        while(lock==1){}
        Intent intent = new Intent(getApplicationContext(),scan.class);
        intent.putExtra("que1",uriall[0]);
        intent.putExtra("que2",uriall[1]);
        intent.putExtra("que3",uriall[2]);
//        intent.putExtra("que4",uriall[3]);
//        intent.putExtra("que5",uriall[4]);
        startActivity(intent);
        pbar.setVisibility(View.INVISIBLE);
        finish();
    }
    class search_thread implements Runnable{
        Socket client;
        InputStream in;
        OutputStream out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte [] data ;
        @Override
        public void run() {
            try {
                client = new Socket("34.100.220.209",5555);
                in = new BufferedInputStream(client.getInputStream());
                out = client.getOutputStream();
                data = new byte[1024];

                out.write("search_image".getBytes(StandardCharsets.UTF_8));
                in.read();
                out.write(byteArrayOutputStream.toByteArray());
                out.write("<END>".getBytes(StandardCharsets.UTF_8));
                //uri1
                int nRead =0;
                finalNRead = nRead;

                nRead = in.read(data,0,data.length);
                buffer.write(data,0,data.length);
                uriall[0] = new String(buffer.toByteArray());
                buffer.close();
                buffer = new ByteArrayOutputStream();
                out.write("1".getBytes());

                nRead = in.read(data,0,data.length);
                buffer.write(data,0,data.length);
                uriall[1] = new String(buffer.toByteArray());
                buffer.close();
                buffer = new ByteArrayOutputStream();
                out.write("1".getBytes());

                nRead = in.read(data,0,data.length);
                buffer.write(data,0,data.length);
                uriall[2] = new String(buffer.toByteArray());
                buffer.close();
                buffer = new ByteArrayOutputStream();
                out.write("1".getBytes());

//                nRead = in.read(data,0,data.length);
//                buffer.write(data,0,data.length);
//                uriall[3] = new String(buffer.toByteArray());
//                buffer.close();
//                buffer = new ByteArrayOutputStream();
//                out.write("1".getBytes());
//
//                nRead = in.read(data,0,data.length);
//                buffer.write(data,0,data.length);
//                uriall[4] = new String(buffer.toByteArray());
//                buffer.close();
//                buffer = new ByteArrayOutputStream();
//                out.write("1".getBytes());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                lock = 0;
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }
        void start_send_img(){
            run();
        }
    }
}