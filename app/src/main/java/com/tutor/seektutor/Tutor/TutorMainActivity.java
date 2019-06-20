package com.tutor.seektutor.Tutor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tutor.seektutor.Activities.NotificationsList;
import com.tutor.seektutor.Activities.Splash;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Students.MainActivity;
import com.tutor.seektutor.Students.MyRatings;
import com.tutor.seektutor.Students.StudentList;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    DatabaseReference mDatabase;
    Button search;
    AutoCompleteTextView subject, location;
    private TextView navUsername,navSubtitle;
    CircleImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Seek Tutor");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        location = findViewById(R.id.location);
        subject = findViewById(R.id.subject);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorMainActivity.this, StudentList.class);
                i.putExtra("location", location.getText().toString());
                i.putExtra("subject", subject.getText().toString());
                startActivity(i);
            }
        });


        ArrayAdapter adapter = new ArrayAdapter(TutorMainActivity.this, android.R.layout.simple_spinner_dropdown_item, CommonUtils.subjectsListWithPrice());


        subject.setAdapter(adapter);
        ArrayList locationsList = new ArrayList<>();

        locationsList.add("Lahore");
        locationsList.add("Islamabad");
        locationsList.add("Karachi");
        locationsList.add("Peshawar");
        locationsList.add("Faisalabad");


        ArrayAdapter adapter2 = new ArrayAdapter(TutorMainActivity.this, android.R.layout.simple_spinner_dropdown_item, locationsList);


        location.setAdapter(adapter2);


        updateFCMKey();

        initDrawer();
    }


    private void updateFCMKey() {
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("fcmKey").setValue(SharedPrefs.getFcmKey());
    }


    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

         navUsername = (TextView) headerView.findViewById(R.id.name_drawer);
         navSubtitle = (TextView) headerView.findViewById(R.id.phone_drawer);
         img = headerView.findViewById(R.id.imageView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(TutorMainActivity.this).load(SharedPrefs.getTutor() == null ? SharedPrefs.getStudent().getPicUrl() : SharedPrefs.getTutor().getPicUrl()).into(img);

        navSubtitle.setText(SharedPrefs.getTutor() == null ? SharedPrefs.getStudent().getPhone() : SharedPrefs.getTutor().getPhone());

        navUsername.setText(SharedPrefs.getTutor() != null ? SharedPrefs.getTutor().getName() : SharedPrefs.getStudent().getName());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_notification) {
            startActivity(new Intent(TutorMainActivity.this, NotificationsList.class));

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(TutorMainActivity.this, TutorProfile.class));

        } else if (id == R.id.nav_rated) {
            startActivity(new Intent(TutorMainActivity.this, TutorRatings.class));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(TutorMainActivity.this, EditTutor.class));

        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(TutorMainActivity.this, TutorContactSelectionScreen.class));

        } else if (id == R.id.nav_logout) {
            SharedPrefs.logout();
            startActivity(new Intent(TutorMainActivity.this, Splash.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
