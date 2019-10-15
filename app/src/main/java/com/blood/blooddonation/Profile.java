package com.blood.blooddonation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    private CircleImageView v1;
    private TextView t1, t2, t3, t4, t5, t6, t7, t8;
    private LinearLayout l1, l2, l3, l4, l5, l6, l7, l8, p1, p2, p3, p4, p5, p6, p7, p8;
    LinearLayout.LayoutParams params;
    private ImageView v01, v2, v3, v4, v5, v6, v7, v8, c1, c2, c3, c4, c5, c6, c7, c8;
    private EditText e1, e2, e4, e5, e6, e8;
    private Spinner e3, e7;
    private ProgressDialog progressDialog;
//    Searching_Process s1 = new Searching_Process();
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private String name, age, phone, email, city, state, address, password, group, preimg, lastBD, gender, group1, state1, city1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    int q = 150;
    UserProfile userProfile;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
                v1.setImageBitmap(bitmap);
//                s1.UserImg.setImageBitmap(bitmap);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                sendUserData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile");
        v1 = v.findViewById(R.id.ivProfilePic);
        t8 = v.findViewById(R.id.tvProfileName);
        t1 = v.findViewById(R.id.UserPhone);
        t2 = v.findViewById(R.id.UserEmail);
        t3 = v.findViewById(R.id.UserGender);
        t4 = v.findViewById(R.id.UserLastBD);
        t5 = v.findViewById(R.id.UserAddress);
        t6 = v.findViewById(R.id.UserCity);
        t7 = v.findViewById(R.id.UserState);

        l1 = v.findViewById(R.id.tvPhone);
        l2 = v.findViewById(R.id.tvEmail);
        l3 = v.findViewById(R.id.tvGender);
        l4 = v.findViewById(R.id.tvLastDB);
        l5 = v.findViewById(R.id.tvAddress);
        l6 = v.findViewById(R.id.tvCity);
        l7 = v.findViewById(R.id.tvState);
        l8 = v.findViewById(R.id.tvName);

        p1 = v.findViewById(R.id.etPhone);
        p2 = v.findViewById(R.id.etEmail);
        p3 = v.findViewById(R.id.etGender);
        p4 = v.findViewById(R.id.etLastDB);
        p5 = v.findViewById(R.id.etAddress);
        p6 = v.findViewById(R.id.etCity);
        p7 = v.findViewById(R.id.etState);
        p8 = v.findViewById(R.id.lName);

        v01 = v.findViewById(R.id.etUserPhone);
        v2 = v.findViewById(R.id.etUserEmail);
        v3 = v.findViewById(R.id.etUserGender);
        v4 = v.findViewById(R.id.etUserLastBD);
        v5 = v.findViewById(R.id.etUserAddress);
        v6 = v.findViewById(R.id.etUserCity);
        v7 = v.findViewById(R.id.etUserState);
        v8 = v.findViewById(R.id.etsName);

        c1 = v.findViewById(R.id.cUserPhone);
        c2 = v.findViewById(R.id.cUserEmail);
        c3 = v.findViewById(R.id.cUserGender);
        c4 = v.findViewById(R.id.cUserLastBD);
        c5 = v.findViewById(R.id.cUserAddress);
        c6 = v.findViewById(R.id.cUserCity);
        c7 = v.findViewById(R.id.cUserState);
        c8 = v.findViewById(R.id.cName);

        e1 = v.findViewById(R.id.ctUserPhone);
        e2 = v.findViewById(R.id.ctUserEmail);
        e3 = v.findViewById(R.id.ctUserGender);
        e4 = v.findViewById(R.id.ctUserLastBD);
        e5 = v.findViewById(R.id.ctUserAddress);
        e6 = v.findViewById(R.id.ctUserCity);
        e7 = v.findViewById(R.id.ctUserState);
        e8 = v.findViewById(R.id.etProfileName);
        progressDialog = new ProgressDialog(getContext());

        params = (LinearLayout.LayoutParams) l1.getLayoutParams();
        params.height = q;
        l1.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) l2.getLayoutParams();
        params.height = q;
        l2.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) l3.getLayoutParams();
        params.height = q;
        l3.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) l4.getLayoutParams();
        params.height = q;
        l4.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) l5.getLayoutParams();
        params.height = q;
        l5.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) l6.getLayoutParams();
        params.height = q;
        l6.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) l7.getLayoutParams();
        params.height = q;
        l7.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) l8.getLayoutParams();
        params.height = q;
        l8.setLayoutParams(params);

        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),
                        PICK_IMAGE);
            }
        });
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.Blood_group, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        User_group.setAdapter(adapter);
        ArrayAdapter<CharSequence> states = ArrayAdapter.createFromResource(getContext(),
                R.array.State, android.R.layout.simple_spinner_item);
        states.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        e7.setAdapter(states);
        ArrayAdapter<CharSequence> gender = ArrayAdapter.createFromResource(getContext(),
                R.array.Gender, android.R.layout.simple_spinner_item);
        gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        e3.setAdapter(gender);
        e4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(getContext(),
                            android.R.style.Theme_Holo_Dialog_MinWidth,
                            mDateSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d("registerUser", "onDateSet: dd/mm/yyyy:" + dayOfMonth + "/" + month + "/" + year);
                String lastBD = dayOfMonth + "/" + month + "/" + year;
                e4.setText(lastBD);
            }
        };
        Show();
        Edit();
        return v;
    }

    private void Show() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = database.getReference("UserData").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
                assert userProfile != null;
                t8.setText(userProfile.getName());
                t1.setText(userProfile.getPhone());
                t2.setText(userProfile.getEmail());
                t3.setText(userProfile.getGender());
                t4.setText(userProfile.getLastBD());
                t5.setText(userProfile.getAddress());
                t6.setText(userProfile.getCity());
                t7.setText(userProfile.getState());

                name = userProfile.getName();
                age = userProfile.getAge();
                phone = userProfile.getPhone();
                email = userProfile.getEmail();
                city = userProfile.getCity();
                state = userProfile.getState();
                address = userProfile.getAddress();
                password = userProfile.getPassword();
                group = userProfile.getGroup();
                preimg = userProfile.getPreimg();
                state1 = userProfile.getState();
                city1 = userProfile.getCity();
                group1 = userProfile.getGroup();
                lastBD = userProfile.getLastBD();
                gender = userProfile.getGender();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference(firebaseAuth.getUid())
                .child("Profile Pic");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(v1);
            }
        });
    }

    private void Edit() {
        v01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(t1.getText().toString());
                params = (LinearLayout.LayoutParams) l1.getLayoutParams();
                params.height = 0;
                l1.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) p1.getLayoutParams();
                params.height = q;
                p1.setLayoutParams(params);
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(t2.getText().toString());
                params = (LinearLayout.LayoutParams) l2.getLayoutParams();
                params.height = 0;
                l2.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) p2.getLayoutParams();
                params.height = q;
                p2.setLayoutParams(params);
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) l3.getLayoutParams();
                params.height = 0;
                l3.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) p3.getLayoutParams();
                params.height = q;
                p3.setLayoutParams(params);
            }
        });
        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e4.setText(t4.getText().toString());
                params = (LinearLayout.LayoutParams) l4.getLayoutParams();
                params.height = 0;
                l4.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) p4.getLayoutParams();
                params.height = q;
                p4.setLayoutParams(params);

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        v5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e5.setText(t5.getText().toString());
                params = (LinearLayout.LayoutParams) l5.getLayoutParams();
                params.height = 0;
                l5.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) p5.getLayoutParams();
                params.height = q;
                p5.setLayoutParams(params);
            }
        });
        v6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e6.setText(t6.getText().toString());
                params = (LinearLayout.LayoutParams) l6.getLayoutParams();
                params.height = 0;
                l6.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) p6.getLayoutParams();
                params.height = q;
                p6.setLayoutParams(params);
            }
        });
        v7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                e7.setText(t7.getText().toString());
                params = (LinearLayout.LayoutParams) l7.getLayoutParams();
                params.height = 0;
                l7.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) p7.getLayoutParams();
                params.height = q;
                p7.setLayoutParams(params);
            }
        });
        v8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e8.setText(t8.getText().toString());
                params = (LinearLayout.LayoutParams) l8.getLayoutParams();
                params.height = 0;
                l8.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) p8.getLayoutParams();
                params.height = q;
                p8.setLayoutParams(params);
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) p1.getLayoutParams();
                params.height = 0;
                p1.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) l1.getLayoutParams();
                params.height = q;
                l1.setLayoutParams(params);
                if ((e1.getText().toString()).equals("")) {
                    phone = t1.getText().toString();
                    sendUserData();
                    Toast.makeText(getContext(), "Phone Is Have No Value", Toast.LENGTH_SHORT).show();
                } else {
                    phone = e1.getText().toString();
                    t1.setText(phone);
                    sendUserData();
                }
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) p2.getLayoutParams();
                params.height = 0;
                p2.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) l2.getLayoutParams();
                params.height = q;
                l2.setLayoutParams(params);
                if ((e2.getText().toString()).equals("")) {
                    email = t2.getText().toString();
                    sendUserData();
                    Toast.makeText(getContext(), "Email Is Have No Value", Toast.LENGTH_SHORT).show();
                } else {
                    email = e2.getText().toString();
                    t2.setText(email);
                    sendUserData();
                }

            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) p3.getLayoutParams();
                params.height = 0;
                p3.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) l3.getLayoutParams();
                params.height = q;
                l3.setLayoutParams(params);
                if ((e3.getSelectedItem().toString()).equals("Select Your Gender")) {
                    gender = t3.getText().toString();
                    sendUserData();
                    Toast.makeText(getContext(), "Gender Is Have No Value", Toast.LENGTH_SHORT).show();
                } else {
                    gender = e3.getSelectedItem().toString();
                    t3.setText(gender);
                    sendUserData();
                }
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) p4.getLayoutParams();
                params.height = 0;
                p4.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) l4.getLayoutParams();
                params.height = q;
                l4.setLayoutParams(params);
                if ((e4.getText().toString()).equals("")) {
                    lastBD = t4.getText().toString();
                    sendUserData();
                    Toast.makeText(getContext(), "Last Blood Donation Is Have No Value", Toast.LENGTH_SHORT).show();
                } else {
                    lastBD = e4.getText().toString();
                    t4.setText(lastBD);
                    sendUserData();
                }

            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) p5.getLayoutParams();
                params.height = 0;
                p5.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) l5.getLayoutParams();
                params.height = q;
                l5.setLayoutParams(params);
                if ((e5.getText().toString()).equals("")) {
                    address = t5.getText().toString();
                    sendUserData();
                    Toast.makeText(getContext(), "Address Is Have No Value", Toast.LENGTH_SHORT).show();
                } else {
                    address = e5.getText().toString();
                    t5.setText(address);
                    sendUserData();
                }

            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) p6.getLayoutParams();
                params.height = 0;
                p6.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) l6.getLayoutParams();
                params.height = q;
                l6.setLayoutParams(params);
                if ((e6.getText().toString()).equals("")) {
                    city = t6.getText().toString();
                    sendUserData();
                    Toast.makeText(getContext(), "City Is Have No Value", Toast.LENGTH_SHORT).show();
                } else {
                    city = e6.getText().toString();
                    t6.setText(city);
                    sendUserData();
                }
            }
        });
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) p7.getLayoutParams();
                params.height = 0;
                p7.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) l7.getLayoutParams();
                params.height = q;
                l7.setLayoutParams(params);
                if (e7.getSelectedItem().toString().equals("Select State")) {
                    state = t7.getText().toString();
                    sendUserData();
                    Toast.makeText(getContext(), "Phone Is Have No Value", Toast.LENGTH_SHORT).show();
                } else {
                    state = e7.getSelectedItem().toString();
                    t7.setText(state);
                    sendUserData();
                }
            }
        });
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = (LinearLayout.LayoutParams) p8.getLayoutParams();
                params.height = 0;
                p8.setLayoutParams(params);
                params = (LinearLayout.LayoutParams) l8.getLayoutParams();
                params.height = q;
                l8.setLayoutParams(params);
                if ((e8.getText().toString()).equals("")) {
                    name = t8.getText().toString();
                    sendUserData();
                    Toast.makeText(getContext(), "Name Is Have No Value", Toast.LENGTH_SHORT).show();
                } else {
                    name = e8.getText().toString();
                    t8.setText(name);
                    sendUserData();
                }
            }
        });
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (!state.equals(state1) || !city.equals(city1) || !group.equals(group1)) {
            DatabaseReference myRefe = firebaseDatabase.getReference(state1).child(city1.toLowerCase()).child(group1).child(firebaseAuth.getUid());
            myRefe.removeValue();
        }
        DatabaseReference myRef = firebaseDatabase.getReference(state).child(city.toLowerCase()).child(group).child(firebaseAuth.getUid());
        DatabaseReference myData = firebaseDatabase.getReference("UserData").child(firebaseAuth.getUid());
        if (imagePath != null) {
            StorageReference storageReference;
            storageReference = firebaseStorage.getReference();
            StorageReference imageReference = storageReference.child(firebaseAuth.getUid())
                    .child("Profile Pic");  //User id/Images/Profile Pic.jpg
            UploadTask uploadTask = imageReference.putFile(imagePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Upload failed!",
                            Toast.LENGTH_SHORT).show();
                }
            });
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Upload successful!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        preimg = firebaseAuth.getUid();
        UserProfile userProfile = new UserProfile(name, age, phone, email,
                city, state, address, password, group, preimg, gender, lastBD);
        myData.setValue(userProfile);
        myRef.setValue(userProfile);
    }

}
