package com.cllasify.cllasify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_DOUBT = 3;

    private static final String TAG = "Message Adapter";
    private final Context context;
    private List<Class_Group> chat = new ArrayList<>();
    private List<Class_Group> doubt = new ArrayList<>();
    onDoubtClickListener onDoubtClickListener;

    public interface onDoubtClickListener {
        void onDoubtClick(String doubtQuestion, String groupPush, String groupClassPush, String groupSubjectPush, String doubtQuestionPush);
    }

    public void setOnDoubtClickListener(MessageAdapter.onDoubtClickListener onDoubtClickListener) {
        this.onDoubtClickListener = onDoubtClickListener;
    }

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void setDoubt(List<Class_Group> doubt) {
        this.doubt = doubt;
        notifyDataSetChanged();
    }

    public void setList(ArrayList<Class_Group> list) {
        this.chat = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_DOUBT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_doubt, parent, false);
            return new ViewHolder(view);
        }
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if (chat != null) {
            holder.show_message.setText(chat.get(position).getGroupSubGroupComb());

            holder.tv_UserName.setText(chat.get(position).getUserId());

            Class_Group class_GroupDetails = chat.get(position);

            String groupPushId = class_GroupDetails.getGroupName();
            String classPushId = class_GroupDetails.getSubGroupName();
            String subjectPushId = class_GroupDetails.getSubGroupPushId();
            String doubtPushId = class_GroupDetails.getDoubtUniPushId();


            DatabaseReference databaseReferenceDoubtCheck = FirebaseDatabase.getInstance().getReference().child("Groups").child("Doubt").child(groupPushId)
                    .child(classPushId).child(subjectPushId).child("All_Doubt").child(doubtPushId);

            databaseReferenceDoubtCheck.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.child("Answer").exists()) {
                            holder.tv_AnswerCount.setText(String.valueOf(snapshot.child("Answer").getChildrenCount()));
                            Log.d("TOPICDATA", "onDataChange: " + snapshot.child("Answer").getChildrenCount());
                        }
                        holder.tv_topicTitle.setText(snapshot.child("groupName").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            String reqUserID = class_GroupDetails.position;

            DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(reqUserID);
            refUserProfPic.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("profilePic").exists()) {
                        String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                        Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                        Glide.with(context.getApplicationContext()).load(profilePicUrl).into(holder.prof_pics_chat_doubt);
                    } else {
                        Glide.with(context.getApplicationContext()).load(R.drawable.maharaji).into(holder.prof_pics_chat_doubt);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chat.get(position).getMsgCategory().equals("doubt")) {
            return MSG_TYPE_DOUBT;
        }
        if (chat.get(position).getPosition().equals(SharePref.getDataFromPref(Constant.USER_ID))) {
            return MSG_TYPE_RIGHT;
        } else
            return MSG_TYPE_LEFT;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView show_message;
        LinearLayout ll_Doubt;
        CircleImageView prof_pics_chat_doubt;
        TextView tv_UserName, tv_topicTitle, tv_AnswerCount;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.tv_MyMessage);
            tv_UserName = itemView.findViewById(R.id.tv_UserName);
            tv_topicTitle = itemView.findViewById(R.id.tv_topicTitle);
            tv_AnswerCount = itemView.findViewById(R.id.answerCount);

            ll_Doubt = itemView.findViewById(R.id.ll_Doubt);
            prof_pics_chat_doubt = itemView.findViewById(R.id.prof_pics_chat_doubt);

            ll_Doubt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Log.d("DBTTT", "onClick: " + position);


                    if (chat.get(position).getMsgCategory().equals("doubt")) {

                        if (doubt != null) {
                            if (position != RecyclerView.NO_POSITION) {


                                Class_Group user = chat.get(position);
                                String doubtQuestion = user.getGroupSubGroupComb();
                                String groupPush = user.getGroupName();
                                String groupClassPush = user.getSubGroupName();
                                String groupSubjectPush = user.getSubGroupPushId();
                                String doubtQuestionPush = user.getDoubtUniPushId();

                                onDoubtClickListener.onDoubtClick(doubtQuestion, groupPush, groupClassPush, groupSubjectPush, doubtQuestionPush);
                            }

                        }
                    }

                }
            });


        }
    }
}
