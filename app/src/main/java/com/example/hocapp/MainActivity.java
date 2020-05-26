package com.example.hocapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;



import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static LayoutInflater inflater;

    private FirebaseAuth firebaseAuth;
    private ActionBar toolbar;  //Navigation Bar







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar= getSupportActionBar();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hocapp_icon2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

      //  toolbar.setTitle("Search");
        loadFragment(new SearchFragment());

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        BottomNavigationView navigation = findViewById(R.id.navigation);                    //.xml dosyamızda tanımladığımız id'si navigasyon olan BottomNavigationView'in nesnesini oluşturduk.
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);  //listener ekledik.

        firebaseAuth = FirebaseAuth.getInstance();  // initializing

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_search:
                  //  toolbar.setTitle("Search");
                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_home:
                  //  toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_video:
                  //  toolbar.setTitle("Canlı Yayın");
                    fragment = new VideoFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_forum:
                  //  toolbar.setTitle("Forum");
                    fragment = new ForumFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                 //   toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        //load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void signOut(View view)
    {
        firebaseAuth.signOut();

        Intent intentToSignIn = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intentToSignIn);
        finish();
    }


}
