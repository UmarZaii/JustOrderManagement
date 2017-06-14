package com.justorder.justordermanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusinessListActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDatabaseUser, fDatabaseBusiness;

    private RecyclerView rvBusinessList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businesslist);

        fAuth = FirebaseAuth.getInstance();
        fDatabaseUser = FirebaseDatabase.getInstance().getReference().child("tblUser");
        fDatabaseBusiness = FirebaseDatabase.getInstance().getReference().child("tblBusiness");

        rvBusinessList = (RecyclerView)findViewById(R.id.rvBusinessList);
        rvBusinessList.setHasFixedSize(true);
        rvBusinessList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        final String strUserID = fAuth.getCurrentUser().getUid();

        FirebaseRecyclerAdapter<BusinessListModel,BusinessListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BusinessListModel, BusinessListViewHolder>(

                BusinessListModel.class,
                R.layout.rvrow_businesslist,
                BusinessListViewHolder.class,
                fDatabaseUser.child(strUserID).child("userType").child("Manager")

        ) {
            @Override
            protected void populateViewHolder(final BusinessListViewHolder viewHolder, BusinessListModel model, int position) {

                String strBusinessID = model.getBusinessID();
                System.out.println(strBusinessID);

                viewHolder.setBusinessID(strBusinessID);

                fDatabaseBusiness.child(strBusinessID).child("businessName").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String strBusinessName = dataSnapshot.getValue().toString();
                        viewHolder.setBusinessName(strBusinessName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        rvBusinessList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BusinessListViewHolder extends RecyclerView.ViewHolder {

        View v;

        public BusinessListViewHolder(View itemView) {
            super(itemView);

            v = itemView;
        }

        public void setBusinessID(String businessID) {
            TextView txtBusinessID = (TextView)v.findViewById(R.id.txtBusinessID);
            txtBusinessID.setText(businessID);
        }

        public void setBusinessName(String businessName) {
            TextView txtBusinessName = (TextView)v.findViewById(R.id.txtBusinessName);
            txtBusinessName.setText(businessName);
        }

    }
}
