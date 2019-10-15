package com.blood.blooddonation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Splash extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Updater updater;
    String url = "http://asksgoogle.com/Download/BloodDonationv2.apk";
    String enable;
    ProgressBar progressBar;
    float version = 2, versionCode;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


//        database = FirebaseDatabase.getInstance();
//        databaseReference = database.getReference("Update");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                updater = dataSnapshot.getValue(Updater.class);
//                url = updater.getUpdateUrl();
//                enable = updater.getUpdateEnable();
//                version = updater.getVersion();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        UpdateSplash updateSplash = new UpdateSplash(version,versionCode,getApplicationContext(),url,Splash.this);


//            startActivity(new Intent(Splash.this, MainActivity.class));


    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}
