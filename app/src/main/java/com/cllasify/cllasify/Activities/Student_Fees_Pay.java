package com.cllasify.cllasify.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.Adapters.Adaptor_ShowGrpMember_Fees;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.Constant;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.cllasify.cllasify.Utility.SharePref;
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

public class Student_Fees_Pay extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {

    private BroadcastReceiver broadcastReceiver;

    String currUserID;
    TextView fees_ammount;
    DatabaseReference databaseReference;
    Button payButton;

    private AlertDialog.Builder alertDialogBuilder;
    private static final String TAG = Student_Fees_Pay.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fees_pay);

        fees_ammount = findViewById(R.id.fees_ammount);

        Checkout.preload(getApplicationContext());

        payButton = findViewById(R.id.pay_button);

        alertDialogBuilder = new AlertDialog.Builder(Student_Fees_Pay.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });


        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        currUserID = SharePref.getDataFromPref(Constant.USER_ID);


        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String classFeesAmt = snapshot.child("classFees").getValue().toString();
                    String className = snapshot.child("className").getValue().toString();
                    String userName = snapshot.child("classStudentList").child(currUserID).child("userName").getValue().toString();


                    if (snapshot.child("classFees").exists()) {
                        if (!(classFeesAmt.isEmpty())) {
                            fees_ammount.setText(classFeesAmt);
                            //extract the rupee sign & amount & pass it to the checkout

                            String[] amt = classFeesAmt.split("₹");

                            String amount = amt[1];
                            long feesAmt = (Long.parseLong(amount) * 100);
                            payButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startPayment(String.valueOf(feesAmt), uniGrpPushId, className, userName);
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    public void startPayment(String amount, String groupPushId, String className, String userName) {

        final Activity activity = this;

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
        try {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    currUserID = SharePref.getDataFromPref(Constant.USER_ID);

                    String grpPush = snapshot.child("Temp").child(currUserID).child("clickedGroupPushId").getValue().toString().trim();
                    String classPush = snapshot.child("Temp").child(currUserID).child("uniPushClassId").getValue().toString().trim();

                    Toast.makeText(Student_Fees_Pay.this, "Paid Successfully", Toast.LENGTH_SHORT).show();

                    databaseReference.child("All_GRPs").child(grpPush).child(classPush)
                            .child("classStudentList").child(currUserID).child("annualFees").setValue("Paid");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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