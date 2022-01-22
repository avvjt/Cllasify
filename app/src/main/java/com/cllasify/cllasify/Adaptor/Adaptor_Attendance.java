package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
//        String groupName=Answers.getGroupName();
//        String subGroupName=Answers.getUserEmailId();
        String attendStatus=Answers.getGrpJoiningStatus();
//        String attendDate=Answers.getDateTime();


//        holder.tv_groupName.setText(groupName);
        holder.tv_UserName.setText(studentName);
        holder.tv_AttendStatus.setText(attendStatus);
//        holder.tv_subGroupName.setText(subGroupName);
//        holder.tv_AttendDate.setText(attendDate);

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_groupName,tv_UserName,tv_AttendStatus;
        TextView tv_subGroupName,tv_AttendDate;


        public MyViewHolder(View itemView) {
            super(itemView);

//            tv_groupName = itemView.findViewById(R.id.tv_groupName);
            tv_UserName = itemView.findViewById(R.id.tv_UserName);
            tv_AttendStatus = itemView.findViewById(R.id.tv_AttendStatus);
//            tv_subGroupName = itemView.findViewById(R.id.tv_subGroupName);
//            tv_AttendDate = itemView.findViewById(R.id.tv_AttendDate);
//
//            ll_Group.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//
//
//                            Class_Group user = mDatalistNew.get(getAdapterPosition());
//                            String groupName = user.getGroupName();
//                            String subGroupName = user.getUserName();
//                            String groupPushId = user.getGroupCategory();
//                            String subGroupPushID = user.getUserEmailId();
//
//                            mListener.showChildChatAdaptor(position, groupName, subGroupName,groupPushId,subGroupPushID);
//                        }
//                    }
//                }
//            });

        }
    }
}


