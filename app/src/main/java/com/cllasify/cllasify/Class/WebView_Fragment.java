package com.cllasify.cllasify.Class;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cllasify.cllasify.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebView_Fragment extends Fragment {
    private ProgressBar progressBar;
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view_, container, false);

        progressBar = view.findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            url = this.getArguments().getString("path");
            WebView webView = view.findViewById(R.id.web);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowContentAccess(true);


            Log.d("BUNDLESTRINGFRAG", "onDownloadClick: "+url);

            Intent intent = getActivity().getIntent();
            final int position = intent.getIntExtra("position", 0);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    webView.loadUrl("javascript:(function() { " +
                            "document.querySelector('[role=\"toolbar\"]').remove();})()");
                    progressBar.setVisibility(View.GONE);
                }
            });

            try {
                url = URLEncoder.encode(url, "UTF-8");
                webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                Log.d("TAG", "onCreate: Successful");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("TAG", "onCreate: " + e);

            }
        }

        return view;
    }
}