package com.tutor.seektutor.Tutor;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tutor.seektutor.Models.NotificationModel;
import com.tutor.seektutor.Models.RatingModel;
import com.tutor.seektutor.Models.Student;
import com.tutor.seektutor.Models.Tutor;
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
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewTutor extends AppCompatActivity implements NotificationObserver {
    TextView userName, name, gender, city, mobile, memberSince;
    ImageView back;
    String tutorId;
    DatabaseReference mDatabase;
    private Tutor tutor;
    CircleImageView image;
    ArrayList<ExperienceModel> experienceList = new ArrayList<>();
    ArrayList<EducationModel> educationList = new ArrayList<>();
    ArrayList<SubjectToTeachModel> subjectsToTeachList = new ArrayList<>();

    ExperienceListAdapter adapter;
    EducationListAdapter adapter1;
    RecyclerView recyclerview, educationrecyclerview;
    Button addAsFriend;
    Student iMStudent;
    AppCompatRatingBar rating;
    int abc = 0;

    RecyclerView subjectsToTeach;
    SubjectsToTeachListAdapter adapter3;
    LinearLayout ratingLayout;

    ImageView phone, sms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tutor);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        tutorId = getIntent().getStringExtra("tutorId");
        sms = findViewById(R.id.sms);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        userName = findViewById(R.id.userName);
        image = findViewById(R.id.userPic);
        gender = findViewById(R.id.gender);
        city = findViewById(R.id.city);
        mobile = findViewById(R.id.mobile);
        memberSince = findViewById(R.id.memberSince);
        educationrecyclerview = findViewById(R.id.educationrecyclerview);
        back = findViewById(R.id.back);
        addAsFriend = findViewById(R.id.addAsFriend);
        rating = findViewById(R.id.rating);
        ratingLayout = findViewById(R.id.ratingLayout);
        subjectsToTeach = findViewById(R.id.recyclerviewTeach);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tutor.getPhone()));
                startActivity(i);
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tutor.getPhone()));
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        educationrecyclerview = findViewById(R.id.educationrecyclerview);
        recyclerview = findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ExperienceListAdapter(this, experienceList, new ExperienceListAdapter.ExperienceListCallbacks() {
            @Override
            public void onSelected() {

            }
        });
        recyclerview.setAdapter(adapter);
        educationrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter1 = new EducationListAdapter(this, educationList, new EducationListAdapter.EducationListCallbacks() {
            @Override
            public void onSelected() {

            }
        });
        educationrecyclerview.setAdapter(adapter1);


        subjectsToTeach.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter3 = new SubjectsToTeachListAdapter(this, subjectsToTeachList, new SubjectsToTeachListAdapter.SubjectsToTeachListCallbacks() {
            @Override
            public void onSelected() {

            }
        });
        subjectsToTeach.setAdapter(adapter3);
        adapter3.setDntGoForward(true);
        getTutorFromDB();

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

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    sendRatingToDb(rating);
                    if (tutor.getRating() != 0) {
                        float abc = 0;
                        abc = ((rating + tutor.getRating()) / (2));
                        updateRating(abc);
                    } else {

                        updateRating(rating);
                    }
                }
            }
        });


    }

    private void sendRatingToDb(float rating) {
        String key = mDatabase.push().getKey();
        RatingModel model = new RatingModel(
                key, SharedPrefs.getStudent().getName(),
                SharedPrefs.getStudent().getPicUrl(),
                rating,
                System.currentTimeMillis(), tutorId, SharedPrefs.getStudent().getUsername()
        );
        mDatabase.child("Ratings").child(key).setValue(model);
    }

    private void updateRating(float rating) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("rating", rating);
        map.put("ratingCount", tutor.getRatingCount() + 1);
        mDatabase.child("Tutors").child(tutorId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                List<String> abc = iMStudent.getRatedTutors();
                if (!iMStudent.getRatedTutors().contains(tutorId)) {
                    abc.add(tutorId);
                }

                mDatabase.child("Students").child(iMStudent.getUsername()).child("ratedTutors").setValue(abc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        canRate = false;
                    }
                });
            }
        });

    }

    private void sendFriendRequest() {
        if (iMStudent != null) {

            if (iMStudent.getRequestSent().contains(tutorId)) {
//            CommonUtils.showToast("Already sent");
            } else {
                iMStudent.getRequestSent().add(tutorId);
                mDatabase.child("Students").child(SharedPrefs.getStudent().getUsername()).child("requestSent")
                        .setValue(iMStudent.getRequestSent()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        if (tutor != null) {
            if (tutor.getRequestReceived().contains(SharedPrefs.getStudent().getUsername())) {
//            CommonUtils.showToast("Already exits");
            } else {
                tutor.getRequestReceived().add(SharedPrefs.getStudent().getUsername());
                mDatabase.child("Tutors").child(tutor.getUsername()).child("requestReceived").setValue(tutor.getRequestReceived()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        NotificationAsync notificationAsync = new NotificationAsync(ViewTutor.this);
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = "New tuition request from " + SharedPrefs.getStudent().getName();
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", tutor.getFcmKey(), NotificationTitle, NotificationMessage,
                "friendRequestFromStudent", SharedPrefs.getStudent().getName(), SharedPrefs.getStudent().getUsername());
        String key = mDatabase.push().getKey();
        NotificationModel model = new NotificationModel(
                key, tutor.getUsername(),
                SharedPrefs.getStudent().getUsername(),
                SharedPrefs.getStudent().getPicUrl(),
                SharedPrefs.getStudent().getName() + " sent you tuition request",
                "newRequest",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(tutor.getUsername()).child(key).setValue(model);
    }

    private void removeAsFriend() {

        iMStudent.getConfirmFriends().remove(iMStudent.getConfirmFriends().indexOf(tutorId));
        tutor.getConfirmFriends().remove(tutor.getConfirmFriends().indexOf(SharedPrefs.getStudent().getUsername()));


        mDatabase.child("Students").child(SharedPrefs.getStudent().getUsername()).child("confirmFriends").setValue(iMStudent.getConfirmFriends());
        mDatabase.child("Tutors").child(tutor.getUsername()).child("confirmFriends").setValue(tutor.getConfirmFriends());
    }

    private void acceptRequest() {
        CommonUtils.showToast("Accepted");
        iMStudent.getConfirmFriends().add(tutorId);
        mDatabase.child("Students").child(SharedPrefs.getStudent().getUsername()).child("confirmFriends").setValue(iMStudent.getConfirmFriends());

        tutor.getConfirmFriends().add(SharedPrefs.getStudent().getUsername());
        mDatabase.child("Tutors").child(tutor.getUsername()).child("confirmFriends").setValue(tutor.getConfirmFriends());


        iMStudent.getRequestReceived().remove(iMStudent.getRequestReceived().indexOf(tutor.getUsername()));
        mDatabase.child("Students").child(iMStudent.getUsername()).child("requestReceived").setValue(iMStudent.getRequestReceived());

        tutor.getRequestSent().remove(tutor.getRequestSent().indexOf(SharedPrefs.getStudent().getUsername()));
        mDatabase.child("Tutors").child(tutor.getUsername()).child("requestSent").setValue(tutor.getRequestSent());

        sendAcceptRequestNotification();


    }

    private void sendAcceptRequestNotification() {
        NotificationAsync notificationAsync = new NotificationAsync(ViewTutor.this);
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = SharedPrefs.getStudent().getName() + " accepted your tuition request";
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", tutor.getFcmKey(), NotificationTitle, NotificationMessage,
                "friendRequestFromStudent", SharedPrefs.getStudent().getName(), SharedPrefs.getStudent().getUsername());
        String key = mDatabase.push().getKey();
        NotificationModel model = new NotificationModel(
                key, tutor.getUsername(),
                SharedPrefs.getStudent().getUsername(),
                SharedPrefs.getStudent().getPicUrl(),
                SharedPrefs.getStudent().getName() + " accepted your tuition request",
                "requestAccept",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(tutor.getUsername()).child(key).setValue(model);
    }

    private void getMyDataFromDB() {
        mDatabase.child("Students").child(SharedPrefs.getStudent().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    iMStudent = dataSnapshot.getValue(Student.class);
                    if (iMStudent != null) {
                        ratingLayout.setVisibility(View.GONE);

                        if (iMStudent.getRequestSent().contains(tutorId)) {
                            addAsFriend.setText("Tuition requested");
                            addAsFriend.setEnabled(false);
                            addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                            abc = 1;
                        } else if (iMStudent.getRequestReceived().contains(tutorId)) {
                            abc = 2;
                            addAsFriend.setText("Accept request");
                            addAsFriend.setEnabled(true);
                            addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        } else if (iMStudent.getConfirmFriends().contains(tutorId)) {
                            ratingLayout.setVisibility(View.VISIBLE);
                            mobile.setText(tutor.getPhone());
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

//                        if (iMStudent.getRatedTutors().contains(tutorId)) {
//                            rating.setEnabled(false);
////                            rating.setFocusable(false);
//                        }


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


    private void getTutorFromDB() {
        mDatabase.child("Tutors").child(tutorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    tutor = dataSnapshot.getValue(Tutor.class);
                    if (tutor != null) {
                        if (!tutor.getPicUrl().equalsIgnoreCase("")) {
                            try {
                                Glide.with(ViewTutor.this).load(tutor.getPicUrl()).into(image);
                            } catch (IllegalArgumentException e) {

                            }
                        }
                        userName.setText(tutor.getName());
                        name.setText(tutor.getName());
                        gender.setText(tutor.getGender());
                        city.setText(tutor.getAddress());

                        mobile.setText("Hidden");

                        memberSince.setText(CommonUtils.getFormattedDate(tutor.getTime()));
                        experienceList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.child("experience").getChildren()) {
                            ExperienceModel model = snapshot.getValue(ExperienceModel.class);
                            if (model != null) {
                                experienceList.add(model);
                            }
                        }
                        educationList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.child("education").getChildren()) {
                            EducationModel model = snapshot.getValue(EducationModel.class);
                            if (model != null) {
                                educationList.add(model);
                            }
                        }
                        adapter.setDntGoForward(true);
                        adapter.notifyDataSetChanged();
                        adapter1.setDntGoForward(true);
                        adapter1.notifyDataSetChanged();

                        rating.setRating(tutor.getRating());
                        subjectsToTeachList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.child("subjectsToTeach").getChildren()) {
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

    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
