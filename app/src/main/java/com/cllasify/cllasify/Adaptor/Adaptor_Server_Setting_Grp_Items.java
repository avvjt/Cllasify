package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adaptor_Server_Setting_Grp_Items extends RecyclerView.Adapter<Adaptor_Server_Setting_Grp_Items.MyViewHolder> {

    private Context context;
    private List<Class_Group_Names> mDatalistNew;

    private OnItemClickListener mListener;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference refGroupStudentList, getTempData;
    String currUserID;
    RecyclerView rv_ShowClass;
    String groupPushId;

    Adaptor_ShowGrpMember showGrpMemberList;
    List<Class_Student_Details> listGrpMemberList;

    public interface OnItemClickListener {
        void onGrpMemberBtn(String classPosition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adaptor_Server_Setting_Grp_Items(Context context, List<Class_Group_Names> mDatalistNew) {
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



/*
        rv_ShowClass = holder.itemView.findViewById(R.id.rv_ShowClass);
        listGrpMemberList = new ArrayList<>();
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        showGrpClassList = new Adaptor_Server_Setting_Items(context.getApplicationContext(), listGrpMemberList);


        getTempData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserID);



        getTempData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                groupPushId = String.valueOf(snapshot.child("clickedGroupPushId").getValue());


                //GroupStudentList..
                refGroupStudentList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                        .child(groupPushId);
                listGrpMemberList.clear();

                refGroupStudentList.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Class_Student_Details class_userDashBoard = snapshot.getValue(Class_Student_Details.class);
                        listGrpMemberList.add(class_userDashBoard);

                        showGrpClassList.setOnItemClickListener(new Adaptor_Server_Setting_Items.OnItemClickListener() {
                            @Override
                            public void onClassDeleteBtn(String classPosition) {

//                                delClass(groupPushId,classPosition);

                            }
                        });

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



/*
            deleteClassBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mListener != null) {
                        int position = getAdapterPosition();
//                        notifyItemRangeChanged(position, mDatalistNew.size());

                        Class_Group_Names class_group_namesList = mDatalistNew.get(position);
                        Log.d("UNIP", "onChildAdded: "+class_group_namesList.getUniPushClassId());

                        mListener.onClassDeleteBtn(class_group_namesList.getUniPushClassId());
                        mDatalistNew.remove(position);
                        notifyItemRemoved(position);
                    }

                    Log.d("GRPT", "onClick: " + getAdapterPosition());
                }
            });
*/

        }


    }
}


