package com.cllasify.cllasify.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Fragments.Friend_Chat_Activity;
import com.cllasify.cllasify.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class WebView_Fragment extends Fragment implements DownloadFile.Listener {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web_view_, container, false);

        url = getArguments().getString("path");
        documentName = getArguments().getString("docName");

        previousPageBtn = view.findViewById(R.id.previous_page);
        nextPageBtn = view.findViewById(R.id.next_page);
        totalPageNumber = view.findViewById(R.id.totalPage);
        currentPage = view.findViewById(R.id.currentPage);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        pdfLayout = view.findViewById(R.id.pdf_layout);
        back_btn = view.findViewById(R.id.back_btn);
        btn_download = view.findViewById(R.id.btn_download);
        docName = view.findViewById(R.id.doc_name);
        imp_messge = view.findViewById(R.id.imp_messge);

        docName.setText(documentName);


        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                    DownloadManager downloadManager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);
                    Toast.makeText(getActivity(), " Downloading Started . ", Toast.LENGTH_SHORT).show();
                }

            }
        });


        remotePDFViewPager = new RemotePDFViewPager(getContext(), url, this);

        return view;
    }

    @Override
    public void onSuccess(String url, String destinationPath) {

        if (url != null && destinationPath != null) {

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
            Activity activity = getActivity();
            if (activity != null) {
                pdfPagerAdapter = new PDFPagerAdapter(requireActivity(), FileUtil.extractFileNameFromURL(url));
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