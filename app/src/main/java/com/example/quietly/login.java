package com.example.quietly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ProgressBar pb;
    private String email;
    private String password;
    private Button singin;
    private EditText emailadd;
    private EditText getpassword;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        singin=findViewById(R.id.singin);
        emailadd=findViewById(R.id.Emailadd);
        getpassword=findViewById(R.id.password);
        info=findViewById(R.id.textView2);
        pb=findViewById(R.id.progressBar);
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailadd.getText().toString();
                password=getpassword.getText().toString();
                if(email.isEmpty())
                {

                    info.setText("Please Enter Email");
                    info.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                }
                else if(password.isEmpty())
                {
                    info.setText("Please Enter password");
                    info.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                }
                else {
                    signin();
                }
            }
        });


    }

    private void signin()
    {

        pb.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent=new Intent(login.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            String e;
                            e=task.getException().getMessage();
                            info.setText("Error..."+e);
                            info.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.INVISIBLE);
                        }


                    }
                });
    }
}