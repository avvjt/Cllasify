package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

public class Adaptor_Attendance extends RecyclerView.Adapter<Adaptor_Attendance.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    ProgressDialog notifyPB;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{


        void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_Attendance(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_attendanceh, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userID=currentUser.getUid();
        Class_Group Answers=mDatalistNew.get(position);

        String studentName=Answers.getUserName();
        String attendStatus=Answers.getGrpJoiningStatus();
        String memberUserId = Answers.getUserId();


        DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(memberUserId);
        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Name").exists()) {
                    holder.tv_UserName.setText(snapshot.child("Name").getValue().toString());
                }
                if (snapshot.child("uniqueUserName").exists()) {
                    holder.tv_unique_userName.setText(snapshot.child("uniqueUserName").getValue().toString());
                }
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

        holder.tv_AttendStatus.setText(attendStatus);

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_UserName,tv_AttendStatus;
        TextView tv_unique_userName;
        ImageView profilePic;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_UserName = itemView.findViewById(R.id.tv_UserName);
            tv_AttendStatus = itemView.findViewById(R.id.tv_AttendStatus);
            tv_unique_userName = itemView.findViewById(R.id.tv_unique_userName);
            profilePic = itemView.findViewById(R.id.civ_UserProfilePic);
        }
    }
}


