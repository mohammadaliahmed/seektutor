package com.tutor.seektutor.Students;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tutor.seektutor.Models.Student;
import com.tutor.seektutor.Models.Tutor;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Tutor.TutorListAdapter;

import com.tutor.seektutor.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StudentContactSelectionScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Tutor> itemList = new ArrayList<>();
    TutorListAdapter adapter;
    DatabaseReference mDatabase;
    TextView noContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Select Contact");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        noContacts = findViewById(R.id.noContacts);
        recyclerView = findViewById(R.id.recyclerview);

        adapter = new TutorListAdapter(this, itemList, new TutorListAdapter.TutorAdapterCallbacks() {
            @Override
            public void onSelected() {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getDataFromDB();


    }

    private void getDataFromDB() {
        mDatabase.child("Students").child(SharedPrefs.getStudent().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    noContacts.setVisibility(View.GONE);
                    itemList.clear();
                    Student model = dataSnapshot.getValue(Student.class);
                    if (model != null) {
                        if (model.getConfirmFriends().size() > 0) {
                            for (String userId : model.getConfirmFriends()) {
                                if (userId != null) {
                                    getFriendsFromDB(userId);
                                }
                            }
                        } else {
                            noContacts.setVisibility(View.VISIBLE);
                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFriendsFromDB(String userId) {
        mDatabase.child("Tutors").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Tutor userModel = dataSnapshot.getValue(Tutor.class);
                    if (userModel != null) {
                        itemList.add(userModel);
                    }
                    getSupportActionBar().setSubtitle(itemList.size() + " Contacts");
                    Collections.sort(itemList, new Comparator<Tutor>() {
                        @Override
                        public int compare(Tutor listData, Tutor t1) {
                            String ob1 = listData.getName();
                            String ob2 = t1.getName();

                            return ob1.compareTo(ob2);

                        }
                    });
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
