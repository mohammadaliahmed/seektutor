package com.tutor.seektutor.Students;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.tutor.seektutor.R;
import com.tutor.seektutor.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StudentList extends AppCompatActivity {
    RecyclerView recyclerview;
    StudentListAdapter adapter;
    ArrayList<SubjectToStudyModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;
    String location, subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutors_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("List of Students");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        location = getIntent().getStringExtra("location");
        subject = getIntent().getStringExtra("subject");
//        CommonUtils.showToast(location+subject);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new StudentListAdapter(this, itemList);

        recyclerview.setAdapter(adapter);
        getDataFromDB();


    }

    private void getDataFromDB() {
        mDatabase.child("SubjectsToStudy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubjectToStudyModel student = snapshot.getValue(SubjectToStudyModel.class);
                        if (student != null) {
                            if (student.getSubject().contains(subject) && student.getLocation().contains(location)) {

                                itemList.add(student);
                            }
                        }

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    CommonUtils.showToast("No Students");
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
