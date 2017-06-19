package com.justorder.justordermanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProductItemAddActivity extends AppCompatActivity {

    private DatabaseReference fDatabaseBusiness;
    private StorageReference fStorage;

    private ImageButton imgProductPicAdd;
    private EditText edtProductNameAdd, edtProductDescAdd, edtProductHotPriceAdd, edtProductColdPriceAdd;
    private Button btnAddProduct;
    private Uri imageUrl;
    private ProgressDialog progressDialog;

    private String strBusinessID = "";
    private String strClassName = "";
    private String strClassDisplay = "";
    private String strClassType = "";

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productitemadd);

        fDatabaseBusiness = FirebaseDatabase.getInstance().getReference().child("tblBusiness");
        fStorage = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        strBusinessID = intent.getStringExtra("strBusinessID");
        strClassName = intent.getStringExtra("strClassName");
        strClassDisplay = intent.getStringExtra("strClassDisplay");
        strClassType = intent.getStringExtra("strClassType");

        imgProductPicAdd = (ImageButton)findViewById(R.id.imgProductPicAdd);
        edtProductNameAdd = (EditText)findViewById(R.id.edtProductNameAdd);
        edtProductDescAdd = (EditText)findViewById(R.id.edtProductDescAdd);
        edtProductHotPriceAdd = (EditText)findViewById(R.id.edtProductHotPriceAdd);
        edtProductColdPriceAdd = (EditText)findViewById(R.id.edtProductColdPriceAdd);
        btnAddProduct = (Button)findViewById(R.id.btnAddProduct);

        if (strClassType.equals("Food") && strClassDisplay.equals("Special")) {

            edtProductColdPriceAdd.setVisibility(View.GONE);

        } else if (strClassType.equals("Drink") && strClassDisplay.equals("Casual")) {

            imgProductPicAdd.setVisibility(View.GONE);
            edtProductDescAdd.setVisibility(View.GONE);

        } else if (strClassType.equals("Food") && strClassDisplay.equals("Casual")) {

            imgProductPicAdd.setVisibility(View.GONE);
            edtProductDescAdd.setVisibility(View.GONE);
            edtProductColdPriceAdd.setVisibility(View.GONE);

        }

        imgProductPicAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUrl = data.getData();
            imgProductPicAdd.setImageURI(imageUrl);
        }
    }

    private void addProduct() {

        final String strProductNameAdd = edtProductNameAdd.getText().toString().trim();
        final String strProductDescAdd = edtProductDescAdd.getText().toString().trim();
        final String strProductHotPriceAdd = edtProductHotPriceAdd.getText().toString().trim();
        final String strProductColdPriceAdd = edtProductColdPriceAdd.getText().toString().trim();

        if (imageUrl == null) {
            Toast.makeText(this, "Please insert menu picture..", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(strProductNameAdd)) {
            Toast.makeText(this, "Please insert menu name..", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(strProductDescAdd)) {
            Toast.makeText(this, "Please insert menu description..", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(strProductHotPriceAdd) && TextUtils.isEmpty(strProductColdPriceAdd)) {
            Toast.makeText(this, "Please insert menu price..", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Adding Menu, Please Wait...");
        progressDialog.show();

        final StorageReference filePath = fStorage.child(strBusinessID).child("Menu_Pic").child(imageUrl.getLastPathSegment());

        if (strClassType.equals("Drink") && strClassDisplay.equals("Special")) {

            filePath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    final HashMap<String, String> tblProductDetails = new HashMap<String, String>();
                    tblProductDetails.put("productImg", downloadUrl.toString());
                    tblProductDetails.put("productName", strProductNameAdd);
                    tblProductDetails.put("productDesc", strProductDescAdd);
                    if (!TextUtils.isEmpty(strProductHotPriceAdd)) {
                        tblProductDetails.put("productHotPrice", strProductHotPriceAdd);
                    }
                    if (!TextUtils.isEmpty(strProductColdPriceAdd)) {
                        tblProductDetails.put("productColdPrice", strProductColdPriceAdd);
                    }

                    fDatabaseBusiness.child(strBusinessID).child("tblProduct").child(strClassName)
                            .child("classItem").child(strProductNameAdd).setValue(tblProductDetails);

                    progressDialog.dismiss();
                }
            });

        } else if (strClassType.equals("Food") && strClassDisplay.equals("Special")) {

            filePath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    final HashMap<String, String> tblProductDetails = new HashMap<String, String>();
                    tblProductDetails.put("productImg", downloadUrl.toString());
                    tblProductDetails.put("productName", strProductNameAdd);
                    tblProductDetails.put("productDesc", strProductDescAdd);
                    tblProductDetails.put("productPrice", strProductHotPriceAdd);

                    fDatabaseBusiness.child(strBusinessID).child("tblProduct").child(strClassName)
                            .child("classItem").child(strProductNameAdd).setValue(tblProductDetails);

                    progressDialog.dismiss();
                }
            });

        } else if (strClassType.equals("Drink") && strClassDisplay.equals("Casual")) {

            final HashMap<String, String> tblProductDetails = new HashMap<String, String>();
            tblProductDetails.put("productName", strProductNameAdd);
            if (!TextUtils.isEmpty(strProductHotPriceAdd)) {
                tblProductDetails.put("productHotPrice", strProductHotPriceAdd);
            }
            if (!TextUtils.isEmpty(strProductColdPriceAdd)) {
                tblProductDetails.put("productColdPrice", strProductColdPriceAdd);
            }

            fDatabaseBusiness.child(strBusinessID).child("tblProduct").child(strClassName)
                    .child("classItem").child(strProductNameAdd).setValue(tblProductDetails);

            progressDialog.dismiss();

        } else if (strClassType.equals("Food") && strClassDisplay.equals("Casual")) {

            final HashMap<String, String> tblProductDetails = new HashMap<String, String>();
            tblProductDetails.put("productName", strProductNameAdd);
            tblProductDetails.put("productHotPrice", strProductHotPriceAdd);

            fDatabaseBusiness.child(strBusinessID).child("tblProduct").child(strClassName)
                    .child("classItem").child(strProductNameAdd).setValue(tblProductDetails);

            progressDialog.dismiss();

        }

    }

}
