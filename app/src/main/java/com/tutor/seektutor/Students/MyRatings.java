package com.tutor.seektutor.Students;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor.seektutor.Adapters.RatingListAdapter;
import com.tutor.seektutor.Models.RatingModel;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.SharedPrefs;

import java.util.ArrayList;


public class MyRatings extends AppCompatActivity {
    RecyclerView recyclerview;
    DatabaseReference mDatabase;
    RatingListAdapter adapter;
    private ArrayList<RatingModel> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutors_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Ratings");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerview=findViewById(R.id.recyclerview);
        adapter = new RatingListAdapter(this, itemList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
        getDataFromDB();
    }

    private void getDataFromDB() {
        mDatabase.child("Ratings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RatingModel model = snapshot.getValue(RatingModel.class);
                        if (model != null) {
                            if (model.getStudentId().equalsIgnoreCase(SharedPrefs.getStudent().getUsername())) {
                                itemList.add(model);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    itemList.clear();
                    CommonUtils.showToast("No Rating");
                    adapter.notifyDataSetChanged();
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
