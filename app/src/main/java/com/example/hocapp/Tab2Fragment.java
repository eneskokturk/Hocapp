package com.example.hocapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */

public class Tab2Fragment extends Fragment {

    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private CollectionReference collectionReference=firebaseFirestore.collection("UserLessons");
    private DocumentReference documentReference;


    TextView lessonName;
    TextView lessonField;
    TextView lessonPrice;

    public Tab2Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tab2,container,false);

        lessonName=view.findViewById(R.id.lessonName);
        lessonPrice= view.findViewById(R.id.lessonPrice);
        lessonField=view.findViewById(R.id.lessonField);



        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        String currentEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String[] currentDocumentId = new String[1];
        String currentId;
        System.out.println(currentEmail);
        System.out.println(currentDocumentId);

        collectionReference.whereEqualTo("lessonUserEmail",currentEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e !=null){
                    Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();    //eger firebasefirestoredan data okunanamazsa exception e yi göster
                }
                if(queryDocumentSnapshots!=null)
                {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                    {
                        currentDocumentId[0] =documentSnapshot.getId();
                        System.out.println("Current Document Id : "+currentDocumentId[0].toString());

                        firebaseFirestore.collection("UserLessons").document(currentDocumentId[0]).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                lessonName.setText(documentSnapshot.getString("lesson"));
                                lessonPrice.setText(documentSnapshot.getString("lessonPrice"));
                                lessonField.setText(documentSnapshot.getString("lessonField"));

                            }
                        });


                    }
                }

            }
        });












        return view;
    }

}
