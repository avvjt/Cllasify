package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Group;
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

public class Adaptor_JoinGroupReq extends RecyclerView.Adapter<Adaptor_JoinGroupReq.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    ProgressDialog notifyPB;
    private OnItemClickListener mListener;
    private int selectedPosition = -1;

    public interface  OnItemClickListener{


        void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_JoinGroupReq(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_childgroup, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        final String userID=currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();

        Class_Group Answers=mDatalistNew.get(position);
        Calendar calenderCC=Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
        String subGroupName=Answers.getUserName();
        String adminUserID=Answers.getUserId();
        String groupPushID=Answers.getGroupCategory();
        String groupName=Answers.getGroupName();
        String subGroupPushId=Answers.getUserEmailId();

        holder.tv_GroupTitle.setText(subGroupName);

        holder.cb_selectChildGroup.setOnCheckedChangeListener(null);
        DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("User").child("GetReq").child(adminUserID);
        DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("User").child("SubmitReq").child(userID);

        holder.cb_selectChildGroup.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            if (holder.cb_selectChildGroup.isChecked()){
//                refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);
//                refChildGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(groupSubGroup).child("SubGroup_SubsList");

                refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long noofQuesinCategory=snapshot.getChildrenCount()+1;
                        String push="Joining Reqno_"+noofQuesinCategory;

                        Class_Group  userAddComment= new Class_Group(dateTimeCC, userName, userID,adminUserID, "req_sent",subGroupName, subGroupPushId, groupName,groupPushID);
                        refjoiningReq.child(push).setValue(userAddComment);
                        refacceptingReq.child(push).setValue(userAddComment);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                Toast.makeText(context, "Child Group "+subGroupName+ " Added", Toast.LENGTH_SHORT).show();
        }
            else {
//                holder.cb_selectChildGroup.setChecked(true);

                refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long noofQuesinCategory=snapshot.getChildrenCount()+1;
                        String push="Joining Reqno_"+noofQuesinCategory;

                        Class_Group  userAddComment= new Class_Group(dateTimeCC, userName, userID,adminUserID, "req_sent",subGroupName, subGroupPushId, groupName,groupPushID);
                        refjoiningReq.child(push).setValue(userAddComment);
                        refacceptingReq.child(push).setValue(userAddComment);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                Toast.makeText(context, "Child Group "+subGroupName+ " Added", Toast.LENGTH_SHORT).show();
            }

        });


//        holder.cb_selectChildGroup.setChecked(user.isChecked());
//        holder.cb_selectChildGroup.setTag(holder.getAdapterPosition());

//        holder.cb_selectChildGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int adapterPosition = holder.getAdapterPosition();
//                if(adapterPosition!=RecyclerView.NO_POSITION){
//
//
//                    Class_Group fruits1 = (Class_Group)holder.cb_selectChildGroup.getTag();
//
//                    fruits1.setChecked(holder.cb_selectChildGroup.isChecked());
//
//                    mDatalistNew.get(position).setChecked(holder.cb_selectChildGroup.isChecked());
//
//                    String data = null;
//                    for (int j = 0; j<mDatalistNew.size(); j++){
//
//                        if (mDatalistNew.get(j).isChecked == true){
//                            data = data + "\n" + mDatalistNew.get(j).getUserName();
//                        }
//                    }
//                    Toast.makeText(context, "Selected Fruits : \n " + data, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
////        holder.checkBox.setText("Checkbox " + position);
//        holder.cb_selectChildGroup.setChecked(imageModelArrayList.get(position).getSelected());
////        holder.tvAnimal.setText(imageModelArrayList.get(position).getAnimal());
//
//        // holder.checkBox.setTag(R.integer.btnplusview, convertView);
//        holder.cb_selectChildGroup.setTag(position);
//        holder.cb_selectChildGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Integer pos = (Integer) holder.cb_selectChildGroup.getTag();
////                Toast.makeText(ctx, imageModelArrayList.get(pos).getAnimal() + " clicked!", Toast.LENGTH_SHORT).show();
//                boolean isChecked =  holder.cb_selectChildGroup.isChecked();
//                if(isChecked){
//                    //checkBox clicked and checked
//                }else{
//                    //checkBox clicked and unchecked
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_GroupTitle;

        ImageButton ib_AddChildGroup;
        CheckBox cb_selectChildGroup;

        LinearLayout ll_Group;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_Group = itemView.findViewById(R.id.ll_Group);
            tv_GroupTitle=itemView.findViewById(R.id.tv_GroupTitle);
            cb_selectChildGroup=itemView.findViewById(R.id.cb_selectChildGroup);

            ll_Group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Class_Group user = mDatalistNew.get(getAdapterPosition());
                            String groupName = user.getGroupName();
                            String subGroupName = user.getUserName();
                            String groupPushId = user.getGroupCategory();
                            String subGroupPushID = user.getUserEmailId();

                            mListener.showChildChatAdaptor(position, groupName, subGroupName,groupPushId,subGroupPushID);
                        }
                    }
                }
            });
    }



    }
}


