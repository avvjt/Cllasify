package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public interface OnItemClickListener {

        void renameSubject(String groupPushId, String classUniPushId, String classPosition, String subjectName);

        void deleteSubject(String groupPushId, String classPos, String subjectUniPush);

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


            subjectMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dropDownMenu.show();
                }
            });

/*
            deleteSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        Toast.makeText(context, "Clicked on Subject", Toast.LENGTH_SHORT).show();
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



                                    mListener.deleteSubject(groupPushId, classUniPushId, subjectUniPush);
                                    subjectDetailsModelList.remove(subPos);
                                    notifyItemRemoved(subPos);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(context, "Clicked on Delete Subject: " + subjectDetailsModelList.get(getAdapterPosition()).getSubjectName(), Toast.LENGTH_SHORT).show();
                    }

                }
            });


//            Log.d("GRPT", "onClick: " + getAdapterPosition());
            renameSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        Toast.makeText(context, "Clicked on Subject", Toast.LENGTH_SHORT).show();
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
                                    String classUniPushId = String.valueOf(snapshot.child("uniPushClassId").getValue());
                                    String subjectUniPush = subject_details_model.getSubjectUniPushId();
                                    String subjectName = subject_details_model.getSubjectName();
                                    mListener.renameSubject(groupPushId, classUniPushId, subjectUniPush, subjectName);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(context, "Clicked on Delete Subject: " + subjectDetailsModelList.get(getAdapterPosition()).getSubjectName(), Toast.LENGTH_SHORT).show();


                    }

                }
            });

 */
        }
    }
}
