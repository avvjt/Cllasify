package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Adaptor_Server_Setting_Items extends RecyclerView.Adapter<Adaptor_Server_Setting_Items.MyViewHolder> {

    private Context context;
    private List<Class_Group_Names> mDatalistNew;

    private OnItemClickListener mListener;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference refGroupStudentList, getTempData;
    String currUserID;
    RecyclerView rv_ShowClass, rv_ShowSubject;
    String groupPushId;

    Adaptor_ShowGrpMember showGrpMemberList;
    Adapter_TopicList adapter_topicList;
    List<Class_Student_Details> listGrpMemberList;
    List<Subject_Details_Model> subjectDetailsModelList;

    public interface OnItemClickListener {
        void onClassDeleteBtn(String classPosition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adaptor_Server_Setting_Items(Context context, List<Class_Group_Names> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_group_class_serv, parent, false);
        return new MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;

        currUserID = currentUser.getUid();

        Class_Group_Names Answers = mDatalistNew.get(position);

        String groupClassName = Answers.getClassName();
        Log.d("Grouupt", "GroupName: " + groupClassName);


        holder.tv_ClassTitle.setText(groupClassName);


        rv_ShowClass = holder.itemView.findViewById(R.id.studentList);
        listGrpMemberList = new ArrayList<>();
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        showGrpMemberList = new Adaptor_ShowGrpMember(context.getApplicationContext(), listGrpMemberList);


        rv_ShowSubject = holder.itemView.findViewById(R.id.subjectList);
        subjectDetailsModelList = new ArrayList<>();
        rv_ShowSubject.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        adapter_topicList = new Adapter_TopicList(context.getApplicationContext());
        adapter_topicList.setSubjectDetailsModelList(subjectDetailsModelList);

        if (mDatalistNew.get(position).getChildItemList() != null) {
            Adapter_TopicList_Serv adapter_topicList = new Adapter_TopicList_Serv(context.getApplicationContext());
            rv_ShowSubject.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
            rv_ShowSubject.setAdapter(adapter_topicList);
            adapter_topicList.setSubjectDetailsModelList(mDatalistNew.get(position).getChildItemList());
            adapter_topicList.notifyDataSetChanged();
        } else {
            List<Subject_Details_Model> testingNo = new ArrayList<>();
            Adapter_TopicList_Serv adapter_topicList = new Adapter_TopicList_Serv(context.getApplicationContext());
            rv_ShowSubject.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
            rv_ShowSubject.setAdapter(adapter_topicList);
            adapter_topicList.setSubjectDetailsModelList(testingNo);
            adapter_topicList.notifyDataSetChanged();
            Toast.makeText(context, "No sub classes", Toast.LENGTH_SHORT).show();
        }


        if (mDatalistNew.get(position).getClass_student_detailsList() != null) {
            Adaptor_ShowGrpMember_Serv showGrpMemberList = new Adaptor_ShowGrpMember_Serv(context.getApplicationContext(), mDatalistNew.get(position).getClass_student_detailsList());
            rv_ShowClass.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
            rv_ShowClass.setAdapter(showGrpMemberList);
//            Log.d("TOP", "onBindViewHolder: "+mDatalistNew.get(position).getChildItemList().get(position).getSubjectName());
            showGrpMemberList.notifyDataSetChanged();
        } else {
            List<Class_Student_Details> testingNo = new ArrayList<>();
            Adaptor_ShowGrpMember_Serv adapter_topicList = new Adaptor_ShowGrpMember_Serv(context.getApplicationContext(), testingNo);
            rv_ShowClass.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
            rv_ShowClass.setAdapter(adapter_topicList);
            showGrpMemberList.notifyDataSetChanged();
            Toast.makeText(context, "No sub classes", Toast.LENGTH_SHORT).show();
        }

/*
        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserID);

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Class_Group_Names class_group_names = mDatalistNew.get(holder.getAdapterPosition());

                String groupPushId = class_group_names.getGroupPushId();
                String uniPushClassId = class_group_names.getUniPushClassId();

                refGroupStudentList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(uniPushClassId);
                listGrpMemberList.clear();
                refGroupStudentList.child("classStudentList").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Class_Student_Details class_student_details = snapshot.getValue(Class_Student_Details.class);
                        listGrpMemberList.add(class_student_details);

                        rv_ShowClass.setAdapter(showGrpMemberList);
                        showGrpMemberList.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Class_Group_Names class_group_names = mDatalistNew.get(holder.getAdapterPosition());

                String groupPushId = class_group_names.getGroupPushId();
                String uniPushClassId = class_group_names.getUniPushClassId();

                refGroupStudentList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(uniPushClassId);
                subjectDetailsModelList.clear();
                refGroupStudentList.child("classSubjectData").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Subject_Details_Model class_student_details = snapshot.getValue(Subject_Details_Model.class);
                        subjectDetailsModelList.add(class_student_details);
                        rv_ShowSubject.setAdapter(adapter_topicList);
                        adapter_topicList.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ClassTitle;
        ImageButton deleteClassBtn;
        RecyclerView studentList;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_ClassTitle = itemView.findViewById(R.id.tv_ClassTitle);
            deleteClassBtn = itemView.findViewById(R.id.deleteClass);
            studentList = itemView.findViewById(R.id.studentList);


            deleteClassBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mListener != null) {
                        int position = getAdapterPosition();
//                        notifyItemRangeChanged(position, mDatalistNew.size());

                        Class_Group_Names class_group_namesList = mDatalistNew.get(position);
                        Log.d("UNIP", "onChildAdded: " + class_group_namesList.getUniPushClassId());

                        mListener.onClassDeleteBtn(class_group_namesList.getUniPushClassId());
                        mDatalistNew.remove(position);
                        notifyItemRemoved(position);
                    }

                    Log.d("GRPT", "onClick: " + getAdapterPosition());
                }
            });


        }


    }
}


