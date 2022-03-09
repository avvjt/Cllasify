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

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Utility.SharePref;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.tv_MyMessage);

            ll_Doubt = itemView.findViewById(R.id.ll_Doubt);


            ll_Doubt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Log.d("DBTTT", "onClick: " + position);


                    if (chat.get(position).getMsgCategory().equals("doubt")) {

                        if (doubt != null) {
                            if (position != RecyclerView.NO_POSITION) {

                                Log.d("DBTTT", "onClick: " + position);

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
