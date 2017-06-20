package com.justorder.justordermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductItemListActivity extends AppCompatActivity {

    private DatabaseReference fDatabaseBusiness;

    private RecyclerView rvProductItemList;
    private Button btnGoToAddProductItem;

    private String strBusinessID = "";
    private String strClassName = "";
    private String strClassDisplay = "";
    private String strClassType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productitemlist);

        Intent intent = getIntent();
        strBusinessID = intent.getStringExtra("strBusinessID");
        strClassName = intent.getStringExtra("strClassName");
        strClassDisplay = intent.getStringExtra("strClassDisplay");
        strClassType = intent.getStringExtra("strClassType");

        fDatabaseBusiness = FirebaseDatabase.getInstance().getReference().child("tblBusiness");

        rvProductItemList = (RecyclerView)findViewById(R.id.rvProductItemList);
        rvProductItemList.setHasFixedSize(true);
        rvProductItemList.setLayoutManager(new LinearLayoutManager(this));
        btnGoToAddProductItem = (Button)findViewById(R.id.btnGoToAddProductItem);

        btnGoToAddProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductItemListActivity.this, ProductItemAddActivity.class);
                intent.putExtra("strBusinessID", strBusinessID);
                intent.putExtra("strClassName", strClassName);
                intent.putExtra("strClassDisplay", strClassDisplay);
                intent.putExtra("strClassType", strClassType);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ProductItemListModel, ProductItemListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductItemListModel, ProductItemListViewHolder>(

                ProductItemListModel.class,
                R.layout.rvrow_productitemlist,
                ProductItemListViewHolder.class,
                fDatabaseBusiness.child(strBusinessID).child("tblProduct").child(strClassName).child("classItem")

        ) {
            @Override
            protected void populateViewHolder(ProductItemListViewHolder viewHolder, ProductItemListModel model, int position) {

                viewHolder.setProductName(model.getProductName());

            }
        };

        rvProductItemList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ProductItemListViewHolder extends RecyclerView.ViewHolder {

        View v;

        public ProductItemListViewHolder(View itemView) {
            super(itemView);

            v = itemView;
        }

        public void setProductName(String productName) {
            TextView txtProductItemName = (TextView)v.findViewById(R.id.txtProductItemName);
            txtProductItemName.setText(productName);
        }

    }

}