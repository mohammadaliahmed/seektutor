package com.tutor.seektutor.Tutor;

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
import com.tutor.seektutor.Students.AddSubjectsToStudy;
import com.tutor.seektutor.Students.SubjectToStudyModel;
import com.tutor.seektutor.Students.SubjectsToStudyListAdapter;
import com.tutor.seektutor.Utils.SharedPrefs;

import java.util.ArrayList;


public class TutorProfile extends AppCompatActivity {
    Button addExperience;
    Button addEducation;
    Button addSubjectToTeach;
    RecyclerView experienceRecycler, educationRecycler, subjectsToTeach;
    DatabaseReference mDatabase;
    ArrayList<ExperienceModel> experienceList = new ArrayList<>();
    ArrayList<SubjectToTeachModel> subjectsToTeachList = new ArrayList<>();
    ArrayList<EducationModel> educationList = new ArrayList<>();
    ExperienceListAdapter adapter;
    EducationListAdapter adapter2;
    SubjectsToTeachListAdapter adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Profile");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        addEducation = findViewById(R.id.addEducation);

        addExperience = findViewById(R.id.addExperience);
        addSubjectToTeach = findViewById(R.id.addSubjectToTeach);
        subjectsToTeach = findViewById(R.id.subjectsToTeach);
        addSubjectToTeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorProfile.this, AddSubjectsToTeach.class));

            }
        });

        addExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorProfile.this, AddExperience.class));
            }
        });

        experienceRecycler = findViewById(R.id.experienceRecycler);

        experienceRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ExperienceListAdapter(this, experienceList, new ExperienceListAdapter.ExperienceListCallbacks() {
            @Override
            public void onSelected() {

            }
        });
        experienceRecycler.setAdapter(adapter);

        addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorProfile.this, AddEducation.class));
            }
        });

        educationRecycler = findViewById(R.id.educationRecycler);

        educationRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter2 = new EducationListAdapter(this, educationList, new EducationListAdapter.EducationListCallbacks() {
            @Override
            public void onSelected() {

            }
        });
        educationRecycler.setAdapter(adapter2);


        subjectsToTeach.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter3 = new SubjectsToTeachListAdapter(this, subjectsToTeachList, new SubjectsToTeachListAdapter.SubjectsToTeachListCallbacks() {
            @Override
            public void onSelected() {

            }
        });
        subjectsToTeach.setAdapter(adapter3);
        getDataFromDB();


    }

    private void getDataFromDB() {
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    experienceList.clear();
                    educationList.clear();
                    subjectsToTeachList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.child("experience").getChildren()) {
                        ExperienceModel model = snapshot.getValue(ExperienceModel.class);
                        if (model != null) {
                            experienceList.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    for (DataSnapshot snapshot : dataSnapshot.child("education").getChildren()) {
                        EducationModel model = snapshot.getValue(EducationModel.class);
                        if (model != null) {
                            educationList.add(model);
                        }
                    }
                    adapter2.notifyDataSetChanged();
                    for (DataSnapshot snapshot : dataSnapshot.child("subjectsToTeach").getChildren()) {
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
        mDatabase.child("SubjectsToTeach").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    SubjectToTeachModel model = dataSnapshot.getValue(SubjectToTeachModel.class);
                    if (model != null) {
                        subjectsToTeachList.add(model);
                    }
                    adapter3.notifyDataSetChanged();
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
