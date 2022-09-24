package com.cllasify.cllasify.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.ModelClasses.Class_Admission;
import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

public class Admission_Structure extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {

    String userID, admissionFee, groupPushId, classPushId, dateTimeCC, adminGroupID, userEmail, subGroupName, userName, groupName;
    Button pay_btn;
    ImageView attachment_btn;
    private Uri fileUri;
    String displayName;
    EditText name_et, dob_et, father_name_et, mother_name_et, address_et, phone_et, religion_et, cast_et;
    String name_tv, dob_tv, father_name_tv, mother_name_tv, address_tv, phone_tv, religion_tv, cast_tv;

    RelativeLayout document, progress_layout;
    String fileUrl;
    boolean fileAdded;
    ProgressBar uploadProgress;
    TextView percentage;
    private int currentProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_structure);

        name_et = findViewById(R.id.name_et);
        dob_et = findViewById(R.id.dob_et);
        father_name_et = findViewById(R.id.father_name_et);
        mother_name_et = findViewById(R.id.mother_name_et);
        address_et = findViewById(R.id.address_et);
        phone_et = findViewById(R.id.phone_et);
        religion_et = findViewById(R.id.religion_et);
        cast_et = findViewById(R.id.cast_et);

        document = findViewById(R.id.document);
        progress_layout = findViewById(R.id.progress_layout);
        uploadProgress = findViewById(R.id.uploadProgress);
        percentage = findViewById(R.id.percentage);

        userID = getIntent().getStringExtra("userID");
        admissionFee = getIntent().getStringExtra("admissionFee");
        groupPushId = getIntent().getStringExtra("groupPushId");
        classPushId = getIntent().getStringExtra("classPushId");
        dateTimeCC = getIntent().getStringExtra("dateTimeCC");
        adminGroupID = getIntent().getStringExtra("adminGroupID");
        userEmail = getIntent().getStringExtra("userEmail");
        subGroupName = getIntent().getStringExtra("subGroupName");
        userName = getIntent().getStringExtra("userName");
        groupName = getIntent().getStringExtra("groupName");

        pay_btn = findViewById(R.id.pay_btn);
        attachment_btn = findViewById(R.id.attachment_btn);


        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name_et.getText().toString().length() == 0 || dob_et.getText().toString().length() == 0 || father_name_et.getText().toString().length() == 0
                        || mother_name_et.getText().toString().trim().length() == 0 || address_et.getText().toString().trim().length() == 0 ||
                        phone_et.getText().toString().trim().length() == 0 || religion_et.getText().toString().trim().length() == 0 ||
                        cast_et.getText().toString().trim().length() == 0 || fileAdded == false) {
                    Toast.makeText(Admission_Structure.this, "Please fill up the above details" + name_et.getText().toString().length(), Toast.LENGTH_SHORT).show();

                } else {
                    String[] amt = admissionFee.split("₹");

                    String amount = amt[1];
                    long feesAmt = (Long.parseLong(amount) * 100);
                    startPayment(String.valueOf(feesAmt), groupPushId, subGroupName, userName);

                }


            }
        });

        attachment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPdf();
            }
        });

    }

    private void sendPdf() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 438);

    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();

            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
            setDocumentInFB(fileUri, displayName);

            Log.d("BUNDLESTRING", "onDownloadClick: " + displayName);


        }

    }


    private void setDocumentInFB(Uri fileUri, String fileName) {


        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String subjectUniPushId = snapshot.child("subjectUniPushId").getValue().toString().trim();
                String uniPushClassId = snapshot.child("uniPushClassId").getValue().toString().trim();
                String groupPushId = snapshot.child("clickedGroupPushId").getValue().toString().trim();

                DatabaseReference allDocumentReference = FirebaseDatabase.getInstance().getReference().
                        child("Groups").child("Documents").child(groupPushId).child(uniPushClassId).child(subjectUniPushId).child("All_Document");

                String fileUriPath = fileUri.toString();

                String onlyPath = fileUriPath.substring(0, fileUriPath.indexOf("/"));

                Log.d("ONLYPATH", "onDataChange: " + onlyPath);


                String pushValue[] = allDocumentReference.push().toString().split("/");

                String push = pushValue[9];

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Document Files");
                String userMsgKeyRef = allDocumentReference.child(onlyPath).push().getKey();
                StorageReference filePath = storageReference.child(userMsgKeyRef + "." + "pdf");

                filePath.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

//                        messageAdapter.setProgVal(1);

                        if (task.isComplete()) {

                            DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                            refUserProfPic.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();

                                    storageReference.child(userMsgKeyRef + "." + "pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Toast.makeText(Admission_Structure.this, "Document sending sucess", Toast.LENGTH_SHORT).show();
                                            progress_layout.setVisibility(View.GONE);
                                            fileUrl = uri.toString();
                                            fileAdded = true;


                                            /*
                                            Class_Group userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId, uniPushClassId, subjectUniPushId, uri.toString(), fileName);
                                            allDocumentReference.child(push).setValue(userAddGroupClass);
                                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, groupPushId, uniPushClassId, uri.toString(), "pdf", subjectUniPushId, push, fileName, profilePicUrl);
                                            reference.child(push).setValue(userAddGroupClass);

//                                    Toast.makeText(Server_Activity.this, "Document uploading successful", Toast.LENGTH_SHORT).show();
                                            uploadPercentage.setVisibility(View.GONE);
                                            ib_pdf_btn.setVisibility(View.VISIBLE);
                                            uploadProgressBar.setVisibility(View.GONE);


                                            */
                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admission_Structure.this, "Document sending failed", Toast.LENGTH_SHORT).show();
//                        messageAdapter.setProgVal(2);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        document.setVisibility(View.VISIBLE);
                        progress_layout.setVisibility(View.VISIBLE);

                        currentProgress = 10;

                        uploadProgress.setVisibility(View.VISIBLE);
                        percentage.setVisibility(View.VISIBLE);
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();

                        int uploadProg = Math.toIntExact(Math.round(progress));

//                        currentProgress = ;

                        uploadProgress.setProgress(uploadProg);
                        uploadProgress.setMax(100);

                        percentage.setText(uploadProg + "%");

//                        Toast.makeText(Server_Activity.this, "Document sending: " + progress, Toast.LENGTH_SHORT).show();


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                        options.put("description", "Admission fees paid by " + userName + " for " + className + " of " + groupName);//set server name,class & student name here
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

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        DatabaseReference userNoti = FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications").child(userID).child(groupPushId).child(classPushId);
        DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(groupPushId).child("groupTeacherJoiningReqs");
        DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(userID);
        DatabaseReference grpJoiningReqs = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classPushId).child("groupAdmissionReqs");

        grpJoiningReqs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                String pushLong = "Joining B Reqno_" + noofQuesinCategory;

                Class_Admission class_admission = new Class_Admission(name_et.getText().toString(), dob_et.getText().toString(), father_name_et.getText().toString(),
                        mother_name_et.getText().toString(), address_et.getText().toString(), phone_et.getText().toString(),
                        religion_et.getText().toString(), cast_et.getText().toString(), dateTimeCC, fileUrl);
                Class_Group userAddComment = new Class_Group(class_admission, dateTimeCC, userName, "admission_req_sent", userID, adminGroupID, userEmail, pushLong, groupName, groupPushId, subGroupName, "Group_JoiningReq", classPushId);
                grpJoiningReqs.child(pushLong).setValue(userAddComment);
                refacceptingReq.child(pushLong).setValue(userAddComment);
                userNoti.child("notificationPushId").setValue(pushLong);
                userNoti.child("joiningStatus").setValue("admission_req_sent");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }
}