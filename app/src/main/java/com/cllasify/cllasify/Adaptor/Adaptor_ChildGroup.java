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

public class Adaptor_ChildGroup extends RecyclerView.Adapter<Adaptor_ChildGroup.MyViewHolder> {

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

    public Adaptor_ChildGroup(Context context, List<Class_Group> mDatalistNew) {
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
        String userID=currentUser.getUid();
        Class_Group Answers=mDatalistNew.get(position);

        String subGroupName=Answers.getUserName();
        holder.tv_GroupTitle.setText(subGroupName);

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_GroupTitle;

        ImageButton ib_AddChildGroup;

        LinearLayout ll_Group;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_Group = itemView.findViewById(R.id.ll_Group);
            tv_GroupTitle=itemView.findViewById(R.id.tv_GroupTitle);

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


