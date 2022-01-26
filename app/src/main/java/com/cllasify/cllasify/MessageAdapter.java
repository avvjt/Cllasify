package com.cllasify.cllasify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private static final String TAG = "Message Adapter";
    private final Context context;
    private List<Class_Group> chat = new ArrayList<>();

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Class_Group> list) {
        this.chat = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(chat!=null)
            holder.show_message.setText(chat.get(position).getGroupSubGroupComb());
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chat.get(position).getPosition().equals(SharePref.getDataFromPref(Constant.USER_ID))){
            return MSG_TYPE_RIGHT;
        }else
            return MSG_TYPE_LEFT;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView show_message;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.tv_MyMessage);
        }
    }
}
