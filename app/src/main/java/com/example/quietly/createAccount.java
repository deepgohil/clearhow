package com.example.quietly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.Random;

import static com.example.quietly.testactivity.hasPermissions;

public class createAccount extends AppCompatActivity {
    final int PERMISSION_ALL = 100;
    private ImagePicker imagePicker;
    Button createaccount;
    EditText name,email,phone,password;
    TextView erromsg,textforimage;
    ProgressBar progressBar;
    ImageView imageView;
    private Uri uri;
    String currentuser;

    int realtimeudi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
/////////////////////////////////////////randomnumber
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        realtimeudi = rand.nextInt(1000);


/////////////////////////////////////////////////////////////


        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        createaccount=findViewById(R.id.createacc);
        erromsg=findViewById(R.id.errormsg);
        progressBar=findViewById(R.id.progressBar);
        textforimage=findViewById(R.id.textforimage);
        imageView=findViewById(R.id.adduserimage);



        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(createAccount.this, "Please Enter name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(email.getText().toString())){
                    Toast.makeText(createAccount.this, "Please Enter email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(createAccount.this, "Please Enter phone number", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(createAccount.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                }

                else {
                    progressBar.setVisibility(View.VISIBLE);

                    register();
                }

            }
        });

        //////////////////////////////////////////////////////////////////for image

        final String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasPermissions(createAccount.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(createAccount.this, PERMISSIONS, PERMISSION_ALL);

                }
                else {
                    imagePicker.choosePicture(true /*show camera intents*/);
                }
            }
        });
        imagePicker = new ImagePicker(this, /* activity non null*/
                null, /* fragment nullable*/
                new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        textforimage.setVisibility(View.INVISIBLE);
                        UCrop.of(imageUri, getTempUri())
                                .withAspectRatio(1, 1)
                                .start(createAccount.this);
                    }
                });

    }

////////////////////image

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private Uri getTempUri(){
    String dri = Environment.getExternalStorageDirectory()+ File.separator+"Temp";
    File dirFile = new File(dri);
    dirFile.mkdir();

    String file = dri+File.separator+"temp.png";
    File tempFile = new File(file);
    try {
        tempFile.createNewFile();
    } catch (IOException e) {
        e.printStackTrace();
    }

    return Uri.fromFile(tempFile);
}

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:{
                if (grantResults.length > 0) {
                    boolean b = true;
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            b=false;

                        }

                    }
                    if(b){
                        imagePicker.choosePicture(true /*show camera intents*/);
                    }

                }
                return;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            uri = UCrop.getOutput(data);
            imageView.setImageURI(null);
            imageView.setImageURI(uri);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.i("dsjknjsdkn", "onActivityResult: "+cropError.getMessage());
        }
    }

    /////////////////////////upload

    void upload(){

        final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("userimage").child(name.getText().toString()+ ".png");
        riversRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressBar.setVisibility(View.GONE);
                Log.i("dscgjdshv", "onFailure: "+exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        insertUserInfo(uri);
                    }
                });

            }
        });
    }
    private void insertUserInfo(Uri uri) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("email",email.getText().toString());
        map.put("name",name.getText().toString());
        map.put("phone",phone.getText().toString());
        map.put("password",password.getText().toString());
        map.put("image",uri.toString());

        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(createAccount.this,MainActivity.class));
                          finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        erromsg.setVisibility(View.VISIBLE);
                        erromsg.setText(e.toString());
                    }
                });
    }


    //////////////////////////////////////////////////extra bottom

//    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    void  register()
    {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                upload();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(createAccount.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }



}