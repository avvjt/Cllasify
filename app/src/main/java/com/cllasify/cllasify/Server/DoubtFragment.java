package com.cllasify.cllasify.Server;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    String groupPushId, groupClassPushId, groupClassSubjectPushId, doubtQuestionPushId;
    TextView tv_DoubtGroupClassSubject, tv_DoubtGroupName, tv_DoubtGroupClass;


    List<Class_Answer> list_DoubtAnswer;
    Adaptor_QueryAnswer answerAdaptor;
    RecyclerView rv_DoubtAnswer;
    ImageButton ib_attachDoubtAns, ib_submitDoubtAns;
    EditText et_DoubtAns;
    Class_Answer userAddAnsClass;




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
        tv_DoubtGroupClassSubject = view.findViewById(R.id.tv_DoubtGroupClassSubject);

        rv_DoubtAnswer = view.findViewById(R.id.rv_DoubtAnswer);

        ib_attachDoubtAns = view.findViewById(R.id.ib_attachDoubtAns);
        et_DoubtAns = view.findViewById(R.id.et_DoubtAns);
        ib_submitDoubtAns = view.findViewById(R.id.ib_submitDoubtAns);


        tv_DoubtGroupClass.setText(groupPushId);
        tv_DoubtGroupName.setText(groupClassPushId);
        tv_DoubtGroupClassSubject.setText(groupClassSubjectPushId);

        list_DoubtAnswer = new ArrayList<>();
        answerAdaptor = new Adaptor_QueryAnswer(getContext(), list_DoubtAnswer);
        rv_DoubtAnswer.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_DoubtAnswer.setAdapter(answerAdaptor);

        Bundle bundle = this.getArguments();
//        if (bundle != null) {
////            bundle.putString("groupPushId", groupPush);
////            bundle.putString("groupClassPushId", groupClassPush);
////            bundle.putString("groupClassSubjectPushId", groupSubjectPush);
//
//
//            groupPushId = bundle.getString("groupPushId");
//            groupClassPushId = bundle.getString("groupClassPushId");
//            groupClassSubjectPushId = bundle.getString("groupClassSubjectPushId");
//        }
        Log.d("DOUUBT", "onClick: ");

        DatabaseReference doubtTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserId);

        doubtTemp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupPushId = snapshot.child("DoubtTemps").child("groupPushId").getValue(String.class);
                groupClassPushId = snapshot.child("DoubtTemps").child("groupClassPushId").getValue(String.class);
                groupClassSubjectPushId = snapshot.child("DoubtTemps").child("groupClassSubjectPushId").getValue(String.class);
                doubtQuestionPushId = snapshot.child("DoubtTemps").child("doubtQuestionPushId").getValue(String.class);



                ib_submitDoubtAns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String subGroupMsg = et_DoubtAns.getText().toString().trim();
                        if (subGroupMsg.isEmpty()) {
                            Toast.makeText(getContext(), "Enter Answer", Toast.LENGTH_SHORT).show();
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
                                    reference.child(push).setValue(userAddAnsClass);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                            DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference().
                                    child("Groups").child("User_Answers").child(currUserId).child(groupPushId + "_" + groupClassPushId + "_" + groupClassSubjectPushId + "_" + doubtQuestionPushId);
                            referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                                    String push = "ansno_" + noofGroupinCategory;

                                    Calendar calenderCC = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                                    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                                    userAddAnsClass = new Class_Answer(dateTimeCC, currUserName, currUserId, currUserEmail, push, doubtQuestionPushId, subGroupMsg);
                                    referenceUser.child(push).setValue(userAddAnsClass);
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


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                        child("Groups").child("Doubt").child(groupPushId).child(groupClassPushId).child(groupClassSubjectPushId).
                        child("All_Doubt").child(doubtQuestionPushId).child("Answer");

                ChildEventListener doubtchildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if (dataSnapshot.getChildrenCount() > 0) {
//                    Toast.makeText(requireContext(), "dataSnapshot"+dataSnapshot, Toast.LENGTH_SHORT).show();
                            Class_Answer class_userDashBoard = dataSnapshot.getValue(Class_Answer.class);
                            list_DoubtAnswer.add(class_userDashBoard);
                            answerAdaptor.notifyDataSetChanged();

//                    notifyPB.dismiss();
                        } else {
                            Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };
                //refAdmin.addChildEventListener(childEventListener);
                reference.addChildEventListener(doubtchildEventListener);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


}