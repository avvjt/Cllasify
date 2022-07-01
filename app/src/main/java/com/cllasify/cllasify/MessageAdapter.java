package com.cllasify.cllasify;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Home.Server_Activity;
import com.cllasify.cllasify.Utility.SharePref;
import com.cllasify.cllasify.Utility.StringUtil;
import com.cllasify.cllasify.Utility.TextViewUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
    onMessageClickListener onMessageClickListener;
    onPDFClickListener onPDFClickListener;

    public interface onPDFClickListener {
        void onPDFClick(int position, String path);
    }

    public int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public interface onMessageClickListener {
        void onMessageClick(int position, Class_Group chat);
    }

    public void setOnPDFClickListener(MessageAdapter.onPDFClickListener onPDFClickListener) {
        this.onPDFClickListener = onPDFClickListener;
    }

    public void setOnMessageClickListener(MessageAdapter.onMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    public void setProgVal(int progVal) {
        this.progVal = progVal;
    }

    public interface onDownloadClickListener {
        void onDownloadClick(String path, String doc_name);
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

    public void removeItem(int position) {
        chat.remove(position);
        notifyItemRemoved(position);
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

        int pos = position;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();

            if (chat != null) {
                if (holder.tv_pdfName != null) {
                    holder.tv_pdfName.setText(chat.get(pos).getDoc_Name());
                }
                if (holder.pdf_file != null) {

                    holder.pdf_file.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            onPDFClickListener.onPDFClick(pos, chat.get(pos).getGroupSubGroupComb());
                            return true;
                        }
                    });

                    holder.pdf_file.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDownloadClickListener.onDownloadClick(chat.get(pos).getGroupSubGroupComb(), chat.get(pos).getDoc_Name());
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
                    boolean isUser = false;

                    Log.d("REPOCHK", "onDataChange: " + chat.get(pos).getReportUsers());

                    if (chat.get(pos).getReportUsers() != null) {

                        String[] ru = chat.get(pos).getReportUsers().split("&");
                        Log.d("REPOCHK", "onDataChange: " + ru.length);

                        for (int i = 0; i < ru.length; i++) {
                            Log.d("REPOCHK", "onDataChange: " + ru[i].equals(userID));
                            if (ru[i].equals(userID)) {
                                isUser = true;
                            }
                        }

                        Log.d("ISREPO", "onBindViewHolder: YES"+chat.get(pos).getGroupSubGroupComb().trim().equals("This message is reported"));


                        if (chat.get(pos).getGroupSubGroupComb().trim().equals("This message is reported")) {


                            holder.show_message.setTypeface(null, Typeface.ITALIC);

                            holder.show_message.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                            holder.show_message.getPaint().setMaskFilter(null);
                        } else {
                            Log.d(TAG, "onBindViewHolder: ");
                        }
                        if ((ru.length >= 3 && ru.length < 5) || isUser == true) {
                            if (chat.get(pos).getGroupSubGroupComb().trim().equals("This message is reported")) {

                                Log.d("ISREPO", "onBindViewHolder: YES 1");

                                holder.show_message.setTypeface(null, Typeface.ITALIC);

                                holder.show_message.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                holder.show_message.getPaint().setMaskFilter(null);
                            } else {
                                holder.show_message.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        holder.show_message.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                        holder.show_message.getPaint().setMaskFilter(null);
                                    }
                                });
                                if (Build.VERSION.SDK_INT >= 11) {
                                    holder.show_message.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                }
                                float radius = holder.show_message.getTextSize() / 3;
                                BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                                holder.show_message.getPaint().setMaskFilter(filter);
                            }


                        } else {
                            holder.show_message.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                            holder.show_message.getPaint().setMaskFilter(null);
                        }
                    }

                }
            }

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


            if (show_message != null) {
                show_message.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onMessageClickListener.onMessageClick(getAdapterPosition(), chat.get(getAdapterPosition()));
                        return true;
                    }
                });
            }

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
