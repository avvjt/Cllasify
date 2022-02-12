package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.Group_Students;
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

public class Adaptor_ShowGrpMember extends RecyclerView.Adapter<Adaptor_ShowGrpMember.MyViewHolder> {

    private Context context;
    private List<Class_Student_Details> mDatalistNew;
    ProgressDialog notifyPB;
    DatabaseReference refUserFollowing;
    boolean subsClick=false;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{

        void AddFrndDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId);
        void FollowFriendDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId);


        void MemberProfile(String memberUserId,String memberUserName);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_ShowGrpMember(Context context, List<Class_Student_Details> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_group_member, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Toast.makeText(context, "Friend List", Toast.LENGTH_SHORT).show();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String currUserID=currentUser.getUid();
        Class_Student_Details Answers=mDatalistNew.get(position);

        String userName=Answers.getUserName();
        String userID=Answers.getUserId();

/*
        holder.ib_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Groups").child("All_Universal_Group").child(Answers.getGroupName()).child("User_Subscribed_Groups").child(Answers.getUserId());
                databaseReference.child("Attendance").setValue("Present");

            }
        });

        holder.ib_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Groups").child("All_Universal_Group").child(Answers.getGroupName()).child("User_Subscribed_Groups").child(Answers.getUserId());
                databaseReference.child("Attendance").setValue("Absent");

            }
        });
*/
        holder.tv_GroupTitle.setText(userName);
        refUserFollowing= FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(currUserID);
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


    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_GroupTitle;
        LinearLayout ll_Group;

        Class_Group class_Group;
        Boolean clicked;
        DatabaseReference refLike;
        ImageButton ib_present,ib_absent;

        ImageButton ib_followFrnd,ib_AddFrnd,ib_SubMenu;
        CircleImageView civ_UserProfilePic;

        public MyViewHolder(View itemView) {
            super(itemView);

            ib_present = itemView.findViewById(R.id.ib_present);
            ib_absent = itemView.findViewById(R.id.ib_absent);

            tv_GroupTitle =itemView.findViewById(R.id.tv_classGroupTitle);
            ib_followFrnd =itemView.findViewById(R.id.ib_followFrnd);
            ib_AddFrnd =itemView.findViewById(R.id.ib_AddFrnd);
            ib_SubMenu =itemView.findViewById(R.id.ib_SubMenu);
            ll_Group =itemView.findViewById(R.id.ll_Group);

            civ_UserProfilePic =itemView.findViewById(R.id.civ_UserProfilePic);


            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String currUserID=currentUser.getUid();
/*
            ib_AddFrnd.setOnClickListener(new View.OnClickListener() {
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
                            mListener.AddFrndDialog(adminGroupID, adminEmailID, adminUserName, pushId);
                            //mListener.dislikeAns();
                        }
                        }
                    }
                }
            });
            ib_followFrnd.setOnClickListener(new View.OnClickListener() {
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
                                mListener.FollowFriendDialog(adminGroupID, adminEmailID, adminUserName, pushId);
                                //mListener.dislikeAns();
                            }
                        }
                    }
                }
            });
            */
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

            ib_SubMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu=new PopupMenu(context,view);
                    popupMenu.inflate(R.menu.groupmemberdetails);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch(menuItem.getItemId()){
                                case R.id.btn_FeesStatus:

                                    break;
                                case R.id.btn_SuspendStatus:

                                    break;
                                default:
                                    return false;


                            }


                            return false;
                        }
                    });
                }
            });

        }



    }
}


