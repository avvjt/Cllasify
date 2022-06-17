package com.cllasify.cllasify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_DOUBT = 3;
    public static final int MSG_TYPE_DOCUMENT_RIGHT = 4;
    public static final int MSG_TYPE_DOCUMENT_LEFT = 5;

    int progVal;

    private static final String TAG = "Message Adapter";
    private final Context context;
    private List<Class_Group> chat = new ArrayList<>();
    private List<Class_Group> doubt = new ArrayList<>();
    onDoubtClickListener onDoubtClickListener;
    String userID;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    onDownloadClickListener onDownloadClickListener;


    public void setProgVal(int progVal) {
        this.progVal = progVal;
    }

    public interface onDownloadClickListener {
        void onDownloadClick(String path);
    }

    public void setOnDownloadClickListener(MessageAdapter.onDownloadClickListener onDownloadClickListener) {
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public interface onDoubtClickListener {
        void onDoubtClick(String doubtQuestion, String groupPush, String groupClassPush, String groupSubjectPush, String doubtQuestionPush, String userId, String userName);
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
        } else if (viewType == MSG_TYPE_DOCUMENT_LEFT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pdf_left, parent, false);
            return new ViewHolder(view);
        } else if (viewType == MSG_TYPE_DOCUMENT_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pdf_right, parent, false);
            return new ViewHolder(view);
        } else if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        Log.d("DOCPROG", "onBindViewHolder: " + progVal);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();

            if (chat != null) {

                if (holder.tv_pdfName != null) {
                    holder.tv_pdfName.setText(chat.get(position).getDoc_Name());
                }
                if (holder.pdf_file != null) {

                    holder.pdf_file.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            onDownloadClickListener.onDownloadClick(chat.get(position).getGroupSubGroupComb());

                        }
                    });


                    if (chat.get(position).getPosition().equals(userID)) {

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.pdf_file.getLayoutParams();
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                        holder.pdf_file.setLayoutParams(params);
                    } else {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.pdf_file.getLayoutParams();
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                        holder.pdf_file.setLayoutParams(params);
                    }
                }

                if (holder.show_message != null) {
                    holder.show_message.setText(chat.get(position).getGroupSubGroupComb());
                }
                Class_Group class_GroupDetails = chat.get(position);

                String reqUserID = class_GroupDetails.position;

                Log.d("TSTNOTIFY", "MyViewHolder: " + class_GroupDetails.getDoubtUniPushId());
                String doubtPushId = class_GroupDetails.getDoubtUniPushId();

                if (doubtPushId.startsWith("Doubt")) {

                    String groupPushId = class_GroupDetails.getGroupName();
                    String classPushId = class_GroupDetails.getSubGroupName();
                    String subjectPushId = class_GroupDetails.getSubGroupPushId();

                    DatabaseReference databaseReferenceDoubtCheck = FirebaseDatabase.getInstance().getReference().child("Groups").child("Doubt").child(groupPushId)
                            .child(classPushId).child(subjectPushId).child("All_Doubt").child(doubtPushId);

                    databaseReferenceDoubtCheck.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                if (snapshot.child("Answer").exists()) {
                                    holder.tv_AnswerCount.setText(String.valueOf(snapshot.child("Answer").getChildrenCount()));
                                    Log.d("TOPICDATA", "onDataChange: " + snapshot.child("Answer").getChildrenCount());
                                } else {
                                    holder.tv_AnswerCount.setText("0");
                                }
                                holder.tv_topicTitle.setText(snapshot.child("groupName").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Log.d("TOPICDATA", "message");
                }

                if (holder.prof_pics_chat_doubt != null) {
                    DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(reqUserID);
                    refUserProfPic.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("Name").exists()) {
                                String userName = snapshot.child("Name").getValue().toString();
                                if (holder.tv_UserName != null) {
                                    holder.tv_UserName.setText(userName);
                                }
                            }

                            if (snapshot.child("profilePic").exists()) {
                                String profilePicUrl = snapshot.child("profilePic").getValue().toString();
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
        } else if (chat.get(position).getMsgCategory().equals("pdf") && chat.get(position).getPosition().equals(SharePref.getDataFromPref(Constant.USER_ID))) {
            return MSG_TYPE_DOCUMENT_RIGHT;
        } else if (chat.get(position).getMsgCategory().equals("pdf") && !(chat.get(position).getPosition().equals(SharePref.getDataFromPref(Constant.USER_ID)))) {
            return MSG_TYPE_DOCUMENT_LEFT;
        } else if (chat.get(position).getPosition().equals(SharePref.getDataFromPref(Constant.USER_ID))) {
            return MSG_TYPE_RIGHT;
        } else
            return MSG_TYPE_LEFT;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView show_message;
        LinearLayout ll_Doubt;
        CircleImageView prof_pics_chat_doubt, download_btn;
        TextView tv_UserName, tv_topicTitle, tv_AnswerCount, tv_pdfName;
        CardView pdf_file;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.tv_MyMessage);
            tv_UserName = itemView.findViewById(R.id.tv_UserName);
            tv_topicTitle = itemView.findViewById(R.id.tv_topicTitle);
            tv_AnswerCount = itemView.findViewById(R.id.answerCount);
            tv_pdfName = itemView.findViewById(R.id.tv_pdfName);
            prof_pics_chat_doubt = itemView.findViewById(R.id.prof_pics_chat_doubt);
            pdf_file = itemView.findViewById(R.id.pdf_file);
            ll_Doubt = itemView.findViewById(R.id.ll_Doubt);
            download_btn = itemView.findViewById(R.id.download_btn);


            if (ll_Doubt != null) {

                ll_Doubt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();


                        if (chat.get(position).getMsgCategory().equals("doubt")) {

                            if (doubt != null) {
                                if (position != RecyclerView.NO_POSITION) {


                                    Class_Group user = chat.get(position);
                                    Log.d("DBTTT", "onClick: " + user.getPosition());

                                    String doubtQuestion = user.getGroupSubGroupComb();
                                    String groupPush = user.getGroupName();
                                    String groupClassPush = user.getSubGroupName();
                                    String groupSubjectPush = user.getSubGroupPushId();
                                    String doubtQuestionPush = user.getDoubtUniPushId();
                                    String userId = user.getPosition();
                                    String userName = user.getUserId();

                                    onDoubtClickListener.onDoubtClick(doubtQuestion, groupPush, groupClassPush, groupSubjectPush, doubtQuestionPush, userId, userName);
                                }

                            }
                        }

                    }
                });
            }

        }
    }
}
