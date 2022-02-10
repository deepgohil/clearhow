package com.example.quietly;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class userFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    String mail;
    String image;
    String name;
    String phone;
TextView username,userphonenumber,useremail;
    View view;
    CircleImageView profile_image;
    Button sign_out;
    public userFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_user, container, false);


        ///////////////////////////////findingview
        useremail=view.findViewById(R.id.useremail);
        userphonenumber=view.findViewById(R.id.userphonenumber);
        username=view.findViewById(R.id.username);
        profile_image=view.findViewById(R.id.profile_image);
        sign_out=view.findViewById(R.id.sign_out);

        ////////////////////////////////////////signout
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               firebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(),
                        createAccount.class);
                getActivity().finish();
            }
        });

        ////////////////////////////////////////////////////// data load
                FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                mail = dataSnapshot.child("email").getValue(String.class);
                                 image = dataSnapshot.child("image").getValue(String.class);
                                 name = dataSnapshot.child("name").getValue(String.class);
                                phone = dataSnapshot.child("phone").getValue(String.class);


                                Log.wtf("TAG", mail+image+name+phone);


                                username.setText(name);
                                useremail.setText(mail);
                                userphonenumber.setText(phone);

/////////////////////////////////////////////image load

                                Picasso.get().load(image).into(profile_image);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



        // Inflate the layout for this fragment
        return view;
    }
}