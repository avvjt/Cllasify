package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Subject_Assign extends RecyclerView.Adapter<Adapter_Subject_Assign.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<Class_Student_Details> classStudentList;
    String uniPush;

    int lastPosition = -1;

    ArrayAdapter<String> adapterItems;

    ArrayList<String> subs = new ArrayList<>();
    ArrayList<String> teacher = new ArrayList<>();

    String itemPrimary = "null";


    public String getUniPush() {
        return uniPush;
    }

    public void setUniPush(String uniPush) {
        this.uniPush = uniPush;
    }

    public Adapter_Subject_Assign(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_Subject_Assign.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.sub_assignment_item, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Subject_Assign.ViewHolder holder, int position) {

        int period = position + 1;

        holder.subjectTopic.setText("Period: " + period);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();

//        Log.d("CHKINGTECH", "onClick: " + classStudentList.get(position).getUserName());

        subs.add(subjectDetailsModelList.get(position).getSubjectName());
//        teacher.add(classStudentList.get(position).getUserName());

        adapterItems = new ArrayAdapter<String>(context, R.layout.priority_list_item, subs);
        holder.autoTvSub.setAdapter(adapterItems);

        holder.autoTvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPrimary = adapterView.getItemAtPosition(i).toString();
//                doneBtn.setEnabled(true);

            }
        });
/*
        DatabaseReference databaseReferenceGetTeachers = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(grpPushId);

        databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                    Log.d("CHKINGTECH", "onClick: " + dataSnapshot1.getValue());
                    Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                    Log.d("CHKSUB27", "onDataChange: " + object.getUserId());
                    classStudentList.add(object);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
/*
        adapterItems = new ArrayAdapter<String>(context, R.layout.priority_list_item, teacher);
        holder.autoTvTeacher.setAdapter(adapterItems);

        holder.autoTvTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPrimary = adapterView.getItemAtPosition(i).toString();
//                doneBtn.setEnabled(true);

            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return subjectDetailsModelList.size();
    }

    public void setSubjectDetailsModelList(List<Subject_Details_Model> subjectDetailsModelList) {
        this.subjectDetailsModelList = subjectDetailsModelList;
    }

    public void setClassStudentList(List<Class_Student_Details> classStudentList) {
        this.classStudentList = classStudentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTopic;
        ImageButton subjectMore;
        AutoCompleteTextView autoTvTeacher, autoTvSub;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTopic = itemView.findViewById(R.id.period_subject);
            autoTvSub = itemView.findViewById(R.id.assignSubs);

            autoTvSub.setFocusable(false);
            autoTvSub.setClickable(true);


        }
    }
}
