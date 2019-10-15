package com.blood.blooddonation;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShortList extends ArrayAdapter<String> {

    private String[] gameName;
    private String[] gameDescription;
    private String[] BloodGroup;
    private String[] imageId;
    private Activity context;
    public ShortList(Context context, String[] gameName, String[] gameDescription, String[] BloodGroup, String[] imageId){
        super(context, R.layout.searched_list,imageId);
        this.context= (Activity) context;
        this.gameName=gameName;
        this.gameDescription=gameDescription;
        this.BloodGroup = BloodGroup;
        this.imageId=imageId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r= convertView;
        ViewHolder viewHolder =null;
        if (r==null)
        {

            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.searched_list,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) r.getTag();
        }

        viewHolder.tv3.setText(BloodGroup[position]);
        viewHolder.tv1.setText(gameName[position]);
        viewHolder.tv2.setText(gameDescription[position]);
        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference(imageId[position])
                .child("Profile Pic");
        final ViewHolder finalViewHolder = viewHolder;
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(finalViewHolder.iv1);
            }
        });

        return r;
    }

    class ViewHolder{
        TextView tv1,tv2,tv3;
        CircleImageView iv1;
        ViewHolder(View view){
            tv1 = view.findViewById(R.id.User_name);
            tv2 = view.findViewById(R.id.User_Group);
            tv3 = view.findViewById(R.id.User_Address);
            iv1 = view.findViewById(R.id.User_image);

        }
    }
}
