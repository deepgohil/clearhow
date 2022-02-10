package com.example.quietly;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

public class showpdf extends AppCompatActivity {
    PDFView pdfview;
    Uri file;
    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpdf);
        Bundle extras = getIntent().getExtras();
        pdfview=findViewById(R.id.pdfView);
        total=findViewById(R.id.total);
        file = Uri.parse(extras.getString("uripdf"));

        Toast.makeText(getApplicationContext(),file.toString(),Toast.LENGTH_LONG).show();

        pdfview.fromUri(file).onLoad(new OnLoadCompleteListener() {
            @Override
            public void loadComplete(int nbPages) {
                Log.d("count", String.valueOf(nbPages));
               total.setText(""+nbPages);
            }
        }).load();

    }

}