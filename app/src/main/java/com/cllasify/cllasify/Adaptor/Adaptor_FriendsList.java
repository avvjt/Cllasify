package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.FriendsListClass;
import com.cllasify.cllasify.R;

import java.util.List;

public class Adaptor_FriendsList extends RecyclerView.Adapter<Adaptor_FriendsList.MyViewHolder> {

    private Context context;
    private List<Class_Group> friendsListClassesList;
    ProgressDialog notifyPB;
    private OnItemClickListener mListener;
    int selected_position = 0;


    public interface OnItemClickListener {

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adaptor_FriendsList(Context context, List<Class_Group> friendsListClassesList) {
        this.context = context;
        this. friendsListClassesList =  friendsListClassesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_lvl1_groupname, parent, false);
        return new MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Toast.makeText(context, "pppppppp", Toast.LENGTH_SHORT).show();

        Class_Group friend = friendsListClassesList.get(position);
        holder.userName.setText(friend.getUserName());

        Toast.makeText(context, ""+friend.getUserName(), Toast.LENGTH_SHORT).show();

        Log.d("TAGGYP", "chatListDashboard: \n" + friend.getUserId());

    }

    @Override
    public int getItemCount() {
        return friendsListClassesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName;

        public MyViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_name);
        }
    }
}


