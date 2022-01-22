package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Home.Discover_Item;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        void createGroupDialog(String adminGroupID, String groupName,String groupPushId);
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
//        String userid=class_GroupDetails.getUserId();

        String databaseUserId=class_GroupDetails.getUserId();
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            assert mUser != null;
            String currUserId = mUser.getUid();
//
//            if(!currUserId.equals(databaseUserId)) {
//
//                holder.ll_list_group_search.setVisibility(View.VISIBLE);
                if (userComment.isEmpty()) {
                    holder.tv_groupname.setVisibility(View.GONE);
                } else {
                    holder.tv_groupname.setText(groupName);
                }

                if (userComment.isEmpty()) {
                    holder.tv_groupownername.setVisibility(View.GONE);
                } else {
                    holder.tv_groupownername.setText(groupUserName);
                }

//        holder.getGroupStatus(groupName, position, pushid);

                DatabaseReference referenceALLGroup = FirebaseDatabase.getInstance().getReference().
                        child("Groups").child("All_Universal_Group").child(pushid).child("User_Subscribed_Groups");
                referenceALLGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            if (snapshot.hasChild(currUserId)) {
                                holder.tv_GroupStatus.setEnabled(false);
                                String joinStatus=snapshot.child(currUserId).child("subsStatus").getValue().toString();
                                if (joinStatus.equals("true")) {
                                    holder.tv_GroupStatus.setText("Subscribed");
                                } else if(joinStatus.equals("req_sent")) {
                                    holder.tv_GroupStatus.setText("Req Sent");
                                } else if(joinStatus.equals("reject")) {
                                    holder.tv_GroupStatus.setText("Rejected");
                                }
                            } else {
                                holder.tv_GroupStatus.setText("Join");
                            }
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
//            }
//            else{
//                holder.ll_list_group_search.setVisibility(View.GONE);
//            }
    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_groupname,tv_groupownername,tv_GroupStatus;
        RelativeLayout ll_list_group_search;
        NumberPicker numberPicker;
        LinearLayout showAllClasses;
        Button showAllClassesBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_groupname=itemView.findViewById(R.id.tv_groupname);
            tv_groupownername = itemView.findViewById(R.id.tv_groupownername);
            tv_GroupStatus = itemView.findViewById(R.id.tv_GroupStatus);
            ll_list_group_search = itemView.findViewById(R.id.ll_list_group_search);
            numberPicker = itemView.findViewById(R.id.classPicker);
            showAllClasses = itemView.findViewById(R.id.showClasses);
            showAllClassesBtn = itemView.findViewById(R.id.showAllClassesBtn);

            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(12);

            ll_list_group_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String adminGroupID=user.userId;
                        String groupName=user.groupName;
                        String groupPushId=user.position;
                        String subGrpName=user.subGroupName;


                        if (position != RecyclerView.NO_POSITION) {
                            mListener.createGroupDialog(adminGroupID,groupName,groupPushId);
                            //mListener.dislikeAns();
                        }
                    }
                }
            });

            showAllClassesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAllClasses.setVisibility(View.VISIBLE);
                }
            });

            tv_GroupStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String adminGroupID=user.userId;
                        String groupName=user.groupName;
                        String groupPushId=user.position;
                        String subGrpName=user.subGroupName;


                        if (position != RecyclerView.NO_POSITION) {
                            mListener.createGroupDialog(adminGroupID,groupName,groupPushId);
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
//                    .child( "Notification" ).child("Submit_Req").child(userID);
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


