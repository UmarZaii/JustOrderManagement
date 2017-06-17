package com.justorder.justordermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProductClassAddActivity extends AppCompatActivity {

    private DatabaseReference fDatabase;

    private Spinner spnProductType, spnProductDisplay;
    private EditText edtProductClassNameAdd;
    private Button btnAddProductClass;

    private String strBusinessID = "";
    private String strProductType = "";
    private String strProductDisplay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productclassadd);

        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblBusiness");

        edtProductClassNameAdd = (EditText)findViewById(R.id.edtProductClassNameAdd);
        btnAddProductClass = (Button)findViewById(R.id.btnAddProductClass);

        Intent intent = getIntent();
        strBusinessID = intent.getStringExtra("strBusinessID");

        ArrayAdapter<CharSequence> menuType = ArrayAdapter.createFromResource(this,
                R.array.menu_type, android.R.layout.simple_spinner_item);
        menuType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProductType = (Spinner)findViewById(R.id.spnProductType);
        spnProductType.setAdapter(menuType);

        ArrayAdapter<CharSequence> menuDisplay = ArrayAdapter.createFromResource(this,
                R.array.menu_display, android.R.layout.simple_spinner_item);
        menuDisplay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProductDisplay = (Spinner)findViewById(R.id.spnProductDisplay);
        spnProductDisplay.setAdapter(menuDisplay);

        spnProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strProductType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnProductDisplay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strProductDisplay = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddProductClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strClassName = edtProductClassNameAdd.getText().toString().trim();

                final HashMap<String,String> tblProductClass = new HashMap<String, String>();
                tblProductClass.put("className", strClassName);
                tblProductClass.put("classType", strProductType);
                tblProductClass.put("classDisplay", strProductDisplay);

                fDatabase.child(strBusinessID).child("tblProduct").child("tblProductClass").child(strClassName).setValue(tblProductClass);
            }
        });
    }

}
