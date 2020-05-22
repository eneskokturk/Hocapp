
package com.example.hocapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hocapp.CommentsActivity;
import com.example.hocapp.MainActivity;
import com.example.hocapp.R;
import com.example.hocapp.models.ForumModel;
import com.example.hocapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;


import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class AdapterForumList extends RecyclerView.Adapter {
    public ArrayList arrayList;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseUser firebaseUser;

    // RecyclerView recyclerView;
    public AdapterForumList(ArrayList listdata, Context context) {

        this.arrayList = listdata;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View list = MainActivity.inflater.inflate(R.layout.forum_card_item, parent, false);
        return new listItem(list);




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final listItem listItem = (AdapterForumList.listItem) holder;
        final ForumModel list = (ForumModel) arrayList.get(position);

        listItem.forumTitle.setText(list.getForumTitle());

        // listItem.forumContent.setText(list.getForumContent());
        // listItem.contentDate.setText(list.getDate());


        listItem.forumTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, CommentsActivity.class);

                context.startActivity(intent);
            }
        });

        /*
        String publisher=((ForumModel) arrayList.get(position)).getPublisher();

        firebaseFirestore.collection("Users").document("userid").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String userName=task.getResult().getString("userName");

                    listItem.publisher.setText(userName);
                }
                else{
                    //Firebase Exception
                }

            }
        });


         */
    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class listItem extends RecyclerView.ViewHolder{

        public TextView forumTitle;
        public TextView forumContent;
        public TextView publisher;
        public TextView comments;
        // public TextView contentDate;




        public listItem(@NonNull View itemView) {
            super(itemView);
            forumTitle = itemView.findViewById(R.id.forumTitle);
            forumContent = itemView.findViewById(R.id.forumContent);
            publisher=itemView.findViewById(R.id.publisher);
            comments=itemView.findViewById(R.id.comments);

            //  contentDate = itemView.findViewById(R.id.contentDate);





        }
    }

}
