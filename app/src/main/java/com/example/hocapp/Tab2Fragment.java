package com.example.hocapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hocapp.adapters.AdapterList;
import com.example.hocapp.adapters.AdapterMyLessonList;
import com.example.hocapp.models.LessonModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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






        getMyUserLessons();







        return view;
    }

    private void getMyUserLessons() {



        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        System.out.println(currentEmail);

        collectionReference.whereEqualTo("lessonUserEmail", currentEmail).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        // Add all to your list
                        List<LessonModel> types = queryDocumentSnapshots.toObjects(LessonModel.class);

                        ArrayList<LessonModel> mArrayList = new ArrayList<LessonModel>();
                        mArrayList.addAll(types);


                        AdapterMyLessonList adapter = new AdapterMyLessonList(mArrayList, getActivity());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        for (int i = 0; i < mArrayList.size(); i++) {
                            Log.e("xxxx", mArrayList.get(i).getLesson());
                            Log.e("xxxx", mArrayList.get(i).getLessonField());
                            Log.e("xxxx", mArrayList.get(i).getLessonPrice());
                            Log.e("xxxx", mArrayList.get(i).getLessonUserEmail());
                            Log.e("xxxx", mArrayList.get(i).getLessonCity());
                        }
                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        getMyUserLessons();
    }


    }




