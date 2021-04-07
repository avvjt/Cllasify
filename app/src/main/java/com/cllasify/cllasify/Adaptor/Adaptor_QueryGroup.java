package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class_Group;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Adaptor_QueryGroup extends RecyclerView.Adapter<Adaptor_QueryGroup.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    ProgressDialog notifyPB;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{
//        void onSaveQues(int position, String mTitle, String mDesc);
        //void fillbyOfficialLink(int position, String offWeb);

        //void dislikeAns(int position, String tag);
//        void shareQues(int position, String question);


//        void listitem(int position, String question, String pushQues, String pushAns,String category);

        void addSubGroup(int position, String groupTitle, String groupUserID);

        void addSubChild1(int position, String groupName, String subgroupName);
        void addSubChild2(int position, String groupName, String subgroupName);
        void addSubChild3(int position, String groupName, String subgroupName);
        void addSubChild4(int position, String groupName, String subgroupName);
        void addSubChild5(int position, String groupName, String subgroupName);
        void addSubChild6(int position, String groupName, String subgroupName);
        void addSubChild7(int position, String groupName, String subgroupName);
        void addSubChild8(int position, String groupName, String subgroupName);
        void addSubChild9(int position, String groupName, String subgroupName);
        //void likeAns(int position, String tag);
//        void saveAns(int position, String tag);
//        void likeAns(View v, int position, Boolean clicked);
//        void onWebLinkClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_QueryGroup(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_groupname, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userID=currentUser.getUid();

//        userEmailID= currentUser.getEmail();
//        userPhoto=currentUser.getPhotoUrl();
//        userName=currentUser.getDisplayName();
////        final Class_Group user = mDatalistNew.get(position);

//        DatabaseReference refLike;


//        String user1=user.getUserId();
//        String userName=user.getUserName();
//        String examname = user.getQuesCategory();
//        String ques = user.getQuestion();
//        String time=user.getDateTime();
//        String userAns=user.getAns();
//        Uri userPhoto=user.getUserPhoto();
//        String pushid= user.getPushId();
//
////        holder.examName_tv.setText("Exam Name: "+examname);
//        holder.examQues_tv.setText(ques);
//        holder.tv_UserName.setText(userName);
//        Glide.with(context)
//                .load(userPhoto)
//                .into(holder.ib_UserProfile);
        Class_Group Answers=mDatalistNew.get(position);

//        String userAnswers=Answers.getAns();
        String groupName=Answers.getGroupName();
        String grouptitle= Answers.getPosition();


        String group1=mDatalistNew.get(position).getGroup1();
        String group2=mDatalistNew.get(position).getGroup2();
        String group3=mDatalistNew.get(position).getGroup3();
        String group4=mDatalistNew.get(position).getGroup4();
        String group5=mDatalistNew.get(position).getGroup5();
        String group6=mDatalistNew.get(position).getGroup6();
        String group7=mDatalistNew.get(position).getGroup7();
        String group8=mDatalistNew.get(position).getGroup8();
        String group9=mDatalistNew.get(position).getGroup9();

//        String group3=mDatalistNew.get(position).getGroup3();
//        String group2=mDatalistNew.get(position).getGroup2();
//        Toast.makeText(context, "group4="+group4, Toast.LENGTH_SHORT).show();

        if(group1==null){
            holder.tv_Group1.setVisibility(View.GONE);
        }else{
            holder.tv_Group1.setText(group1);
        }
        if(group2==null){
            holder.tv_Group2.setVisibility(View.GONE);
        }else{
            holder.tv_Group2.setText(group2);
        }
        if(group3==null){
            holder.tv_Group3.setVisibility(View.GONE);
        }else{
            holder.tv_Group3.setText(group3);
        }
        if(group4==null){
            holder.tv_Group4.setVisibility(View.GONE);
        }else{
            holder.tv_Group4.setText(group4);
        }
        if(group5==null){
            holder.tv_Group5.setVisibility(View.GONE);
        }else{
            holder.tv_Group5.setText(group5);
        }
        if(group6==null){
            holder.tv_Group6.setVisibility(View.GONE);
        }else{
            holder.tv_Group6.setText(group6);
        }
        if(group7==null){
            holder.tv_Group7.setVisibility(View.GONE);
        }else{
            holder.tv_Group7.setText(group7);
        }
        if(group8==null){
            holder.tv_Group8.setVisibility(View.GONE);
        }else{
            holder.tv_Group8.setText(group8);
        }
        if(group9==null){
            holder.tv_Group9.setVisibility(View.GONE);
        }else{
            holder.tv_Group9.setText(group9);
        }

        //        String group2=Answers.getGroup2();
//        String group3=Answers.getGroup3();
//        String group4=Answers.getGroup3();
//        String group5=Answers.getGroup3();
//        String group6=Answers.getGroup3();
//        String group7=Answers.getGroup3();
//        String group8=Answers.getGroup3();
//
//        if (group1.isEmpty()){
//            holder.tv_Group1.setVisibility(View.GONE);
//        }else{
//            holder.tv_Group1.setText(group1);
//        }
//        if (group2.isEmpty()){
//            holder.tv_Group2.setVisibility(View.GONE);
//        }else{
//            holder.tv_Group2.setText(group2);
//        }
//        if (group3.isEmpty()){
//            holder.tv_Group3.setVisibility(View.GONE);
//        }else{
//            holder.tv_Group3.setText(group3);
//        }
//        if (group4.isEmpty()){
//            holder.tv_Group4.setVisibility(View.GONE);
//        }else{
//            holder.tv_Group4.setText(group4);
//        }
//        if (group5.isEmpty()){
//            holder.tv_Group5.setVisibility(View.GONE);
//        }else{
//            holder.tv_Group5.setText(group5);
//        }
//        if (group6.isEmpty()){
//            holder.tv_Group6.setVisibility(View.GONE);
//        }else{
//            holder.tv_Group6.setText(group6);
//        }
//        if (group7.isEmpty()){
//            holder.tv_Group7.setVisibility(View.GONE);
//        }else{
//            holder.tv_Group7.setText(group7);
//        }
//        if (group8.isEmpty()){
//            holder.tv_Group8.setVisibility(View.GONE);
//        }else{
//            holder.tv_Group8.setText(group8);
//        }

//        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference().
//                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group1");
//        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    holder.tv_Group1.setText(snapshot.getValue().toString());
//                }else{
//                    holder.tv_Group1.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
////        DatabaseReference reference2= FirebaseDatabase.getInstance().getReference().
////                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group2");
////        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                if (snapshot.exists()){
////                    holder.tv_Group2.setText(snapshot.getValue().toString());
////                }else{
////                    holder.tv_Group2.setVisibility(View.GONE);
////                }
////            }
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////            }
////        });
////        DatabaseReference reference3= FirebaseDatabase.getInstance().getReference().
////                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group3");
////        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                if (snapshot.exists()){
////                    holder.tv_Group3.setText(snapshot.getValue().toString());
////                }else{
////                    holder.tv_Group3.setVisibility(View.GONE);
////                }
////            }
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////            }
////        });
////        DatabaseReference reference4= FirebaseDatabase.getInstance().getReference().
////                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group4");
////        reference4.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                if (snapshot.exists()){
////                    holder.tv_Group4.setText(snapshot.getValue().toString());
////                }else{
////                    holder.tv_Group4.setVisibility(View.GONE);
////                }
////            }
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////            }
////        });
//        DatabaseReference reference5= FirebaseDatabase.getInstance().getReference().
//                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group5");
//        reference5.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    holder.tv_Group5.setText(snapshot.getValue().toString());
//                }else{
//                    holder.tv_Group5.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//        DatabaseReference reference6= FirebaseDatabase.getInstance().getReference().
//                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group6");
//        reference6.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    holder.tv_Group6.setText(snapshot.getValue().toString());
//                }else{
//                    holder.tv_Group6.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//        DatabaseReference reference7= FirebaseDatabase.getInstance().getReference().
//                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group7");
//        reference7.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    holder.tv_Group7.setText(snapshot.getValue().toString());
//                }else{
//                    holder.tv_Group7.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//        DatabaseReference reference8= FirebaseDatabase.getInstance().getReference().
//                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group8");
//        reference8.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    holder.tv_Group8.setText(snapshot.getValue().toString());
//                }else{
//                    holder.tv_Group8.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//        DatabaseReference reference9= FirebaseDatabase.getInstance().getReference().
//                child( "Groups" ).child( "User_All_Group" ).child(userID).child(grouptitle).child("group9");
//        reference9.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    holder.tv_Group9.setText(snapshot.getValue().toString());
//                }else{
//                    holder.tv_Group9.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });


//        String answerUserName=Answers.getAnsUserName();
//        String date=Answers.getDateTime();
//        String quespushid= Answers.getPushId();
//        String anspushid= Answers.getAnsPushId();

//        if (examname != null) {
//            holder.examName_tv.setText("Exam Name: "+examname);
//        }else{
//        holder.examName_tv.setVisibility(View.GONE);
//        }
//        if (ques != null) {
//            holder.examQues_tv.setText("Q.No-"+(position+1)+" : "+ques);
//        }else{
//            holder.examQues_tv.setVisibility(View.GONE);
//        }
//        if (time != null) {
//            holder.date_tv.setText("Asked on: "+time);
//        }else{
//            holder.date_tv.setVisibility(View.GONE);
//        }
//        if (userName != null) {
//            holder.userName_tv.setText(userName+" asked a Question, Please answer ");
//        }else{
//            holder.userName_tv.setText("New User asked a Question, Please answer ");
//
//        }

//        holder.like_ib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        if (holder.clicked){
//            holder.like_ib.setBackgroundResource(R.drawable.save_hover);
//            holder.clicked=false;
//            notifyDataSetChanged();
//
//        }
//        else   {
//            holder.like_ib.setBackgroundResource(R.drawable.save);
//            holder.clicked=true;
//            notifyDataSetChanged();
//
//        }

        Boolean isExpanded=mDatalistNew.get(position).isExpandable();
        holder.expandable_ll.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.tv_GroupTitle.setText(groupName);
//        notifyItemChanged(position);


//        boolean Expandable=mDatalistNew.get(position).isExpandable();
//        if (Expandable) {
//            holder.expandable_ll.setVisibility(View.VISIBLE);
//
//        }
//            holder.LikebtnStatus(position);
//            holder.LikeCount(position);
//            holder.SavedQStatus(position);
//            holder.getCommentCount(userAnswers,position,quespushid,anspushid);
//            holder.getAnsLikesCount(userAnswers,position,quespushid,anspushid);
//            holder.getAnsDislikeCount(userAnswers,position,quespushid,anspushid);
//            holder.getAnsFlagCount(userAnswers,position,quespushid,anspushid);

//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//            assert mUser != null;
//            String userId = mUser.getUid();


//            holder.tv_GroupTitle.setText(answerUserName+" Answered on: "+date);

//            holder.getUserLikeDislikes(quespushid,anspushid,userId);
//
//            holder.examQues_tv.setText(userAnswers);
//            holder.tv_UserName.setText(answerUserName+" Answered on: "+date);
//
//
//            holder.tv_AnslikeCount.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        if (position != RecyclerView.NO_POSITION) {
//                            //mListener.dislikeAns();
//                            String tag = String.valueOf(v.getTag());
//                            // int id = (int) dislike_ib.getTag();
//                            if (holder.tv_AnslikeCount.getTag().equals("Like")) {
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("Feed")
//                                        .child("AllAnswerReview")
//                                        .child("Like")
//                                        .child(quespushid)
//                                        .child(anspushid)
//                                        .child(userId).setValue(true);
//                                holder.tv_AnslikeCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbupdark16, 0, 0, 0);
//                                holder.tv_AnslikeCount.setTag("Like_user");
//                            } else {
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("Feed")
//                                        .child("AllAnswerReview")
//                                        .child("Like")
//                                        .child(quespushid)
//                                        .child(anspushid)
//                                        .child(userId).removeValue();
//                                holder.tv_AnslikeCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbup_16, 0, 0, 0);
//                                holder.tv_AnslikeCount.setTag("Like");
//                            }
//                            //mListener.likeAns(position, tag);
//                        }
//                    }
//
//                }
//            });
//            holder.tv_AnsdislikeCount.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (mListener != null) {
//                                    if (position != RecyclerView.NO_POSITION) {
//                                        //mListener.dislikeAns();
//                                        String tag = String.valueOf(v.getTag());
//                                        // int id = (int) dislike_ib.getTag();
//                                        if (holder.tv_AnsdislikeCount.getTag().equals("Dislike")) {
//                                            FirebaseDatabase.getInstance().getReference()
//                                                    .child("Feed")
//                                                    .child("AllAnswerReview")
//                                                    .child("Dislike")
//                                                    .child(quespushid)
//                                                    .child(anspushid)
//                                                    .child(userId).setValue(true);
//                                            holder.tv_AnsdislikeCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbdowndark_16, 0, 0, 0);
//                                            holder.tv_AnsdislikeCount.setTag("Dislike_user");
//                                        } else {
//                                            FirebaseDatabase.getInstance().getReference()
//                                                    .child("Feed")
//                                                    .child("AllAnswerReview")
//                                                    .child("Dislike")
//                                                    .child(quespushid)
//                                                    .child(anspushid)
//                                                    .child(userId).removeValue();
//                                            holder.tv_AnsdislikeCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbdowndark_16, 0, 0, 0);
//                                            holder.tv_AnsdislikeCount.setTag("Dislike");
//                                        }
//                                        //mListener.likeAns(position, tag);
//                                    }
//                                }
//
//                            }
//                        });
//            holder.tv_AnsflagCount.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (mListener != null) {
//                                    if (position != RecyclerView.NO_POSITION) {
//                                        //mListener.dislikeAns();
//                                        String tag = String.valueOf(v.getTag());
//                                        // int id = (int) dislike_ib.getTag();
//                                        if (holder.tv_AnsflagCount.getTag().equals("Flag")) {
//                                            FirebaseDatabase.getInstance().getReference()
//                                                    .child("Feed")
//                                                    .child("AllAnswerReview")
//                                                    .child("Flag")
//                                                    .child(quespushid)
//                                                    .child(anspushid)
//                                                    .child(userId).setValue(true);
//                                            holder.tv_AnsflagCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.flagdark_16, 0, 0, 0);
//                                            holder.tv_AnsflagCount.setTag("Flag_user");
//                                        } else {
//                                            FirebaseDatabase.getInstance().getReference()
//                                                    .child("Feed")
//                                                    .child("AllAnswerReview")
//                                                    .child("Flag")
//                                                    .child(quespushid)
//                                                    .child(anspushid)
//                                                    .child(userId).removeValue();
//                                            holder.tv_AnsflagCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.flags_16, 0, 0, 0);
//                                            holder.tv_AnsflagCount.setTag("Flag");
//
//                                        }
//                                        //mListener.likeAns(position, tag);
//                                    }
//                                }
//
//                            }
//                        });
//        }else{
//            holder.tv_AnsflagCount.setEnabled(false);
//            holder.tv_AnsdislikeCount.setEnabled(false);
//            holder.tv_AnslikeCount.setEnabled(false);
//        }

    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

//        TextView examName_tv;
        TextView examQues_tv;
        TextView date_tv;
//        TextView likedislike_tv;
        TextView tv_GroupTitle,tv_Group1,tv_Group2,tv_Group3,tv_Group4,
        tv_Group5,tv_Group6,tv_Group7,tv_Group8,tv_Group9;
//        TextView userallAns_tv, tv_CommentCount, tv_AnslikeCount, tv_AnsflagCount, tv_AnsdislikeCount, tv_GroupTitle;

        TextView saveQ, tv_anslikecount,shareQ;
        ImageButton save_ib,like_ib,share_ib, ib_ib_AddGroup;
       // LinearLayout l1,l2,l3,l4;
//        LinearLayout like_ll,viewAns_ll,touchexpand_ll;
        LinearLayout touchexpand_ll,expandable_ll;

        Class_Group class_Group;
        Boolean clicked;
        DatabaseReference refLike;
//        FirebaseAuth mAuth;
//        FirebaseUser mCurrentUser;

        public MyViewHolder(View itemView) {
            super(itemView);

//            mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
//            examName_tv = itemView.findViewById(R.id.tv_ExamName);
            tv_GroupTitle =itemView.findViewById(R.id.tv_GroupTitle);
            tv_Group1=itemView.findViewById(R.id.tv_Group1);
            tv_Group2=itemView.findViewById(R.id.tv_Group2);
            tv_Group3=itemView.findViewById(R.id.tv_Group3);
            tv_Group4=itemView.findViewById(R.id.tv_Group4);
            tv_Group5=itemView.findViewById(R.id.tv_Group5);
            tv_Group6=itemView.findViewById(R.id.tv_Group6);
            tv_Group7=itemView.findViewById(R.id.tv_Group7);
            tv_Group8=itemView.findViewById(R.id.tv_Group8);
            tv_Group9=itemView.findViewById(R.id.tv_Group9);

//            tv_Group8=itemView.findViewById(R.id.tv_Group8);
////            examQues_tv = itemView.findViewById(R.id.tv_AskQuestion);
//            touchll = itemView.findViewById(R.id.touch_ll);
//            tv_CommentCount = itemView.findViewById(R.id.tv_AnsCount);
//            tv_AnslikeCount = itemView.findViewById(R.id.tv_likeCount);
//            tv_AnsdislikeCount = itemView.findViewById(R.id.tv_dislikeCount);
//            tv_AnsflagCount = itemView.findViewById(R.id.tv_flagCount);
//            userAns_tv=itemView.findViewById(R.id.userAns_tv);
//            userallAns_tv=itemView.findViewById(R.id.userallAns_tv);

            ib_ib_AddGroup = itemView.findViewById(R.id.ib_AddGroup);

//            tv_AnsflagCount.setTag("Flag");
//            tv_AnslikeCount.setTag("Like");
//            tv_AnsdislikeCount.setTag("Dislike");
//            date_tv = itemView.findViewById(R.id.tv_date);
//            likedislike_tv=itemView.findViewById(R.id.tv_like);

            touchexpand_ll=itemView.findViewById(R.id.ll_ShowGroup);
            expandable_ll=itemView.findViewById(R.id.ll_subGroup);

//            like_ll=itemView.findViewById(R.id.likequestion_ll);
//            viewAns_ll=itemView.findViewById(R.id.viewAns_ll);

//            save_ib= itemView.findViewById(R.id.ib_save);
//            like_ib= itemView.findViewById(R.id.ib_like);
//            dislike_ib= itemView.findViewById(R.id.ib_dislike);
//            share_ib= itemView.findViewById(R.id.ib_share);

//            dislike_count_tv=itemView.findViewById(R.id.dislike_count_tv);

//            dislike_ib.setTag("dislike");
//            like_ib.setTag("like");
//            save_ib.setTag("save");

            //expandLL=itemView.findViewById(R.id.expandAns_ll);

//            refLike= FirebaseDatabase.getInstance().getReference().child("FeedQuestions").child("Likes");
//            mAuth= FirebaseAuth.getInstance();
//            refLike.keepSynced(true);

            touchexpand_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class_Group user = mDatalistNew.get(getAdapterPosition());
                    user.setExpandable(!user.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            tv_Group1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup1();

                            mListener.addSubChild1(position,groupName,subgroupName);
                        }
                    }
                }
            });
            tv_Group2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup2();

                            mListener.addSubChild2(position,groupName,subgroupName);
                        }
                    }
                }
            });
            tv_Group3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup3();

                            mListener.addSubChild3(position,groupName,subgroupName);
                        }
                    }
                }
            });
            tv_Group4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup4();

                            mListener.addSubChild4(position,groupName,subgroupName);
                        }
                    }
                }
            });
            tv_Group5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup5();

                            mListener.addSubChild5(position,groupName,subgroupName);
                        }
                    }
                }
            });
            tv_Group6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup6();

                            mListener.addSubChild6(position,groupName,subgroupName);
                        }
                    }
                }
            });
            tv_Group7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup7();

                            mListener.addSubChild7(position,groupName,subgroupName);
                        }
                    }
                }
            });
            tv_Group8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup8();

                            mListener.addSubChild8(position,groupName,subgroupName);
                        }
                    }
                }
            });
            tv_Group9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            class_Group = mDatalistNew.get(getAdapterPosition());
                            String groupName = class_Group.getGroupName();
                            String subgroupName = class_Group.getGroup9();

                            mListener.addSubChild9(position,groupName,subgroupName);
                        }
                    }
                }
            });

            ib_ib_AddGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            //mListener.dislikeAns();
//                            String tag=String.valueOf(v.getTag());
//                            int id = (int) dislike_ib.getTag();
//                            if(tag.equals("save")){
//                                save_ib.setTag("save_hover");
//                                save_ib.setImageResource(R.drawable.save_hover);
//                            }else{
//                                save_ib.setTag("save");
//                                save_ib.setImageResource(R.drawable.save);
//                            }

                            Class_Group user = mDatalistNew.get(getAdapterPosition());
                        String groupName = user.getGroupName();
                        String groupTitle = user.getPosition();
                        String groupUserID = user.getUserId();

                            mListener.addSubGroup(position,groupTitle,groupUserID);
                        }
                    }
                }
            });

//            touchll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        Class_Group user = mDatalistNew.get(getAdapterPosition());
//                        String question = user.getQuestion();
//                        String pushQues = user.getPushId();
//                        String pushAns = user.getAnsPushId();
//                        String answer = user.getAns();
//                        String category = user.getQuesCategory();
//
//                            //mListener.dislikeAns();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.listitem(position, question,pushQues,pushAns,category);
//                        }
//                    }
//                }
//            });
//            like_ib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            //mListener.dislikeAns();
//                            String tag = String.valueOf(v.getTag());
////                            int id = (int) dislike_ib.getTag();
////                            if (tag.equals("like")) {
////                                like_ib.setTag("like_hover");
////                                like_ib.setImageResource(R.drawable.save_hover);
////                            } else {
////                                dislike_ib.setTag("like");
////                                dislike_ib.setImageResource(R.drawable.save);
////                            }
//                            mListener.likeAns(position, tag);
//
//                        }
//                    }
//                }
//            });
//            dislike_ib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            //mListener.dislikeAns();
//                            String tag = String.valueOf(v.getTag());
////                            int id = (int) dislike_ib.getTag();
//
//
//                            if (tag.equals("dislike")) {
//                                dislike_ib.setTag("dislike_hover");
//                                dislike_ib.setImageResource(R.drawable.dislike_hover);
//                            } else {
//                                dislike_ib.setTag("dislike");
//                                dislike_ib.setImageResource(R.drawable.dislike);
//                            }
//                            mListener.dislikeAns(position, tag);
//                        }
//                    }
//
//                }
//            });
//            like_ib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            //mListener.dislikeAns();
//                            String tag = String.valueOf(v.getTag());
//                           // int id = (int) dislike_ib.getTag();
//                            if (tag.equals("like")) {
//                                like_ib.setTag("like_hover");
//                                like_ib.setImageResource(R.drawable.like_hover);
//                            } else {
//                                like_ib.setTag("like");
//                                like_ib.setImageResource(R.drawable.like);
//                            }
//                            mListener.likeAns(position, tag);
//                        }
//                    }
//
//                }
//            });
//            share_ib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//
//                        //String offWeb=offWebSite.getText().toString();
//                        Class_Group user = mDatalistNew.get(getAdapterPosition());
//
//                        //String moreInfoLink=moreInfo.getText().toString();
//                        String question = user.getQuestion();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.shareQues(position, question);
//
//
//                        }
//                    }
//
//                }
//            });
//            ans_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    class_UserData user = mDatalistNew.get(getAdapterPosition());
//                    user.setExpanded(!user.isExpanded());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
//            expandLL=itemView.findViewById(R.id.expandableLL);
//            touchExpandll=itemView.findViewById(R.id.touchll);


//            touchExpandll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//                void onClick(View v) {
//                    class_UserData user = mDatalistNew.get(getAdapterPosition());
//                    user.setExpanded(!user.isExpanded());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
//
//            withOfficialLink.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        //String offWeb=offWebSite.getText().toString();
//                        class_UserData user = mDatalistNew.get(getAdapterPosition());
//
//                        //String moreInfoLink=moreInfo.getText().toString();
//                        String offwebsite=user.getFormname();
//                        if (position!=RecyclerView.NO_POSITION){
//                            mListener.fillbyOfficialLink(position,offwebsite);
//                        }
//                    }
//                }
//            });

//            moreInfo.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if(mListener!=null){
//                                    int position=getAdapterPosition();
//                                    class_UserData user = mDatalistNew.get(getAdapterPosition());
//
//                                    //String moreInfoLink=moreInfo.getText().toString();
//                                    //String moreInfoLinknew=user.getFormremarks();
//                                    String moreInfoLinknew=user.getName();
//
//                                    if (position!=RecyclerView.NO_POSITION){
//                                        mListener.onMoreInfoClick(position,moreInfoLinknew);
//                                    }
//                                }
//
//                            }
//                        });
//            withUs.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        String newTitle=Title.getText().toString();
//                        String newDesc=Desc.getText().toString();
//
//                        if (position!=RecyclerView.NO_POSITION){
//                            mListener.onItemClick(position,newTitle,newDesc);
//                        }
//                    }
//
//                }
//            });


//        public void clickLikebtn(String title){
//            refLike.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child(title).hasChild(mAuth.getCurrentUser().getUid())){
//                    like_ib.setImageResource(R.drawable.save_hover);
//                    }
//                    like_ib.setImageResource(R.drawable.save);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
        }

//        public void LikebtnStatus(int position) {
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
//                    .child("FeedQuestions")
//                    .child("Likes")
//                    .child(String.valueOf(position));
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child(mCurrentUser.getUid()).exists()){
//                        MyViewHolder.this.like_ib.setTag("like_hover");
//                        MyViewHolder.this.like_ib.setImageResource(R.drawable.like_hover);
//                        //dislike_ib.setEnabled(false);
//
//                    }else{
//                        MyViewHolder.this.like_ib.setTag("like");
//                        MyViewHolder.this.like_ib.setImageResource(R.drawable.like);
//                        //dislike_ib.setEnabled(true);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        public void DisLikebtnStatus(int position, ImageButton like) {
//            DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
//                    .child("FeedQuestions")
//                    .child("DisLikes")
//                    .child(String.valueOf(position));
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child(mCurrentUser.getUid()).exists()){
//                        MyViewHolder.this.dislike_ib.setTag("dislike_hover");
//                        MyViewHolder.this.dislike_ib.setImageResource(R.drawable.dislike_hover);
//                        like_ib.setEnabled(false);
//
//                    }else{
//                        MyViewHolder.this.dislike_ib.setTag("dislike");
//                        MyViewHolder.this.dislike_ib.setImageResource(R.drawable.dislike);
//                        like_ib.setEnabled(true);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        public void SavedQStatus(int position) {
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
//                    .child("FeedQuestions")
//                    .child("SavedQuestions")
//                    .child(String.valueOf(position));
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child(mCurrentUser.getUid()).exists()){
//                        MyViewHolder.this.save_ib.setTag("save_hover");
//                        MyViewHolder.this.save_ib.setImageResource(R.drawable.save_hover);
//
//                    }else{
//                        MyViewHolder.this.save_ib.setTag("save");
//                        MyViewHolder.this.save_ib.setImageResource(R.drawable.save);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        public void LikeCount(int position) {
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
//                    .child("FeedQuestions").child("Likes").child(String.valueOf(position));
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    like_count_tv.setText(snapshot.getChildrenCount()+" Likes");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        public void DisLikeCount(int position, ImageButton like_ib) {
//            DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
//                    .child("FeedQuestions").child("DisLikes").child(String.valueOf(position));
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    dislike_count_tv.setText(snapshot.getChildrenCount()+" DisLikes");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        private void getCommentCount(String Answer, int position, String quespushid,String ansPushid){
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
//                    .child("Feed").child("Comment_List").child("Feed_All_Comment").child(quespushid).child(ansPushid);
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//
//                    tv_CommentCount.setText(""+snapshot.getChildrenCount());
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }
//        private void getAnsLikesCount(String Answer, int position, String quespushid,String anspushid){
//        DatabaseReference referenceLike=FirebaseDatabase.getInstance().getReference()
//                .child("Feed").child("AllAnswerReview").child("Like").child(quespushid).child(anspushid);
//
//        referenceLike.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//
//                    tv_AnslikeCount.setText(""+snapshot.getChildrenCount());
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }
//    private void getAnsDislikeCount(String Answer, int position, String quespushid,String anspushid){
//        DatabaseReference referencedisLike=FirebaseDatabase.getInstance().getReference()
//                .child("Feed").child("AllAnswerReview").child("Dislike").child(quespushid).child(anspushid);
//        referencedisLike.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//
//                    tv_AnsdislikeCount.setText(""+snapshot.getChildrenCount());
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }
//    private void getAnsFlagCount(String Answer, int position, String quespushid,String anspushid){
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
//                    .child("Feed").child("AllAnswerReview").child("Flag").child(quespushid).child(anspushid);
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//
//                    tv_AnsflagCount.setText(""+snapshot.getChildrenCount());
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }
//
//        public void getUserLikeDislikes(String quespushid, String anspushid, String userId) {
//
//            DatabaseReference referencedislike=FirebaseDatabase.getInstance().getReference()
//                    .child("Feed")
//                    .child("AllAnswerReview")
//                    .child("Dislike")
//                    .child(quespushid)
//                    .child(anspushid);
//            referencedislike.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child(userId).exists()){
//                        tv_AnsdislikeCount.setTag("Dislike_user");
//                        tv_AnsdislikeCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbdowndark_16, 0, 0, 0);//                        ib_likeQues.setEnabled(false);
////                        ib_flagQues.setEnabled(false);
//                    }else{
//                        tv_AnsdislikeCount.setTag("Dislike");
//                        tv_AnsdislikeCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbdown_16, 0, 0, 0);//                        ib_likeQues.setEnabled(false);
////                        ib_likeQues.setEnabled(true);
////                        ib_flagQues.setEnabled(true);
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            DatabaseReference referencelike=FirebaseDatabase.getInstance().getReference()
//                    .child("Feed")
//                    .child("AllAnswerReview")
//                    .child("Like")
//                    .child(quespushid)
//                    .child(anspushid);
//            referencelike.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child(userId).exists()){
//                        tv_AnslikeCount.setTag("Like_user");
//                        tv_AnslikeCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbupdark16, 0, 0, 0);//                        ib_likeQues.setEnabled(false);
////                        ib_flagQues.setEnabled(false);
//                    }else{
//                        tv_AnslikeCount.setTag("Like");
//                        tv_AnslikeCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbup_16, 0, 0, 0);//                        ib_likeQues.setEnabled(false);
////                        ib_likeQues.setEnabled(true);
////                        ib_flagQues.setEnabled(true);
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            DatabaseReference referenceFlag=FirebaseDatabase.getInstance().getReference()
//                    .child("Feed")
//                    .child("AllAnswerReview")
//                    .child("Flag")
//                    .child(quespushid)
//                    .child(anspushid);
//            referenceFlag.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child(userId).exists()){
//                        tv_AnsflagCount.setTag("Flag_user");
//                        tv_AnsflagCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.flagdark_16, 0, 0, 0);//                        ib_likeQues.setEnabled(false);
////                        ib_flagQues.setEnabled(false);
//                    }else{
//                        tv_AnsflagCount.setTag("Flag");
//                        tv_AnsflagCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.flags_16, 0, 0, 0);//                        ib_likeQues.setEnabled(false);
////                        ib_likeQues.setEnabled(true);
////                        ib_flagQues.setEnabled(true);
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }

    }
}


