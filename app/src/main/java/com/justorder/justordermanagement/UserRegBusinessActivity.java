package com.justorder.justordermanagement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UserRegBusinessActivity extends AppCompatActivity {

    private DatabaseReference fDatabase;
    private StorageReference fStorage;

    private EditText edtBusinessNameReg, edtBusinessPhoneNoReg;
    private Button btnBusinessReg;
    private ImageButton imgBusinessReg;
    private Uri imageUrl;
    private ProgressDialog progressDialog;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregbusiness);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblRestaurant");
        fStorage = FirebaseStorage.getInstance().getReference();

        edtBusinessNameReg = (EditText)findViewById(R.id.edtBusinnessNameReg);
        edtBusinessPhoneNoReg = (EditText)findViewById(R.id.edtBusinessPhoneNoReg);

        imgBusinessReg = (ImageButton)findViewById(R.id.imgBusinessReg);
//        imgBusinessReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, GALLERY_REQUEST);
//            }
//        });

        btnBusinessReg = (Button)findViewById(R.id.btnBusinessReg);
        btnBusinessReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBusiness();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUrl = data.getData();
            imgBusinessReg.setImageURI(imageUrl);
        }
    }

    private void registerBusiness() {

        progressDialog.setMessage("Registering, Please Wait...");
        progressDialog.show();

        StringGenerator stringGenerator = new StringGenerator();

        final String strBusinessNameReg = edtBusinessNameReg.getText().toString().trim();
        final String strBusinessPhoneNoReg = edtBusinessPhoneNoReg.getText().toString().trim();
        final String strBusinessIDReg = stringGenerator.createStringID(36);

        if (!TextUtils.isEmpty(strBusinessNameReg) && !TextUtils.isEmpty(strBusinessPhoneNoReg)) {

//            StorageReference filePath = fStorage.child(strBusinessIDReg).child("Profile_Pic").child(imageUrl.getLastPathSegment());
//            filePath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    final HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("businessName", strBusinessNameReg);
                    dataMap.put("businessID", strBusinessIDReg);
                    dataMap.put("businessContact", strBusinessPhoneNoReg);
//                    dataMap.put("businessPic", downloadUrl.toString());
                    fDatabase.child(strBusinessIDReg).setValue(dataMap);

                    progressDialog.dismiss();
//                }
//            });
//
//        } else if (!TextUtils.isEmpty(strBusinessNameReg) && !TextUtils.isEmpty(strBusinessPhoneNoReg) && fStorage == null) {
//            Toast.makeText(this, "Please insert profile picture...", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
        } else if (!TextUtils.isEmpty(strBusinessNameReg) && TextUtils.isEmpty(strBusinessPhoneNoReg)) {
            Toast.makeText(this, "Please insert contact number..", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (TextUtils.isEmpty(strBusinessNameReg) && !TextUtils.isEmpty(strBusinessPhoneNoReg)) {
            Toast.makeText(this, "Please insert restaurant name..", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

}
