package com.example.solutionchallenge3;


import static androidx.core.content.ContextCompat.checkSelfPermission;

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

    private NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout fragmentlinear;
    private static final int BACK_PRESS_DELAY = 2000; // Delay time in milliseconds
    private long lastBackPressTime = 0;
    Button asl,psl;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check if camera and storage permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // Request camera and storage permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are already granted, continue with app startup
            startApp();
        }
    }
    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Camera and storage permissions are granted, continue with app startup
                startApp();
            } else {
                // Camera and storage permissions are not granted, show an error message or exit the app
                Toast.makeText(this, "Camera and storage permissions are required to use this app", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private void startApp() {
        // TODO: Implement app startup code here

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
                } else if (id == R.id.nav_about) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentlinear, new About())
                            .commit();
                    psl.setTextColor(Color.WHITE);
                    asl.setTextColor(Color.WHITE);
                    psl.setBackground(new ColorDrawable(Color.TRANSPARENT));
                    asl.setBackground(new ColorDrawable(Color.TRANSPARENT));

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
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastBackPressTime < BACK_PRESS_DELAY) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                lastBackPressTime = currentTime;
            }
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

