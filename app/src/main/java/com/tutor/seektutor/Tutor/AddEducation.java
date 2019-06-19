package com.tutor.seektutor.Tutor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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


public class AddEducation extends AppCompatActivity {
    Spinner spinner;
    DatabaseReference mDatabase;
    Button update;
    EditText description, year, education;
    ArrayList subjectList = new ArrayList<>();

    String id;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_education);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Add Education");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        id = getIntent().getStringExtra("id");
        if (id == null) {
            id = mDatabase.push().getKey();
        }

        year = findViewById(R.id.year);
        education = findViewById(R.id.education);
        description = findViewById(R.id.description);
        update = findViewById(R.id.update);
        spinner = findViewById(R.id.spinner);
        delete = findViewById(R.id.delete);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EducationModel model = new EducationModel(
                        id,
                        education.getText().toString(),
                        year.getText().toString(),
                        description.getText().toString()
                );

                mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("education").child(id).setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Updated");
                            }
                        });
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("education").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Deleted");
                        finish();
                    }
                });
            }
        });
        getDataFromDB();
    }

    private void getDataFromDB() {
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("education").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    EducationModel model = dataSnapshot.getValue(EducationModel.class);
                    if (model != null) {
                        education.setText(model.getEducationName());
                        year.setText(model.getYear());
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
