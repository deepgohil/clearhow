package com.example.quietly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class testactivity extends AppCompatActivity {
    private EditText productprice,productname;
    Button upload;
    ImageView imageView;
    final int PERMISSION_ALL = 100;
    ProgressBar progressBar;

    private ImagePicker imagePicker;

    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testactivity);

/////////////geting progress bar
        progressBar=findViewById(R.id.progressBar);

        /////////////////////getingprice and productleft name
        productprice=findViewById(R.id.productprice);
        productname=findViewById(R.id.productname);

        upload=findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(productprice.getText().toString())){
                    Toast.makeText(testactivity.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(productname.getText().toString())){
                    Toast.makeText(testactivity.this, "Please Enter name", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    upload();

                }
            }
        });

////////////////////////////////////////////////////for image dont touch/////////////////////////////
        final String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };

        imageView=findViewById(R.id.selectimage);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(testactivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(testactivity.this, PERMISSIONS, PERMISSION_ALL);
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
                        UCrop.of(imageUri, getTempUri())
                                .withAspectRatio(1, 1)
                                .start(testactivity.this);
                    }
                });

        ///////////////////////////////////dont touch code//////////////////////////////////////////////////////////////////////
    }

    ///////////////////////////////////dont touch code//////////////////////////////////////////////////////////////////
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


    ///////////////////////////////////dont touch code/////////////////////////////////////////////////////////////////////////////////
    void upload(){

        final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("Temp/" + System.currentTimeMillis() + ".png");
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
        map.put("productnameone",productname.getText().toString());
        map.put("productpriceone",productprice.getText().toString());
        map.put("image",uri.toString());

        FirebaseDatabase.getInstance().getReference()
                .child("productleft")
                .child("canteen")
                .push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(testactivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("dscgjdshv", "onFailure: "+e.getMessage());
                    }
                });
    }

}