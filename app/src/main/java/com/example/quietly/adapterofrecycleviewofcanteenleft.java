package com.example.quietly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class adapterofrecycleviewofcanteenleft extends FirebaseRecyclerAdapter<modelofcanteenleft, adapterofrecycleviewofcanteenleft.AllUserViewHolder> {



    public adapterofrecycleviewofcanteenleft(@NonNull FirebaseRecyclerOptions<modelofcanteenleft> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllUserViewHolder holder, int position, @NonNull modelofcanteenleft model) {

        holder.priceofproductleft.setText(model.getProductpriceone());
        holder.nameofproductleft.setText(model.getProductnameone());
        Picasso.get().load(model.getImage()).into(holder.imageViewofproductleft);

    }

    @NonNull
    @Override
    public AllUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productleft, parent, false);
        return new AllUserViewHolder(view);
    }

    class AllUserViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewofproductleft;
        TextView priceofproductleft,nameofproductleft;


        public AllUserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewofproductleft=itemView.findViewById(R.id.imageViewofproductleft);
            nameofproductleft=itemView.findViewById(R.id.nameofproductleft);
            priceofproductleft=itemView.findViewById(R.id.priceofproductleft);
        }
    }
}
