package com.cllasify.cllasify.Adaptor;

import static com.cllasify.cllasify.Profile.AccountSetting_Activity.getDefaults;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.Home.Server_Activity;
import com.cllasify.cllasify.Profile.AccountSetting_Activity;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adapter_TopicList extends RecyclerView.Adapter<Adapter_TopicList.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    onSubjectClickListener onSubjectClickListener;

    public void setOnSubjectClickListener(Adapter_TopicList.onSubjectClickListener onSubjectClickListener) {
        this.onSubjectClickListener = onSubjectClickListener;
    }

    public Adapter_TopicList(Context context) {
        this.context = context;
    }

    public interface onSubjectClickListener{
        void onSubjectClick();
    }

    @NonNull
    @Override
    public Adapter_TopicList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.subject_list_item, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_TopicList.ViewHolder holder, int position) {
        holder.subjectTopic.setText(subjectDetailsModelList.get(position).getSubjectName());

        Subject_Details_Model getTopic = subjectDetailsModelList.get(position);

        String userID = SharePref.getDataFromPref(Constant.USER_ID);

        DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

        String subjectPushId = getTopic.getSubjectUniPushId();

        posTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("subjectUniPushId").exists()) {
                    if (snapshot.child("subjectUniPushId").getValue().toString().equals(subjectPushId)) {
                        holder.subjectTopic.setBackgroundResource(R.drawable.selector_subject);
                        holder.subjectTopic.setTextColor(Color.WHITE);
                    } else {
                        holder.subjectTopic.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout subListItem;
        TextView subjectTopic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subListItem = itemView.findViewById(R.id.subListItem);
            subjectTopic = itemView.findViewById(R.id.tv_subjectTopic);


            String darkLightDefaultVal = getDefaults("DefaultDarkLight", context);

            if (darkLightDefaultVal != null) {
                if (darkLightDefaultVal.equals("Dark")) {
                    Log.d("Theme", "ViewHolder: Dark");
                    subjectTopic.setTextColor(Color.parseColor("#ffffff"));
                }
                if (darkLightDefaultVal.equals("Light")) {
                    Log.d("Theme", "ViewHolder: Light");
                    subjectTopic.setTextColor(Color.parseColor("#050505"));

                }
            }

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String userID = currentUser.getUid();

            DatabaseReference setTempData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

            subjectTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subjectDetailsModelList != null) {

                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
//                            Toast.makeText(context, "Clicked on Subject", Toast.LENGTH_SHORT).show();

                            Subject_Details_Model user = subjectDetailsModelList.get(position);

                            String subjectName = user.getSubjectName();
                            String subjectUniPush = user.getSubjectUniPushId();

                            setTempData.child("subjectPosition").setValue(position);
                            setTempData.child("clickedSubjectName").setValue(subjectName);
                            setTempData.child("subjectUniPushId").setValue(subjectUniPush);

                            onSubjectClickListener.onSubjectClick();
                        }
                    }
                }
            });

        }
    }
}
