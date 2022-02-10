package com.example.quietly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class viewpdf extends AppCompatActivity {
    TextView mypdfname,pdftotalpage;
    WebView pdfview;
    int aa;
    String id;
    Button downloadpdf;
    String pdftotalpagecount;
    String pdfuri;
    String pdfname;
    String Actualfilename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpdf);

        final String fileurl=getIntent().getStringExtra("fileurl");
        id=getIntent().getStringExtra("pdfid");


////////////////////////////////////////////////IMPORTING INFORMATION
        mypdfname=findViewById(R.id.mypdfname);
        pdftotalpage=findViewById(R.id.pdftotalpage);
      FirebaseDatabase.getInstance().getReference()
              .child("pdf")
              .child(id)
              .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              pdfname = dataSnapshot.child("filename").getValue(String.class);
              pdfuri = dataSnapshot.child("fileurl").getValue(String.class);
              pdftotalpagecount = dataSnapshot.child("totalpage").getValue(String.class);

              pdftotalpage.setText(pdftotalpagecount);

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }

      });
////////////////////////reducing filename
//        final String myfilename=pdfname.substring(0,pdfname.length()-4);

      ////////////////////////////////////////////////Button to download
        downloadpdf=findViewById(R.id.downloadpdf);
        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(pdfuri))
                        .setTitle(pdfname+".pdf")
                        .setDescription("FILE IS DOWNLOADING")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                downloadManager.enqueue(request);


            }
        });


        //////////////////////////WEB VIEW
        pdfview=(WebView)findViewById(R.id.viewpdf);
        pdfview.getSettings().setJavaScriptEnabled(true);


        String filename=getIntent().getStringExtra("filename");
        final ProgressDialog pd=new ProgressDialog(this);




        pd.setTitle(filename);
        pd.setMessage("Opening....!!!");

        pdfview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        String url="";
        try {
            url= URLEncoder.encode(fileurl,"UTF-8");
        }catch (Exception ex)
        {}

        pdfview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
//////////////////////////////////////////////////////////////////mypdfname
        mypdfname=findViewById(R.id.mypdfname);
        mypdfname.setText(filename);

    }
}