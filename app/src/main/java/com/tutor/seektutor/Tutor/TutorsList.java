package com.tutor.seektutor.Tutor;

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


public class TutorsList extends AppCompatActivity {
    RecyclerView recyclerview;
    SearchedTutorListAdapter adapter;
    ArrayList<SubjectToTeachModel> itemList = new ArrayList<>();
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
        this.setTitle("List of tutors");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        location = getIntent().getStringExtra("location");
        subject = getIntent().getStringExtra("subject");
//        CommonUtils.showToast(location+subject);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SearchedTutorListAdapter(this, itemList, new SearchedTutorListAdapter.SearchedTutorAdapterCallbacks() {
            @Override
            public void onSelected() {

            }
        });


        recyclerview.setAdapter(adapter);
        getDataFromDB();


    }

    private void getDataFromDB() {
        mDatabase.child("SubjectsToTeach").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubjectToTeachModel tutor = snapshot.getValue(SubjectToTeachModel.class);
                        if (tutor != null) {
                            if (tutor.getSubject().toLowerCase().contains(subject)
                                    && tutor.getLocation().toLowerCase().contains(location)) {
                                itemList.add(tutor);
                            }
                        }

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    CommonUtils.showToast("No Tutors");
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
