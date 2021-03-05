package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Class_Answer;
import com.cllasify.cllasify.Landing_Feed_Answer;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adaptor_QueryQuestions extends RecyclerView.Adapter<Adaptor_QueryQuestions.MyViewHolder> {

    private Context context;
    private List<Class_Answer> mDatalistNew;

    private OnItemClickListener mListener;

    public interface  OnItemClickListener{
//        void onSaveQues(int position, String mTitle, String mDesc);
        //void fillbyOfficialLink(int position, String offWeb);

        //void dislikeAns(int position, String tag);
        void shareQues(int position, String question);
        //void likeAns(int position, String tag);
//        void saveAns(int position, String tag);
//        void likeAns(View v, int position, Boolean clicked);
//        void onWebLinkClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adaptor_QueryQuestions(Context context, List<Class_Answer> mDatalistNew) {
        this.context = context;
        this.mDatalistNew = mDatalistNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_queryques, parent, false);
        return new  MyViewHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Class_Answer user = mDatalistNew.get(position);

//        DatabaseReference refLike;
//        FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
//        assert mUser != null;
//        final String userId=mUser.getUid();
        String user1=user.getUserId();
        String userName=user.getUserName();
        String quesCategory = user.getQuesCategory();
        String ques = user.getQuestion();
        String time=user.getDateTime();
        String userAns=user.getAns();
        Uri userPhoto=user.getUserPhoto();
        String pushid= user.getPushId();

//        holder.examName_tv.setText("Exam Name: "+examname);
        holder.examQues_tv.setText(ques);
        holder.tv_UserName.setText(userName);
        Glide.with(context)
                .load(userPhoto)
                .into(holder.ib_UserProfile);

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

//        boolean Expandable=mDatalistNew.get(position).isExpandable();
//        if (Expandable){
//            holder.expandable_ll.setVisibility(View.VISIBLE);
//            holder.LikebtnStatus(position);
//            holder.LikeCount(position);
//            holder.SavedQStatus(position);
            holder.getAnswersCount(userAns,position,pushid);
            holder.getLikesCount(userAns,position,pushid);
            holder.getDislikeCount(userAns,position,pushid);
            holder.getFlagCount(userAns,position,pushid);
//
//            holder.like_ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        if (position != RecyclerView.NO_POSITION) {
//                            //mListener.dislikeAns();
//                            String tag = String.valueOf(v.getTag());
//                            // int id = (int) dislike_ib.getTag();
//                            if (holder.like_ib.getTag().equals("like")){
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("FeedQuestions")
//                                        .child("Likes")
//                                        .child(String.valueOf(position))
//                                        .child(userId).setValue(true);
//                                holder.like_ib.setImageResource(R.drawable.like_hover);
//                            }else{
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("FeedQuestions")
//                                        .child("Likes")
//                                        .child(String.valueOf(position))
//                                        .child(userId).removeValue();
//                                holder.like_ib.setImageResource(R.drawable.like);
//
//                            }
//                            //mListener.likeAns(position, tag);
//                        }
//                    }
//
//                }
//            });
//            holder.save_ib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        if (position != RecyclerView.NO_POSITION) {
//                            //mListener.dislikeAns();
//                            String tag = String.valueOf(v.getTag());
//                            // int id = (int) dislike_ib.getTag();
//                            if (holder.save_ib.getTag().equals("save")){
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("FeedQuestions")
//                                        .child("SavedQuestions")
//                                        .child(String.valueOf(position))
//                                        .child(userId).setValue(true);
//                            }else{
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("FeedQuestions")
//                                        .child("SavedQuestions")
//                                        .child(String.valueOf(position))
//                                        .child(userId).removeValue();
//
//                            }
//                            //mListener.likeAns(position, tag);
//                        }
//                    }
//
//                }
//            });
            holder.touchll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            //mListener.dislikeAns();
                            String pos= user.getPushId();
                            String examQues=user.getExamfQues();
                            String userName=user.getUserName();
                            String askedDate=user.getDateTime();

                            Intent intent=new Intent(context, Landing_Feed_Answer.class);
                            intent.putExtra("position",position);
                            intent.putExtra("quesUserName",userName);
                            intent.putExtra("quesask",ques);
                            intent.putExtra("quesCategory",quesCategory);
                            intent.putExtra("quesaskDate",askedDate);
                            intent.putExtra("pushId",pos);
                            context.startActivity(intent);
                        }
                    }

                }
            });

//
//        }
//        else{
//            holder.expandable_ll.setVisibility(View.GONE);
//        }


//        holder.SavedQStatus(position, holder.save_ib);
//        holder.DisLikebtnStatus(position, holder.dislike_ib);
//        holder.DisLikeCount(position, holder.dislike_ib);
//        holder.like_ib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    if (position != RecyclerView.NO_POSITION) {
//                        //mListener.dislikeAns();
//                        String tag = String.valueOf(v.getTag());
//                        // int id = (int) dislike_ib.getTag();
//                        if (holder.like_ib.getTag().equals("like")){
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("FeedQuestions")
//                                    .child("Likes")
//                                    .child(String.valueOf(position))
//                                    .child(userId).setValue(true);
//                        }else{
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("FeedQuestions")
//                                    .child("Likes")
//                                    .child(String.valueOf(position))
//                                    .child(userId).removeValue();
//
//                        }
//                        //mListener.likeAns(position, tag);
//                    }
//                }
//
//            }
//        });

//        holder.dislike_ib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    if (position != RecyclerView.NO_POSITION) {
//                        //mListener.dislikeAns();
//                        String tag = String.valueOf(v.getTag());
//                        // int id = (int) dislike_ib.getTag();
//                        if (holder.dislike_ib.getTag().equals("dislike")){
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("FeedQuestions")
//                                    .child("DisLikes")
//                                    .child(String.valueOf(position))
//                                    .child(userId).setValue(true);
//                        }else{
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("FeedQuestions")
//                                    .child("DisLikes")
//                                    .child(String.valueOf(position))
//                                    .child(userId).removeValue();
//
//                        }
//                        //mListener.likeAns(position, tag);
//                    }
//                }
//
//            }
//        });

//        holder.userallAns_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    if (position != RecyclerView.NO_POSITION) {
//                        //mListener.dislikeAns();
//                        Intent intent=new Intent(context,exam_Doubts.class);
//                        intent.putExtra("postid",pos);
//                        intent.putExtra("publisherid",user.getName());
//                        context.startActivity(intent);
//                    }
//                }
//
//            }
//        });
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
        TextView userAns_tv;
        TextView userallAns_tv, tv_AnsCount,tv_likeCount,tv_flagCount,tv_dislikeCount,tv_UserName;

        TextView saveQ, like_count_tv,shareQ;
        ImageButton save_ib,like_ib,share_ib,ib_UserProfile;
       // LinearLayout l1,l2,l3,l4,expandLL,touchExpandll;
//        LinearLayout like_ll,viewAns_ll,touchexpand_ll;
        LinearLayout touchll;

        Boolean clicked;
        DatabaseReference refLike;
//        FirebaseAuth mAuth;
//        FirebaseUser mCurrentUser;

        public MyViewHolder(View itemView) {
            super(itemView);

//            mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
            tv_UserName=itemView.findViewById(R.id.tv_Name);
//            examName_tv = itemView.findViewById(R.id.tv_ExamName);
            examQues_tv = itemView.findViewById(R.id.tv_AskQuestion);
            touchll = itemView.findViewById(R.id.touch_ll);

            tv_AnsCount = itemView.findViewById(R.id.tv_AnsCount);
            tv_likeCount = itemView.findViewById(R.id.tv_likeCount);
            tv_dislikeCount = itemView.findViewById(R.id.tv_dislikeCount);
            tv_flagCount = itemView.findViewById(R.id.tv_flagCount);

            ib_UserProfile = itemView.findViewById(R.id.ib_UserProfile);
//            date_tv = itemView.findViewById(R.id.tv_date);
//            likedislike_tv=itemView.findViewById(R.id.tv_like);

//            touchexpand_ll=itemView.findViewById(R.id.touch_ll);
//            expandable_ll=itemView.findViewById(R.id.expandable_ll);

//            like_ll=itemView.findViewById(R.id.likequestion_ll);
//            viewAns_ll=itemView.findViewById(R.id.viewAns_ll);

//            save_ib= itemView.findViewById(R.id.ib_save);
//            like_ib= itemView.findViewById(R.id.ib_like);
//            dislike_ib= itemView.findViewById(R.id.ib_dislike);
//            share_ib= itemView.findViewById(R.id.ib_share);
//            like_count_tv=itemView.findViewById(R.id.like_count_tv);
//            dislike_count_tv=itemView.findViewById(R.id.dislike_count_tv);

//            dislike_ib.setTag("dislike");
//            like_ib.setTag("like");
//            save_ib.setTag("save");

            userAns_tv=itemView.findViewById(R.id.userAns_tv);
            userallAns_tv=itemView.findViewById(R.id.userallAns_tv);
            //expandLL=itemView.findViewById(R.id.expandAns_ll);

//            refLike= FirebaseDatabase.getInstance().getReference().child("FeedQuestions").child("Likes");
//            mAuth= FirebaseAuth.getInstance();
//            refLike.keepSynced(true);

//            touchexpand_ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    class_Answer user = mDatalistNew.get(getAdapterPosition());
//                    user.setExpandable(!user.isExpandable());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });

//            save_ib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            //mListener.dislikeAns();
//                            String tag=String.valueOf(v.getTag());
////                            int id = (int) dislike_ib.getTag();
//                            if(tag.equals("save")){
//                                save_ib.setTag("save_hover");
//                                save_ib.setImageResource(R.drawable.save_hover);
//                            }else{
//                                save_ib.setTag("save");
//                                save_ib.setImageResource(R.drawable.save);
//                            }
//                            mListener.saveAns(position,tag);
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
//                        class_Answer user = mDatalistNew.get(getAdapterPosition());
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
//                public void onClick(View v) {
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
        private void getAnswersCount(String Answer, int position,String pushid){
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                    .child("Feed").child("Answer_List").child("Feed_All_Answer").child(pushid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" AnswersTab");

                    tv_AnsCount.setText(""+snapshot.getChildrenCount());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    private void getLikesCount(String Answer, int position,String pushid){
        DatabaseReference referenceLike=FirebaseDatabase.getInstance().getReference()
                .child("Feed").child("AllQuestionReview").child("Like").child(pushid);

        referenceLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" AnswersTab");

                    tv_likeCount.setText(""+snapshot.getChildrenCount());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    private void getDislikeCount(String Answer, int position,String pushId){
        DatabaseReference referencedisLike=FirebaseDatabase.getInstance().getReference()
                .child("Feed").child("AllQuestionReview").child("Dislike").child(pushId);
        referencedisLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" AnswersTab");

                    tv_dislikeCount.setText(""+snapshot.getChildrenCount());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    private void getFlagCount(String Answer, int position,String pushidflag){
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                    .child("Feed").child("AllQuestionReview").child("Flag").child(pushidflag);;
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" AnswersTab");

                    tv_flagCount.setText(""+snapshot.getChildrenCount());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}


