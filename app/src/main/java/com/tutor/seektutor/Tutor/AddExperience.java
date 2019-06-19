package com.tutor.seektutor.Tutor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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


public class AddExperience extends AppCompatActivity {
    Spinner spinner;
    private String experience;
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
        setContentView(R.layout.activity_add_experince);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Add Experience");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        id = getIntent().getStringExtra("id");
        if (id == null) {
            id = mDatabase.push().getKey();
        }
        delete = findViewById(R.id.delete);

        subject = findViewById(R.id.subject);
        description = findViewById(R.id.description);
        update = findViewById(R.id.update);
        spinner = findViewById(R.id.spinner);

        final String[] items = new String[]{"Experience less than 5 years", "Experience more than 5 years", "Experience more than 10 years"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                experience = items[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        subjectList.add("Physics 9th class");
        subjectList.add("Physics 10th class");
        subjectList.add("Physics 1st year");
        subjectList.add("Physics 2nd year");
        subjectList.add("Maths Matric");
        subjectList.add("Maths Olevels");
        subjectList.add("Maths Alevels");
        subjectList.add("English");
        subjectList.add("Urdu");

        ArrayAdapter adapter1 = new ArrayAdapter(AddExperience.this, android.R.layout.simple_spinner_dropdown_item, subjectList);


        subject.setAdapter(adapter1);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExperienceModel model = new ExperienceModel(
                        id, subject.getText().toString(),
                        experience,
                        description.getText().toString()
                );

                mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("experience").child(id).setValue(model)
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
                mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("experience").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("experience").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ExperienceModel model = dataSnapshot.getValue(ExperienceModel.class);
                    if (model != null) {
                        subject.setText(model.getSubject());
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
