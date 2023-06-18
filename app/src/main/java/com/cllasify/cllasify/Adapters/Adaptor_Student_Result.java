package com.cllasify.cllasify.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
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

public class Adaptor_Student_Result extends RecyclerView.Adapter<Adaptor_Student_Result.MyViewHolder> {

    private final Context context;
    ProgressDialog notifyPB;
    DatabaseReference refUserFollowing;
    boolean subsClick = false;
    int lastPosition = -1;
    private List<Class_Student_Details> mDatalistNew;
    private OnItemClickListener mListener;

    public Adaptor_Student_Result(Context context) {
        this.context = context;
    }

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_group_member_result, parent, false);
        return new MyViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

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

        String roll = String.valueOf(position + 1);

        holder.tv_roll.setText(roll);


        holder.res_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.clickedStudent(userID, userName, roll);

            }
        });

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

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    public interface OnItemClickListener {

        void clickedStudent(String userID, String userName, String position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_GroupTitle, tv_userName, tv_roll;
        CircleImageView civ_UserProfilePic;
        RelativeLayout res_student;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_userName = itemView.findViewById(R.id.tv_unique_userName);
            tv_GroupTitle = itemView.findViewById(R.id.tv_classGroupTitle);
            civ_UserProfilePic = itemView.findViewById(R.id.civ_UserProfilePic);
            tv_roll = itemView.findViewById(R.id.rollTV);
            res_student = itemView.findViewById(R.id.res_student);


            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {


                }
            }
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


