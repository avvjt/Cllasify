package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adaptor_ShowGrpMember_Serv extends RecyclerView.Adapter<Adaptor_ShowGrpMember_Serv.MyViewHolder> {

    private Context context;
    private List<Class_Student_Details> mDatalistNew;
    ProgressDialog notifyPB;
    DatabaseReference refUserFollowing;
    boolean subsClick = false;
    private OnItemClickListener mListener;

    public void removeItem(int position) {
        mDatalistNew.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Class_Student_Details item, int position) {
        mDatalistNew.add(position, item);
        notifyItemInserted(position);
    }

    public List<Class_Student_Details> getData() {
        return mDatalistNew;
    }

    public void setClassStudents(List<Class_Student_Details> mDatalistNew) {
        this.mDatalistNew = mDatalistNew;
    }

    public interface OnItemClickListener {

        void removeStudent(String groupPushId, String classUniPushId, String studentUserId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adaptor_ShowGrpMember_Serv(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_group_member_serv, parent, false);
        return new MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Toast.makeText(context, "Friend List", Toast.LENGTH_SHORT).show();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String currUserID = currentUser.getUid();
        Class_Student_Details Answers = mDatalistNew.get(position);

        String userName = Answers.getUserName();
        String userID = Answers.getUserId();

        holder.tv_GroupTitle.setText(userName);
        refUserFollowing = FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(currUserID);
/*
        DatabaseReference refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){

                    if (snapshot.child("profilePic").exists()){
                        String profilePic=snapshot.child("profilePic").getValue().toString();
                        Glide.with(context).load(profilePic).into(holder.civ_UserProfilePic);
                    }
//                    else{
//                        Picasso.get().load(userPhoto).into(prof_pic);
//                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_GroupTitle;
        ImageButton memberDelete;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_GroupTitle = itemView.findViewById(R.id.tv_classGroupTitle);
            memberDelete = itemView.findViewById(R.id.memberDelete);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String currUserID = currentUser.getUid();


            memberDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int studPos = getAdapterPosition();
                    Class_Student_Details class_student_details = mDatalistNew.get(studPos);
                    Log.d("MEMBERMATCH", "onClick: " + class_student_details.getUserId() + "\tCurrentUserId: " + currUserID);

                    if (class_student_details.getUserId().equals(currUserID)) {

                        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(context);
                        alertdialogbuilder.setTitle("You are admin!!!")
                                .setMessage("You cannot remove yourself from the School")
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = alertdialogbuilder.create();
                        alert.show();

                    } else {


                        String userID = SharePref.getDataFromPref(Constant.USER_ID);
                        DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

                        posTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (mListener != null) {
                                    int studPos = getAdapterPosition();
                                    Class_Student_Details class_student_details = mDatalistNew.get(studPos);
                                    String groupPushId = String.valueOf(snapshot.child("clickedGroupPushId").getValue());
                                    String classUniPushId = String.valueOf(snapshot.child("clickedStudentUniPushClassId").getValue());
                                    String studentUniPush = class_student_details.getUserId();
                                    String studentName = class_student_details.getUserName();

                                    mListener.removeStudent(groupPushId, classUniPushId, studentUniPush);

                                    mDatalistNew.remove(studPos);
                                    notifyItemRemoved(studPos);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                }
            });

            /*
//            onFriendClick
                        ll_Group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context.getApplicationContext(), "Clicked Friend", Toast.LENGTH_SHORT).show();
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Student_Details user = mDatalistNew.get(getAdapterPosition());
                        String memberUserId=user.userId;
                        String memberUserName=user.userName;

                        if (!currUserID.equals(memberUserId)) {
                            if (position != RecyclerView.NO_POSITION) {
//                                String adminUserName=user.userName;


                                mListener.MemberProfile(memberUserId,memberUserName);
                                //mListener.dislikeAns();
                            }
                        }
                    }
                }
            });
             */


        }


    }
}


