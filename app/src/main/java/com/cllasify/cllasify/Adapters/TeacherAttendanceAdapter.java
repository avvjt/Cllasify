package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.ModelClasses.User;
import com.cllasify.cllasify.databinding.ItemTeacherAttendanceBinding;

import java.util.List;

public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.ViewHolder> {

    private final Context context;

    private DiffUtil.ItemCallback<User> diffUtil = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUserId().equals(newItem.getUserId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUserId().equals(newItem.getUserId());
        }
    };

    private AsyncListDiffer<User> differ;

    public TeacherAttendanceAdapter(Context context) {
        this.context = context;
        differ = new AsyncListDiffer<>(this, diffUtil);
    }


    public void submitUsers(List<User> users) {
        differ.submitList(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTeacherAttendanceBinding.inflate(
                LayoutInflater.from(context), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = differ.getCurrentList().get(position);
        ItemTeacherAttendanceBinding binding = holder.binding;
        Glide.with(context).load(Uri.parse(user.getProfilePic())).into(binding.profilePic);
        binding.sNo.setText(String.valueOf(position + 1));
        String name = (user.getName() != null || !user.getName().equalsIgnoreCase("")) ? user.getName() : user.getUserEmailId();
        binding.tvClassGroupTitle.setText(name);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTeacherAttendanceBinding binding;


        public ViewHolder(@NonNull ItemTeacherAttendanceBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
