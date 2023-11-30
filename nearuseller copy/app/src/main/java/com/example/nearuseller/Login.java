package com.example.nearuseller;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override

    public void onStart () {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editText = (EditText) findViewById(R.id.editTextText);
        EditText editText2 = (EditText) findViewById(R.id.editTextText2);
        ImageButton button = (ImageButton) findViewById(R.id.registerbutton);
        ProgressBar pbar = (ProgressBar) findViewById(R.id.pbar);
        TextView textView2 = (TextView) findViewById(R.id.signuptextView2);
        mAuth = FirebaseAuth.getInstance();



        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),register.class);
                startActivity(intent);
                finish();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbar.setVisibility(View.VISIBLE);
                String email,pass;
                email = "s"+editText.getText().toString();
                pass = editText2.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                    pbar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(), "Enter pass", Toast.LENGTH_SHORT).show();
                    pbar.setVisibility(View.GONE);
                    return;
                }


                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(  new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pbar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });


    }

}
