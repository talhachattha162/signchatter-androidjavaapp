package com.example.solutionchallenge3;


import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

String psoas="psl";
    private NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout fragmentlinear;

    Button asl,psl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentlinear, new PSL())
                .commit();
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.app_name , R.string.app_name);
fragmentlinear=findViewById(R.id.fragmentlinear);
        navigationView = findViewById(R.id.navview);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        psl=findViewById(R.id.button);
        asl=findViewById(R.id.button1);
        psl.setBackground(new ColorDrawable(Color.YELLOW));
        asl.setBackground(new ColorDrawable(Color.TRANSPARENT));
        asl.setTextColor(Color.WHITE);
        psl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentlinear, new PSL())
                        .commit();
                psl.setBackground(new ColorDrawable(Color.YELLOW));
                asl.setBackground(new ColorDrawable(Color.TRANSPARENT));
                asl.setTextColor(Color.WHITE);
            }
        });
        asl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentlinear, new ASL())
                    .commit();
                asl.setBackground(new ColorDrawable(Color.YELLOW));
                psl.setBackground(new ColorDrawable(Color.TRANSPARENT));
                psl.setTextColor(Color.WHITE);
            }
        });
        // Set item click listener for the navigation drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Handle navigation view item clicks here
                int id = menuItem.getItemId();

                if (id == R.id.nav_asl) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentlinear, new ASL())
                            .commit();

                    asl.setBackground(new ColorDrawable(Color.YELLOW));
                    psl.setBackground(new ColorDrawable(Color.TRANSPARENT));
                    psl.setTextColor(Color.WHITE);
//                    l2.setVisibility(View.INVISIBLE);

                } else if (id == R.id.nav_about) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentlinear, new About())
                            .commit();

//                    l2.setVisibility(View.INVISIBLE);

                } else if (id == R.id.nav_psl) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentlinear, new PSL())
                            .commit();

                    psl.setBackground(new ColorDrawable(Color.YELLOW));
                    asl.setBackground(new ColorDrawable(Color.TRANSPARENT));
                    asl.setTextColor(Color.WHITE);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

