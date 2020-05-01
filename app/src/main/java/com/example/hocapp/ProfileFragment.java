package com.example.hocapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.BreakIterator;
import java.util.Map;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
        TextView text;


    }

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseUser firebaseUser;
    ImageView profilePicture;
    String userType;
    String userName;
    String userGender;
    String userEmail;
    String userBiography;
    String userBirthday;
    public String userPictureUrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        TextView textView1 = view.findViewById(R.id.text);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore =FirebaseFirestore.getInstance();
        profilePicture=view.findViewById(R.id.profilePicture);


        firebaseUser = firebaseAuth.getCurrentUser();    // kullanici giris yapmis ise deger döndürür ,kimse yok ise null dondurur
        getDataUserFromFirestore();






        return view;
    }

    public void getDataUserFromFirestore()              //Giris yapan kullanici tipini firestoredan ceker. Anasayfada ögrenci veya ögretmen oldugunu anlamamıza yarar.
    {
        CollectionReference collectionReference =firebaseFirestore.collection("Users");

        collectionReference.whereEqualTo("email",firebaseUser.getEmail().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {   //firebase den gelen datayi giris yaparken kullanilan e maile göre filtreliyoruz.
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e !=null){
                    Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();    //eger firebasefirestoredan data okunanamazsa exception e yi göster
                }

                if(queryDocumentSnapshots!=null)
                {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data= snapshot.getData();   //map olarak datayi geri cekiyoruz

                        userType =(String) data.get("userType");  //gelecek verinin string oldugundan emin oldugumuz icin casting islemi yapabiliyoruz
                        userName= (String) data.get("userName");
                        userGender =(String) data.get("gender");
                        userEmail=(String) data.get("email");
                        userBiography=(String) data.get("biography");
                        userBirthday=(String) data.get("birthday");
                        userPictureUrl=(String) data.get("downloadurl");
                        Picasso.get().load(userPictureUrl).into(profilePicture);  //picasso kütüphanesi ile profil fotoğrafı firebaseden alindi ve basildi
                                                                                    //try and catch içine alarak userPictureUrl olmadıgı zamanlarda avatar bastirilabilir
                    }
                }



            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
