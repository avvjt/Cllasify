package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.R;

public class Adapter_Discover_Item extends RecyclerView.Adapter<Adapter_Discover_Item.ViewHolder> {

    Context context;

    private Adaptor_SearchGroup.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void createGroupDialog(String adminGroupID, String groupName, String groupPushId);
    }

    public void setOnItemClickListener(Adaptor_SearchGroup.OnItemClickListener listener) {
        mListener = listener;
    }

    String[] classesList = {"class 1", "class 2", "class 3", "class 4", "class 5", "class 6", "class 7", "class 8", "class 9", "class 10", "class 11", "class 12"};

    public Adapter_Discover_Item(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_Discover_Item.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discover_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Discover_Item.ViewHolder holder, int position) {
        holder.classes.setText(classesList[position]);
    }

    @Override
    public int getItemCount() {
        return classesList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView classes;
        Button joinBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            classes = itemView.findViewById(R.id.classes);
            joinBtn = itemView.findViewById(R.id.joinBtn);

/*
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String adminGroupID=user.userId;
                        String groupName=user.groupName;
                        String groupPushId=user.position;
                        String subGrpName=user.subGroupName;


                        if (position != RecyclerView.NO_POSITION) {
                            mListener.createGroupDialog(adminGroupID,groupName,groupPushId);
                            //mListener.dislikeAns();
                        }
                    }
                }
            });
            */
        }
    }
}
