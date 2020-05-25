package com.example.hocapp;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.hocapp.adapters.MessageAdapter;
import com.example.hocapp.models.ChatModel;
import com.example.hocapp.models.LessonModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {


    ImageView profile_image;
    TextView messageUsername;

    MessageAdapter messageAdapter;

    ImageView btn_send;
    EditText text_send;


    RecyclerView recyclerView;

    private DocumentReference ref;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("Chats");
    private FirebaseAuth firebaseAuth;
    String userId;


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profile_image = findViewById(R.id.profile_image);
        messageUsername = findViewById(R.id.messageUsername);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.txt_send);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        firebaseAuth = FirebaseAuth.getInstance();

        intent = getIntent();
        String lessonUsername = intent.getStringExtra("lessonUsername");
        final String lessonUserId = intent.getStringExtra("userid");


        messageUsername.setText(lessonUsername);

        userId = firebaseAuth.getCurrentUser().getUid();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(userId, lessonUserId, msg);
                    Toast.makeText(MessageActivity.this, "Mesaj gönderildi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessageActivity.this, "Boş mesaj gönderemezsiniz...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        readMessage(userId, lessonUserId);

    }

    private void sendMessage(String sender, String receiver, String message) {

        DocumentReference ref = firebaseFirestore.collection("Chats").document();

        final HashMap<String, Object> forumData = new HashMap<>();

        forumData.put("sender", sender);
        forumData.put("receiver", receiver);
        forumData.put("message", message);


        firebaseFirestore.collection("Chats").add(forumData);
    }

    private void readMessage(final String myid, final String userid) {

        ref = firebaseFirestore.collection("Chats").document();
       ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

           }
       });

    }




}

