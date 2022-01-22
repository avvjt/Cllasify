package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor_QueryGroup extends RecyclerView.Adapter<Adaptor_QueryGroup.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{


        void addChildGroupAdaptor(int position, String groupName,String groupPushId, String groupUserID);

        void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID,String groupCategory);
        void showll_Group(int position, String groupName,String groupPushId, String groupUserID);




    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_QueryGroup(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_groupname, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userID=currentUser.getUid();
        Class_Group Answers=mDatalistNew.get(position);

        String groupName=Answers.getGroupName();
        String groupPushid=Answers.getPosition();

        String firstchar=groupName.substring(0,1);

        holder.btn_GroupTitle.setText(firstchar);

        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        refSaveCurrentData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    if(snapshot.child("tGroupPushId").exists() ) {
                        String GroupPushId = snapshot.child("tGroupPushId").getValue().toString().trim();
                        if (groupPushid.equals(GroupPushId)){
                            holder.btn_GroupTitle.setBackgroundColor(context.getColor(R.color.colorPrimary));
                        }else{
                            holder.btn_GroupTitle.setBackgroundColor(context.getColor(R.color.transparent));
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Button btn_GroupTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            btn_GroupTitle = itemView.findViewById(R.id.btn_GroupTitle);


            btn_GroupTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Class_Group user = mDatalistNew.get(getAdapterPosition());
                            String groupName = user.getGroupName();
                            String groupPushId = user.getPosition();
                            String groupUserID = user.getUserId();
                            String groupCategory = user.getGroupCategory();

                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            assert currentUser != null;
                            String userID = currentUser.getUid();


                            DatabaseReference changeCOor = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                            changeCOor.child("tGroupPushId").setValue(groupPushId);


                            btn_GroupTitle.setBackgroundColor(context.getColor(R.color.colorPrimary));


                            mListener.showChildGroupAdaptor(position, groupName, groupPushId, groupUserID, groupCategory);
                        }
                    }
                }
            });


        }
    }
}


