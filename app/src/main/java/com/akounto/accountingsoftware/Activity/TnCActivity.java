package com.akounto.accountingsoftware.Activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.databinding.FragmentWebViewBinding;
import com.akounto.accountingsoftware.util.UiUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class TnCActivity extends AppCompatActivity {

    FragmentWebViewBinding binding;
    String l_type = "-1";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_web_view);
        l_type=getIntent().getStringExtra(Constant.LAUNCH_TYPE);
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        SSLTolerentWebViewClient webViewClient = new SSLTolerentWebViewClient();
        binding.webView.setWebViewClient(webViewClient);
        if (l_type.equalsIgnoreCase("1")) {
            binding.webView.loadUrl("https://www.akounto.com/terms-condition");
        } else if(l_type.equalsIgnoreCase("2")){
            binding.webView.loadUrl("https://www.akounto.com/privacy-policy");
        }else{
            binding.webView.loadUrl("https://www.akounto.com/terms-condition");
        }
    }

    public static String StreamToString(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception t) {

        }
        return writer.toString();
    }

    class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

            if (error.toString() == "piglet")
                handler.cancel();
            else
                handler.proceed(); // Ignore SSL certificate errors
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            UiUtil.cancelProgressDialogue();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            UiUtil.showProgressDialogue(getApplicationContext(), "", "Loding");
        }
    }
}
