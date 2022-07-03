package com.cllasify.cllasify.Adaptor;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Single_Friend;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.Friend_Chat_Activity;
import com.cllasify.cllasify.MessageAdapter;
import com.cllasify.cllasify.R;
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

public class Adaptor_Friend_Chat extends RecyclerView.Adapter<Adaptor_Friend_Chat.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_DOCUMENT_RIGHT = 3;
    public static final int MSG_TYPE_DOCUMENT_LEFT = 4;

    private static final String TAG = "Message Adapter";
    private final Context context;
    private List<Class_Single_Friend> chat = new ArrayList<>();
    private OnItemLongClickListener longClickListener;

    onDownloadClickListener onDownloadClickListener;
    onPDFClickListener onPDFClickListener;

    String senderUserId, receiverUserId;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String userID = currentUser.getUid();
    String userName = currentUser.getDisplayName();


    public interface onDownloadClickListener {
        void onDownloadClick(String path, String doc_name);
    }

    public void setOnDownloadClickListener(onDownloadClickListener onDownloadClickListener) {
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public interface onPDFClickListener {
        void onPDFClick(int position, String path);
    }

    public void setOnPDFClickListener(onPDFClickListener onPDFClickListener) {
        this.onPDFClickListener = onPDFClickListener;
    }


    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public interface OnItemLongClickListener {
        void getPosition(int position, String senderUserId, String receiverUserId, String messageId, String text);
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public Adaptor_Friend_Chat(Context context) {
        this.context = context;
    }


    public void setList(ArrayList<Class_Single_Friend> list) {
        this.chat = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_DOCUMENT_LEFT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pdf_left, parent, false);
            return new Adaptor_Friend_Chat.ViewHolder(view);
        } else if (viewType == MSG_TYPE_DOCUMENT_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pdf_right, parent, false);
            return new Adaptor_Friend_Chat.ViewHolder(view);
        } else if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left_friend, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if (chat != null) {

            int pos = position;


            if (holder.tv_pdfName != null) {
                holder.tv_pdfName.setText(chat.get(position).getMessage());
            }

            if (holder.pdf_file != null) {

                holder.pdf_file.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onPDFClickListener.onPDFClick(pos, chat.get(pos).getPdfUrl());
                        return true;
                    }
                });

                holder.pdf_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDownloadClickListener.onDownloadClick(chat.get(pos).getPdfUrl(), chat.get(pos).getMessage());
                    }
                });

            }
            if (holder.show_message != null) {
                holder.show_message.setText(chat.get(position).getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }


    public void removeItem(int position) {
        chat.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {

        Log.d("USERID", "getItemViewType: " + chat.get(position).getSenderId());

        if (chat.get(position).getMsgCategory().equals("pdf") && chat.get(position).getSenderId().equals(SharePref.getDataFromPref(Constant.USER_ID))) {
            return MSG_TYPE_DOCUMENT_RIGHT;
        } else if (chat.get(position).getMsgCategory().equals("pdf") && !(chat.get(position).getSenderId().equals(SharePref.getDataFromPref(Constant.USER_ID)))) {
            return MSG_TYPE_DOCUMENT_LEFT;
        } else if (chat.get(position).getSenderId() != null && chat.get(position).getSenderId().equals(userID)) {
            return MSG_TYPE_RIGHT;
        } else
            return MSG_TYPE_LEFT;

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView show_message, tv_pdfName;
        private final CardView pdf_file;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.tv_MyMessage);
            tv_pdfName = itemView.findViewById(R.id.tv_pdfName);
            pdf_file = itemView.findViewById(R.id.pdf_file);

            String senderRoom = senderUserId + receiverUserId;
            String receiverRoom = receiverUserId + senderUserId;

            if (show_message != null) {

                show_message.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.more_chat_options);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.getWindow().setGravity(Gravity.BOTTOM);


                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


                            if (chat.get(getAdapterPosition()).getSenderId().equals(userID)) {

                                dialog.show();


                                Log.d("USERID", "getPosition: " + chat.get(getAdapterPosition()).getSenderId());
                                Log.d("USERID", "messageId: " + chat.get(getAdapterPosition()).getMessageIdSender());


                                Button copyBtn = dialog.findViewById(R.id.copy_button);
                                copyBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();

                                        ClipboardManager clipboardManager = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData data = (ClipData) ClipData.newPlainText("text", show_message.getText().toString());
                                        clipboardManager.setPrimaryClip(data);

                                    }
                                });

                                Button unsend = dialog.findViewById(R.id.unsend_button);
                                unsend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();


                                        DatabaseReference firebaseDatabaseUnsendMSG = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");

                                        firebaseDatabaseUnsendMSG.child(chat.get(getAdapterPosition()).getMessageIdSender()).removeValue();

                                        DatabaseReference firebaseDatabaseUnsendMSGReceiver = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages");

                                        firebaseDatabaseUnsendMSGReceiver.child(chat.get(getAdapterPosition()).getMessageIdReceiver()).removeValue();

                                        removeItem(getAdapterPosition());

                                    }
                                });

                                Button reply = dialog.findViewById(R.id.reply_button);
                                reply.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });

                            } else {
//                            Log.d("USERID", "getPosition: " + senderUserId);
//                            Log.d("USERID", "messageId: " + messageId);


                                if (chat.get(getAdapterPosition()).getMessage().trim().equals("This message is reported")) {


                                    Toast.makeText(context, "You reported this message", Toast.LENGTH_SHORT).show();

                                } else {

                                    dialog.show();

                                    Log.d("NOTREPORTED", "getPosition: " + chat.get(getAdapterPosition()).getMessage());

                                    dialog.setContentView(R.layout.more_chat_options_others);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    dialog.getWindow().setGravity(Gravity.BOTTOM);


                                    Button copyBtn = dialog.findViewById(R.id.copy_button);
                                    copyBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog.dismiss();

                                            ClipboardManager clipboardManager = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData data = (ClipData) ClipData.newPlainText("text", show_message.getText().toString());
                                            clipboardManager.setPrimaryClip(data);

                                        }
                                    });

                                    Button report = dialog.findViewById(R.id.report_button);
                                    report.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog.dismiss();

                                            DatabaseReference firebaseDatabaseUnsendMSG = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");

                                            firebaseDatabaseUnsendMSG.child(chat.get(getAdapterPosition()).getMessageIdReceiver()).child("message").setValue("This message is reported");
                                        }
                                    });

                                    Button reply = dialog.findViewById(R.id.reply_button);
                                    reply.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                }
                            }

                        }


                        Log.d("USERIDADAPTER", "messageId: " + chat.get(getAdapterPosition()).getMessageIdSender());

//                    longClickListener.getPosition(getAdapterPosition(), chat.get(getAdapterPosition()).getSenderId(), chat.get(getAdapterPosition()).getMessageIdReceiver(), chat.get(getAdapterPosition()).getMessageIdSender(), show_message.getText().toString());

                        return false;
                    }
                });
            }
        }
    }
}
