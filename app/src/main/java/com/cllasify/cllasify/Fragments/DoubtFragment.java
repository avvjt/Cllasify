package com.cllasify.cllasify.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adapters.Adaptor_QueryAnswer;
import com.cllasify.cllasify.ModelClasses.Class_Answer;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class DoubtFragment extends Fragment {

    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    String currUserId, currUserName, currUserEmail;

    String groupPushId, groupClassPushId, groupClassSubjectPushId, doubtQuestionPushId, doubtCreatorName, doubtCreatorId, doubtMSGId;
    TextView tv_DoubtGroupName, tv_DoubtGroupClass, tv_Name, byName;


    List<Class_Answer> list_DoubtAnswer;
    Adaptor_QueryAnswer answerAdaptor;
    RecyclerView rv_DoubtAnswer;
    ImageButton ib_attachDoubtAns, ib_submitDoubtAns, btn_Back, btn_menu;
    EditText et_DoubtAns;
    Class_Answer userAddAnsClass;
    ImageView ib_AnsUserProfile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.doubt_fragment, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        currUserId = currentUser.getUid();
        currUserName = currentUser.getDisplayName();
        currUserEmail = currentUser.getEmail();


        tv_DoubtGroupClass = view.findViewById(R.id.tv_DoubtGroupClass);
        tv_DoubtGroupName = view.findViewById(R.id.tv_DoubtGroupName);
        tv_Name = view.findViewById(R.id.tv_Name);

        ib_AnsUserProfile = view.findViewById(R.id.ib_UserProfile);

        rv_DoubtAnswer = view.findViewById(R.id.rv_DoubtAnswer);

        et_DoubtAns = view.findViewById(R.id.et_DoubtAns);
        ib_submitDoubtAns = view.findViewById(R.id.ib_submitDoubtAns);
        byName = view.findViewById(R.id.byName);

        btn_Back = view.findViewById(R.id.btn_Back);
        btn_menu = view.findViewById(R.id.btn_menu);

        ib_submitDoubtAns.setEnabled(false);


        et_DoubtAns.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String c = String.valueOf(s);

                if (c.trim().isEmpty()) {
                    ib_submitDoubtAns.setEnabled(false);
                    ib_submitDoubtAns.setImageResource(R.drawable.ic_send_disable);
                } else {
                    ib_submitDoubtAns.setEnabled(true);
                    ib_submitDoubtAns.setImageResource(R.drawable.ic_send_24);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Fragment doubtFragment = this;


        list_DoubtAnswer = new ArrayList<>();
        answerAdaptor = new Adaptor_QueryAnswer(getContext(), list_DoubtAnswer);
        rv_DoubtAnswer.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_DoubtAnswer.setAdapter(answerAdaptor);


        DatabaseReference doubtTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserId);

        doubtTemp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    groupPushId = snapshot.child("DoubtTemps").child("groupPushId").getValue(String.class);
                    groupClassPushId = snapshot.child("DoubtTemps").child("groupClassPushId").getValue(String.class);
                    groupClassSubjectPushId = snapshot.child("DoubtTemps").child("groupClassSubjectPushId").getValue(String.class);
                    doubtQuestionPushId = snapshot.child("DoubtTemps").child("doubtQuestionPushId").getValue(String.class);
                    doubtCreatorName = snapshot.child("DoubtTemps").child("doubtCreatorName").getValue(String.class);
                    doubtCreatorId = snapshot.child("DoubtTemps").child("doubtCreatorId").getValue(String.class);
                    doubtMSGId = snapshot.child("DoubtTemps").child("doubtMSGId").getValue(String.class);


                    if (doubtCreatorId.equals(currUserId)) {
                        btn_menu.setVisibility(View.VISIBLE);

                        DatabaseReference firebaseDatabaseUnsendMSG = FirebaseDatabase.getInstance().getReference()
                                .child("Groups").child("Chat_Message").child(groupPushId).child(groupClassPushId)
                                .child(groupClassSubjectPushId).child(doubtMSGId);

                        DatabaseReference firebaseDatabaseUnsendDoubt = FirebaseDatabase.getInstance().getReference()
                                .child("Groups").child("Doubt").child(groupPushId).child(groupClassPushId).child(groupClassSubjectPushId);

                        final PopupMenu dropDownMenu = new PopupMenu(requireContext(), btn_menu);

                        final Menu menu = dropDownMenu.getMenu();

                        dropDownMenu.getMenuInflater().inflate(R.menu.doubt_more, menu);

                        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.toString()) {

                                    case "Delete Doubt":
                                        requireActivity().onBackPressed();
                                        Toast.makeText(requireContext(), "Delete", Toast.LENGTH_SHORT).show();

                                        firebaseDatabaseUnsendMSG.removeValue();
                                        firebaseDatabaseUnsendDoubt.child("All_Doubt").child(doubtQuestionPushId).removeValue();
                                        firebaseDatabaseUnsendDoubt.child("Topic").child(doubtQuestionPushId).removeValue();
                                        break;


                                }
                                return false;
                            }
                        });

                        btn_menu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dropDownMenu.show();
                            }
                        });


                    }

                    DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(doubtCreatorId);
                    refUserProfPic.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("Name").exists()) {
                                String userName = snapshot.child("Name").getValue().toString();
                                byName.setText("Asked by - " + userName);

                            }
                            Activity activity = getActivity();
                            if (activity != null) {

                                if (snapshot.child("profilePic").exists()) {
                                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                                    Glide.with(requireActivity()).load(profilePicUrl).into(ib_AnsUserProfile);

                                } else {
                                    Glide.with(requireActivity()).load(R.drawable.maharaji).into(ib_AnsUserProfile);
                                }

                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    /*Send button*/
                    ib_submitDoubtAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String subGroupMsg = et_DoubtAns.getText().toString().trim();
                            if (subGroupMsg.isEmpty()) {
                                showToast();
//                            Toast.makeText(getContext(), "Enter Answer", Toast.LENGTH_SHORT).show();
                                et_DoubtAns.setError("Enter Answer");
                            } else {

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                                        child("Groups").child("Doubt").child(groupPushId).child(groupClassPushId).child(groupClassSubjectPushId).
                                        child("All_Doubt").child(doubtQuestionPushId).child("Answer");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                                        String push = "ansno_" + noofGroupinCategory;

                                        Calendar calenderCC = Calendar.getInstance();
                                        SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                                        String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                                        userAddAnsClass = new Class_Answer();
                                        //dateTimeCC, currUserName, currUserId,currUserEmail, push, doubtQuestionPushId,subGroupMsg
                                        userAddAnsClass.setAns(subGroupMsg);
                                        userAddAnsClass.setUserName(currUserName);
                                        userAddAnsClass.setAnsUserName(currUserName);
                                        userAddAnsClass.setDateTime(dateTimeCC);
                                        userAddAnsClass.setPushId(doubtQuestionPushId);
//                            userAddAnsClass.setAnsPushId(pus);
                                        userAddAnsClass.setQuesCategory(push);
                                        userAddAnsClass.setUserId(currUserId);
                                        reference.push().setValue(userAddAnsClass);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });


                                et_DoubtAns.setText("");
//                    et_DoubtAns.setfocus
                            }
                        }

                    });

                    doubtTemp.child("clickedSubjectName").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                tv_DoubtGroupName.setText(snapshot.getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    btn_Back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requireActivity().onBackPressed();

                        }
                    });


                    DatabaseReference referenceDoubt = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Doubt").child(groupPushId).child(groupClassPushId).child(groupClassSubjectPushId).
                            child("All_Doubt").child(doubtQuestionPushId);

                    referenceDoubt.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Log.d("DOUBTANS", "onChildAdded: " + snapshot.getValue());
                                tv_DoubtGroupClass.setText(snapshot.child("groupName").getValue().toString());
                                tv_Name.setText(snapshot.child("groupPositionId").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Doubt").child(groupPushId).child(groupClassPushId).child(groupClassSubjectPushId).
                            child("All_Doubt").child(doubtQuestionPushId).child("Answer");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                list_DoubtAnswer.clear();
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    Log.d("DOUBTANS", "onChildAdded: " + dataSnapshot1.getValue());
                                    Class_Answer class_userDashBoard = dataSnapshot1.getValue(Class_Answer.class);
                                    list_DoubtAnswer.add(class_userDashBoard);
                                    answerAdaptor.notifyDataSetChanged();
                                    rv_DoubtAnswer.smoothScrollToPosition(Objects.requireNonNull(rv_DoubtAnswer.getAdapter()).getItemCount());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_endittext, null);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}