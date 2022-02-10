package com.example.quietly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation;
//import com.tom_roush.pdfbox.pdmodel.PDDocument;
//import com.tom_roush.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class uploadpdf extends AppCompatActivity {
    TextView pdfname,pdftotalpage,totalpage,totalpn;
    Button upload;
    Uri filepath;

    int total;
    ProgressBar progressBarpdf;
    ImageView selectimage,cancel;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String path;
    PDFView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpdf);
        totalpn=findViewById(R.id.totalpn);
        preview=findViewById(R.id.preview);
        /////////////////////////////geting data
        totalpage=findViewById(R.id.totalpage);
        pdftotalpage=findViewById(R.id.pdftotalpage);
        pdfname=findViewById(R.id.pdfname);
        //////////////////////////////geting image and button
        cancel=findViewById(R.id.cancel);
        upload=findViewById(R.id.upload);
        selectimage=findViewById(R.id.selectimage);

        //////////////////////////////////////

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("documents");

        /////////////////////////onimageclick
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override

                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
                cancel.setVisibility(View.VISIBLE);

                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            }
        });
cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        progressBarpdf.setVisibility(View.INVISIBLE);
        selectimage.setImageDrawable(ContextCompat.getDrawable(uploadpdf.this, R.drawable.ic_upload));
      cancel.setVisibility(View.INVISIBLE);
        pdftotalpage.setText("");
        pdfname.setText("");


    }
});

upload.setOnClickListener(new View.OnClickListener() {


    @Override
            public void onClick(View v) {

                processupload(filepath);

              path=filepath.getPath();
                Log.v("xya", path);



    }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            selectimage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pdf));

            preview.fromUri(filepath).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    Log.d("count", String.valueOf(nbPages));
                    totalpn.setText(""+nbPages);
                }
            }).load();
        }
    }

    public void processupload(final Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();

        final StorageReference reference=storageReference.child("pdf/"+ pdfname.getText().toString()+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {


                                HashMap<String,Object> map = new HashMap<>();
                                map.put("filename",pdfname.getText().toString());
                                map.put("totalpage",pdftotalpage.getText().toString());
                                map.put("fileurl",uri.toString());




                                FirebaseDatabase.getInstance().getReference()
                                        .child("pdf")
                                        .push()
                                        .setValue(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(uploadpdf.this,showpdf.class);
                                                intent.putExtra("uripdf",filepath.toString()); // getText() SHOULD NOT be static!!!
                                                startActivity(intent);
                                                Log.d("path",filepath.toString());
                                                Toast.makeText(getApplicationContext(),filepath.toString(),Toast.LENGTH_LONG).show();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBarpdf.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(),"Upload error",Toast.LENGTH_LONG).show();
                                            }
                                        });

                                pd.dismiss();



                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });
    }


}