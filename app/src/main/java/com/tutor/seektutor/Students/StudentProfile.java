package com.tutor.seektutor.Students;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.SharedPrefs;

import java.util.ArrayList;


public class StudentProfile extends AppCompatActivity {

    SubjectsToStudyListAdapter adapter1;
    ArrayList<SubjectToStudyModel> subjectToStudyList = new ArrayList<>();
    Button addSubjectToStudy;
    RecyclerView subjectsToStudy;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Profile");

        subjectsToStudy = findViewById(R.id.subjectsToStudy);
        addSubjectToStudy = findViewById(R.id.addSubjectToStudy);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        subjectsToStudy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter1 = new SubjectsToStudyListAdapter(this, subjectToStudyList);
        subjectsToStudy.setAdapter(adapter1);
        getDataFromDB();


        addSubjectToStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentProfile.this, AddSubjectsToStudy.class));
            }
        });

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
