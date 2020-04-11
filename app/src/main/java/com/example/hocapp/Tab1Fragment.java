
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ZoomControls;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */



public class Tab1Fragment extends Fragment implements OnMapReadyCallback {

    Spinner lessonSpinner;          //Dersler Spinner
    Spinner lessonFieldSpinner;     //Ders alanları Spinner
    EditText lessonPrice;           //Ders Ücreti
    Button createLessonButton;       //ilan yayınlama butonu
    GoogleMap mMap;
    MapView mMapView;
    View mView;
    LocationManager locationManager;
    LocationListener locationListener;
    EditText mapsText;
    Geocoder geocoder;
    String address = "";
    ArrayList<String> lessonList;    //Dersler Dizisi
    ArrayList<String> lessonFieldList;      //Ders Alanları Dizisi

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public Tab1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        firebaseAuth = FirebaseAuth.getInstance();              // initializing



        Spinner lessonSpinner = view.findViewById(R.id.lessonSpinner);
        Spinner lessonFieldSpinner = view.findViewById(R.id.lessonFieldSpinner);
        EditText lessonPrice=view.findViewById(R.id.lessonPrice);
        Button createLessonButton = view.findViewById(R.id.createLessonButton);
        Button mapsButton =view.findViewById(R.id.mapsButton);
        Button mapsFindMyLocationButton =view.findViewById(R.id.mapsFindMyLocationButton);          //konumumu bul
        mapsText=view.findViewById(R.id.mapsText);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        firebaseFirestore = FirebaseFirestore.getInstance();                         // initializing
        lessonList = new ArrayList<>();                                              // dersler listesi
        lessonFieldList = new ArrayList<>();                                         // ders alanları listesi




        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                    //konuma git butonu
                 mMap.clear();
                String location=mapsText.getText().toString();

                if(location!=null && !location.equals("")){
                    List<Address> addressList=null;
                    Geocoder geocoder=new Geocoder(getContext());
                    try {
                        addressList=geocoder.getFromLocationName(location,1);
                        if (addressList != null && addressList.size() > 0) {
                            if (addressList.get(0).getThoroughfare() != null) {                                 //cadde adi
                                address += addressList.get(0).getThoroughfare();

                                if (addressList.get(0).getAdminArea() != null) {                                    //Sehir adi ve cadde adi title da gösterilmek üzere birleştirildi
                                    address += " " + addressList.get(0).getAdminArea();
                                }
                            }
                        }
                    } catch ( IOException e) {
                        e.printStackTrace();
                    }
                    Address address=addressList.get(0);
                    LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Burası "+ address.getAddressLine(0)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    System.out.println(latLng);
                }

            }

        });

        ZoomControls zoom=(ZoomControls)view.findViewById(R.id.zoom);  //Zoom out, Zoom in kontrol
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {         //Zoom out tıklandı
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {      //Zoom in tıklandı
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        getDataFirebaseLesson();                                                              //firebaseden ders adlarını alan fonksiyon cagirildi.
        lessonSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,lessonList));  //Dersler Spinner

        lessonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    System.out.println("gg");           //Spinner 0 konumunda seçim yapılmadı
                }
                else
                {
                    System.out.println(lessonFieldList.get(position));       //Spinner 0 konumunda değil. Seçim yapıldı.
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getDataFirebaseLessonsField();                                                      //fireseden ders alanlarını cekecek fonksiyon cagirildi
        lessonFieldSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,lessonFieldList));       //ders alanları spinner

        lessonFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    System.out.println("gg");           //Spinner 0 konumunda seçim yapılmadı

                }
                else
                {
                    System.out.println(lessonFieldList.get(position));         //Spinner 0 konumunda değil. Seçim yapıldı.
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    public void getDataFirebaseLesson()                                 //ders adları firebaseden cekildi ve spinnerlara gönderilmek uzere degiskene atıldı
    {

        CollectionReference collectionReference =firebaseFirestore.collection("Lessons");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments())
                {
                    Map<String,Object> data= snapshot.getData();   //map olarak datayi geri cekiyoruz

                    String lessonsForDatabase =(String) data.get("lessonsDatabase");  //gelecek verinin string oldugundan emin oldugumuz icin casting islemi yapabiliyoruz

                        lessonList.add(lessonsForDatabase);
                }

            }
        });
    }

    public void getDataFirebaseLessonsField()                                                   //ders alanları firebaseden cekildi ve spinnerlara gönderilmek uzere degiskene atıldı
    {
        CollectionReference collectionReference =firebaseFirestore.collection("LessonsField");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments())
                {
                    Map<String,Object> data= snapshot.getData();   //map olarak datayi geri cekiyoruz

                    String lessonsFieldForDatabase =(String) data.get("lessonsField");  //gelecek verinin string oldugundan emin oldugumuz icin casting islemi yapabiliyoruz

                    lessonFieldList.add(lessonsFieldForDatabase);
                }

            }
        });
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.mapViewLayout);
        if(mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap = googleMap;
        locationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();                   //haritayi temizle

                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (addressList != null && addressList.size() > 0) {
                        if (addressList.get(0).getThoroughfare() != null) {                                 //cadde adi
                            address += addressList.get(0).getThoroughfare();

                            if (addressList.get(0).getAdminArea() != null) {                                    //Sehir adi ve cadde adi title da gösterilmek üzere birleştirildi
                                address += " " + addressList.get(0).getAdminArea();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMap.addMarker(new MarkerOptions().position(userLocation).title(address));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,18));
                System.out.println(userLocation);
                address="";
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,20,locationListener);// 5000 ms veya 20 metre konum güncelle
            Location lastLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation!=null)                                      //son lokasyon null degilse  bilinen son lokasyonu goster
            {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,18));
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
                    }
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
