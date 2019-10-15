package com.blood.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class ForceUpdateChecker extends AsyncTask<String, Void, Void> {

    private Context context;
    File outputFile;

    public void setContext(Context contextf) {
        context = contextf;
    }

    @Override
    protected Void doInBackground(String... arg0) {
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = "/mnt/sdcard/Download/";
            File file = new File(PATH);
            file.mkdirs();
            outputFile = new File(file, "update.apk");
            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // without this flag android returned a intent error!
//            intent.setDataAndType(Uri.parse("/mnt/sdcard/Download/update.apk"), "application/vnd.android.package-archive");//"+Environment.getExternalStorageDirectory()+"
//            context.startActivity(intent);


            File toInstall = new File("/mnt/sdcard/Download/update.apk");
            try {

                String[] command = new String[4];
                command[0] = "adb";
                command[0] = "shell";
                command[0] = "pm";
                command[1] = "install";
                command[0] = "-t";
                command[2] = "-r ";
                command[3] = toInstall.getAbsolutePath();
                Process p1 = Runtime.getRuntime().exec(command, null);
                p1.waitFor();
            } catch (Exception e1) {
                Log.e("UpdateAPP", "Update error! " + e1.getMessage());
                context.startActivity(new Intent(context,MainActivity.class));

            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", toInstall);
//                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
//                intent.setData(apkUri);
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                context.startActivity(intent);
//            } else {
//                Uri apkUri = Uri.fromFile(toInstall);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }


        } catch (Exception e) {
            Log.e("UpdateAPP", "Update error! " + e.getMessage());
            context.startActivity(new Intent(context,MainActivity.class));
        }

        File file = new File("/mnt/sdcard/Download/");
        file.mkdirs();
        outputFile = new File(file, "update.apk");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        return null;
    }

}
