package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Group_Names;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adapter_Result_Subject extends RecyclerView.Adapter<Adapter_Result_Subject.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<Class_Group_Names> class_group_namesList;
    String uniPush, studentUserId;
    int specPos;
    int lastPosition = -1;
    private OnItemClickListener mListener;

    public void setSpecPos(int specPos) {
        this.specPos = specPos;
    }

    public Adapter_Result_Subject(Context context) {
        this.context = context;
    }

    public void setUniPush(String uniPush) {
        this.uniPush = uniPush;
    }

    public void setStudentUserId(String studentUserId) {
        this.studentUserId = studentUserId;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Adapter_Result_Subject.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.subject_list_item_result, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Result_Subject.ViewHolder holder, int position) {
        holder.subjectTopic.setText(subjectDetailsModelList.get(position).getSubjectName());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();

        int pos = position;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userID = currentUser.getUid();


        Subject_Details_Model subjectDetailsModel = subjectDetailsModelList.get(pos);

        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    if (snapshot.child("subjectUniPushId").exists() && snapshot.child("uniPushClassId").exists() && snapshot.child("clickedGroupPushId").exists()) {


                        String subjectUniPushId = snapshot.child("subjectUniPushId").getValue().toString().trim();
                        String uniPushClassId = snapshot.child("uniPushClassId").getValue().toString().trim();
                        String groupPushId = snapshot.child("clickedGroupPushId").getValue().toString().trim();

                        DatabaseReference resDb = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result")
                                .child(groupPushId).child(uniPushClassId).child(studentUserId).child(subjectUniPushId);

                        resDb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                    int fullTheory = Integer.parseInt(snapshot.child("theoryFullMarks").getValue().toString());
                                    int practicalFullMarks = Integer.parseInt(snapshot.child("practicalFullMarks").getValue().toString());
                                    int theoryMarks = Integer.parseInt(snapshot.child("theoryMarks").getValue().toString());
                                    int practicalMarks = Integer.parseInt(snapshot.child("practicalMarks").getValue().toString());

                                    int fullTheoryPractical = fullTheory + practicalFullMarks;
                                    int theoryPractical = theoryMarks + practicalMarks;

                                    Log.d("RESCHKK", "onDataChange: " + theoryPractical + "/" + fullTheoryPractical);

                                    holder.obtainedMarks.setText(String.valueOf(theoryPractical));
                                    holder.totalMarks.setText(String.valueOf(fullTheoryPractical));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.subListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.clickedSub(subjectDetailsModel.getSubjectUniPushId(), subjectDetailsModel.getSubjectName(), String.valueOf(pos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectDetailsModelList.size();
    }

    public void setSubjectDetailsModelList(List<Subject_Details_Model> subjectDetailsModelList) {
        this.subjectDetailsModelList = subjectDetailsModelList;
    }

    public interface OnItemClickListener {

        void clickedSub(String subUniPushId, String subjectName, String position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTopic;
        ImageButton subjectMore;
        RelativeLayout subListItem;

        TextView obtainedMarks, totalMarks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTopic = itemView.findViewById(R.id.tv_subjectTopic);
            subjectMore = itemView.findViewById(R.id.subjectMore);
            subListItem = itemView.findViewById(R.id.subListItem);
            obtainedMarks = itemView.findViewById(R.id.obtained_marks);
            totalMarks = itemView.findViewById(R.id.full_marks);

        }
    }
}
