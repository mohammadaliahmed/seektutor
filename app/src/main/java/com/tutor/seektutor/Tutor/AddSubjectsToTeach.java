package com.tutor.seektutor.Tutor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.tutor.seektutor.R;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddSubjectsToTeach extends AppCompatActivity {
    DatabaseReference mDatabase;
    Button update;
    AutoCompleteTextView subject;
    EditText description;
    ArrayList subjectList = new ArrayList<>();
    Button delete;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subjects_toteach);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Add Subject to teach");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        id = getIntent().getStringExtra("id");
        if (id == null) {
            id = mDatabase.push().getKey();
        }

        delete = findViewById(R.id.delete);
        subject = findViewById(R.id.subject);
        description = findViewById(R.id.description);
        update = findViewById(R.id.update);

        ArrayAdapter adapter = new ArrayAdapter(AddSubjectsToTeach.this, android.R.layout.simple_spinner_dropdown_item, CommonUtils.subjectsListWithPrice());


        subject.setAdapter(adapter);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectToTeachModel model = new SubjectToTeachModel(
                        id,
                        subject.getText().toString(),

                        description.getText().toString(),
                        SharedPrefs.getTutor().getName(),
                        SharedPrefs.getTutor().getPicUrl(),
                        SharedPrefs.getTutor().getUsername(),
                        SharedPrefs.getTutor().getCity()
                );

                mDatabase.child("SubjectsToTeach").child(id).setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Updated");
                                mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("subjectsToTeach")
                                        .child(id).setValue(id);
                            }
                        });
            }
        });

        getDataFromDB();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("SubjectsToTeach").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Deleted");
                        finish();
                    }
                });
            }
        });
    }

    private void getDataFromDB() {
        mDatabase.child("SubjectsToTeach").child(id).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            SubjectToTeachModel model = dataSnapshot.getValue(SubjectToTeachModel.class);
                            if (model != null) {
                                subject.setText(model.getSubject());
//                                price.setText(model.getPrice());
                                description.setText(model.getDescription());


                            }
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
