package com.example.hocapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.BreakIterator;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
        TextView text;


    }

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        TextView textView1 = view.findViewById(R.id.text);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore =FirebaseFirestore.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                // Eğer geçerli bir kullanıcı oturumu yoksa LoginActivity e geçilir.
                // Oturum kapatma işlemi yapıldığında bu sayede LoginActivity e geçilir.

            }
        };

        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();


        textView1.setText(user.getEmail());




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
