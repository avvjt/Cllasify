package com.cllasify.cllasify.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.ModelClasses.Class_Group_Names;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adaptor_ShowGrpClass extends RecyclerView.Adapter<Adaptor_ShowGrpClass.MyViewHolder> {

    private Context context;
    private List<Class_Group_Names> mDatalistNew;
    ProgressDialog notifyPB;
    DatabaseReference refUserFollowing;
    boolean subsClick = false;
    private OnItemClickListener mListener;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String currUserID;

    public interface OnItemClickListener {
        /*
        void JoinGroupClass(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String pushId, String classPushId, String classReqPosition);

        void admissionClass(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String adminEmailId);
*/
        void joinAndAdmission(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String pushId, String classPushId, String classReqPosition);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adaptor_ShowGrpClass(Context context, List<Class_Group_Names> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_server_class, parent, false);
        return new MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String currUserID = currentUser.getUid();
        Class_Group_Names Answers = mDatalistNew.get(position);

        String groupClassName = Answers.getClassName();
        Log.d("Grouupt", "GroupName: " + groupClassName);


        String classPushId = Answers.getUniPushClassId();
        String groupPushId = Answers.getGroupPushId();




        holder.tv_ClassTitle.setText(groupClassName);

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        Class_Group class_Group;
        Boolean clicked;
        DatabaseReference refLike;
        RelativeLayout joining_rl;

        TextView tv_ClassTitle;
        Button btn_ClassAdmission, btn_ClassJoin;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_ClassTitle = itemView.findViewById(R.id.tv_ClassTitle);
            btn_ClassAdmission = itemView.findViewById(R.id.btn_ClassAdmission);
            btn_ClassJoin = itemView.findViewById(R.id.btn_ClassJoin);
            joining_rl = itemView.findViewById(R.id.joining_rl);


            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            currUserID = currentUser.getUid();

            joining_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    assert currentUser != null;
                    String userID = currentUser.getUid();

                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group_Names classGroupNames = mDatalistNew.get(position);
                        String className = classGroupNames.getClassName();


                        DatabaseReference databaseReferenceGetPush = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

                        databaseReferenceGetPush.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String groupPushId = snapshot.child("clickedJoinSearchGroup").getValue().toString();


                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group")
                                        .child(groupPushId);

                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String adminGroupID = snapshot.child("userId").getValue().toString();
                                        String adminUserName = snapshot.child("userName").getValue().toString();
                                        String groupName = snapshot.child("groupName").getValue().toString();

                                        Log.d("JOIN", "adminGroupID: " + adminGroupID + "\nsubGroupName: " + className +
                                                "\nadminUserName: " + adminUserName + "\ngroupName: " + groupName + "\ngroupPushId: " + groupPushId + "\nClass position: " + position);

                                        String classReqPosition = String.valueOf(position);
                                        String classUniPush = classGroupNames.getUniPushClassId();

                                        if (!(classUniPush.equals("null"))) {
                                            Log.d("GRPPush", "Class Uni Group Push Id is: " + classUniPush);
                                            mListener.joinAndAdmission(adminGroupID, adminUserName, groupName, groupPushId, className, "pushId", classUniPush, classReqPosition);
                                        } else {
                                            Log.d("GRPPush", "Class Uni Group Push Id is: null");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            });

/*
            btn_ClassAdmission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group_Names classGroupNames = mDatalistNew.get(position);
                        String className = classGroupNames.getClassName();
                        String groupPushId = classGroupNames.getGroupPushId();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group")
                                .child(groupPushId);

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String adminUserId = snapshot.child("userId").getValue().toString();
                                String adminUserName = snapshot.child("userName").getValue().toString();
                                String groupName = snapshot.child("groupName").getValue().toString();

                                DatabaseReference addmissionDR = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration")
                                        .child(adminUserId);

                                addmissionDR.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot01) {
                                        //userEmailId
                                        String adminEmailId = snapshot01.child("userEmailId").getValue().toString();
                                        Log.d("ADMISSION", "adminUserId: " + adminUserId + "\nuserEmailId: " + adminEmailId + "\nsubGroupName: " + className +
                                                "\nadminUserName: " + adminUserName + "\ngroupName: " + groupName + "\ngroupPushId: " + groupPushId + "\nClass position: " + position);


                                        String userID = currentUser.getUid();

                                        DatabaseReference databaseReferenceTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID)
                                                .child("classPos");
                                        databaseReferenceTemp.setValue(position);

                                        //Push Id lochaaa
                                        if (snapshot.child("ServerEmail").exists()) {
                                            String serverEmailId = snapshot.child("ServerEmail").getValue().toString();
                                            mListener.admissionClass(adminUserId, adminUserName, groupName, groupPushId, className, serverEmailId);
                                        } else {
                                            showToast();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                }
            });

            btn_ClassJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    assert currentUser != null;
                    String userID = currentUser.getUid();

                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group_Names classGroupNames = mDatalistNew.get(position);
                        String className = classGroupNames.getClassName();


                        DatabaseReference databaseReferenceGetPush = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

                        databaseReferenceGetPush.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String groupPushId = snapshot.child("clickedJoinSearchGroup").getValue().toString();


                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group")
                                        .child(groupPushId);

                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String adminGroupID = snapshot.child("userId").getValue().toString();
                                        String adminUserName = snapshot.child("userName").getValue().toString();
                                        String groupName = snapshot.child("groupName").getValue().toString();

                                        Log.d("JOIN", "adminGroupID: " + adminGroupID + "\nsubGroupName: " + className +
                                                "\nadminUserName: " + adminUserName + "\ngroupName: " + groupName + "\ngroupPushId: " + groupPushId + "\nClass position: " + position);

                                        String classReqPosition = String.valueOf(position);
                                        String classUniPush = classGroupNames.getUniPushClassId();

                                        if (!(classUniPush.equals("null"))) {
                                            Log.d("GRPPush", "Class Uni Group Push Id is: " + classUniPush);
                                            mListener.JoinGroupClass(adminGroupID, adminUserName, groupName, groupPushId, className, "pushId", classUniPush, classReqPosition);
                                        } else {
                                            Log.d("GRPPush", "Class Uni Group Push Id is: null");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }
            });
*/

        }


    }

    public void showToast() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_school_email, null);
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}


