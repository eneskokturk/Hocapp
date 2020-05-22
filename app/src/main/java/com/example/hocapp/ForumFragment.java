package com.example.hocapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.hocapp.adapters.AdapterForumList;
import com.example.hocapp.models.ForumModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ForumFragment extends Fragment {
    public ForumFragment() {
        // Required empty public constructor
    }

    FloatingActionButton fab2;
    RecyclerView recyclerView2;
    String userId;
    String userFirebaseEmail;

    private AdapterForumList adapterForumList;
    public Adapter forumAdapter;
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private CollectionReference collectionReference=firebaseFirestore.collection("Forums");




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_forum,container,false);

        fab2=view.findViewById(R.id.addFab);
        recyclerView2=view.findViewById(R.id.forumView);



        firebaseAuth = FirebaseAuth.getInstance();              // initializing
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String currentEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        getForum();


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AddForumActivity.class));
            }
        });

        return view;

    }



    @Override
    public void onResume() {

        getForum();
        super.onResume();
    }

    public void getForum(){

            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    //Add all to your list
                    List<ForumModel> types= queryDocumentSnapshots.toObjects(ForumModel.class);

                    ArrayList<ForumModel> forumArrayList= new ArrayList<ForumModel>();
                    forumArrayList.addAll(types);

                    AdapterForumList adapterForumList= new AdapterForumList(forumArrayList,getActivity());
                    recyclerView2.setHasFixedSize(true);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView2.setAdapter(adapterForumList);
                    for(int i=0;i<forumArrayList.size();i++){
                        //  Log.e("xxxx",forumArrayList.get(i).getDate().toString());

                        Log.e("xxxx",forumArrayList.get(i).getForumContent());
                        Log.e("xxxx",forumArrayList.get(i).getForumTitle());
                        // Log.e("xxxx",forumArrayList.get(i).getLessonUserEmail());
                    }

                }
            });


    }
}
