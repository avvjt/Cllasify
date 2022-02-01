package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.R;

public class Test_Adapter extends RecyclerView.Adapter<Test_Adapter.ViewHolder> {

    Context context;

    public Test_Adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Test_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_group_member, parent, false);
        return new Test_Adapter.ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Test_Adapter.ViewHolder holder, int position) {
        Log.d("FRD", "Friend List.....");
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
