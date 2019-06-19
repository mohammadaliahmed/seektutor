package com.tutor.seektutor.Tutor;

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
import com.tutor.seektutor.Models.Tutor;
import com.tutor.seektutor.R;
import com.tutor.seektutor.UserManagement.Login;
import com.tutor.seektutor.Utils.CommonUtils;
import com.tutor.seektutor.Utils.CompressImage;
import com.tutor.seektutor.Utils.GifSizeFilter;
import com.tutor.seektutor.Utils.PrefManager;
import com.tutor.seektutor.Utils.SharedPrefs;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditTutor extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;
    Button update;
    DatabaseReference mDatabase;
    EditText name, username, password, phone, address, age, city;

    List<Uri> mSelected = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    private RadioGroup radioType;
    private RadioButton radioButton;
    CircleImageView profilePic;
    StorageReference mStorageRef;
    private Uri downloadUrl;
    Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tutor);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Edit profile");
        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        update = findViewById(R.id.update);
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
        getMyUserFromDB();

        update.setOnClickListener(new View.OnClickListener() {
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
                } else if (address.getText().toString().length() == 0) {
                    address.setError("Please enter address");
                } else if (city.getText().toString().length() == 0) {
                    city.setError("Please enter city");
                } else if (age.getText().toString().length() == 0) {
                    age.setError("Please enter age");
                } else {
                    updateUser();
                }
            }
        });
    }

    private void getMyUserFromDB() {
        mDatabase.child("Tutors").child(SharedPrefs.getTutor().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    tutor = dataSnapshot.getValue(Tutor.class);
                    if (tutor != null) {
                        name.setText(tutor.getName());
                        username.setText(tutor.getUsername());
                        password.setText(tutor.getPassword());
                        phone.setText(tutor.getPhone());
                        address.setText(tutor.getAddress());
                        city.setText(tutor.getCity());
                        age.setText(tutor.getAge());
                        if (tutor.getGender().equalsIgnoreCase("male")) {
                            radioType.getChildAt(0).setSelected(true);
                        } else {
                            radioType.getChildAt(1).setSelected(true);
                        }
                        Glide.with(EditTutor.this).load(tutor.getPicUrl()).into(profilePic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initMatisse() {

        Matisse.from(EditTutor.this)
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
    private void updateUser() {

        int selectedId = radioType.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("password", password.getText().toString());
        map.put("phone", phone.getText().toString());
        map.put("address", address.getText().toString());
        map.put("city", city.getText().toString());
        map.put("age", age.getText().toString());
        map.put("gender", radioButton.getText().toString());
        map.put("picUrl", downloadUrl == null ? tutor.getPicUrl() : "" + downloadUrl);


        mDatabase.child("Tutors")
                .child(SharedPrefs.getTutor().getUsername())
                .updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Updated");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast("There was some error");
            }

        });
    }


    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,

        };

        if (!hasPermissions(EditTutor.this, PERMISSIONS)) {
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
