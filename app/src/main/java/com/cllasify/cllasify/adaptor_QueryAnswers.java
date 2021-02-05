package com.cllasify.cllasify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class adaptor_QueryAnswers extends RecyclerView.Adapter<adaptor_QueryAnswers.MyViewHolder> {

    private Context mcontext;
    private List<class_Answer> mAnswers;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{

    }
    private FirebaseUser firebaseUser;

    public adaptor_QueryAnswers(Context mcontext, List<class_Answer> mAnswers) {
        this.mcontext = mcontext;
        this.mAnswers = mAnswers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.list_d_answer,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        String userId=firebaseUser.getUid();
        class_Answer Answers=mAnswers.get(position);

        String userAnswers=Answers.getAns();
        String userName=Answers.getUserName();
        String date=Answers.getDateTime();

        if (userAnswers != null) {
            holder.userAnswers_tv.setText(userAnswers);
        }else{
            holder.userAnswers_tv.setVisibility(View.GONE);
        }

        if (userName != null) {
            holder.userName_tv.setText(userName+" Answered on: "+date);
        }else{
            holder.userName_tv.setVisibility(View.GONE);
        }
        //getuserinfo(holder.userName_tv,Answers.getPublisher());



//        holder.ansLikeCount_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    if (position != RecyclerView.NO_POSITION) {
//                        //mListener.dislikeAns();
//                        String tag = String.valueOf(v.getTag());
//                        // int id = (int) dislike_ib.getTag();
//                        if (holder.ansLikeCount_tv.getTag().equals("like")){
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("FeedQuestions")
//                                    .child("Answer")
//                                    .child("Likes")
//                                    .child(String.valueOf(position))
//                                    .child(userId).setValue(true);
//                        }else{
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("FeedQuestions")
//                                    .child("Answer")
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
//        holder.ansDisLikeCount_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    if (position != RecyclerView.NO_POSITION) {
//                        //mListener.dislikeAns();
//                        String tag = String.valueOf(v.getTag());
//                        // int id = (int) dislike_ib.getTag();
//                        if (holder.ansDisLikeCount_tv.getTag().equals("dislike")){
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("FeedQuestions")
//                                    .child("Answer")
//                                    .child("DisLikes")
//                                    .child(String.valueOf(position))
//                                    .child(userId).setValue(true);
//                        }else{
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("FeedQuestions")
//                                    .child("Answer")
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

//        holder.userAnswers_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(mcontext,landing_ExamDoubts.class);
//                intent.putExtra("publisher",Answers.getPublisher());
//                mcontext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

//        public ImageView userImage_iv;
        public TextView userName_tv,userAnswers_tv,ansLikeCount_tv,ansDisLikeCount_tv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

//            userImage_iv = itemView.findViewById(R.id.userImage_iv);
            userName_tv = itemView.findViewById(R.id.userName_tv);
            userAnswers_tv = itemView.findViewById(R.id.userAnswer_tv);
//            ansLikeCount_tv = itemView.findViewById(R.id.ansLikeCount_tv);
//            ansDisLikeCount_tv = itemView.findViewById(R.id.ansDisLikeCount_tv);
//
//            ansLikeCount_tv.setTag("like");
//            ansDisLikeCount_tv.setTag("dislike");

        }

//        public void LikeCount(int position) {
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
//                    .child("FeedQuestions").child("Answer").child("Likes").child(String.valueOf(position));
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ansLikeCount_tv.setText("Like("+snapshot.getChildrenCount()+")");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        public void DisLikeCount(int position) {
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
//                    .child("FeedQuestions").child("Answer").child("DisLikes").child(String.valueOf(position));
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ansDisLikeCount_tv.setText("DisLike("+snapshot.getChildrenCount()+")");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
    }

//    private void getuserinfo(TextView textView, String publisher){
//
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Admin").child("UserRegister").child(publisher);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                class_User user=snapshot.getValue(class_User.class);
//                textView.setText(user.getName());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//
//            }
//        });
//    }
}
