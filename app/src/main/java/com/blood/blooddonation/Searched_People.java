package com.blood.blooddonation;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Searched_People extends AppCompatActivity {
    private ListView listView;
    private TextView Error;
    FirebaseDatabase database;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    DatabaseReference databaseReference;
    public String States, City, Blood;
    UserProfile userProfile;
    ImageView back;
    int i = 0;
    private String[] User_name, User_blood, User_Address, User_Profile, User_phone, userGet;
    private ProgressDialog progressDialog;

    public boolean isPerissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched__people);


        listView = findViewById(R.id.listView);
        Error = findViewById(R.id.Error);
        database = FirebaseDatabase.getInstance();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Searched_People.this, Searching_Process.class));
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        States = getIntent().getExtras().getString("states");
        City = getIntent().getExtras().getString("Citys");
        Blood = getIntent().getExtras().getString("blood");
        progressDialog = new ProgressDialog(this);
        databaseReference = database.getReference(States).child(City).child(Blood);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long p = dataSnapshot.getChildrenCount();
                userGet = new String[Integer.valueOf(String.valueOf(p))];
                User_name = new String[Integer.valueOf(String.valueOf(p))];
                User_Address = new String[Integer.valueOf(String.valueOf(p))];
                User_blood = new String[Integer.valueOf(String.valueOf(p))];
                User_Profile = new String[Integer.valueOf(String.valueOf(p))];
                User_phone = new String[Integer.valueOf(String.valueOf(p))];
                String user = FirebaseAuth.getInstance().getUid();
                if (Integer.valueOf(String.valueOf(p)) == 0) {
                    Error.setVisibility(View.VISIBLE);
                    Error.setText("No Data Is Available");
                    progressDialog.dismiss();
                } else {
                    Error.setVisibility(View.INVISIBLE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        userProfile = ds.getValue(UserProfile.class);
                        userGet[i] = userProfile.getPreimg();
                        if (p == 1 && userGet[i].equals(user)) {
                            Error.setVisibility(View.VISIBLE);
                            Error.setText("No Data Is Available");
                            progressDialog.dismiss();
                        } else if (userGet[i].equals(user)) {

                        } else {
                            User_name[i] = userProfile.getName();
                            User_Address[i] = userProfile.getAddress();
                            User_blood[i] = userProfile.getGroup();
                            User_Profile[i] = userProfile.getPreimg();
                            User_phone[i] = userProfile.getPhone();
                            i++;
                        }
                    }

                    List<String> list = new ArrayList<String>();

                    for (String s : User_Profile) {
                        if (s != null && s.length() > 0) {
                            list.add(s);
                        }
                    }

                    User_Profile = list.toArray(new String[list.size()]);

                    ShortList shortList = new ShortList(Searched_People.this, User_name, User_blood, User_Address, User_Profile);
                    listView.setAdapter(shortList);
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
//                Intent intent = new Intent(Searched_People.this,)
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Searched_People.this);
                final Dialog v = new Dialog(Searched_People.this);
                v.setContentView(R.layout.donnerinfo);
//                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View v = inflater.inflate(R.layout.donnerinfo,null);
                TextView name, group, address;
                final CircleImageView v1;
                LinearLayout Call, Massage;
                name = v.findViewById(R.id.User_name);
                group = v.findViewById(R.id.User_Group);
                address = v.findViewById(R.id.User_Address);
                v1 = v.findViewById(R.id.User_image);
                Call = v.findViewById(R.id.CallToUser);
                Massage = v.findViewById(R.id.MassageToUser);

                name.setText(User_name[position]);
                group.setText(User_blood[position]);
                address.setText(User_Address[position]);

                FirebaseStorage firebaseStorage;
                StorageReference storageReference;
                firebaseStorage = FirebaseStorage.getInstance();
                storageReference = firebaseStorage.getReference(User_Profile[position])
                        .child("Profile Pic");

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(v1);
                    }
                });
                Call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vi) {
                        if (isPerissionGranted()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", User_phone[position], null));
                            startActivity(intent);
//                        startActivity(new Intent(Searched_People.this,searching_process.class));
                            v.dismiss();
                        }
                    }
                });
                Massage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vi) {
                        if (isPerissionGranted()) {
                            Intent intent = new Intent(getApplicationContext(), Searching_Process.class);
                            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(User_phone[position], null,
                                    "I Want Urgent Blood Of " + User_blood[position]
                                            + " If You Are Ready To Give Blood Then Call Me On " +
                                            "Same Number\n User Of BloodDonation App", pi,
                                    null);
                            Toast.makeText(Searched_People.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                            v.dismiss();
                        }
                    }
                });
                v.show();
                v.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });

    }
}
