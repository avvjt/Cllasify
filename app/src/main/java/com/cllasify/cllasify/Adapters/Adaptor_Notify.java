package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.ModelClasses.Class_Admission;
import com.cllasify.cllasify.ModelClasses.Class_Group;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor_Notify extends RecyclerView.Adapter<Adaptor_Notify.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID;

    private OnItemClickListener mListener;

    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());


    public interface OnItemClickListener {
        void createGroupDialog(String adminGroupID, String groupName);

        void rejectNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId);

        void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId, String classUniPush);

        void reqClick(String reqUserID, String currUserId, String groupName, String userName, String notPushId, String classUniPushId, String groupPushId, String notifyReq, String classPushid, String pushid, String grpJoiningStatus);

        void reqAdmissionClick(Class_Admission class_admission, String reqUserID, String currUserId, String groupName, String userName, String notPushId, String classUniPushId, String groupPushId, String notifyReq, String classPushid, String pushid);

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

        holder.tv_ReqDate.setText(reqDate);


        String reqUserID = class_GroupDetails.userId;
        DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(reqUserID);
        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                //Setting name
                if (NotifyCategory.equals("Friend_Request")) {


                    if (inviteStatus.equals("Approve")) {

                        holder.btn_Status.setText("Approved");


                        if (snapshot.child("Name").exists()) {
                            String userNameRealtime = snapshot.child("Name").getValue().toString();
                            holder.username.setText(userNameRealtime);
                            holder.tv_Groupinvite.setText("User " + userNameRealtime + " request for Add you as a friend has been approved");
                        }
//                        holder.tv_ReqDate.setVisibility(View.GONE);
//                        holder.ll_groupdetails.setVisibility(View.GONE);
                    } else if (inviteStatus.equals("Reject")) {
                        holder.btn_Status.setText("Rejected");


                        if (snapshot.child("Name").exists()) {
                            String userNameRealtime = snapshot.child("Name").getValue().toString();
                            holder.username.setText(userNameRealtime);
                            holder.tv_Groupinvite.setText("User " + userNameRealtime + " request for Add you as a friend has been rejected");
                        }
//                        holder.ll_groupdetails.setVisibility(View.GONE);
//                        holder.tv_ReqDate.setVisibility(View.GONE);

                    } else {
                        holder.btn_Status.setText("Pending");


//                        holder.ll_groupdetails.setVisibility(View.VISIBLE);
                        if (GroupName.isEmpty()) {
                            holder.tv_Groupinvite.setVisibility(View.GONE);
                        } else {
                            if (snapshot.child("Name").exists()) {
                                String userNameRealtime = snapshot.child("Name").getValue().toString();
                                holder.username.setText(userNameRealtime);
                                holder.tv_Groupinvite.setText("User " + userNameRealtime + " wants to add you as a Friend");
                            }

                        }
                        if (GroupName.isEmpty()) {
//                            holder.tv_ReqDate.setVisibility(View.GONE);
                        } else {
                        }
                    }
                }


                DatabaseReference servName = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                servName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot01) {
                        if (snapshot01.getChildrenCount() > 0) {

                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert mUser != null;
                            String userId = mUser.getUid();
                            Log.d("NOTI00", "onBindViewHolder: yessssssss!!!!!" + NotifyCategory);


                            if (NotifyCategory.equals("AdmissionAccepted")) {

                                holder.btn_Status.setText("Accepted");

                                if (snapshot.child("Name").exists()) {
                                    String userNameRealtime = snapshot.child("Name").getValue().toString();
                                    holder.username.setText(userNameRealtime);
                                }

                            }

                            if (NotifyCategory.equals("AdmissionRejected")) {

                                    holder.btn_Status.setText("Rejected");

                                if (snapshot.child("Name").exists()) {
                                    String userNameRealtime = snapshot.child("Name").getValue().toString();
                                    holder.username.setText(userNameRealtime);
                                }
                            }


                            if (NotifyCategory.equals("Group_JoiningReq_Teacher")) {

                                if (inviteStatus.equals("Approve")) {

                                    holder.btn_Status.setText("Approved");


                                    if (snapshot.child("Name").exists()) {
                                        String userNameRealtime = snapshot.child("Name").getValue().toString();
                                        holder.username.setText(userNameRealtime);
                                    }

//                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                    holder.tv_Groupinvite.setText(GroupName + " joining has been approved");
//                                    holder.ll_groupdetails.setVisibility(View.GONE);
                                } else if (inviteStatus.equals("Reject")) {

                                    holder.btn_Status.setText("Rejected");


                                    if (snapshot.child("Name").exists()) {
                                        String userNameRealtime = snapshot.child("Name").getValue().toString();
                                        holder.username.setText(userNameRealtime);
                                    }
//                                    holder.ll_groupdetails.setVisibility(View.GONE);
//                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                    holder.tv_Groupinvite.setText(GroupName + " joining has been rejected");
                                } else {

                                    holder.btn_Status.setText("Pending");


//                                    holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                    if (GroupName.isEmpty()) {
                                        holder.tv_Groupinvite.setVisibility(View.GONE);
                                    } else {
                                        if (snapshot.child("Name").exists()) {
                                            String userNameRealtime = snapshot.child("Name").getValue().toString();
                                            holder.username.setText(userNameRealtime);
                                        }
                                        holder.tv_Groupinvite.setText("Wants to join");

                                    }
                                    if (GroupName.isEmpty()) {
//                                        holder.tv_ReqDate.setVisibility(View.GONE);
                                    } else {
//                                        holder.tv_ReqDate.setText("Requested on : " + reqDate);
                                    }
                                }
                            }
                            if (NotifyCategory.equals("Group_JoiningReq")) {

                                if (inviteStatus.equals("Approve")) {

                                    holder.btn_Status.setText("Approved");


                                    holder.tv_Groupinvite.setText(GroupName + " joining has been approved");
//                                    holder.ll_groupdetails.setVisibility(View.GONE);
//                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                    if (snapshot.child("Name").exists()) {
                                        String userNameRealtime = snapshot.child("Name").getValue().toString();
                                        holder.username.setText(userNameRealtime);
                                    }

                                } else if (inviteStatus.equals("Reject")) {

                                    holder.btn_Status.setText("Rejected");


//                                    holder.ll_groupdetails.setVisibility(View.GONE);
//                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                    if (snapshot.child("Name").exists()) {
                                        String userNameRealtime = snapshot.child("Name").getValue().toString();
                                        holder.username.setText(userNameRealtime);
                                    }
                                    holder.tv_Groupinvite.setText(GroupName + " joining has been rejected");
                                } else {


                                    holder.btn_Status.setText("Pending");


//                                    holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                    if (GroupName.isEmpty()) {
                                        holder.tv_Groupinvite.setVisibility(View.GONE);
                                    } else {
                                        if (snapshot.child("Name").exists()) {
                                            String userNameRealtime = snapshot.child("Name").getValue().toString();
                                            holder.username.setText(userNameRealtime);
                                        }
                                        holder.tv_Groupinvite.setText("Wants to join");

                                    }
                                    if (GroupName.isEmpty()) {
//                                        holder.tv_ReqDate.setVisibility(View.GONE);
                                    } else {
//                                        holder.tv_ReqDate.setText("Requested on : " + reqDate);
                                    }
                                }
                            }
                            if (NotifyCategory.equals("Group_AdmissionReq")) {


                                if (inviteStatus.equals("Approve")) {

                                    holder.btn_Status.setText("Approved");

                                    holder.tv_Groupinvite.setText(GroupName + " joining has been approved");
//                                    holder.ll_groupdetails.setVisibility(View.GONE);
//                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                    if (snapshot.child("Name").exists()) {
                                        String userNameRealtime = snapshot.child("Name").getValue().toString();
                                        holder.username.setText(userNameRealtime);
                                    }

                                } else if (inviteStatus.equals("Reject")) {
                                    holder.btn_Status.setText("Rejected");

                                    //                                    holder.ll_groupdetails.setVisibility(View.GONE);
//                                    holder.tv_ReqDate.setVisibility(View.GONE);
                                    if (snapshot.child("Name").exists()) {
                                        String userNameRealtime = snapshot.child("Name").getValue().toString();
                                        holder.username.setText(userNameRealtime);
                                    }
                                    holder.tv_Groupinvite.setText(GroupName + " joining has been rejected");
                                } else {
                                    holder.btn_Status.setText("Pending");

//                                    holder.ll_groupdetails.setVisibility(View.VISIBLE);
                                    if (GroupName.isEmpty()) {
                                        holder.tv_Groupinvite.setVisibility(View.GONE);
                                    } else {
                                        if (snapshot.child("Name").exists()) {
                                            String userNameRealtime = snapshot.child("Name").getValue().toString();
                                            holder.username.setText(userNameRealtime);
                                        }
                                        holder.tv_Groupinvite.setText("Wants to join");

                                    }
                                    if (GroupName.isEmpty()) {
//                                        holder.tv_ReqDate.setVisibility(View.GONE);
                                    } else {
//                                        holder.tv_ReqDate.setText("Requested on : " + reqDate);
                                    }
                                }
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                if (snapshot.child("profilePic").exists()) {
                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                    Glide.with(context.getApplicationContext()).load(profilePicUrl).into(holder.profilePic);
                } else {
                    Glide.with(context.getApplicationContext()).load(R.drawable.maharaji).into(holder.profilePic);
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
        RelativeLayout req_layout;
        Button btn_Status;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_Groupinvite = itemView.findViewById(R.id.tv_Groupinvite);
            tv_ReqDate = itemView.findViewById(R.id.tv_ReqDate);
            ll_groupdetails = itemView.findViewById(R.id.ll_groupdetails);
            profilePic = itemView.findViewById(R.id.profilePicture);
            username = itemView.findViewById(R.id.username);
            req_layout = itemView.findViewById(R.id.req_layout);
            btn_Status = itemView.findViewById(R.id.btn_Status);

            req_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        Class_Admission class_admission = user.class_admission;
                        String reqUserID = user.userId;
                        String currUserId = user.adminUserId;
                        String groupName = user.groupName;
                        String userName = user.userName;
                        String notPushId = user.getPosition();
                        String classPushid = user.subGroupName;
                        String pushid = user.subGroupName;
                        String groupPushId = user.groupPositionId;
                        String notifyReq = user.notifyCategory;
                        String classUni = user.classUniPushId;
                        String grpJoiningStatus = user.notifyCategory;

                        if (position != RecyclerView.NO_POSITION) {

                            if (reqUserID != null && notifyReq != null) {


                                if (notifyReq.equals("AdmissionAccepted")) {

                                    Log.d("ReqCLICK", "onClick: " + "\nnotifyReq: " + notifyReq);


                                    DatabaseReference refAccUserNotifyFriend = FirebaseDatabase.getInstance().getReference().
                                            child("Notification").child("Received_Req").child(userID).child(notPushId);

                                    refAccUserNotifyFriend.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            mListener.reqClick(reqUserID, currUserId, groupName, userName, notPushId, user.classUniPushId, groupPushId, notifyReq, classPushid, pushid, grpJoiningStatus);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                                if (notifyReq.equals("AdmissionRejected")) {


                                    DatabaseReference refAccUserNotifyFriend = FirebaseDatabase.getInstance().getReference().
                                            child("Notification").child("Received_Req").child(userID).child(notPushId);

                                    refAccUserNotifyFriend.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            mListener.reqClick(reqUserID, currUserId, groupName, userName, notPushId, user.classUniPushId, groupPushId, notifyReq, classPushid, pushid, grpJoiningStatus);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }


                                if (notifyReq.equals("Friend_Request")) {


                                    DatabaseReference refAccUserNotifyFriend = FirebaseDatabase.getInstance().getReference().
                                            child("Notification").child("Received_Req").child(userID).child(notPushId);

                                    refAccUserNotifyFriend.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.child("grpJoiningStatus").getValue().toString().equals("Approve")) {

                                                btn_Status.setText("Approved");


                                                Toast.makeText(context.getApplicationContext(), "Already approved", Toast.LENGTH_SHORT).show();
                                            } else if (snapshot.child("grpJoiningStatus").getValue().toString().equals("Reject")) {

                                                btn_Status.setText("Rejected");

                                                Toast.makeText(context.getApplicationContext(), "Rejected", Toast.LENGTH_SHORT).show();
                                            } else {

                                                btn_Status.setText("Pending");

                                                mListener.reqClick(reqUserID, currUserId, groupName, userName, notPushId, user.classUniPushId, groupPushId, notifyReq, classPushid, pushid, grpJoiningStatus);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                            }

                            if (reqUserID != null && groupPushId != null && classUni != null) {


                                if (notifyReq.equals("Group_AdmissionReq")) {

                                    Log.d("ReqCLICK", "onClick: " + class_admission.getName() + "\ngrpJoiningStatus: " + user.grpJoiningStatus + "\nreqUserID: " + reqUserID + "\ncurrUserId: " + currUserId +
                                            "\ngroupName: " + groupName + "\nuserName: " + userName + "\nnotPushId: " + notPushId + "\nclassUniPushId: " + user.classUniPushId +
                                            "\ngroupPushId: " + groupPushId + "\nnotifyReq: " + notifyReq + "\nclassPushid: " + classPushid + "\npushid: " + pushid);

                                    DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().
                                            child("Groups").child("All_GRPs").child(groupPushId).child(user.classUniPushId).child("groupAdmissionReqs").child(notPushId);

                                    refAccUserNotify.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                                            if (snapshot.child("grpJoiningStatus").getValue().toString().equals("Approve")) {

                                                btn_Status.setText("Approved");

                                                Toast.makeText(context.getApplicationContext(), "Already approved", Toast.LENGTH_SHORT).show();
                                            } else if (snapshot.child("grpJoiningStatus").getValue().toString().equals("Reject")) {

                                                btn_Status.setText("Rejected");


                                                Toast.makeText(context.getApplicationContext(), "Rejected", Toast.LENGTH_SHORT).show();
                                            } else {

                                                btn_Status.setText("Pending");


                                                mListener.reqAdmissionClick(class_admission, reqUserID, currUserId, groupName, userName, notPushId, user.classUniPushId, groupPushId, notifyReq, classPushid, pushid);

                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                if (notifyReq.equals("Group_JoiningReq")) {

                                    DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().
                                            child("Groups").child("All_GRPs").child(groupPushId).child(user.classUniPushId).child("groupJoiningReqs").child(notPushId);

                                    refAccUserNotify.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.child("grpJoiningStatus").getValue().toString().equals("Approve")) {

                                                btn_Status.setText("Approved");


                                                Toast.makeText(context.getApplicationContext(), "Already approved", Toast.LENGTH_SHORT).show();
                                            } else if (snapshot.child("grpJoiningStatus").getValue().toString().equals("Reject")) {

                                                btn_Status.setText("Rejected");


                                                Toast.makeText(context.getApplicationContext(), "Rejected", Toast.LENGTH_SHORT).show();
                                            } else {

                                                btn_Status.setText("Pending");


                                                mListener.reqClick(reqUserID, currUserId, groupName, userName, notPushId, user.classUniPushId, groupPushId, notifyReq, classPushid, pushid, grpJoiningStatus);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                if (notifyReq.equals("Group_JoiningReq_Teacher")) {
                                    DatabaseReference refAccUserNotifyTeacher = FirebaseDatabase.getInstance().getReference().
                                            child("Notification").child("Received_Req").child(groupPushId).child("groupTeacherJoiningReqs").child(notPushId);

                                    refAccUserNotifyTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.child("grpJoiningStatus").getValue().toString().equals("Approve")) {

                                                btn_Status.setText("Approved");


                                                Toast.makeText(context.getApplicationContext(), "Already approved", Toast.LENGTH_SHORT).show();
                                            } else if (snapshot.child("grpJoiningStatus").getValue().toString().equals("Reject")) {

                                                btn_Status.setText("Rejected");


                                                Toast.makeText(context.getApplicationContext(), "Rejected", Toast.LENGTH_SHORT).show();
                                            } else {

                                                btn_Status.setText("Pending");


                                                mListener.reqClick(reqUserID, currUserId, groupName, userName, notPushId, user.classUniPushId, groupPushId, notifyReq, classPushid, pushid, grpJoiningStatus);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                            }
                        }
                    }
                }
            });


        }
    }
}



