package com.cllasify.cllasify.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Fragments.Friend_Chat_Activity;
import com.cllasify.cllasify.R;

import de.hdodenhof.circleimageview.CircleImageView;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class WebViewActivity extends AppCompatActivity implements DownloadFile.Listener {

    private RemotePDFViewPager remotePDFViewPager;

    private PDFPagerAdapter pdfPagerAdapter;

    private String url, documentName;

    private ProgressBar progressBar;

    private LinearLayout pdfLayout;

    private int totalPage;

    CircleImageView previousPageBtn, nextPageBtn;

    private int pageNumber = 0;

    private TextView totalPageNumber, currentPage, docName, imp_messge;

    ImageButton back_btn, btn_download;
    private Friend_Chat_Activity friendChatFragment;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

//        url = getArguments().getString("path");
//        documentName = getArguments().getString("docName");

        previousPageBtn = findViewById(R.id.previous_page);
        nextPageBtn = findViewById(R.id.next_page);
        totalPageNumber = findViewById(R.id.totalPage);
        currentPage = findViewById(R.id.currentPage);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        pdfLayout = findViewById(R.id.pdf_layout);
        back_btn = findViewById(R.id.back_btn);
        btn_download = findViewById(R.id.btn_download);
        docName = findViewById(R.id.doc_name);
        imp_messge = findViewById(R.id.imp_messge);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra("pdfUrl")) {
            url = getIntent().getStringExtra("pdfUrl");
            documentName = "Result.pdf";


            docName.setText(documentName);


            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ActivityCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WebViewActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        // this will request for permission when permission is not true
                    } else {

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        String title = URLUtil.guessFileName(url, null, null);
                        request.setTitle(title);
                        request.setDescription(" Downloading File please wait ..... ");
                        String cookie = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookie);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
                        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);
                    }

                }
            });


            remotePDFViewPager = new RemotePDFViewPager(WebViewActivity.this, url, this);

        }
    }

    @Override
    public void onSuccess(String url, String destinationPath) {

        if (url != null && destinationPath != null) {
/*
            String friendName = getArguments().getString("name");
            String receiverUid = getArguments().getString("receiverUid");

            String type = getArguments().getString("type");

            if (type.equals("activity")) {
                back_btn.setOnClickListener(view1 -> getActivity().onBackPressed());
            }
            if (type.equals("fragment")) {

                back_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendChatFragment = new Friend_Chat_Activity();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.below_friend_toolbar, friendChatFragment).addToBackStack(friendChatFragment.getClass().getSimpleName()).commit();

                        Bundle bundle = new Bundle();
                        bundle.putString("name", friendName);
                        bundle.putString("receiverUid", receiverUid);
                        friendChatFragment.setArguments(bundle);
                    }
                });
            }
            */
            Activity activity = WebViewActivity.this;
            if (activity != null) {

                try {

                    pdfPagerAdapter = new PDFPagerAdapter(WebViewActivity.this, FileUtil.extractFileNameFromURL(url));
                    remotePDFViewPager.setAdapter(pdfPagerAdapter);
                    updateLayout();
                    progressBar.setVisibility(View.GONE);
                    imp_messge.setVisibility(View.GONE);
                    setTotalPage(remotePDFViewPager.getAdapter().getCount());
                    Log.d("TAG", "onPageSelected: " + remotePDFViewPager.getAdapter().getCount());

                    nextPageBtn.setOnClickListener(view -> {
                        if (pageNumber == getTotalPage() - 1) {
                            nextPageBtn.setEnabled(false);
                        } else {
                            nextPageBtn.setEnabled(true);
                            remotePDFViewPager.setCurrentItem(pageNumber += 1);
                        }
                        if (pageNumber > 0) {
                            previousPageBtn.setEnabled(true);
                        }
                        Log.d("CURRPAGE", "Next: " + pageNumber);

                        currentPage.setText("" + (pageNumber + 1));


                    });

                    previousPageBtn.setOnClickListener(view -> {
                        if (pageNumber == 0) {
                            previousPageBtn.setEnabled(false);
                        } else {
                            previousPageBtn.setEnabled(true);
                            remotePDFViewPager.setCurrentItem(pageNumber -= 1);
                        }
                        if (pageNumber < getTotalPage() - 1) {
                            nextPageBtn.setEnabled(true);
                        }
                        Log.d("CURRPAGE", "Previous: " + pageNumber);

                        currentPage.setText("" + (pageNumber + 1));

                    });

                    Log.d("CURRPAGE", "Total Page: " + getTotalPage());
                    totalPageNumber.setText("" + getTotalPage());
                } catch (Exception e) {
                    Toast.makeText(WebViewActivity.this, "This pdf is password protected,please download it to open", Toast.LENGTH_SHORT).show();
                    Log.d("PDFF", "onSuccess: " + e);
                    //Do nothing
                }
            }
        }

    }

    private void updateLayout() {

        pdfLayout.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onFailure(Exception e) {
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (pdfPagerAdapter != null) {
            pdfPagerAdapter.close();
        }
    }
}