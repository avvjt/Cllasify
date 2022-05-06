package com.cllasify.cllasify.Adaptor;

import static com.cllasify.cllasify.Profile.AccountSetting_Activity.getDefaults;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.FriendsListClass;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor_FriendsList extends RecyclerView.Adapter<Adaptor_FriendsList.MyViewHolder> {

    private Context context;
    private List<FriendsListClass> friendsListClassesList;
    ProgressDialog notifyPB;
    private OnItemClickListener mListener;
    int selected_position = 0;


    public interface OnItemClickListener {
        void onFriendClick(String friendName,String friendUserId);
    }

    public void setFriendListClick(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adaptor_FriendsList(Context context, List<FriendsListClass> friendsListClassesList) {
        this.context = context;
        this. friendsListClassesList =  friendsListClassesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_rec_friends, parent, false);
        return new MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FriendsListClass friend = friendsListClassesList.get(position);

        String friendName = friend.getName();
        String friendUserId = friend.getUserId();

        DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(friendUserId);
        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("profilePic").exists()) {
                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                    Glide.with(context.getApplicationContext()).load(profilePicUrl).into(holder.rec_friends_profPic);
                }else{
                    Glide.with(context.getApplicationContext()).load(R.drawable.maharaji).into(holder.rec_friends_profPic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.userName.setText(friendName);

        Log.d("TAGGYP", "FriendName: " + friendName+"\nFriendUserId: "+friendUserId);

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFriendClick(friendName,friendUserId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendsListClassesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        CircleImageView rec_friends_profPic;

        public MyViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_name);
            String darkLightDefaultVal = getDefaults("DefaultDarkLight", context);

            if (darkLightDefaultVal != null) {
                if (darkLightDefaultVal.equals("Dark")) {
                    Log.d("Theme", "ViewHolder: Dark");
                    userName.setTextColor(Color.parseColor("#ffffff"));
                }
                if (darkLightDefaultVal.equals("Light")) {
                    Log.d("Theme", "ViewHolder: Light");
                    userName.setTextColor(Color.parseColor("#050505"));
                }
            }
            rec_friends_profPic = itemView.findViewById(R.id.rec_friends_profPic);


        }
    }
}


