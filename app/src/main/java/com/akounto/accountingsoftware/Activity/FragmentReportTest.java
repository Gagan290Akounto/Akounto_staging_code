package com.akounto.accountingsoftware.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.util.UiUtil;

public class FragmentReportTest extends Fragment {

    private String ci = "", fn = "", ln = "", un = "", at = "", ex = "";
    private View view;
    private WebView webView;
    private ProgressDialog pd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.test_webview, container, false);
        inItUi();
        return this.view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void inItUi() {
        webView = view.findViewById(R.id.web_view);
        try {
            ci = UiUtil.getComp_Id(getContext());
            at = UiUtil.getAcccessToken(getContext());
            fn = UiUtil.getFirstName(getContext());
            ln = UiUtil.getLastName(getContext());
            un = UiUtil.getUserName(getContext());
            ex = UiUtil.getExpireIn(getContext());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            webView.setVerticalScrollBarEnabled(true);
            webView.requestFocus();

            pd = new ProgressDialog(getContext());
            pd.setMessage("Please wait...");
            pd.show();
            webView.setWebViewClient(new MyWebViewClient());
        } catch (Exception e) {
        }
        Log.e("URL :: ", Constant.WebURL + "/app/?returnUrl=/a/reports&ci=" + ci + "&fn=" + fn + "&&ln=" + ln + "&&un=" + un + "&&tk=" + at + "&&ex=" + ex + "&ty=bearer&");
        //"http://192.168.4.186:4200/app/?returnUrl=/a/reports&ci=" + ci + "&fn=" + fn + "&&ln=" + ln + "&&un=" + un + "&&tk=" + at + "&&ex=" + ex + "&ty=bearer&"
        this.webView.loadUrl(Constant.WebURL + "/app/?returnUrl=/a/reports&ci=" + ci + "&fn=" + fn + "&&ln=" + ln + "&&un=" + un + "&&tk=" + at + "&&ex=" + ex + "&ty=bearer&");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }

    }
}