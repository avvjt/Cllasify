package com.cllasify.cllasify.Adaptor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.Home.Edit_RollNumber;
import com.cllasify.cllasify.Home.Server_Settings;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        void onClassRenameBtn(String className, String groupPushId, String uniClassPushId);
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

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs");

            adapter_topicList.setOnItemClickListener(new Adapter_TopicList_Serv.OnItemClickListener() {
                @Override
                public void renameSubject(String groupPushId, String classUniPushId, String classPosition, String subjectName) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context.getApplicationContext())) {

                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                        context.startActivity(intent);
                    }
                    else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            final Dialog dialogBox = new Dialog(context.getApplicationContext());
                            dialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogBox.setCancelable(false);
                            dialogBox.setContentView(R.layout.dialog_rename_subject);
                            dialogBox.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                            dialogBox.show();


                            EditText et_SubjectName = dialogBox.findViewById(R.id.et_SubjectName);
                            Button btn_nextAddTopic = dialogBox.findViewById(R.id.btn_nextAddTopic);
                            et_SubjectName.setText(subjectName);

                            btn_nextAddTopic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String subjectName = et_SubjectName.getText().toString().trim();

                                    databaseReference.child(groupPushId).child(classUniPushId).child("classSubjectData").child(classPosition).child("subjectName").setValue(subjectName);


                                    dialogBox.dismiss();

                                }
                            });

                        }

                    }



                }

                @Override
                public void deleteSubject(String groupPushId, String classPos, String subjectUniPush) {
                    delSubject(groupPushId,classPos,subjectUniPush);
                }
            });


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

            showGrpMemberList.setOnItemClickListener(new Adaptor_ShowGrpMember_Serv.OnItemClickListener() {
                @Override
                public void removeStudent(String groupPushId, String classUniPushId, String studentUserId) {
                    delStudent(groupPushId,classUniPushId,studentUserId);
                }
            });

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


    private void delSubject(String groupPushId, String classPos, String subjectUniPush) {
        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child(groupPushId).child(classPos).child("classSubjectData").child(subjectUniPush).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    private void delStudent(String groupPushId,String classUniPushId,String studentUserId) {
        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child(groupPushId).child(classUniPushId).child("classStudentList").child(studentUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class")
                .child(groupPushId).child(studentUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ClassTitle;
        RecyclerView studentList;
        ImageButton spinnerMore;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_ClassTitle = itemView.findViewById(R.id.tv_ClassTitle);
            studentList = itemView.findViewById(R.id.studentList);
            spinnerMore = itemView.findViewById(R.id.spinner1);

            final PopupMenu dropDownMenu = new PopupMenu(context, spinnerMore);

            final Menu menu = dropDownMenu.getMenu();

            dropDownMenu.getMenuInflater().inflate(R.menu.class_more, menu);

            dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.toString()) {
                        case "Delete Class":
                            if (mListener != null) {
                                int position = getAdapterPosition();
                                Class_Group_Names class_group_namesList = mDatalistNew.get(position);
                                Log.d("UNIP", "onChildAdded: " + class_group_namesList.getUniPushClassId());

                                mListener.onClassDeleteBtn(class_group_namesList.getUniPushClassId());
                                mDatalistNew.remove(position);
                                notifyItemRemoved(position);
                            }

                            Log.d("GRPT", "onClick: " + getAdapterPosition());
                            break;

                        case "Edit Class":

                            if (mListener != null) {
                                int position = getAdapterPosition();
                                Class_Group_Names class_group_namesList = mDatalistNew.get(position);
                                mListener.onClassRenameBtn(class_group_namesList.getClassName(), class_group_namesList.getGroupPushId(), class_group_namesList.getUniPushClassId());
                            }
                            break;

                        case "Edit RollNumbers":
                            int position = getAdapterPosition();
                            Class_Group_Names class_group_namesList = mDatalistNew.get(position);
                            String uniClassPush = class_group_namesList.getUniPushClassId();
                            String groupPushId = class_group_namesList.getGroupPushId();
                            Intent intent = new Intent(context.getApplicationContext(), Edit_RollNumber.class);
                            intent.putExtra("uniClassPush",uniClassPush);
                            intent.putExtra("groupPushId",groupPushId);
                            context.startActivity(intent);
                            break;
                    }
                    return false;
                }
            });

            spinnerMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dropDownMenu.show();
                }
            });

        }


    }
}


