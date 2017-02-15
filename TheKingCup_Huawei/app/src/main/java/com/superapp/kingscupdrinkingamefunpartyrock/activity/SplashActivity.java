package com.superapp.kingscupdrinkingamefunpartyrock.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.superapp.kingscupdrinkingamefunpartyrock.R;

/**
 * Created by ManhNV on 3/8/2016.
 */
public class SplashActivity extends Activity {
    private static final long SPLASH_TIME_OUT = 2000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "Splash Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), LanguageActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */


}
