package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.List;

public class Adaptor_SearchGroup extends RecyclerView.Adapter<Adaptor_SearchGroup.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void createGroupDialog(String adminGroupID, String groupName, String groupPushId);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adaptor_SearchGroup(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_group_searchview, parent, false);
        return new MyViewHolder(rootview);
    }

    public void filterList(List<Class_Group> filterList) {
        mDatalistNew = filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Class_Group class_GroupDetails = mDatalistNew.get(position);

        String groupName = class_GroupDetails.getGroupName();
        String userComment = class_GroupDetails.getGroupCategory();
        String groupUserName = class_GroupDetails.getUserName();
        String pushid = class_GroupDetails.getPosition();


        String databaseUserId = class_GroupDetails.getUserId();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        String currUserId = mUser.getUid();

        if (userComment.isEmpty()) {
            holder.tv_groupname.setVisibility(View.GONE);
        } else {
            holder.tv_groupname.setText(groupName);
        }

        if (userComment.isEmpty()) {
            holder.tv_groupownername.setVisibility(View.GONE);
        } else {
            holder.tv_groupownername.setText(groupUserName);
        }



        DatabaseReference referenceALLGroup = FirebaseDatabase.getInstance().getReference().
                child("Groups").child("All_Universal_Group").child(pushid).child("User_Subscribed_Groups");
        referenceALLGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    if (snapshot.hasChild(currUserId)) {
                        holder.tv_GroupStatus.setEnabled(false);
                        String joinStatus = snapshot.child(currUserId).child("subsStatus").getValue().toString();
                        if (joinStatus.equals("true")) {
                            holder.tv_GroupStatus.setText("Subscribed");
                        } else if (joinStatus.equals("req_sent")) {
                            holder.tv_GroupStatus.setText("Req Sent");
                        } else if (joinStatus.equals("reject")) {
                            holder.tv_GroupStatus.setText("Rejected");
                        }
                    } else {
                        holder.tv_GroupStatus.setText("Join");
                    }
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

        TextView tv_groupname, tv_groupownername, tv_GroupStatus;
        RelativeLayout ll_list_group_search;
        NumberPicker numberPicker;
        LinearLayout showAllClasses;
        Button showAllClassesBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_groupname = itemView.findViewById(R.id.tv_groupname);
            tv_groupownername = itemView.findViewById(R.id.tv_groupownername);
            tv_GroupStatus = itemView.findViewById(R.id.tv_GroupStatus);
            ll_list_group_search = itemView.findViewById(R.id.ll_list_group_search);
            numberPicker = itemView.findViewById(R.id.classPicker);
            showAllClasses = itemView.findViewById(R.id.showClasses);
            showAllClassesBtn = itemView.findViewById(R.id.showAllClassesBtn);

            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(12);

            ll_list_group_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    assert currentUser != null;
                    String userID = currentUser.getUid();


                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String adminGroupID = user.userId;
                        String groupName = user.groupName;
                        String groupPushId = user.position;
                        String subGrpName = user.subGroupName;

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID).child("clickedJoinSearchGroup");
                        databaseReference.setValue(groupPushId);

                        DatabaseReference checkOnGroupClick = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                        checkOnGroupClick.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(context.getApplicationContext(), "Classes : " + snapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                                if (snapshot.getChildrenCount() > 0) {
                                    if (position != RecyclerView.NO_POSITION) {
                                        mListener.createGroupDialog(adminGroupID, groupName, groupPushId);
                                        //mListener.dislikeAns();
                                    }
                                } else {
                                    Toast.makeText(context.getApplicationContext(), "No class isn't created yet!!", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                }
            });

            showAllClassesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAllClasses.setVisibility(View.VISIBLE);
                }
            });

            tv_GroupStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String adminGroupID = user.userId;
                        String groupName = user.groupName;
                        String groupPushId = user.position;
                        String subGrpName = user.subGroupName;


                        if (position != RecyclerView.NO_POSITION) {
                            mListener.createGroupDialog(adminGroupID, groupName, groupPushId);
                        }
                    }
                }
            });
        }

    }

}


