package com.cllasify.cllasify.Adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Adaptor_ShowDoubt extends RecyclerView.Adapter<Adaptor_ShowDoubt.MyViewHolder> {

    private Context context;
    private List<Class_Group> mDatalistNew;
    ProgressDialog notifyPB;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{

        void showDoubtChat(String doubtQuestion, String groupPush, String groupClassPush, String groupSubjectPush, String doubtQuestionPush);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_ShowDoubt(Context context, List<Class_Group> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_doubt, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userID=currentUser.getUid();
        Class_Group class_Doubt=mDatalistNew.get(position);
        Calendar calenderCC= Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

        String topic=class_Doubt.getGroupName();
        String doubt=class_Doubt.getGroupPositionId();
        String doubtUserName=class_Doubt.getUserName();

        holder.tv_DateTime.setText(dateTimeCC);

        if(topic==null){
            holder.tv_Topic.setVisibility(View.GONE);
        }else{
            holder.tv_Topic.setText(topic);
        }
        if(doubtUserName==null){
            holder.tv_Username.setVisibility(View.GONE);
        }else{
            holder.tv_Username.setText(doubtUserName);
        }
        if(doubt==null){
            holder.tv_Doubt.setVisibility(View.GONE);
        }else{
            holder.tv_Doubt.setText(doubt);
        }

        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.ll_Doubt.setBackgroundColor(randomAndroidColor);


    }

    @Override
    public int getItemCount() {
        return mDatalistNew.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Topic,tv_Username,tv_Doubt,tv_DateTime;
        LinearLayout ll_Doubt,doubt_item;


        Class_Group class_Group;
        Boolean clicked;
        DatabaseReference refLike;

        public MyViewHolder(View itemView) {
            super(itemView);

            doubt_item = itemView.findViewById(R.id.doubt_item);
            tv_Topic =itemView.findViewById(R.id.tv_Topic);
            tv_Username =itemView.findViewById(R.id.tv_Username);
            tv_Doubt =itemView.findViewById(R.id.tv_Doubt);
            tv_DateTime =itemView.findViewById(R.id.tv_DateTime);
            ll_Doubt =itemView.findViewById(R.id.ll_Doubt);

            doubt_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            Class_Group user = mDatalistNew.get(getAdapterPosition());
                            String doubtQuestion = user.getGroupPositionId();
                            String groupPush = user.getGrpJoiningStatus();
                            String groupClassPush = user.getUserEmailId();
                            String groupSubjectPush = user.getPosition();
                            String doubtQuestionPush = user.getAdminUserId();

                            mListener.showDoubtChat(doubtQuestion,groupPush,groupClassPush,groupSubjectPush,doubtQuestionPush);
                        }
                    }else{
                        Toast.makeText(context, "no any doubt", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            tv_Group2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            class_Group = mDatalistNew.get(getAdapterPosition());
//                            String groupName = class_Group.getGroupName();
//                            String subgroupName = class_Group.getGroup2();
//                            String groupTitle = class_Group.getPosition();
//
//                            mListener.addSubChild2(position,groupName,subgroupName,groupTitle);
//                        }
//                    }
//                }
//            });
//            tv_Group3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            class_Group = mDatalistNew.get(getAdapterPosition());
//                            String groupName = class_Group.getGroupName();
//                            String subgroupName = class_Group.getGroup3();
//                            String groupTitle = class_Group.getPosition();
//
//                            mListener.addSubChild3(position,groupName,subgroupName,groupTitle);
//                        }
//                    }
//                }
//            });
//            tv_Group4.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            class_Group = mDatalistNew.get(getAdapterPosition());
//                            String groupName = class_Group.getGroupName();
//                            String subgroupName = class_Group.getGroup4();
//                            String groupTitle = class_Group.getPosition();
//
//                            mListener.addSubChild4(position,groupName,subgroupName,groupTitle);
//                        }
//                    }
//                }
//            });
//            tv_Group5.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            class_Group = mDatalistNew.get(getAdapterPosition());
//                            String groupName = class_Group.getGroupName();
//                            String subgroupName = class_Group.getGroup5();
//                            String groupTitle = class_Group.getPosition();
//
//                            mListener.addSubChild5(position,groupName,subgroupName,groupTitle);
//                        }
//                    }
//                }
//            });
//            tv_Group6.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            class_Group = mDatalistNew.get(getAdapterPosition());
//                            String groupName = class_Group.getGroupName();
//                            String subgroupName = class_Group.getGroup6();
//                            String groupTitle = class_Group.getPosition();
//
//                            mListener.addSubChild6(position,groupName,subgroupName,groupTitle);
//                        }
//                    }
//                }
//            });
//            tv_Group7.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            class_Group = mDatalistNew.get(getAdapterPosition());
//                            String groupName = class_Group.getGroupName();
//                            String subgroupName = class_Group.getGroup7();
//                            String groupTitle = class_Group.getPosition();
//
//                            mListener.addSubChild7(position,groupName,subgroupName,groupTitle);
//                        }
//                    }
//                }
//            });
//            tv_Group8.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            class_Group = mDatalistNew.get(getAdapterPosition());
//                            String groupName = class_Group.getGroupName();
//                            String subgroupName = class_Group.getGroup8();
//                            String groupTitle = class_Group.getPosition();
//
//                            mListener.addSubChild8(position,groupName,subgroupName,groupTitle);
//                        }
//                    }
//                }
//            });
//            tv_Group9.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            class_Group = mDatalistNew.get(getAdapterPosition());
//                            String groupName = class_Group.getGroupName();
//                            String subgroupName = class_Group.getGroup9();
//                            String groupTitle = class_Group.getPosition();
//                            mListener.addSubChild9(position,groupName,subgroupName,groupTitle);
//                        }
//                    }
//                }
//            });

//            ib_ib_AddGroup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
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
//                        String groupName = user.getGroupName();
//                        String groupTitle = user.getPosition();
//                        String groupUserID = user.getUserId();
//
//                            mListener.addSubGroup(position,groupTitle,groupUserID);
//                        }
//                    }
//                }
//            });

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


