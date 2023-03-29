package com.cllasify.cllasify.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.Constant;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class Fees_Frag extends BottomSheetDialogFragment implements PaymentResultWithDataListener, ExternalWalletListener {

    private BroadcastReceiver broadcastReceiver;

    String currUserID;
    TextView fees_ammount, school_tv, class_tv;
    DatabaseReference databaseReference;
    Button payButton;
    ProgressBar fees_prog;

    private AlertDialog.Builder alertDialogBuilder;
    public static final String TAG = "Fees_Frag";

    public static Fees_Frag newInstance() {
        return new Fees_Frag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Activity activity = (Server_Activity) getActivity();

        View v = inflater.inflate(R.layout.fragment_fees_, container, false);


        fees_ammount = v.findViewById(R.id.fees_ammount);
        school_tv = v.findViewById(R.id.school_tv);
        class_tv = v.findViewById(R.id.class_tv);

        Checkout.preload(activity.getApplicationContext());

        payButton = v.findViewById(R.id.pay_button);

        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });


        broadcastReceiver = new NetworkBroadcast();
        activity.registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        currUserID = SharePref.getDataFromPref(Constant.USER_ID);


        String uniGrpPushId = getArguments().getString("uniGroupPushId");
        String uniClassPushId = getArguments().getString("uniClassPushId");


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);

        DatabaseReference refGetGRPName = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(uniGrpPushId);
        refGetGRPName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("groupName").exists()) {
                    school_tv.setText(snapshot.child("groupName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String classFeesAmt = snapshot.child("classFees").getValue().toString();
                String className = snapshot.child("className").getValue().toString();
                String userName = snapshot.child("classStudentList").child(currUserID).child("userName").getValue().toString();

                class_tv.setText(className);

                if (snapshot.child("classFees").exists()) {
                    if (!(classFeesAmt.isEmpty())) {
                        fees_ammount.setText("Fees: " + classFeesAmt);
                        payButton.setText("Pay: " + classFeesAmt);
                        //extract the rupee sign & amount & pass it to the checkout

                        String[] amt = classFeesAmt.split("₹");

                        String amount = amt[1];
                        long feesAmt = (Long.parseLong(amount) * 100);
                        payButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (feesAmt == 0) {
                                    Toast.makeText(activity, "Invalid Fees Amount", Toast.LENGTH_SHORT).show();
                                } else {
                                    startPayment(String.valueOf(feesAmt), uniGrpPushId, className, userName);
                                }
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;

    }


    public void startPayment(String amount, String groupPushId, String className, String userName) {

        final Activity activity = (Server_Activity) getActivity();

        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);
        co.setKeyID("rzp_live_O91TyhivEf7J5v");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    try {

                        String groupName = snapshot.child("groupName").getValue().toString().trim();
                        String groupPic = snapshot.child("serverProfilePic").getValue().toString().trim();


                        JSONObject options = new JSONObject();
                        options.put("name", groupName);
                        options.put("description", "Paid by " + userName + " from " + groupName + " of " + className);//set server name,class & student name here
//            options.put("send_sms_hash", true);
                        options.put("currency", "INR");
                        options.put("amount", amount);

                        co.open(activity, options);

                    } catch (Exception e) {
                        Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




            /*
            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");
            options.put("prefill", preFill);
            */


    }


    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        final Activity activity = (Server_Activity) getActivity();


//        String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
//        String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

        String uniGrpPushId = getArguments().getString("uniGroupPushId");
        String uniClassPushId = getArguments().getString("uniClassPushId");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");

        databaseReference.child("All_GRPs").child(uniGrpPushId).child(uniClassPushId)
                .child("classStudentList").child(currUserID).child("annualFees").setValue("Paid");

        Toast.makeText(activity, "Paid Successfully", Toast.LENGTH_SHORT).show();

        alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
        alertDialogBuilder.show();

    }


    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}