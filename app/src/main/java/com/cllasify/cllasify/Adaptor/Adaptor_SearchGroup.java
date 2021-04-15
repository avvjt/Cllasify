package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Answer;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Feed.Landing_Feed_Answer;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Adaptor_SearchGroup extends RecyclerView.Adapter<Adaptor_SearchGroup.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;

    private OnItemClickListener mListener;

    public interface  OnItemClickListener{
//        void onSaveQues(int position, String mTitle, String mDesc);
        //void fillbyOfficialLink(int position, String offWeb);

        //void dislikeAns(int position, String tag);
//        void shareQues(int position, String question);


//        void listitem(int position, String question, String pushQues, String pushAns,String category);

        void createGroupDialog(String adminGroupID, String groupName);
        //void likeAns(int position, String tag);
//        void saveAns(int position, String tag);
//        void likeAns(View v, int position, Boolean clicked);
//        void onWebLinkClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_SearchGroup(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_group_searchview, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Class_Group class_GroupDetails=mDatalistNew.get(position);

        String groupName=class_GroupDetails.getGroupName();
        String userComment=class_GroupDetails.getGroupCategory();
        String groupUserName=class_GroupDetails.getUserName();
        String pushid=class_GroupDetails.getPosition();

        String databaseUserId=class_GroupDetails.getUserId();
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            assert mUser != null;
            String userId = mUser.getUid();
            if (userComment.isEmpty()){
                holder.tv_groupname.setVisibility(View.GONE);
            }else{
                holder.tv_groupname.setText(groupName);
            }

            if (userComment.isEmpty()){
                holder.tv_groupownername.setVisibility(View.GONE);
            }else{
                holder.tv_groupownername.setText(groupUserName);
            }

//        holder.getGroupStatus(groupName, position, pushid);

        DatabaseReference referenceALLGroup= FirebaseDatabase.getInstance().getReference().
                child( "Groups" ).child( "User_Subscribed_Groups" );
        referenceALLGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)) {
                    if (snapshot.child(userId).getValue().equals(true)) {
                        holder.tv_GroupStatus.setText("Subscribed");
                    } else {
                        holder.tv_GroupStatus.setText("Join");
                    }
                }else {
                    holder.tv_GroupStatus.setText("Join");
                }

////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//                long noofGroupinCategory=snapshot.getChildrenCount()+1;
////                            String position=getString((int) noofQuesinCategory);
//                String push="GroupNo_"+noofGroupinCategory+"_"+GroupName;
//                Calendar calenderCC= Calendar.getInstance();
//                SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
//                String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
//
////                            refuserPersonalGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID).child(push);
////                            refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);
//                refuserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID).child(push);
//                refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups");
////                            refAllPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Public_Group").child(push);
//
//                if (GroupCategory.equals("Private")) {
//                    userAddGroup = new Class_Group(dateTimeCC,userName, userID, userEmailID,push, GroupName,GroupCategory,noofGroupinCategory);
////                                refuserPersonalGroup.setValue(userAddGroup);
//                    refuserAllGroup.setValue(userAddGroup);
//                } else if (GroupCategory.equals("Public")) {
//                    userAddGroup = new Class_Group(dateTimeCC,userName, userID, userEmailID,push, GroupName,GroupCategory,noofGroupinCategory);
////                                refuserPublicGroup.setValue(userAddGroup);
////                                refAllPublicGroup.setValue(userAddGroup);
//                    refuserAllGroup.setValue(userAddGroup);
//                }
//                refSubsGroup.child(userID).setValue(true);


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

        TextView tv_groupname,tv_groupownername,tv_GroupStatus;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_groupname=itemView.findViewById(R.id.tv_groupname);
            tv_groupownername = itemView.findViewById(R.id.tv_groupownername);
            tv_GroupStatus = itemView.findViewById(R.id.tv_GroupStatus);

            tv_GroupStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String adminGroupID=user.userId;
                        String groupName=user.groupName;

                        if (position != RecyclerView.NO_POSITION) {
                            mListener.createGroupDialog(adminGroupID,groupName);
                                                        //mListener.dislikeAns();
                        }
                    }
                }
            });
        }

//        private void getGroupStatus(String Answer, int position,String pushid){
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//            final String userID = currentUser.getUid();
//            final String userName = currentUser.getDisplayName();
//            final String userEmail = currentUser.getEmail();
//
//            DatabaseReference refAcceptingReq = FirebaseDatabase.getInstance().getReference()
//                    .child( "Notification" ).child("User").child("SubmitReq").child(userID);
//
//            refAcceptingReq.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" AnswersTab");
//                    if(snapshot.hasChildren()){
//                        String groupStatus=snapshot.child("grpJoiningStatus").getValue().toString();
//
//                        if(groupStatus.equals("req_sent")){
//                            tv_GroupStatus.setText("Request Sent");
//                        }else{
//                            tv_GroupStatus.setText("Join");
//                        }
//
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }

    }

}


