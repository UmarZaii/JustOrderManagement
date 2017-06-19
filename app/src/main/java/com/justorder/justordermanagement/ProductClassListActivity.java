package com.justorder.justordermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductClassListActivity extends AppCompatActivity{

    private DatabaseReference fDatabaseBusiness;

    private RecyclerView rvProductClassList;
    private Button btnGoToAddProductClass;

    private String strBusinessID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productclasslist);

        Intent intent = getIntent();
        strBusinessID = intent.getStringExtra("strBusinessID");

        fDatabaseBusiness = FirebaseDatabase.getInstance().getReference().child("tblBusiness");

        rvProductClassList = (RecyclerView)findViewById(R.id.rvProductClassList);
        rvProductClassList.setHasFixedSize(true);
        rvProductClassList.setLayoutManager(new LinearLayoutManager(this));
        btnGoToAddProductClass = (Button)findViewById(R.id.btnGoToAddProductClass);

        btnGoToAddProductClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductClassListActivity.this, ProductClassAddActivity.class);
                intent.putExtra("strBusinessID", strBusinessID);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ProductClassListModel,ProductClassListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductClassListModel, ProductClassListViewHolder>(

                ProductClassListModel.class,
                R.layout.rvrow_productclasslist,
                ProductClassListViewHolder.class,
                fDatabaseBusiness.child(strBusinessID).child("tblProduct")

        ) {
            @Override
            protected void populateViewHolder(final ProductClassListViewHolder viewHolder, ProductClassListModel model, int position) {

                final String strClassName = model.getClassName();
                final String strClassDisplay = model.getClassDisplay();
                final String strClassType = model.getClassType();

                viewHolder.setClassName(strClassName);

                viewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProductClassListActivity.this, ProductItemListActivity.class);
                        intent.putExtra("strBusinessID", strBusinessID);
                        intent.putExtra("strClassName", strClassName);
                        intent.putExtra("strClassDisplay", strClassDisplay);
                        intent.putExtra("strClassType", strClassType);
                        startActivity(intent);
                    }
                });

            }
        };

        rvProductClassList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ProductClassListViewHolder extends RecyclerView.ViewHolder {

        View v;

        public ProductClassListViewHolder(View itemView) {
            super(itemView);

            v = itemView;
        }

        public void setClassName(String className) {
            TextView txtProductClassName = (TextView)v.findViewById(R.id.txtProductClassName);
            txtProductClassName.setText(className);
        }

    }
}
