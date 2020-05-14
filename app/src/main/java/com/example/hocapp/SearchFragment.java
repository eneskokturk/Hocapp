package com.example.hocapp;

import android.Manifest;
import android.content.Context;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
public class SearchFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;


    MapView mMapView;
    View mView;
    LocationManager locationManager;
    LocationListener locationListener;
    Button getLessonButton;

    ArrayList<String> lessonList;    //Dersler Dizisi
    ArrayList<String> lessonFieldList;      //Ders Alanları Dizisi

    String userId;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    public FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private CollectionReference collectionReference = firebaseFirestore.collection("UserLessons");



    ArrayList<String> lessonUserEmailArrayList;

     ArrayList<String> lessonArrayDB;
     ArrayList<String> lessonFieldArrayDB;
     ArrayList<String> lessonPriceArrayDB;
     ArrayList<String> lessonLatLngArrayDB;
     ArrayList<String> lessonUserEmailArrayDB;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);

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

        lessonArrayDB =new ArrayList<>();
        lessonFieldArrayDB =new ArrayList<>();
        lessonPriceArrayDB =new ArrayList<>();
        lessonLatLngArrayDB =new ArrayList<>();
        lessonUserEmailArrayDB =new ArrayList<>();

        firebaseUser = firebaseAuth.getCurrentUser();    // kullanici giris yapmis ise deger döndürür ,kimse yok ise null dondurur



        getLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String lessonDatabase=lessonSpinner.getSelectedItem().toString();       //Spinnerdan secilen ders adı String olarak tutuldu.
                String lessonFieldDatabase=lessonFieldSpinner.getSelectedItem().toString();  //Spinnerdan secilen ders alanı String olarak tutuldu.

                collectionReference.whereEqualTo("lesson",lessonDatabase).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e !=null){
                            Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();    //eger firebasefirestoredan data okunanamazsa exception e yi göster
                            Toast.makeText(getContext(), "Uygun ilan bulunamadı.", Toast.LENGTH_SHORT).show();
                        }

                        if(queryDocumentSnapshots!=null)
                        {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments())
                            {
                                Map<String,Object> data= snapshot.getData();   //map olarak datayi geri cekiyoruz

                                String lesson =(String) data.get("lesson");  //gelecek verinin string oldugundan emin oldugumuz icin casting islemi yapabiliyoruz
                                String lessonField =(String) data.get("lessonField");
                                String lessonPrice =(String) data.get("lessonPrice");
                               // String lessonLatLng=(String) data.get("lessonLatLng");
                                String lessonUserEmail=(String) data.get("lessonUserEmail");


                                lessonArrayDB.add(lesson);
                                lessonFieldArrayDB.add(lessonField);
                                lessonPriceArrayDB.add(lessonPrice);
                                //lessonLatLngArrayDB.add(lessonLatLng);
                                lessonUserEmailArrayDB.add(lessonUserEmail);



                            }

                        }


                    }
                });


            }
        });

        getDataFirebaseLesson();                                                              //firebaseden ders adlarını alan fonksiyon cagirildi.
        lessonSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, lessonList));  //Dersler Spinner

        lessonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println(lessonList.get(position));       //Spinner 0 konumunda değil. Seçim yapıldı.

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getDataFirebaseLessonsField();                                                      //fireseden ders alanlarını cekecek fonksiyon cagirildi
        lessonFieldSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, lessonFieldList));       //ders alanları spinner

        lessonFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //System.out.println(lessonFieldList.get(position));         //Spinner 0 konumunda değil. Seçim yapıldı.

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.mapViewSearchLayout);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
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

    public void getLessonFirebase()
    {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        MapsInitializer.initialize(getContext());   //initilalize

        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);             //Zoom control

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);                                //My current Location Button

        locationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else
        {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,locationListener);// 5000 ms veya 20 metre konum güncelle
            Location lastLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation!=null)                                      //son lokasyon null degilse  bilinen son lokasyonu goster
            {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,18));
                System.out.println("last known location");
            }
        }
    }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {       //kullanici izinleri verdiginde

                if(grantResults.length>0)           //izinler dizisi bos degil ise
                {
                    if(requestCode==1)              //requestCode 1 ise yani access fine location icin kullandigimiz degere esit ise
                    {
                        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)  //izin verildiyse
                        {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,20,locationListener);

                            Location lastLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(lastLocation!=null)                                      //son lokasyon null degilse  bilinen son lokasyonu goster
                            {
                                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,18));
                                System.out.println("last known location");
                            }
                        }
                    }
                }

                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
