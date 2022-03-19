package com.cllasify.cllasify.Adaptor;

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

public class Adapter_All_Friends extends RecyclerView.Adapter<Adapter_All_Friends.MyViewHolder> {

    private Context context;
    private List<String> mDatalistNew;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void MemberProfile(String memberUserId, String memberUserName);
    }

    public Adapter_All_Friends(Context context, List<String> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_group_member, parent, false);
        return new MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Toast.makeText(context, "Friend List", Toast.LENGTH_SHORT).show();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String currUserID=currentUser.getUid();

        Log.d("TAG", "onDataChange: FRNDS: "+mDatalistNew.get(position));

        String requiredUserId = mDatalistNew.get(position);

        DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(requiredUserId);
        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("profilePic").exists()) {
                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                    Glide.with(context.getApplicationContext()).load(profilePicUrl).into(holder.civ_UserProfilePic);
                }else{
                    Glide.with(context.getApplicationContext()).load(R.drawable.maharaji).into(holder.civ_UserProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(mDatalistNew.get(position));
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.tv_GroupTitle.setText(snapshot.child("Name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_GroupTitle;
        LinearLayout ll_Group;

        Class_Group class_Group;
        Boolean clicked;
        DatabaseReference refLike;

        ImageButton ib_followFrnd, ib_AddFrnd, ib_SubMenu;
        CircleImageView civ_UserProfilePic;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_GroupTitle = itemView.findViewById(R.id.tv_classGroupTitle);
            ib_followFrnd = itemView.findViewById(R.id.ib_followFrnd);
            ib_AddFrnd = itemView.findViewById(R.id.ib_AddFrnd);
            ib_SubMenu = itemView.findViewById(R.id.ib_SubMenu);
            ll_Group = itemView.findViewById(R.id.ll_Group);

            civ_UserProfilePic = itemView.findViewById(R.id.civ_UserProfilePic);


            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String currUserID=currentUser.getUid();

            ll_Group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context.getApplicationContext(), "Clicked Friend", Toast.LENGTH_SHORT).show();
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(mDatalistNew.get(position));
                        dr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String memberUserId=snapshot.child("userId").getValue().toString();
                                String memberUserName=snapshot.child("Name").getValue().toString();

                                if (!currUserID.equals(memberUserId)) {
                                    if (position != RecyclerView.NO_POSITION) {
//                                String adminUserName=user.userName;
                                        mListener.MemberProfile(memberUserId,memberUserName);
                                        //mListener.dislikeAns();
                                    }
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


    }
}


