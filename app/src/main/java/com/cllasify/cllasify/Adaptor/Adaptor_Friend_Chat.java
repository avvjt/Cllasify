package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Single_Friend;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.SharePref;
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

    private static final String TAG = "Message Adapter";
    private final Context context;
    private List<Class_Single_Friend> chat = new ArrayList<>();
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void getPosition(int position,String senderUserId,String receiverUserId,String messageId,String text);
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
        if (viewType == MSG_TYPE_RIGHT) {
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
            holder.show_message.setText(chat.get(position).getMessage());
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
        if (chat.get(position).getSenderId().equals(SharePref.getDataFromPref(Constant.USER_ID))) {
            return MSG_TYPE_RIGHT;
        } else
            return MSG_TYPE_LEFT;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView show_message;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.tv_MyMessage);

            show_message.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Log.d("USERIDADAPTER", "messageId: " + chat.get(getAdapterPosition()).getMessageIdSender());

                    longClickListener.getPosition(getAdapterPosition(),chat.get(getAdapterPosition()).getSenderId(),chat.get(getAdapterPosition()).getMessageIdReceiver(),chat.get(getAdapterPosition()).getMessageIdSender(),show_message.getText().toString());

                    return false;
                }
            });

        }
    }
}
