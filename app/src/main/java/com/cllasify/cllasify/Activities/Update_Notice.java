package com.cllasify.cllasify.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cllasify.cllasify.ModelClasses.Class_Notice;
import com.cllasify.cllasify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class Update_Notice extends AppCompatActivity {

    FloatingActionButton doneNotesBtn;
    String groupPushId, classUniPushId, subjectUniPushId, userID;
    EditText notesTitle, noticeData;
    //    String title, notes;
    DatabaseReference firebaseDBNotice;
    Button uploadAttachmentsBtn;
    FirebaseUser currentUser;
    private Uri fileUri;
    String displayName;
    RelativeLayout document, progress_layout;
    String fileUrl;
    boolean fileAdded;
    ProgressBar uploadProgress;
    TextView percentage;
    private int currentProgress = 0;


    String title, notes, date, docs, key;
    RelativeLayout pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notice);

        doneNotesBtn = findViewById(R.id.doneNotesBtn);
        notesTitle = findViewById(R.id.notesTitle);
        noticeData = findViewById(R.id.noticeData);

        document = findViewById(R.id.document);
        progress_layout = findViewById(R.id.progress_layout);
        uploadProgress = findViewById(R.id.uploadProgress);
        percentage = findViewById(R.id.percentage);
        uploadAttachmentsBtn = findViewById(R.id.uploadAttachmentsBtn);


        title = getIntent().getStringExtra("title");
        notes = getIntent().getStringExtra("notes");
        date = getIntent().getStringExtra("date");
        docs = getIntent().getStringExtra("docs");
        key = getIntent().getStringExtra("key");

        notesTitle.setText(title);
        noticeData.setText(notes);
        fileUrl = docs;

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        groupPushId = getIntent().getStringExtra("groupPushId");
        classUniPushId = getIntent().getStringExtra("classUniPushId");
        subjectUniPushId = getIntent().getStringExtra("subjectUniPushId");

        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMMM d,yyyy", date.getTime());

        firebaseDBNotice = FirebaseDatabase.getInstance().getReference()
                .child("Groups").child("Notice").child(groupPushId).child(classUniPushId).child(subjectUniPushId).child("All_Notice");

        uploadAttachmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPdf();
            }
        });

        if (!docs.equals("null")) {
            document.setVisibility(View.VISIBLE);
        } else {
            document.setVisibility(View.GONE);
        }

        doneNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(notesTitle.getText())) {
                    notesTitle.setError("This field is mandatory");
                }
                if (TextUtils.isEmpty(noticeData.getText())) {
                    noticeData.setError("This field is mandatory");

                } else {

                    title = notesTitle.getText().toString();
                    notes = noticeData.getText().toString();

                    Class_Notice class_notice = new Class_Notice(title, notes, sequence.toString(), key, fileUrl);
                    firebaseDBNotice.child(key).setValue(class_notice);
                    onBackPressed();
                }


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


                        if (task.isComplete()) {

                            DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                            refUserProfPic.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();

                                    storageReference.child(userMsgKeyRef + "." + "pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Toast.makeText(Update_Notice.this, "Document sending sucess", Toast.LENGTH_SHORT).show();

                                            progress_layout.setVisibility(View.GONE);
                                            fileUrl = uri.toString();
                                            fileAdded = true;

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
                        Toast.makeText(Update_Notice.this, "Document sending failed", Toast.LENGTH_SHORT).show();
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


}