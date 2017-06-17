package com.justorder.justordermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BusinessMainActivity extends AppCompatActivity {

    private Button btnStaffList, btnProductClassList;

    private String strBusinessID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businessmain);

        btnStaffList = (Button)findViewById(R.id.btnStaffList);
        btnProductClassList = (Button)findViewById(R.id.btnProductClassList);

        Intent intent = getIntent();
        strBusinessID = intent.getStringExtra("strBusinessID");

        btnStaffList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessMainActivity.this, StaffListActivity.class);
                intent.putExtra("strBusinessID",strBusinessID);
                startActivity(intent);
            }
        });

        btnProductClassList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessMainActivity.this, ProductClassListActivity.class);
                intent.putExtra("strBusinessID",strBusinessID);
                startActivity(intent);
            }
        });

    }

}
