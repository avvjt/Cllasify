package com.cllasify.cllasify.Server;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryAnswer;
import com.cllasify.cllasify.Class.Class_Answer;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoubtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoubtFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoubtFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoubtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoubtFragment newInstance(String param1, String param2) {
        DoubtFragment fragment = new DoubtFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    String currUserId, currUserName, currUserEmail;

    String groupPushId, groupClassPushId, groupClassSubjectPushId, doubtQuestionPushId, doubtCreatorName, doubtCreatorId;
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


                    DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(doubtCreatorId);
                    refUserProfPic.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("Name").exists()) {
                                String userName = snapshot.child("Name").getValue().toString();
                                byName.setText("Asked by - " + userName);

                            }
                            if (snapshot.child("profilePic").exists()) {
                                if (!getActivity().isFinishing()) {
                                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                                    Glide.with(DoubtFragment.this).load(profilePicUrl).into(ib_AnsUserProfile);
                                }
                            } else {
                                if (!getActivity().isFinishing()) {
                                    Glide.with(DoubtFragment.this).load(R.drawable.maharaji).into(ib_AnsUserProfile);
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

                    btn_menu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "In process doubt features", Toast.LENGTH_SHORT).show();
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