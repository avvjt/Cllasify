package com.cllasify.cllasify.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.Utility.Constant;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor_ShowGrpMember_Serv extends RecyclerView.Adapter<Adaptor_ShowGrpMember_Serv.MyViewHolder> {

    private Context context;
    private List<Class_Student_Details> mDatalistNew;
    ProgressDialog notifyPB;
    DatabaseReference refUserFollowing;
    boolean subsClick = false;
    private OnItemClickListener mListener;

    int lastPosition = -1;

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

        void removeTeacher(String groupPushId, String studentUserId);
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

//        Toast.makeText(context, "Friend List", Toast.LENGTH_SHORT).show();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String currUserID = currentUser.getUid();
        Class_Student_Details Answers = mDatalistNew.get(position);

        String userName = Answers.getUserName();
        String userID = Answers.getUserId();

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();

        DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Name").exists()) {
                    holder.tv_GroupTitle.setText(snapshot.child("Name").getValue().toString());
                }
                if (snapshot.child("uniqueUserName").exists()) {
                    holder.tv_userName.setText(snapshot.child("uniqueUserName").getValue().toString());
                }
                if (snapshot.child("profilePic").exists()) {
                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                    Glide.with(context.getApplicationContext()).load(profilePicUrl).into(holder.civ_UserProfilePic);
                } else {
                    Glide.with(context.getApplicationContext()).load(R.drawable.maharaji).into(holder.civ_UserProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        TextView tv_GroupTitle, tv_userName;
        ImageButton memberDelete;
        CircleImageView civ_UserProfilePic;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_userName = itemView.findViewById(R.id.tv_unique_userName);
            tv_GroupTitle = itemView.findViewById(R.id.tv_classGroupTitle);
            memberDelete = itemView.findViewById(R.id.memberDelete);
            civ_UserProfilePic = itemView.findViewById(R.id.civ_UserProfilePic);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String currUserID = currentUser.getUid();


            memberDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int studPos = getAdapterPosition();
                    Class_Student_Details class_student_details = mDatalistNew.get(studPos);


                    if (class_student_details.getUserId().equals(currUserID)) {

                        showToast();

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
                                    String classUniPushId = String.valueOf(snapshot.child("uniPushClassId").getValue());
                                    String studentUniPush = class_student_details.getUserId();
                                    String studentName = class_student_details.getUserName();


                                    DatabaseReference checkAdmin = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins")
                                            .child(groupPushId).child("classAdminList").child(studentUniPush).child("userId");

                                    checkAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue() == studentUniPush) {
                                                Log.d("STUTECH", "onDataChange: " + snapshot.getValue());
                                                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(context);
                                                alertdialogbuilder.setTitle("You are admin!!!")
                                                        .setMessage("Do you really want to remove this teacher")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Yes",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        mListener.removeTeacher(groupPushId, studentUniPush);
                                                                        mDatalistNew.remove(studPos);
                                                                        notifyItemRemoved(studPos);
                                                                        dialog.dismiss();
                                                                    }
                                                                })
                                                .setNegativeButton("No",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                                AlertDialog alert = alertdialogbuilder.create();
                                                alert.show();

                                            } else {

                                                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(context);
                                                alertdialogbuilder.setTitle("You are admin!!!")
                                                        .setMessage("Do you really want to remove this student")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        mListener.removeStudent(groupPushId, classUniPushId, studentUniPush);
                                                                        mDatalistNew.remove(studPos);
                                                                        notifyItemRemoved(studPos);
                                                                        dialog.dismiss();
                                                                    }
                                                                })
                                                        .setNegativeButton("No",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.cancel();
                                                                    }
                                                                });
                                                AlertDialog alert = alertdialogbuilder.create();
                                                alert.show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                }
            });


        }

        public void showToast() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.toast_cant_remove, null);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }
}


