package com.example.quietly;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**

 */
public class canteenFragment extends Fragment {

View view;
RecyclerView recycleviewofcanteenleft;
adapterofrecycleviewofcanteenleft adapter;

    public canteenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_canteen, container, false);
        /////////////////////geting recycler view
        recycleviewofcanteenleft=view.findViewById(R.id.recycleviewofcanteenleft);

        recycleviewofcanteenleft.setLayoutManager(new LinearLayoutManager(getContext()));


////////////////////////////////////////////seting QUERY
        FirebaseRecyclerOptions<modelofcanteenleft> options =
                new FirebaseRecyclerOptions.Builder<modelofcanteenleft>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("product").child("canteen"), modelofcanteenleft.class)
                        .build();


        adapter=new adapterofrecycleviewofcanteenleft(options);
        recycleviewofcanteenleft.setAdapter(adapter);

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