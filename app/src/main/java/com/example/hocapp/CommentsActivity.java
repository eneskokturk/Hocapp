package com.example.hocapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.hocapp.adapters.AdapterCommentList;
import com.example.hocapp.models.CommentsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    EditText addcomment;
    ImageView post;
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();



    RecyclerView recyclerView3;
    private FirebaseAuth firebaseAuth;
    String postid;
    String publisherid;
    String userId;
    String email;

    String commentuser;


    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private CollectionReference collectionReference=firebaseFirestore.collection("Comments");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        firebaseAuth = FirebaseAuth.getInstance();


        email=firebaseAuth.getCurrentUser().getEmail();


        recyclerView3=findViewById(R.id.recycler_view);

        userId = firebaseAuth.getCurrentUser().getUid();


        firebaseFirestore.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                 commentuser = document.getString("userName");

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });



        firebaseFirestore = FirebaseFirestore.getInstance();



        addcomment=findViewById(R.id.add_comment);
        post=findViewById(R.id.post);

        Intent intent= getIntent();


        postid= intent.getStringExtra("postid");
        publisherid=intent.getStringExtra("publisherid");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this,"Lütfen yorum giriniz",Toast.LENGTH_SHORT).show();
                }
                else{
                    addComment();
                }


                getComment();
            }
        });

        getComment();


    }


    private void getComment() {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                //Add all to your list
                List<CommentsModel> types= queryDocumentSnapshots.toObjects(CommentsModel.class);

                ArrayList<CommentsModel> commentModelArrayList= new ArrayList<CommentsModel>();
                commentModelArrayList.addAll(types);

                AdapterCommentList adapterCommentList= new AdapterCommentList(commentModelArrayList,getApplicationContext());


                LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                recyclerView3.setHasFixedSize(true);
                recyclerView3.setLayoutManager(layoutManager);

                recyclerView3.setAdapter(adapterCommentList);
                for(int i=0;i<commentModelArrayList.size();i++){
                    //  Log.e("xxxx",forumArrayList.get(i).getDate().toString());

                    Log.e("xxxx",commentModelArrayList.get(i).getCommentUser());
                    Log.e("xxxx",commentModelArrayList.get(i).getComment());
                    Log.e("xxxx", commentModelArrayList.get(i).getEmail());

                    // Log.e("xxxx",forumArrayList.get(i).getLessonUserEmail());
                }

            }
        });
    }



    private void addComment(){



        HashMap<String, Object> hashMap= new HashMap<>();

        hashMap.put("commentUser",commentuser);
        hashMap.put("email",email);
        hashMap.put("comment",addcomment.getText().toString());
        hashMap.put("publisher",userId);

        firebaseFirestore.collection("Comments").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                addcomment.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });


    }
}
