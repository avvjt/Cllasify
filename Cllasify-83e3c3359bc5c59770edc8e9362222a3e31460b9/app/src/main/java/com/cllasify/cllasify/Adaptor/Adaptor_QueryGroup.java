package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        String userID = currentUser.getUid();
        Class_Group Answers = mDatalistNew.get(position);

        String groupName = Answers.getGroupName();
        String groupPushid = Answers.getPosition();

        String firstchar = groupName.substring(0, 1);

        DatabaseReference refSaveServerProfPic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushid).child("serverProfilePic");
        refSaveServerProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(context.getApplicationContext()).load(snapshot.getValue()).into(holder.btn_GroupTitle);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        refSaveCurrentData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    if (snapshot.child("clickedGroupPushId").exists()) {
                        String GroupPushId = snapshot.child("clickedGroupPushId").getValue().toString().trim();

                        Log.d("GRPPUSH", "onDataChange: " + GroupPushId + "\t" + groupPushid);

                        if (groupPushid.equals(GroupPushId)) {
                            holder.btn_GroupTitle.setBorderColor(context.getColor(R.color.splash_end));
                            holder.btn_GroupTitle.setBorderWidth(10);
//                            holder.btn_GroupTitle.setBackgroundColor(context.getColor(R.color.colorPrimary));
                        } else {
//                            holder.btn_GroupTitle.setBackgroundColor(context.getColor(R.color.transparent));
                            holder.btn_GroupTitle.setBorderColor(context.getColor(R.color.transparent));
                            holder.btn_GroupTitle.setBorderWidth(10);
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
        CircleImageView btn_GroupTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            btn_GroupTitle = itemView.findViewById(R.id.btn_GroupTitle);


            btn_GroupTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "Clicked on Server", Toast.LENGTH_SHORT).show();
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
                            changeCOor.child("clickedGroupPushId").setValue(groupPushId);
                            changeCOor.child("clickedGroupName").setValue(groupName);


                            mListener.showChildGroupAdaptor(position, groupName, groupPushId, groupUserID, groupCategory);

                            Log.d("GroupCLK", "position: "+position+"\nGroupName: "+groupName+"\ngroupPushId: "+groupPushId+"\ngroupUserID: "+groupUserID+"\ngroupCategory: "+groupCategory);
                        }
                    }
                }
            });


        }
    }
}


