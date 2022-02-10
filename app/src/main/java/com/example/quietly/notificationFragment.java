package com.example.quietly;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class notificationFragment extends Fragment {
    View persnoluseview;
    RecyclerView recyclepdf;

    adapterpdf adapter;
    View view;
    public notificationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_notification, container, false);

        persnoluseview=view.findViewById(R.id.persnoluseview);

        persnoluseview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),uploadpdf.class));
            }
        });
////////////////////////////////////////recyclepdf
        recyclepdf=view.findViewById(R.id.recyclepdf);
        recyclepdf.setLayoutManager(new LinearLayoutManager(view.getContext()));

        FirebaseRecyclerOptions<modelofpdf> options =
                new FirebaseRecyclerOptions.Builder<modelofpdf>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("pdf"), modelofpdf.class)
                        .build();

        adapter=new adapterpdf(options);
        recyclepdf.setAdapter(adapter);
//////////////////////////////////////////////////////////////////


        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}