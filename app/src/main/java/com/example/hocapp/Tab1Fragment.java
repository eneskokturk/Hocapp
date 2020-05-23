package com.example.hocapp;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */


public class Tab1Fragment extends Fragment implements OnMapReadyCallback {


    String userId;
    String userFirebaseName;
    String userFirebaseEmail;
    EditText lessonPriceText;           //Ders Ücreti
    Button createLessonButton;       //ilan yayınlama butonu
    GoogleMap mMap;
    MapView mMapView;
    View mView;
    LocationManager locationManager;
    LocationListener locationListener;
    EditText mapsText;
    Geocoder geocoder;
    public String address;
    LatLng userLocation;
    LatLng latLngMapButton;
    ArrayList<String> lessonList;    //Dersler Dizisi
    ArrayList<String> lessonFieldList;      //Ders Alanları Dizisi
    Boolean selectedLesson;
    Boolean selectedLessonField;
    String lessonPriceDatabase;
    String lessonCityName;
    Double userLocationLat;
    Double userLocationLng;


    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private CollectionReference collectionReference = firebaseFirestore.collection("Users");

    public Tab1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        firebaseAuth = FirebaseAuth.getInstance();              // initializing
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        final Spinner lessonSpinner = view.findViewById(R.id.lessonSpinner);
        final Spinner lessonFieldSpinner = view.findViewById(R.id.lessonFieldSpinner);
        lessonPriceText = view.findViewById(R.id.lessonPrice);
        createLessonButton = view.findViewById(R.id.createLessonButton);
        final Button mapsButton = view.findViewById(R.id.mapsButton);

        mapsText = view.findViewById(R.id.mapsText);
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        lessonList = new ArrayList<>();                                              // dersler listesi
        lessonFieldList = new ArrayList<>();                                         // ders alanları listesi
        lessonPriceDatabase = lessonPriceText.getText().toString();


        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                    //konuma git butonu
                mMap.clear();
                String location = mapsText.getText().toString();

                if (location != null && !location.equals("")) {
                    List<Address> addressList = null;
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        if (addressList != null && addressList.size() > 0) {
                            if (addressList.get(0).getThoroughfare() != null) {                                 //cadde adi
                                address += addressList.get(0).getThoroughfare();

                                if (addressList.get(0).getAdminArea() != null) {                                    //Sehir adi ve cadde adi title da gösterilmek üzere birleştirildi
                                    address += addressList.get(0).getAdminArea();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLngMapButton = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLngMapButton).title("Burası " + address.getAddressLine(0)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLngMapButton));

                    System.out.println(address.getAdminArea() + "mapsbutton");

                }

            }

        });


        createLessonButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {                //ilan olustur butonu

                                                      if (selectedLesson == false || selectedLessonField == false) {
                                                          Toast.makeText(getContext(), "İlan yayınlamak için bilgileri eksiksiz giriniz.", Toast.LENGTH_SHORT).show();
                                                      } else {
                                                          userId = firebaseAuth.getCurrentUser().getUid();
                                                          userFirebaseEmail = firebaseAuth.getCurrentUser().getEmail();


                                                          String lessonPriceDatabase = lessonPriceText.getText().toString();            //Ders ücreti Stringe cevrildi.
                                                          String lessonDatabase = lessonSpinner.getSelectedItem().toString();       //Spinnerdan secilen ders adı String olarak tutuldu.
                                                          String lessonFieldDatabase = lessonFieldSpinner.getSelectedItem().toString();  //Spinnerdan secilen ders alanı String olarak tutuldu.
                                                          LatLng lessonLocationDatabase;
                                                          String lessonAddress = null;

                                                          if (lessonPriceText.getText().toString().equals("")) {
                                                              Toast.makeText(getContext(), "Lütfen Ders Ücreti Giriniz..", Toast.LENGTH_SHORT).show();
                                                          } else {
                                                              if (latLngMapButton == null) {
                                                                  lessonLocationDatabase = userLocation;
                                                              } else {
                                                                  lessonLocationDatabase = latLngMapButton;
                                                              }

                                                              try {
                                                                  List<Address> addressList = geocoder.getFromLocation(userLocationLat, userLocationLng, 1);
                                                                  if (addressList != null && addressList.size() > 0) {


                                                                      if (addressList.get(0).getAdminArea() != null) {                                    //Sehir adi ve cadde adi title da gösterilmek üzere birleştirildi
                                                                          address += addressList.get(0).getAdminArea();

                                                                      }

                                                                  }
                                                              } catch (IOException e) {
                                                                  e.printStackTrace();
                                                              }


                                                          }

                                                          HashMap<String, Object> lessonData = new HashMap<>();

                                                          lessonData.put("lesson", lessonDatabase);
                                                          lessonData.put("lessonField", lessonFieldDatabase);
                                                          lessonData.put("lessonPrice", lessonPriceDatabase);
                                                          // lessonData.put("lessonLatLng", lessonLocationDatabase);
                                                          lessonData.put("lessonCity", address);
                                                          lessonData.put("lessonUserEmail", userFirebaseEmail);

                                                          firebaseFirestore.collection("UserLessons").add(lessonData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                              @Override
                                                              public void onSuccess(DocumentReference documentReference) {
                                                                  Toast.makeText(getContext(), "İlanınız oluşturulmuştur..", Toast.LENGTH_SHORT).show();
                                                              }
                                                          }).addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  Toast.makeText(getContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                                                              }
                                                          });

                                                      }
                                                  }
                                              }
        );


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


        return view;
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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.mapViewLayout);


        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);             //Zoom control

        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);                                //My current Location Button


        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();                   //haritayi temizle

                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                userLocationLat = location.getLatitude();
                userLocationLng = location.getLongitude();
                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addressList != null && addressList.size() > 0) {


                        if (addressList.get(0).getAdminArea() != null) {                                    //Sehir adi ve cadde adi title da gösterilmek üzere birleştirildi
                            address += " " + addressList.get(0).getAdminArea();

                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMap.addMarker(new MarkerOptions().position(userLocation).title(address));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
                System.out.println(address + "onlocationchange");
                address = "";


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

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);// 5000 ms veya 20 metre konum güncelle
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null)                                      //son lokasyon null degilse  bilinen son lokasyonu goster
            {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 18));
                System.out.println("last known location");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {       //kullanici izinleri verdiginde

        if (grantResults.length > 0)           //izinler dizisi bos degil ise
        {
            if (requestCode == 1)              //requestCode 1 ise yani access fine location icin kullandigimiz degere esit ise
            {
                if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)  //izin verildiyse
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 20, locationListener);

                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastLocation != null)                                      //son lokasyon null degilse  bilinen son lokasyonu goster
                    {
                        LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 18));
                        System.out.println("last known location");
                    }
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}