package com.tutor.seektutor.UserManagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tutor.seektutor.Students.MainActivity;
import com.tutor.seektutor.Models.Student;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.CompressImage;
import com.tutor.seektutor.Utils.GifSizeFilter;
import com.tutor.seektutor.Utils.PrefManager;
import com.tutor.seektutor.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterStudent extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;
    Button signup;
    TextView login;
    DatabaseReference mDatabase;
    private PrefManager prefManager;
    ArrayList<String> userslist = new ArrayList<String>();
    EditText name, username, password, phone, address, age, city;

    ArrayList<String> usernameList = new ArrayList<>();
    List<Uri> mSelected = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    private RadioGroup radioType;
    private RadioButton radioButton;
    CircleImageView profilePic;
    StorageReference mStorageRef;
    private Uri downloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Signup");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomescreen();
            finish();
        }
        getPermissions();
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.signin);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        age = findViewById(R.id.age);
        radioType = findViewById(R.id.radioType);
        profilePic = findViewById(R.id.profilePic);
        city = findViewById(R.id.city);


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMatisse();
            }
        });
        getAllUserFromDB();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length() == 0) {
                    name.setError("Please enter your name");
                } else if (username.getText().toString().length() == 0) {
                    username.setError("Please enter username");
                } else if (password.getText().toString().length() == 0) {
                    password.setError("Please enter your password");
                } else if (phone.getText().toString().length() == 0) {
                    phone.setError("Please enter your phone");
                } else if (phone.getText().length() < 11) {
                    phone.setError("Please enter your phone");
                } else if (address.getText().toString().length() == 0) {
                    address.setError("Please enter address");
                } else if (city.getText().toString().length() == 0) {
                    city.setError("Please enter city");
                } else if (age.getText().toString().length() == 0) {
                    age.setError("Please enter age");
                } else {
                    signUpUser();

                }


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterStudent.this, Login.class);
                startActivity(i);
                finish();
            }
        });
//
    }

    private void initMatisse() {
        mSelected.clear();
        imageUrl.clear();
        Matisse.from(RegisterStudent.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    //
    private void getAllUserFromDB() {
        mDatabase.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String usernames = snapshot.getKey();
                        usernameList.add(usernames);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && data != null) {
            mSelected = Matisse.obtainResult(data);
            for (Uri img : mSelected) {
                CompressImage compressImage = new CompressImage(this);
                imageUrl.add(compressImage.compressImage("" + img));
            }
            Glide.with(this).load(imageUrl.get(0)).into(profilePic);

            putPictures(imageUrl.get(0));


        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void putPictures(String path) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = Uri.fromFile(new File(path));

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        downloadUrl = taskSnapshot.getDownloadUrl();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }

    //
    private void signUpUser() {

        if (userslist.contains("" + username)) {
            CommonUtils.showToast("Username is already taken\nPlease choose another");
        } else {
            int selectedId = radioType.getCheckedRadioButtonId();
// find the radiobutton by returned id
            radioButton = (RadioButton) findViewById(selectedId);
            final Student student = new Student(
                    username.getText().toString(),
                    name.getText().toString(),
                    password.getText().toString(),
                    phone.getText().toString(),
                    address.getText().toString()
                    , city.getText().toString(),
                    age.getText().toString(),
                    radioButton.getText().toString(),
                    SharedPrefs.getFcmKey(),
                    "" + downloadUrl,
                    System.currentTimeMillis()
            );

            mDatabase.child("Students")
                    .child(username.getText().toString())
                    .setValue(student)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Thank you for registering");
                            SharedPrefs.setStudent(student);
                            SharedPrefs.setUsername(username.getText().toString());

                            SharedPrefs.setIsLoggedIn("yes");
                            launchHomescreen();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    CommonUtils.showToast("There was some error");
                }

            });
        }

    }


    private void launchHomescreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(RegisterStudent.this, MainActivity.class));


        finish();

    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,

        };

        if (!hasPermissions(RegisterStudent.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {

                }
            }
        }
        return true;
    }

}
