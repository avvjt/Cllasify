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

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Profile.AccountSetting_Activity;
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

public class Adaptor_Notify extends RecyclerView.Adapter<Adaptor_Notify.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void createGroupDialog(String adminGroupID, String groupName);

        void rejectNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId);

        void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId, String classUniPush);

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

        String reqUserID = class_GroupDetails.userId;
        DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(reqUserID);
        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("profilePic").exists()) {
                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                    Glide.with(context.getApplicationContext()).load(profilePicUrl).into(holder.profilePic);
                }else{
                    Glide.with(context.getApplicationContext()).load(R.drawable.maharaji).into(holder.profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                holder.username.setText(userName);
                holder.tv_Groupinvite.setText("User " + userName + " request for Add you as a friend has been approved");
                holder.tv_ReqDate.setVisibility(View.GONE);
                holder.ll_groupdetails.setVisibility(View.GONE);
            } else if (inviteStatus.equals("Reject")) {
                holder.username.setText(userName);
                holder.ll_groupdetails.setVisibility(View.GONE);
                holder.tv_ReqDate.setVisibility(View.GONE);
                holder.tv_Groupinvite.setText("User " + userName + " request for Add you as a friend has been rejected");
            } else {

                holder.ll_groupdetails.setVisibility(View.VISIBLE);
                if (GroupName.isEmpty()) {
                    holder.tv_Groupinvite.setVisibility(View.GONE);
                } else {
                    holder.username.setText(userName);
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
        servName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {

                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                        assert mUser != null;
                        String userId = mUser.getUid();
                        Log.d("NOTI00", "onBindViewHolder: yessssssss!!!!!" + NotifyCategory);
                        if (NotifyCategory.equals("Group_JoiningReq_Teacher")) {

                            if (inviteStatus.equals("Approve")) {
                                holder.username.setText(userName);
                                holder.tv_ReqDate.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setText(GroupName+" joining has been approved");
                                holder.ll_groupdetails.setVisibility(View.GONE);
                            } else if (inviteStatus.equals("Reject")) {
                                holder.username.setText(userName);
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_ReqDate.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setText(GroupName+" joining has been rejected");
                            } else {

                                holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                if (GroupName.isEmpty()) {
                                    holder.tv_Groupinvite.setVisibility(View.GONE);
                                } else {
                                    holder.username.setText(userName);
                                    holder.tv_Groupinvite.setText("Wants to join");

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
                                holder.tv_Groupinvite.setText(GroupName+" joining has been approved");
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_ReqDate.setVisibility(View.GONE);
                                holder.username.setText(userName);

                            } else if (inviteStatus.equals("Reject")) {
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_ReqDate.setVisibility(View.GONE);
                                holder.username.setText(userName);
                                holder.tv_Groupinvite.setText(GroupName+" joining has been rejected");
                            } else {

                                holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                if (GroupName.isEmpty()) {
                                    holder.tv_Groupinvite.setVisibility(View.GONE);
                                } else {
                                    holder.username.setText(userName);
                                    holder.tv_Groupinvite.setText("Wants to join");

                                }
                                if (GroupName.isEmpty()) {
                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                } else {
                                    holder.tv_ReqDate.setText("Requested on : " + reqDate);
                                }
                            }
                        }
                    if (NotifyCategory.equals("Friend_Request")) {

                        if (inviteStatus.equals("Approve")) {
                            holder.username.setText(userName);
                            holder.tv_Groupinvite.setText("Follow request has been approved");
                            holder.tv_ReqDate.setVisibility(View.GONE);
                            holder.ll_groupdetails.setVisibility(View.GONE);
                        } else if (inviteStatus.equals("Reject")) {
                            holder.username.setText(userName);
                            holder.ll_groupdetails.setVisibility(View.GONE);
                            holder.tv_ReqDate.setVisibility(View.GONE);
                            holder.tv_Groupinvite.setText("Follow request has been rejected");
                        } else {
                            holder.ll_groupdetails.setVisibility(View.VISIBLE);
                            if (GroupName.isEmpty()) {
                                holder.tv_Groupinvite.setVisibility(View.GONE);
                            } else {
                                holder.username.setText(userName);
                                holder.tv_Groupinvite.setText("Follow request");
                            }
                            if (GroupName.isEmpty()) {
                                holder.tv_ReqDate.setVisibility(View.GONE);
                            } else {
                                holder.tv_ReqDate.setText("Requested on : " + reqDate);
                            }
                        }
                    }
                        else if (NotifyCategory.equals("Follow_Request")) {

                            if (inviteStatus.equals("Approve")) {
                                holder.tv_Groupinvite.setText("Follow request has been approved");
                                holder.tv_ReqDate.setVisibility(View.GONE);
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.username.setText(userName);
                            } else if (inviteStatus.equals("Reject")) {
                                holder.username.setText(userName);
                                holder.ll_groupdetails.setVisibility(View.GONE);
                                holder.tv_ReqDate.setVisibility(View.GONE);
                                holder.tv_Groupinvite.setText("Follow request has been rejected");
                            } else {
                                holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                if (GroupName.isEmpty()) {
                                    holder.tv_Groupinvite.setVisibility(View.GONE);
                                } else {
                                    holder.username.setText(userName);
                                    holder.tv_Groupinvite.setText("Follow request");
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

    }


    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Groupinvite, tv_approve, tv_reject, tv_ReqDate, username;
        LinearLayout ll_groupdetails;
        CircleImageView profilePic;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_Groupinvite = itemView.findViewById(R.id.tv_Groupinvite);
            tv_approve = itemView.findViewById(R.id.tv_approve);
            tv_reject = itemView.findViewById(R.id.tv_reject);
            tv_ReqDate = itemView.findViewById(R.id.tv_ReqDate);
            ll_groupdetails = itemView.findViewById(R.id.ll_groupdetails);
            profilePic = itemView.findViewById(R.id.profilePicture);
            username = itemView.findViewById(R.id.username);


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
                        String classUni = user.classUniPushId;

                        ll_groupdetails.setVisibility(View.GONE);

                        if (position != RecyclerView.NO_POSITION) {
//                            DatabaseReference refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups").child(groupName);
//                            refSubsGroup.child(reqUserID).setValue(false);

                            Log.d("REJECT", "onClick: " + "reqUserID: " + reqUserID + "\ncurrUserId: " + currUserId +
                                    "\ngroupName: " + groupName + "\nuserName: " + userName + "\nnotPushId: " + notPushId + "\nclassPushid: "+ user.classUniPushId+
                                    "\ngroupPushId: " + groupPushId + "\nnotifyReq: " + notifyReq );

                            ll_groupdetails.setVisibility(View.GONE);
                            DatabaseReference userNoti = FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications").child(reqUserID).child(groupPushId).child(classUni);
                            userNoti.child("joiningStatus").setValue("Reject");


//                            tv_Groupinvite.setText("User " + userName + " request to join Sub-class : " + classPushid + " of server " + groupName + " has been approved");

                            mListener.rejectNotify(reqUserID, currUserId, groupName, userName, pushid, groupPushId, notifyReq, notPushId);
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

                            DatabaseReference addedOrJoinedGroups = FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp").child(reqUserID).child(groupPushId).child("addedOrJoined");
//
                            addedOrJoinedGroups.setValue("StudentJoin");

                            DatabaseReference userNoti = FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications").child(reqUserID).child(groupPushId).child(classPushid);

                            userNoti.child("joiningStatus").setValue("Approve");


                            Log.d("ACCEPTTT", "acceptNotify: "+reqUserID+groupPushId);

                            mListener.acceptNotify(reqUserID, currUserId, groupName, userName, classPushid, groupPushId, notifyReq, notPushId, classUni);

                            ll_groupdetails.setVisibility(View.GONE);
//                            tv_Groupinvite.setText("User " + userName + " request to join Sub-class : " + classPushid + " of server " + groupName + " has been approved");

                            tv_reject.setEnabled(false);

                        }
                    }
                }
            });
        }
    }
}