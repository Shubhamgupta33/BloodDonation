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
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

public class registerUser extends AppCompatActivity {

    private ImageView User_Image;
    private EditText User_name, User_age, User_phone, User_email,
            User_city, User_address, User_password, User_lastBD;
    private TextView Back_login;
    private Button User_register;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseStorage firebaseStorage;
    private DatePickerDialog.OnDateSetListener mDateSetListener, mage;
    private Spinner User_group, User_state, User_gender;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;
    //    private String States[] = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
//            "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir",
//            "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
//            "Mizoram", "Nagaland", "Odisha", "Punjab", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
//            "Uttar Pradesh", "Uttarakhand", "West Bengal"};
    private String name, age, phone, email, city, state, address, password, group, preimg, lastBD, gender;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                User_Image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        User_address = findViewById(R.id.user_address);
        User_password = findViewById(R.id.user_password);
        User_age = findViewById(R.id.user_age);
        User_city = findViewById(R.id.user_city);
        User_email = findViewById(R.id.user_email);
        User_name = findViewById(R.id.user_name);
        User_phone = findViewById(R.id.user_phone);
        User_state = findViewById(R.id.user_state);
        User_Image = findViewById(R.id.Upload_user);
        User_gender = findViewById(R.id.user_gender);
        User_register = findViewById(R.id.Submit);
        Back_login = findViewById(R.id.tvLogin);
        User_lastBD = findViewById(R.id.UserLastBD);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressDialog = new ProgressDialog(this);
        User_group = findViewById(R.id.group);

//        User_group = (Spinner)findViewById(R.id.User_Group);
        User_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),
                        PICK_IMAGE);
            }
        });


        User_lastBD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(registerUser.this,
                            android.R.style.Theme_Holo_Dialog_MinWidth,
                            mDateSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });
        User_age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar ca = Calendar.getInstance();
                    int ye = ca.get(Calendar.YEAR);
                    int mo = ca.get(Calendar.MONTH);
                    int da = ca.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(registerUser.this,
                            android.R.style.Theme_Holo_Dialog_MinWidth,
                            mage,
                            ye, mo, da);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });
        mage = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                m = m + 1;

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, y);
                c.set(Calendar.MONTH, m);
                c.set(Calendar.DAY_OF_MONTH, d);
                age = String.valueOf(calAge(c.getTimeInMillis()));
                User_age.setText(age);
//                Log.d("registerUser", "onDateSet: dd/mm/yyyy:" + dayOfMonth + "/" + month + "/" + year);
//                lastBD = dayOfMonth + "/" + month + "/" + year;
//                User_lastBD.setText(lastBD);
            }
        };
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d("registerUser", "onDateSet: dd/mm/yyyy:" + dayOfMonth + "/" + month + "/" + year);
                lastBD = dayOfMonth + "/" + month + "/" + year;
                User_lastBD.setText(lastBD);
            }
        };
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        User_group.setAdapter(adapter);
        ArrayAdapter<CharSequence> states = ArrayAdapter.createFromResource(this,
                R.array.State, android.R.layout.simple_spinner_item);
        states.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        User_state.setAdapter(states);
        ArrayAdapter<CharSequence> gender = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);
        gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        User_gender.setAdapter(gender);


        User_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                RegisterUser();
            }
        });

        Back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerUser.this, MainActivity.class));
            }
        });

    }

    int calAge(long date) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
    }

    private void RegisterUser() {
        if (validate()) {
            //Upload data to the database
            String user_email = User_email.getText().toString().trim();
            String user_password = User_password.getText().toString().trim();

            firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(registerUser.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }

    private Boolean validate() {
        Boolean result = false;

        name = User_name.getText().toString();
        password = User_password.getText().toString();
        email = User_email.getText().toString();
//        age = User_age.getText().toString();
        phone = User_phone.getText().toString();
        address = User_address.getText().toString();
        state = User_state.getSelectedItem().toString();
        city = User_city.getText().toString();
        gender = User_gender.getSelectedItem().toString();
        group = User_group.getSelectedItem().toString();
        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty() ||
                phone.isEmpty() || phone.length() < 10 ||
                city.isEmpty() || address.isEmpty() || group.equals("Select Blood Group") ||
                state.equals("Select State") || imagePath == null || gender.equals("Select Your Gender") || lastBD.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter all the details",
                    Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(registerUser.this, "Password Length Is Less Then 8", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(age) < 18) {
            Toast.makeText(registerUser.this, "Your Age Not More Then 18\n Unable To Register", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }


    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserData();
                        progressDialog.dismiss();
                        Toast.makeText(registerUser.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(registerUser.this, MainActivity.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(registerUser.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(state).child(city.toLowerCase()).child(group).child(firebaseAuth.getUid());
        DatabaseReference myData = firebaseDatabase.getReference("UserData").child(firebaseAuth.getUid());
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid())
                .child("Profile Pic");  //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(registerUser.this, "Upload failed!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(registerUser.this, "Upload successful!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        preimg = firebaseAuth.getUid();
        UserProfile userProfile = new UserProfile(name, age, phone, email,
                city, state, address, password, group, preimg, gender, lastBD);
        myData.setValue(userProfile);
        myRef.setValue(userProfile);
    }
}
