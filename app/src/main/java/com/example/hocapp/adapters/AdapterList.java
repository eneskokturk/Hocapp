package com.example.hocapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hocapp.MainActivity;
import com.example.hocapp.R;
import com.example.hocapp.models.LessonModel;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter {
    public ArrayList arrayList;
    public Context context;

    // RecyclerView recyclerView;
    public AdapterList(ArrayList listdata, Context context) {

        this.arrayList = listdata;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View list = MainActivity.inflater.inflate(R.layout.lesson_card_item, parent, false);
        return new listItem(list);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final listItem listItem = (AdapterList.listItem) holder;
        final LessonModel list = (LessonModel) arrayList.get(position);

        listItem.lessonName.setText(list.getLesson());
        listItem.lessonField.setText(list.getLessonField());
        listItem.lessonPrice.setText(list.getLessonPrice());
        listItem.lessonLatLng.setText(String.valueOf(list.getLessonLatLng().getLatitude()+" "+String.valueOf(list.getLessonLatLng().getLongitude())));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class listItem extends RecyclerView.ViewHolder{

        public TextView lessonName;
        public TextView lessonField;
        public TextView lessonPrice;
        public TextView lessonLatLng;



        public listItem(@NonNull View itemView) {
            super(itemView);
            lessonName = itemView.findViewById(R.id.lessonName);
            lessonField = itemView.findViewById(R.id.lessonField);
            lessonPrice = itemView.findViewById(R.id.lessonPrice);
            lessonLatLng= itemView.findViewById(R.id.lessonLatLng);
        }
    }

}
