package com.example.hocapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.hocapp.adapters.AdapterList;
import com.example.hocapp.models.LessonModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class Tab2Fragment extends Fragment {


    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private CollectionReference collectionReference = firebaseFirestore.collection("UserLessons");


    RecyclerView recyclerView;


    public Tab2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        recyclerView = view.findViewById(R.id.listView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


<<<<<<< HEAD



        getUserLessons();







        return view;
    }

    private void getUserLessons() {

        String currentEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();
=======
        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
>>>>>>> 2712704604a8c37ef9ba1b0b9b3e6c69f478117b

        System.out.println(currentEmail);

        collectionReference.whereEqualTo("lessonUserEmail", currentEmail).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        // Add all to your list
                        List<LessonModel> types = queryDocumentSnapshots.toObjects(LessonModel.class);

                        ArrayList<LessonModel> mArrayList = new ArrayList<LessonModel>();
                        mArrayList.addAll(types);


                        AdapterList adapter = new AdapterList(mArrayList, getActivity());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        for (int i = 0; i < mArrayList.size(); i++) {
                            Log.e("xxxx", mArrayList.get(i).getLesson());
                            Log.e("xxxx", mArrayList.get(i).getLessonField());
                            Log.e("xxxx", mArrayList.get(i).getLessonPrice());
                            Log.e("xxxx", mArrayList.get(i).getLessonUserEmail());
                            Log.e("xxxx", mArrayList.get(i).getLessonCity());
                            //  Log.e("xxxx", String.valueOf(mArrayList.get(i).getLessonLatLng().getLatitude()));
                            // Log.e("xxxx", String.valueOf(mArrayList.get(i).getLessonLatLng().getLongitude()));
                        }
                    }
                });


<<<<<<< HEAD
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserLessons();
    }
=======
//        collectionReference.whereEqualTo("lessonUserEmail",currentEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//                if (e !=null){
//                    Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();    //eger firebasefirestoredan data okunanamazsa exception e yi göster
//                }
//                if(queryDocumentSnapshots!=null)
//                {
//                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots)
//                    {
//                        currentDocumentId =documentSnapshot.getId();
//                        System.out.println("Current Document Id : "+currentDocumentId.toString());
//
//                        firebaseFirestore.collection("UserLessons").document(currentDocumentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                                lessonName.setText(documentSnapshot.getString("lesson"));
//                                lessonPrice.setText(documentSnapshot.getString("lessonPrice"));
//                                lessonField.setText(documentSnapshot.getString("lessonField"));
//
//                            }
//                        });
//                    }
//                }
//            }
//        });


        return view;
    }


>>>>>>> 2712704604a8c37ef9ba1b0b9b3e6c69f478117b
}

