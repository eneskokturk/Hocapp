package com.example.hocapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hocapp.adapters.AdapterList;
import com.example.hocapp.models.LessonModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    View mView;
    Button getLessonButton;

    ArrayList<String> lessonList;    //Dersler Dizisi
    ArrayList<String> lessonFieldList;      //Ders Alanları Dizisi

    String userId;

    private AdapterList mAdapter;
    public Adapter lessonAdapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    public FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private CollectionReference collectionReference = firebaseFirestore.collection("UserLessons");


    TextView lessonName;
    TextView lessonField;
    TextView lessonPrice;
    RecyclerView recyclerView;

    String currentDocumentId;
    String currentId;

    Boolean selectedLesson;
    Boolean selectedLessonField;

    public SearchFragment() {
        // Required empty public constructor
    }

    public void ListFunc() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = mView.findViewById(R.id.listView);

        firebaseAuth = FirebaseAuth.getInstance();              // initializing
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        lessonList = new ArrayList<>();                                              // dersler listesi
        lessonFieldList = new ArrayList<>();                                         // ders alanları listesi
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseAuth.getCurrentUser().getUid();

        Button getLessonButton = mView.findViewById(R.id.getLessonButton);


        final Spinner lessonSpinner = mView.findViewById(R.id.lessonSpinner);        //dersler Spinner
        final Spinner lessonFieldSpinner = mView.findViewById(R.id.lessonFieldSpinner);      //ders alanları Spinner


        firebaseUser = firebaseAuth.getCurrentUser();    // kullanici giris yapmis ise deger döndürür ,kimse yok ise null dondurur


        getLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedLesson == false || selectedLessonField == false) {
                    Toast.makeText(getContext(), "İlan aramak için bilgileri eksiksiz giriniz.", Toast.LENGTH_SHORT).show();
                } else {
                    String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


                    collectionReference.whereEqualTo("lesson",lessonSpinner.getSelectedItem()).get()
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
                                        Log.e("xxxx", String.valueOf(mArrayList.get(i).getLessonLatLng().getLatitude()));
                                        Log.e("xxxx", String.valueOf(mArrayList.get(i).getLessonLatLng().getLongitude()));
                                    }
                                }
                            });
                }
            }
        });


        getDataFirebaseLesson();                                                              //firebaseden ders adlarını alan fonksiyon cagirildi.
        lessonSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, lessonList));  //Dersler Spinner

        lessonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedLesson = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLesson = false;
            }
        });


        getDataFirebaseLessonsField();                                                      //fireseden ders alanlarını cekecek fonksiyon cagirildi
        lessonFieldSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, lessonFieldList));       //ders alanları spinner

        lessonFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedLessonField = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLessonField = false;
            }
        });

        return mView;
    }


    public void getDataFirebaseLesson()                                 //ders adları firebaseden cekildi ve spinnerlara gönderilmek uzere degiskene atıldı
    {

        final CollectionReference collectionReference = firebaseFirestore.collection("Lessons");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();   //map olarak datayi geri cekiyoruz

                    String lessonsForDatabase = (String) data.get("lessonsDatabase");  //gelecek verinin string oldugundan emin oldugumuz icin casting islemi yapabiliyoruz

                    lessonList.add(lessonsForDatabase);

                }

            }
        });
    }

    public void getDataFirebaseLessonsField()                                                   //ders alanları firebaseden cekildi ve spinnerlara gönderilmek uzere degiskene atıldı
    {
        CollectionReference collectionReference = firebaseFirestore.collection("LessonsField");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();   //map olarak datayi geri cekiyoruz

                    String lessonsFieldForDatabase = (String) data.get("lessonsField");  //gelecek verinin string oldugundan emin oldugumuz icin casting islemi yapabiliyoruz

                    lessonFieldList.add(lessonsFieldForDatabase);
                }

            }
        });
    }


}

