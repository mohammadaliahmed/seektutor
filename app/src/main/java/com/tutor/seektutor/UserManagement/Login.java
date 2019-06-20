package com.tutor.seektutor.UserManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tutor.seektutor.Students.MainActivity;
import com.tutor.seektutor.Tutor.TutorMainActivity;
import com.tutor.seektutor.Models.Student;
import com.tutor.seektutor.Models.Tutor;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.PrefManager;
import com.tutor.seektutor.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {
    Button signup, login;
    EditText password, username;
    DatabaseReference mDatabase;
    ArrayList<String> userList = new ArrayList<>();
    private PrefManager prefManager;
    HashMap<String, Student> studenMap = new HashMap<>();
    private HashMap<String, Tutor> tutorMap = new HashMap<>();

    private RadioGroup radioType;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login");
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        radioType = (RadioGroup) findViewById(R.id.radioType);

//        radioType = findViewById(R.id.radioGroup);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            if (SharedPrefs.getTutor() != null) {
                launchTutorHomescreen();
            } else {
                launchHomescreen();
            }
            finish();
        }

        getAllStudentFromDB();
        getAllTutorsFromDB();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioType.getCheckedRadioButtonId();
// find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
//                Toast.makeText(Login.this, radioButton.getText(), Toast.LENGTH_SHORT).show();

                if (username.getText().length() == 0) {
                    username.setError("Enter username");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter password");
                } else {


                    if (radioButton.getText().toString().contains("Student")) {

                        loginStudent();
                    } else {
                        loginTutor();
                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();

            }
        });

    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Select Registration type");
        AlertDialog alert = builder.create();


        builder.setItems(new CharSequence[]
                        {"Sign up as Student", "Sign up as Tutor"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                startActivity(new Intent(Login.this, RegisterStudent.class));
                                break;
                            case 1:
                                startActivity(new Intent(Login.this, RegisterTutor.class));
                                break;


                        }
                    }
                });
        alert = builder.create();
        alert.show();


    }


    private void loginTutor() {
        if (tutorMap.containsKey(username.getText().toString())) {
            if (tutorMap.get(username.getText().toString()).getPassword().equalsIgnoreCase(password.getText().toString())) {
                SharedPrefs.setTutor(tutorMap.get(username.getText().toString()));
                SharedPrefs.setIsLoggedIn("yes");
                launchTutorHomescreen();
            } else {
                CommonUtils.showToast("Wrong password");
            }
        } else {
            CommonUtils.showToast("User does not exist\nPlease signup");
        }
    }

    private void loginStudent() {
        if (studenMap.containsKey(username.getText().toString())) {
            if (studenMap.get(username.getText().toString()).getPassword().equalsIgnoreCase(password.getText().toString())) {
                SharedPrefs.setStudent(studenMap.get(username.getText().toString()));
                SharedPrefs.setIsLoggedIn("yes");
                launchHomescreen();
            } else {
                CommonUtils.showToast("Wrong password");
            }
        } else {
            CommonUtils.showToast("User does not exist\nPlease signup");
        }
    }

    private void getAllStudentFromDB() {
        mDatabase.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Student student = snapshot.getValue(Student.class);
                        if (student != null) {
                            if (!studenMap.containsKey(student.getUsername())) {
                                studenMap.put(snapshot.getKey(), student);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAllTutorsFromDB() {
        mDatabase.child("Tutors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Tutor tutor = snapshot.getValue(Tutor.class);
                        if (tutor != null) {
                            if (!tutorMap.containsKey(tutor.getUsername())) {
                                tutorMap.put(snapshot.getKey(), tutor);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loginUser() {

    }


    private void launchHomescreen() {
        SharedPrefs.setIsLoggedIn("yes");
        prefManager.setFirstTimeLaunch(false);
        SharedPrefs.setUserType("student");
        startActivity(new Intent(Login.this, MainActivity.class));


        finish();
    }

    private void launchTutorHomescreen() {
        SharedPrefs.setIsLoggedIn("yes");
        prefManager.setFirstTimeLaunch(false);
        SharedPrefs.setUserType("tutor");

        startActivity(new Intent(Login.this, TutorMainActivity.class));


        finish();
    }
}
