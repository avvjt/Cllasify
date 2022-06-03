package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.Home.Students_Subjects;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adapter_TopicList_Serv extends RecyclerView.Adapter<Adapter_TopicList_Serv.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<Class_Group_Names> class_group_namesList;
    private OnItemClickListener mListener;
    String uniPush;

    int lastPosition = -1;

    public interface OnItemClickListener {

        void renameSubject(String groupPushId, String classUniPushId, String classPosition, String subjectName);

        void deleteSubject(String groupPushId, String classPos, String subjectUniPush);

    }

    public String getUniPush() {
        return uniPush;
    }

    public void setUniPush(String uniPush) {
        this.uniPush = uniPush;
    }

    public void setClass_group_namesList(List<Class_Group_Names> class_group_namesList) {
        this.class_group_namesList = class_group_namesList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adapter_TopicList_Serv(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_TopicList_Serv.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.subject_list_item_serv, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_TopicList_Serv.ViewHolder holder, int position) {
        holder.subjectTopic.setText(subjectDetailsModelList.get(position).getSubjectName());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();

    }

    @Override
    public int getItemCount() {
        return subjectDetailsModelList.size();
    }

    public void setSubjectDetailsModelList(List<Subject_Details_Model> subjectDetailsModelList) {
        this.subjectDetailsModelList = subjectDetailsModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTopic;
        ImageButton subjectMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTopic = itemView.findViewById(R.id.tv_subjectTopic);
            subjectMore = itemView.findViewById(R.id.subjectMore);

            final PopupMenu dropDownMenu = new PopupMenu(context, subjectMore);

            final Menu menu = dropDownMenu.getMenu();

            dropDownMenu.getMenuInflater().inflate(R.menu.subject_more, menu);

            dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.toString()) {

                        case "Rename Subject":
                            if (mListener != null) {
//                                Toast.makeText(context, "Clicked on Subject", Toast.LENGTH_SHORT).show();
                                String userID = SharePref.getDataFromPref(Constant.USER_ID);
                                DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                                posTemp.child("subjectPosition").setValue(getAdapterPosition());
                                posTemp.child("clickedSubjectName").setValue(subjectDetailsModelList.get(getAdapterPosition()).getSubjectName());

                                posTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (mListener != null) {
                                            int subPos = getAdapterPosition();
                                            Subject_Details_Model subject_details_model = subjectDetailsModelList.get(subPos);
                                            String groupPushId = String.valueOf(snapshot.child("clickedGroupPushId").getValue());
                                            String classUniPushId = getUniPush();
                                            String subjectUniPush = subject_details_model.getSubjectUniPushId();
                                            String subjectName = subject_details_model.getSubjectName();
                                            mListener.renameSubject(groupPushId, classUniPushId, subjectUniPush, subjectName);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

//                                Toast.makeText(context, "Clicked on Delete Subject: " + subjectDetailsModelList.get(getAdapterPosition()).getSubjectName(), Toast.LENGTH_SHORT).show();


                            }
                            break;

                        case "Delete Subject":
                            if (mListener != null) {
//                                Toast.makeText(context, "Clicked on Subject", Toast.LENGTH_SHORT).show();
                                String userID = SharePref.getDataFromPref(Constant.USER_ID);
                                DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                                posTemp.child("subjectPosition").setValue(getAdapterPosition());
                                posTemp.child("clickedSubjectName").setValue(subjectDetailsModelList.get(getAdapterPosition()).getSubjectName());

                                posTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (mListener != null) {
                                            int subPos = getAdapterPosition();
                                            Subject_Details_Model subject_details_model = subjectDetailsModelList.get(subPos);
                                            String groupPushId = String.valueOf(snapshot.child("clickedGroupPushId").getValue());
                                            String classUniPushId = String.valueOf(snapshot.child("clickedStudentUniPushClassId").getValue());
                                            String subjectUniPush = subject_details_model.getSubjectUniPushId();
                                            String subjectName = subject_details_model.getSubjectName();


                                            androidx.appcompat.app.AlertDialog.Builder alertdialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                                            alertdialogbuilder.setTitle("Please confirm !!!")
                                                    .setMessage("Do you delete Subject")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            mListener.deleteSubject(groupPushId, classUniPushId, subjectUniPush);
                                                            subjectDetailsModelList.remove(subPos);
                                                            notifyItemRemoved(subPos);
                                                        }
                                                    }).setNegativeButton("No",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            androidx.appcompat.app.AlertDialog alert = alertdialogbuilder.create();
                                            alert.show();




                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

//                                Toast.makeText(context, "Clicked on Delete Subject: " + subjectDetailsModelList.get(getAdapterPosition()).getSubjectName(), Toast.LENGTH_SHORT).show();
                            }
                            break;


                    }
                    return false;
                }
            });

            subjectMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dropDownMenu.show();
                }
            });





        }
    }
}
