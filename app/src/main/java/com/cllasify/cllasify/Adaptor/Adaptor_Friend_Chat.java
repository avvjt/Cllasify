package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Single_Friend;
import com.cllasify.cllasify.Home.Server_Activity;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor_Friend_Chat extends RecyclerView.Adapter<Adaptor_Friend_Chat.MyViewHolder> {

    private Context context;
    private List<Class_Single_Friend> mMessages;

    public Adaptor_Friend_Chat(Context context, List<Class_Single_Friend> mMessages) {
        this.context = context;
        this.mMessages = mMessages;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_group_dashboard, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Class_Single_Friend message = mMessages.get(position);
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        if(userId.equals(message.getSenderId())){
            holder.tv_MyMsz.setText(message.getMessage());
            holder.ll_OtherMsz.setVisibility(View.GONE);

        } else{
            holder.tv_OtherMsz.setText(message.getMessage());
            holder.ll_MyMsz.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {

        return mMessages.size();
    }

    public void scrollDownl(){
        Server_Activity.rv_ChatDashboard.smoothScrollToPosition(mMessages.size());
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


