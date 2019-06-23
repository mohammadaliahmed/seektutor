package com.tutor.seektutor.Students;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutor.seektutor.Models.NotificationModel;
import com.tutor.seektutor.Models.Student;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.NotificationAsync;
import com.tutor.seektutor.Utils.NotificationObserver;
import com.tutor.seektutor.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewStudent extends AppCompatActivity implements NotificationObserver {
    TextView userName, name, gender, city, mobile, memberSince;
    ImageView back;
    String studentId;
    DatabaseReference mDatabase;
    private Student student;
    CircleImageView image;
    SubjectsToStudyListAdapter adapter1;
    ArrayList<SubjectToStudyModel> subjectToStudyList = new ArrayList<>();

    Button addAsFriend;
    Student iMTutor;
    int abc = 0;
    RecyclerView subjectsToStudy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        studentId = getIntent().getStringExtra("studentId");
        name = findViewById(R.id.name);
        userName = findViewById(R.id.userName);
        image = findViewById(R.id.userPic);
        gender = findViewById(R.id.gender);
        city = findViewById(R.id.city);
        mobile = findViewById(R.id.mobile);
        memberSince = findViewById(R.id.memberSince);
        subjectsToStudy = findViewById(R.id.recyclerviewToStudy);
        back = findViewById(R.id.back);
        addAsFriend = findViewById(R.id.addAsFriend);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        subjectsToStudy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter1 = new SubjectsToStudyListAdapter(this, subjectToStudyList);
        subjectsToStudy.setAdapter(adapter1);
        adapter1.setDntGoForward(true);
//        getDataFromDB();

        getStudentFromDB();

        addAsFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (abc == 0) {
                    sendFriendRequest();
                } else if (abc == 1) {

                } else if (abc == 2) {
                    acceptRequest();
                } else if (abc == 3) {
                    removeAsFriend();
                }

            }
        });

        getMyDataFromDB();

    }

    private void sendFriendRequest() {
        if (iMTutor != null) {

            if (iMTutor.getRequestSent().contains(studentId)) {
//            CommonUtils.showToast("Already sent");
            } else {
                iMTutor.getRequestSent().add(studentId);
                mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("requestSent")
                        .setValue(iMTutor.getRequestSent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("tuition Requested");
                        addAsFriend.setText("tuition Requested");
                        addAsFriend.setEnabled(false);
                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                        sendNewFriendRequestNotification();
                    }
                });

            }
        } else {
            CommonUtils.showToast("No internet");
        }
        if (student != null) {
            if (student.getRequestReceived().contains(SharedPrefs.getTutor().getUsername())) {
//            CommonUtils.showToast("Already exits");
            } else {
                student.getRequestReceived().add(SharedPrefs.getTutor().getUsername());
                mDatabase.child("Students").child(student.getUsername()).child("requestReceived").setValue(student.getRequestReceived()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });

            }
        } else {
            CommonUtils.showToast("No internet");
        }
    }


    private void sendNewFriendRequestNotification() {
        NotificationAsync notificationAsync = new NotificationAsync(ViewStudent.this);
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = "New tuition request from " + SharedPrefs.getTutor().getName();
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", student.getFcmKey(), NotificationTitle, NotificationMessage,
                "friendRequestFromTutor", SharedPrefs.getTutor().getName(), SharedPrefs.getTutor().getUsername());


        String key = mDatabase.push().getKey();
        NotificationModel model = new NotificationModel(
                key,
                student.getUsername(),
                SharedPrefs.getTutor().getUsername(),
                SharedPrefs.getTutor().getPicUrl(),
                SharedPrefs.getTutor().getName() + " sent you tuition request",
                "newRequest",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(student.getUsername()).child(key).setValue(model);
    }

    private void removeAsFriend() {

        iMTutor.getConfirmFriends().remove(iMTutor.getConfirmFriends().indexOf(studentId));
        student.getConfirmFriends().remove(student.getConfirmFriends().indexOf(SharedPrefs.getTutor().getUsername()));


        mDatabase.child("Students").child(studentId).child("confirmFriends").setValue(iMTutor.getConfirmFriends());
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("confirmFriends").setValue(student.getConfirmFriends());
    }

    private void acceptRequest() {
        CommonUtils.showToast("Accepted");
        iMTutor.getConfirmFriends().add(studentId);
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("confirmFriends").setValue(iMTutor.getConfirmFriends());

        student.getConfirmFriends().add(SharedPrefs.getTutor().getUsername());
        mDatabase.child("Students").child(student.getUsername()).child("confirmFriends").setValue(student.getConfirmFriends());


        iMTutor.getRequestReceived().remove(iMTutor.getRequestReceived().indexOf(studentId));
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).child("requestReceived").setValue(iMTutor.getRequestReceived());

        student.getRequestSent().remove(student.getRequestSent().indexOf(SharedPrefs.getTutor().getUsername()));
        mDatabase.child("Students").child(studentId).child("requestSent").setValue(student.getRequestSent());

        sendAcceptRequestNotification();


    }

    private void sendAcceptRequestNotification() {
        NotificationAsync notificationAsync = new NotificationAsync(ViewStudent.this);
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = SharedPrefs.getTutor().getName() + " accepted your tuition request";
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", student.getFcmKey(), NotificationTitle, NotificationMessage,
                "friendRequestFromTutor", SharedPrefs.getTutor().getName(), SharedPrefs.getTutor().getUsername());
        String key = mDatabase.push().getKey();
        NotificationModel model = new NotificationModel(
                key,
                student.getUsername(),
                SharedPrefs.getTutor().getUsername(),
                SharedPrefs.getTutor().getPicUrl(),
                SharedPrefs.getTutor().getName() + " accepted your tuition request",
                "requestAccept",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(student.getUsername()).child(key).setValue(model);
    }

    private void getMyDataFromDB() {
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    iMTutor = dataSnapshot.getValue(Student.class);
                    if (iMTutor != null) {
                        if (iMTutor.getRequestSent().contains(studentId)) {
                            addAsFriend.setText("tuition Requested");
                            addAsFriend.setEnabled(false);
                            addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                            abc = 1;
                        } else if (iMTutor.getRequestReceived().contains(studentId)) {
                            abc = 2;
                            addAsFriend.setText("Accept request");
                            addAsFriend.setEnabled(true);
                            addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        } else if (iMTutor.getConfirmFriends().contains(studentId)) {
                            abc = 3;
                            addAsFriend.setText("End tuition");
                            addAsFriend.setEnabled(true);
                            addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorRed));
                        } else {
                            abc = 0;
                            addAsFriend.setText("Request tuition");
                            addAsFriend.setEnabled(true);
                            addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getStudentFromDB() {
        mDatabase.child("Students").child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    student = dataSnapshot.getValue(Student.class);
                    if (student != null) {
                        if (!student.getPicUrl().equalsIgnoreCase("")) {
                            try {
                                Glide.with(ViewStudent.this).load(student.getPicUrl()).into(image);

                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }
                        userName.setText(student.getName());
                        name.setText(student.getName());
                        gender.setText(student.getGender());
                        city.setText(student.getAddress());
                        mobile.setText(student.getPhone());
                        memberSince.setText(CommonUtils.getFormattedDate(student.getTime()));
                        subjectToStudyList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.child("subjectsToStudy").getChildren()) {
                            String id = snapshot.getValue(String.class);
                            if (id != null) {
                                getSubjectsFromDb(id);
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

    private void getSubjectsFromDb(String id) {
        mDatabase.child("SubjectsToStudy").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
