package com.example.hocapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hocapp.MainActivity;
import com.example.hocapp.MessageActivity;
import com.example.hocapp.R;
import com.example.hocapp.models.LessonModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.google.gson.internal.$Gson$Preconditions;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter {
    public ArrayList arrayList;
    public Context context;

    private FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;





    // RecyclerView recyclerView;
    public AdapterList(ArrayList listdata, Context context) {

        this.arrayList = listdata;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();




    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View list = MainActivity.inflater.inflate(R.layout.lesson_card_item, parent, false);
        return new listItem(list);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final listItem listItem = (AdapterList.listItem) holder;
        final LessonModel list = (LessonModel) arrayList.get(position);

        listItem.userName.setText("Ders Veren: "+list.getLessonUsername());
        listItem.lessonName.setText(list.getLesson()+"  -");
        listItem.lessonField.setText(list.getLessonField());
        listItem.lessonPrice.setText("Saatlik ders ücreti: "+list.getLessonPrice()+" ₺");
        listItem.lessonCity.setText("Bulunduğu İl: "+list.getLessonCity());




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid",list.getUserid());
                intent.putExtra("lessonUsername",list.getLessonUsername());
                System.out.println(list.getLessonUsername());
                context.startActivity(intent);

            }
        });


    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class listItem extends RecyclerView.ViewHolder{

        public TextView userName;
        public TextView lessonName;
        public TextView lessonField;
        public TextView lessonPrice;
        public ImageView image_delete;
      //  public TextView lessonLatLng;
        public TextView lessonCity;


        public listItem(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.lessonUsername);
            lessonName = itemView.findViewById(R.id.lessonName);
            lessonField = itemView.findViewById(R.id.lessonField);
            lessonPrice = itemView.findViewById(R.id.lessonPrice);
        //    image_delete=itemView.findViewById(R.id.image_delete);
          //  lessonLatLng=itemView.findViewById(R.id.lessonLatLng);
            lessonCity=itemView.findViewById(R.id.lessonCity);


        }
    }

}
