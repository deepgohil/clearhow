package com.example.quietly;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class adapterpdf extends FirebaseRecyclerAdapter<modelofpdf,adapterpdf.myviewholder> {


    public adapterpdf(@NonNull FirebaseRecyclerOptions<modelofpdf> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final modelofpdf model) {

        holder.nameofpdf.setText(model.getFilename());
        holder.totalpageinpdf.setText(model.getTotalpage());
        holder.pdfuri.setText(model.getFileurl());

holder.nameofpdf.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(holder.nameofpdf.getContext(),viewpdf.class);
        String id=getRef(position).getKey();
        intent.putExtra("filename",model.getFilename());
        intent.putExtra("fileurl",model.getFileurl());
        intent.putExtra("pdfid",id);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        holder.nameofpdf.getContext().startActivity(intent);
    }
});

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf,parent,false);
        return  new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {


        TextView nameofpdf,totalpageinpdf,pdfuri;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            nameofpdf=itemView.findViewById(R.id.nameofpdf);
            totalpageinpdf=itemView.findViewById(R.id.totalpageinpdf);
            pdfuri=itemView.findViewById(R.id.pdfuri);


        }
    }

}
