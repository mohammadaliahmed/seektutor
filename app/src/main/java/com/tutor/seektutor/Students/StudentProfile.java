package com.tutor.seektutor.Students;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor.seektutor.Models.Student;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Tutor.TutorProfile;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.SharedPrefs;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentProfile extends AppCompatActivity {

    SubjectsToStudyListAdapter adapter1;
    public static ArrayList<SubjectToStudyModel> subjectToStudyList = new ArrayList<>();
    Button addSubjectToStudy;
    RecyclerView subjectsToStudy;

    DatabaseReference mDatabase;


    ImageView back;
    CircleImageView userPic;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Profile");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        subjectsToStudy = findViewById(R.id.subjectsToStudy);
        addSubjectToStudy = findViewById(R.id.addSubjectToStudy);

        userName = findViewById(R.id.userName);
        userPic = findViewById(R.id.userPic);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        subjectsToStudy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter1 = new SubjectsToStudyListAdapter(this, subjectToStudyList);
        subjectsToStudy.setAdapter(adapter1);
//        getDataFromDB();


        addSubjectToStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentProfile.this, AddSubjectsToStudy.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromDB();
    }

    private void getDataFromDB() {
        mDatabase.child("Students").child(SharedPrefs.getStudent().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    subjectToStudyList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.child("subjectsToStudy").getChildren()) {
                        String id = snapshot.getValue(String.class);
                        if (id != null) {
                            getSubjectsFromDb(id);
                        }
                    }
                    Student student = dataSnapshot.getValue(Student.class);
                    userName.setText(student.getName());
                    Glide.with(StudentProfile.this).load(student.getPicUrl()).into(userPic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getSubjectsFromDb(String id) {
        mDatabase.child("SubjectsToStudy").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    SubjectToStudyModel model = dataSnapshot.getValue(SubjectToStudyModel.class);
                    if (model != null) {
                        subjectToStudyList.add(model);
                    }
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
