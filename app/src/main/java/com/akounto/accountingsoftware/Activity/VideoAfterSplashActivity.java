package com.akounto.accountingsoftware.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.akounto.accountingsoftware.R;

public class VideoAfterSplashActivity extends AppCompatActivity {
    public static String VIDEO_URL = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_video_after_splash);
        findViewById(R.id.getStartedButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                VideoAfterSplashActivity.this.lambda$onCreate$0$VideoAfterSplashActivity(view);
            }
        });
    }

    public void lambda$onCreate$0$VideoAfterSplashActivity(View click) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        VideoView videoView = findViewById(R.id.videoViewDummy);
        videoView.setVideoURI(Uri.parse(VIDEO_URL));
        videoView.requestFocus();
        videoView.setVideoPath("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        videoView.start();
    }
}
