package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;

import java.util.List;

public class Adaptor_RollNumbers extends RecyclerView.Adapter<Adaptor_RollNumbers.MyViewHolder> {

    private Context context;
    private List<Class_Student_Details> mDatalistNew;
    int lastPosition = -1;

    public void removeItem(int position) {
        mDatalistNew.remove(position);
        notifyItemRemoved(position);
    }

    public List<Class_Student_Details> getData() {
        return mDatalistNew;
    }

    public Adaptor_RollNumbers(Context context, List<Class_Student_Details> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_group_roll_numbers, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.rollNumbers.setText(String.valueOf(position+1));

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rollNumbers;

        public MyViewHolder(View itemView) {
            super(itemView);
            rollNumbers = itemView.findViewById(R.id.rollNumbers);
        }

    }
}


