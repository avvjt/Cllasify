package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor_ShowGroup extends RecyclerView.Adapter<Adaptor_ShowGroup.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{
        void listitem(int position, String question, String pushQues, String pushAns,String category);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_ShowGroup(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_group_dashboard, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Class_Group class_GroupDetails=mDatalistNew.get(position);

        String userGroupName=class_GroupDetails.getGroupName();
        String userComment=class_GroupDetails.getGroupCategory();
        String otherUserComment=class_GroupDetails.getGroupOtherUserCmnt();
        String databaseUserId=class_GroupDetails.getUserId();
        String datetime=class_GroupDetails.getDateTime();
        String[] parts=datetime.split(" ");

        DateFormat outputformat=new SimpleDateFormat("KK:mm a");
        SimpleDateFormat parseformaat=new SimpleDateFormat("hh:mm:ss");

        String display= null;
        try {
            Date dt=parseformaat.parse(parts[1]);
            display = outputformat.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            assert mUser != null;
            String userId = mUser.getUid();

        if (false) {

            holder.ll_chat.setVisibility(View.GONE);


        }else{

        if(userId.equals(databaseUserId)) {

            holder.tv_MyMsz.setText(userComment);
            holder.tvMyDateTime.setText(display);
            holder.ll_OtherMsz.setVisibility(View.GONE);
            }
        else {
                holder.tv_OtherMsz.setText(userComment);
                holder.tvOtherDateTime.setText(display);
            holder.tv_MyMsz.setVisibility(View.GONE);
/*
            DatabaseReference refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(databaseUserId);
            refUserStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){

                        if (snapshot.child("profilePic").exists()){
                            String profilePic=snapshot.child("profilePic").getValue().toString();
                            Glide.with(context).load(profilePic).into(holder.tv_OtherProfPic);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            */
        }
        }

    }

    @Override
    public int getItemCount() {

        return mDatalistNew.size();
    }

    public void scrollDownl(){
        Server_Activity.rv_ChatDashboard.smoothScrollToPosition(mDatalistNew.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_MyMsz,tv_OtherMsz,tvMyDateTime,tvOtherDateTime;
        CircleImageView tv_OtherProfPic;
        LinearLayout ll_MyMsz,ll_OtherMsz,ll_chat;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_MyMsz=itemView.findViewById(R.id.tv_MyMsz);
            tv_OtherMsz = itemView.findViewById(R.id.tv_OtherMsz);
            tvOtherDateTime = itemView.findViewById(R.id.tvOtherDateTime);
            tvMyDateTime = itemView.findViewById(R.id.tvMyDateTime);
            tv_OtherProfPic = itemView.findViewById(R.id.tv_OtherProfPic);
            ll_MyMsz = itemView.findViewById(R.id.ll_MyMsz);
            ll_OtherMsz = itemView.findViewById(R.id.ll_OtherMsz);
            ll_chat = itemView.findViewById(R.id.ll_chat);

        }
    }
}


