package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adaptor_ShowGrpClass extends RecyclerView.Adapter<Adaptor_ShowGrpClass.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    ProgressDialog notifyPB;
    DatabaseReference refUserFollowing;
    boolean subsClick=false;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{
        void JoinGroupClass(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String pushId);

//        void AddFrndDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId);
//        void FollowFriendDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_ShowGrpClass(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_group_class, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
//        String currUserID=currentUser.getUid();
        Class_Group Answers=mDatalistNew.get(position);

        String groupClassName=Answers.getUserEmailId();
//        String userID=Answers.getUserId();

        holder.tv_ClassTitle.setText(groupClassName);
//        refUserFollowing= FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(currUserID);
//
//        holder.ib_followFrnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!subsClick){
//                    refUserFollowing.child(userID).setValue(true);
//                subsClick=true;
//                }else{
//                    refUserFollowing.child(userID).setValue(null);
//                    subsClick=false;
//                }
//
//            }
//        });
//        holder.ib_AddFrnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!addFrndClick){
//                    refUserFollowing.child(userID).setValue(true);
//                    addFrndClick=true;
//                }else{
//                    refUserFollowing.child(userID).setValue(null);
//                    addFrndClick=false;
//                }
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Class_Group class_Group;
        Boolean clicked;
        DatabaseReference refLike;

        TextView tv_ClassTitle;
        Button btn_ClassAdmission,btn_ClassJoin;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_ClassTitle =itemView.findViewById(R.id.tv_ClassTitle);
            btn_ClassAdmission =itemView.findViewById(R.id.btn_ClassAdmission);
            btn_ClassJoin =itemView.findViewById(R.id.btn_ClassJoin);
//            ib_SubMenu =itemView.findViewById(R.id.ib_SubMenu);


            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String currUserID=currentUser.getUid();

            btn_ClassAdmission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String adminGroupID=user.userId;
                        String adminEmailID=user.userEmailId;
                        String adminUserName=user.userName;
                        String pushId=user.position;
                        if (!currUserID.equals(adminGroupID)) {
                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.AddFrndDialog(adminGroupID, adminEmailID, adminUserName, pushId);
                            //mListener.dislikeAns();
                        }
                        }
                    }
                }
            });

            btn_ClassJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String adminGroupID=user.userId;
                        String subGroupName=user.userEmailId;
                        String adminUserName=user.userName;
                        String pushId=user.position;
                        String groupName=user.groupName;
                        String groupPushId=user.groupCategory;

                        if (!currUserID.equals(adminGroupID)) {
                            if (position != RecyclerView.NO_POSITION) {
                                mListener.JoinGroupClass(adminGroupID,adminUserName, groupName,groupPushId,subGroupName, pushId);
                                //mListener.dislikeAns();
                            }
                        }
                    }
                }
            });

//            ib_SubMenu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    PopupMenu popupMenu=new PopupMenu(context,view);
//                    popupMenu.inflate(R.menu.groupmemberdetails);
//                    popupMenu.show();
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem menuItem) {
//                            switch(menuItem.getItemId()){
//                                case R.id.btn_FeesStatus:
//
//                                    break;
//                                case R.id.btn_SuspendStatus:
//
//                                    break;
//                                default:
//                                    return false;
//
//
//                            }
//
//
//                            return false;
//                        }
//                    });
//                }
//            });

        }



    }
}


