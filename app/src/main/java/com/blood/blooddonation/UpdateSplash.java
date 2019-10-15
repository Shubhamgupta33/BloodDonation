package com.blood.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class UpdateSplash {

    public  UpdateSplash(float version, float versionCode, Context getApp , String url, final Context context){
        if (version>versionCode) {
            ForceUpdateChecker atualizaApp = new ForceUpdateChecker();
            atualizaApp.setContext(getApp);
            atualizaApp.execute(url);
        } else {
            Toast.makeText(context, String.valueOf(versionCode+" "+version), Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(new Intent(context,MainActivity.class));
                }
            }, 4000);
        }

    }
    public UpdateSplash() {
    }
}
