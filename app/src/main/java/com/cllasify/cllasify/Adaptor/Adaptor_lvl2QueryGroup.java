//package com.cllasify.cllasify.Adaptor;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.cllasify.cllasify.Class.Class_Group;
//import com.cllasify.cllasify.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.List;
//
//public class Adaptor_lvl2QueryGroup extends RecyclerView.Adapter<Adaptor_lvl2QueryGroup.MyViewHolder> {
//
//    private Context context;
//    private List<Class_Group> mDatalistNew;
//    ProgressDialog notifyPB;
//    private OnItemClickListener mListener;
//    int selected_position = 0;
//
//
//    public interface OnItemClickListener {
//
//        void addSubGroup(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID);
//
//
//        void viewChildGroup1(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }
//
//    public Adaptor_lvl2QueryGroup(Context context, List<Class_Group> mDatalistNew) {
//        this.context = context;
//        this.mDatalistNew = mDatalistNew;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_lvl2_groupname, parent, false);
//        return new MyViewHolder(rootview);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        assert currentUser != null;
//        String userID = currentUser.getUid();
//        Class_Group Answers = mDatalistNew.get(position);
//
//        String subGroupName = Answers.getUserEmailId();
//        String group1 = mDatalistNew.get(position).getGroup1();
//
//        holder.tv_GroupTitle.setText(subGroupName);
//        holder.tv_Group1.setBackgroundColor(selected_position == position ? context.getColor(R.color.colorPrimary) : Color.TRANSPARENT);
//
//        if (group1 == null) {
//            holder.tv_Group1.setVisibility(View.GONE);
//        } else {
//            holder.tv_Group1.setText(group1);
//        }
//
//
//        //highlight the selected class that are earlier seleceted
//        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
//        refSaveCurrentData.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount() > 0) {
//                    if (snapshot.child("tSubGroupPushId").exists()) {
//                        String GroupPushId = snapshot.child("tSubGroupPushId").getValue().toString().trim();
//                        if (subGroupName.equals(GroupPushId)) {
//                            holder.tv_GroupTitle.setBackgroundColor(context.getColor(R.color.colorPrimary));
//                        } else {
//                            holder.tv_GroupTitle.setBackgroundColor(context.getColor(R.color.transparent));
//                        }
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//        //highlight the selected topic that are earlier selected
//        DatabaseReference refSaveCurrentData1 = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
//        refSaveCurrentData1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount() > 0) {
//
//                    if (snapshot.child("tgroupClassSubjects").exists()) {
//                        String GroupClassSubjects = snapshot.child("tgroupClassSubjects").getValue().toString().trim();
//
//                        if (GroupClassSubjects.equals(group1)) {
//                            holder.tv_Group1.setBackgroundColor(context.getColor(R.color.colorPrimary));
//                        } else {
//                            holder.tv_Group2.setBackgroundColor(context.getColor(R.color.transparent));
//                            holder.tv_Group3.setBackgroundColor(context.getColor(R.color.transparent));
//                            holder.tv_Group4.setBackgroundColor(context.getColor(R.color.transparent));
//                            holder.tv_Group5.setBackgroundColor(context.getColor(R.color.transparent));
//                            holder.tv_Group6.setBackgroundColor(context.getColor(R.color.transparent));
//                            holder.tv_Group7.setBackgroundColor(context.getColor(R.color.transparent));
//                            holder.tv_Group8.setBackgroundColor(context.getColor(R.color.transparent));
//                            holder.tv_Group9.setBackgroundColor(context.getColor(R.color.transparent));
//                        }
//
//
//
//
////                        if (subGroupName.equals(GroupPushId)) {
////                            holder.tv_GroupTitle.setBackgroundColor(context.getColor(R.color.colorPrimary));
////                        } else {
////                            holder.tv_GroupTitle.setBackgroundColor(context.getColor(R.color.transparent));
////                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDatalistNew.size();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tv_GroupTitle, tv_Group1;
//
//
//        ImageButton ib_ib_AddGroup;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//
//            tv_GroupTitle = itemView.findViewById(R.id.tv_GroupTitle);
//            tv_Group1 = itemView.findViewById(R.id.tv_Group1);
//
//
//            ib_ib_AddGroup = itemView.findViewById(R.id.ib_AddGroup);
//
//            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//            assert currentUser != null;
//            String userID = currentUser.getUid();
//
//
//            DatabaseReference changeCOor = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
//
//
//            tv_GroupTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //on class name clicked
//                }
//            });
//
//            tv_Group1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            Class_Group user = mDatalistNew.get(getAdapterPosition());
//                            String groupPushId = user.getGroupCategory();
//                            String groupName = user.getGroupName();
//                            String groupCreatorName = user.getUserName();
//                            String groupClassName = user.getUserEmailId();
//                            String groupUserID = user.getUserId();
//                            String groupClassSubjects = user.getGroup1();
//
//
//                            tv_Group1.setBackgroundColor(context.getColor(R.color.colorPrimary));
//                            changeCOor.child("tgroupClassSubjects").setValue(groupClassSubjects);
//
//                            mListener.viewChildGroup1(position, groupName, groupPushId, groupClassName, groupCreatorName, groupUserID, groupClassSubjects);
//                        }
//                    }
//                }
//            });
//
//            ib_ib_AddGroup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            //mListener.dislikeAns();
////                            String tag=String.valueOf(v.getTag());
////                            int id = (int) dislike_ib.getTag();
////                            if(tag.equals("save")){
////                                save_ib.setTag("save_hover");
////                                save_ib.setImageResource(R.drawable.save_hover);
////                            }else{
////                                save_ib.setTag("save");
////                                save_ib.setImageResource(R.drawable.save);
////                            }
//
//                            Class_Group user = mDatalistNew.get(getAdapterPosition());
//                            String groupPushId = user.getGroupCategory();
//                            String groupName = user.getGroupName();
//                            String groupCreatorName = user.getUserName();
//                            String groupClassName = user.getUserEmailId();
//                            String groupUserID = user.getUserId();
//                            notifyDataSetChanged();
//
//                            mListener.addSubGroup(position, groupName, groupPushId, groupClassName, groupCreatorName, groupUserID);
//                        }
//                    }
//                }
//            });
//
//
//        }
//    }
//}
//
//
