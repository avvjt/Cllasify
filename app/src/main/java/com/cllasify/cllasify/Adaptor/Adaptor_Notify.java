package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Adaptor_Notify extends RecyclerView.Adapter<Adaptor_Notify.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
//        void onSaveQues(int position, String mTitle, String mDesc);
        //void fillbyOfficialLink(int position, String offWeb);

        //void dislikeAns(int position, String tag);
//        void shareQues(int position, String question);


//        void listitem(int position, String question, String pushQues, String pushAns,String category);

        void createGroupDialog(String adminGroupID, String groupName);

        void rejectNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId);

        void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId, String classUniPush);
        //void likeAns(int position, String tag);
        //        void saveAns(int position, String tag);
        //        void likeAns(View v, int position, Boolean clicked);
        //        void onWebLinkClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adaptor_Notify(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_notify, parent, false);
        return new MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Class_Group class_GroupDetails = mDatalistNew.get(position);

        String GroupName = class_GroupDetails.getGroupName();
        String userName = class_GroupDetails.getUserName();
        String reqDate = class_GroupDetails.getDateTime();
        String subServer = class_GroupDetails.getSubGroupName();
        String inviteStatus = class_GroupDetails.getGrpJoiningStatus();
        String NotifyCategory = class_GroupDetails.getNotifyCategory();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        userID = currentUser.getUid();

        if (NotifyCategory.equals("Friend_Request")) {

            if (inviteStatus.equals("Approve")) {
                holder.tv_Groupinvite.setText("User " + userName + " request for Add you as a friend has been approved");
                holder.ll_groupdetails.setVisibility(View.GONE);
                holder.tv_Groupinvite.setBackgroundColor(Color.GREEN);
            } else if (inviteStatus.equals("Reject")) {
                holder.tv_Groupinvite.setBackgroundColor(Color.RED);
                holder.ll_groupdetails.setVisibility(View.GONE);
                holder.tv_Groupinvite.setText("User " + userName + " request for Add you as a friend has been rejected");
            } else {

                holder.ll_groupdetails.setVisibility(View.VISIBLE);
                if (GroupName.isEmpty()) {
                    holder.tv_Groupinvite.setVisibility(View.GONE);
                } else {
                    holder.tv_Groupinvite.setText("User " + userName + " wants to add you as a Friend");
                }
                if (GroupName.isEmpty()) {
                    holder.tv_ReqDate.setVisibility(View.GONE);
                } else {
                    holder.tv_ReqDate.setText("Requested on : " + reqDate);
                }
            }
        }


        DatabaseReference servName = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        servName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {

                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                        assert mUser != null;
                        String userId = mUser.getUid();
                        Log.d("NOTI00", "onBindViewHolder: yessssssss!!!!!" + NotifyCategory);
                        if (NotifyCategory.equals("Group_JoiningReq_Teacher")) {

                            if (inviteStatus.equals("Approve")) {
                                holder.tv_Groupinvite.setText("User " + userName + " request to join Sub-class : " + subServer + " of server " + GroupName + " has been approved");
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setBackgroundColor(Color.GREEN);
                            } else if (inviteStatus.equals("Reject")) {
                                holder.tv_Groupinvite.setBackgroundColor(Color.RED);
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setText("User " + userName + " request to join Sub-class : " + subServer + " of server " + GroupName + " has been rejected");
                            } else {

                                holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                if (GroupName.isEmpty()) {
                                    holder.tv_Groupinvite.setVisibility(View.GONE);
                                } else {
                                    holder.tv_Groupinvite.setText("User " + userName + " wants to join Sub-class : " + subServer + " of server " + GroupName);

                                }
                                if (GroupName.isEmpty()) {
                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                } else {
                                    holder.tv_ReqDate.setText("Requested on : " + reqDate);
                                }
                            }
                        }
                        if (NotifyCategory.equals("Group_JoiningReq")) {

                            if (inviteStatus.equals("Approve")) {
                                holder.tv_Groupinvite.setText("User " + userName + " request to join Sub-class : " + subServer + " of server " + GroupName + " has been approved");
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setBackgroundColor(Color.GREEN);
                            } else if (inviteStatus.equals("Reject")) {
                                holder.tv_Groupinvite.setBackgroundColor(Color.RED);
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setText("User " + userName + " request to join Sub-class : " + subServer + " of server " + GroupName + " has been rejected");
                            } else {

                                holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                if (GroupName.isEmpty()) {
                                    holder.tv_Groupinvite.setVisibility(View.GONE);
                                } else {
                                    holder.tv_Groupinvite.setText("User " + userName + " wants to join Sub-class : " + subServer + " of server " + GroupName);

                                }
                                if (GroupName.isEmpty()) {
                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                } else {
                                    holder.tv_ReqDate.setText("Requested on : " + reqDate);
                                }
                            }
                        } else if (NotifyCategory.equals("Follow_Request")) {

                            if (inviteStatus.equals("Approve")) {
                                holder.tv_Groupinvite.setText("User " + userName + " request to Follow you has been approved");
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setBackgroundColor(Color.GREEN);
                            } else if (inviteStatus.equals("Reject")) {
                                holder.tv_Groupinvite.setBackgroundColor(Color.RED);
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setText("User " + userName + " request to Follow you has been rejected");
                            } else {
                                holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                if (GroupName.isEmpty()) {
                                    holder.tv_Groupinvite.setVisibility(View.GONE);
                                } else {
                                    holder.tv_Groupinvite.setText("User " + userName + " wants to follow you");
                                }
                                if (GroupName.isEmpty()) {
                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                } else {
                                    holder.tv_ReqDate.setText("Requested on : " + reqDate);
                                }
                            }
                        }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//
//        holder.tv_approve.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mListener != null) {
//                    if (position != RecyclerView.NO_POSITION) {
////                        int position = getAdapterPosition();
//                        Class_Group user = mDatalistNew.get(position);
//                        String reqUserID=user.userId;
//                        String currUserId=user.groupCategory;
//                        String groupName=user.groupName;
//                        String userName=user.userName;
//
//
//                        DatabaseReference refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups").child(groupName);
//                        refSubsGroup.child(reqUserID).setValue(true);
//                        DatabaseReference refNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child(currUserId);
//                        refNotify.child("groupno").setValue("Approve");
//                        holder.tv_approve.setBackgroundColor(Color.GREEN);
//                        holder.tv_reject.setEnabled(false);
//                        Toast.makeText(context, "Group request from "+userName+"has been Approve", Toast.LENGTH_SHORT).show();
//
//                        //mListener.dislikeAns();
//                    }
//                }
//            }
//        });
//
//        DatabaseReference referenceALLGroup= FirebaseDatabase.getInstance().getReference().
//                child( "Groups" ).child( "User_Subscribed_Groups" );
//        referenceALLGroup.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChild(userId)) {
//                    if (snapshot.child(userId).getValue().equals(true)) {
//                        holder.tv_GroupStatus.setText("Subscribed");
//                    } else {
//                        holder.tv_GroupStatus.setText("Join");
//                    }
//                }else {
//                    holder.tv_GroupStatus.setText("Join");
//                }

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

//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Groupinvite, tv_approve, tv_reject, tv_ReqDate;
        LinearLayout ll_groupdetails;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_Groupinvite = itemView.findViewById(R.id.tv_Groupinvite);
            tv_approve = itemView.findViewById(R.id.tv_approve);
            tv_reject = itemView.findViewById(R.id.tv_reject);
            tv_ReqDate = itemView.findViewById(R.id.tv_ReqDate);
            ll_groupdetails = itemView.findViewById(R.id.ll_groupdetails);


            tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String reqUserID = user.userId;
                        String currUserId = user.adminUserId;
                        String groupName = user.groupName;
                        String userName = user.userName;
                        String pushid = user.subGroupName;
                        String notPushId = user.position;
                        String groupPushId = user.groupPositionId;
                        String notifyReq = user.notifyCategory;

                        if (position != RecyclerView.NO_POSITION) {
//                            DatabaseReference refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups").child(groupName);
//                            refSubsGroup.child(reqUserID).setValue(false);
                            mListener.rejectNotify(reqUserID, currUserId, groupName, userName, pushid, groupPushId, notifyReq, notPushId);
                            tv_approve.setBackgroundColor(Color.RED);
                            tv_approve.setEnabled(false);
                        }
                    }
                }
            });
            tv_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String reqUserID = user.userId;
                        String currUserId = user.adminUserId;
                        String groupName = user.groupName;
                        String userName = user.userName;
                        String notPushId = user.getPosition();
                        String classPushid = user.subGroupName;
                        String groupPushId = user.groupPositionId;
                        String notifyReq = user.notifyCategory;
                        String classUni = user.classUniPushId;

                        Log.d("APPROVE", "onClick: " + "reqUserID: " + reqUserID + "\ncurrUserId: " + currUserId +
                                "\ngroupName: " + groupName + "\nuserName: " + userName + "\nnotPushId: " + notPushId + "\nclassPushid: " + classPushid +
                                "\ngroupPushId: " + groupPushId + "\nnotifyReq: " + notifyReq + "\nclassUni: " + classUni);

                        if (position != RecyclerView.NO_POSITION) {
//                            DatabaseReference refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups").child(groupName);
//                            refSubsGroup.child(reqUserID).setValue(false);
                            Log.d("USSERR", "onClick: " + userName);
                            DatabaseReference saveUserGroupClassRef = FirebaseDatabase.getInstance().getReference().child("Groups")
                                    .child("All_User_Group_Class").child(groupPushId).child(reqUserID).child("classUniPushId");
                            saveUserGroupClassRef.setValue(classUni);
                            mListener.acceptNotify(reqUserID, currUserId, groupName, userName, classPushid, groupPushId, notifyReq, notPushId, classUni);

                            ll_groupdetails.setVisibility(View.GONE);
                            tv_Groupinvite.setText("User " + userName + " request to join Sub-class : " + classPushid + " of server " + groupName + " has been approved");

//                            tv_approve.setBackgroundColor(Color.GREEN);
                            tv_reject.setEnabled(false);

                        }
                    }
                }
            });
        }
    }
}