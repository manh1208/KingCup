package com.superapp.kingscupdrinkingamefunpartyrock.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.banner.RevMobBanner;
import com.superapp.kingscupdrinkingamefunpartyrock.AnalyticsTrackers;
import com.superapp.kingscupdrinkingamefunpartyrock.R;
import com.superapp.kingscupdrinkingamefunpartyrock.application.AnalyticsApplication;
import com.superapp.kingscupdrinkingamefunpartyrock.model.Language;
import com.superapp.kingscupdrinkingamefunpartyrock.utils.DataUtils;


import java.util.Calendar;

public class MainActivity extends Activity {
    private Language mCurrentLanguage;
    private Button mbtnStart;
//    private AdView mAdView;
    private Tracker mTracker;
    private long count;
    private RevMob revmob;
    private RevMobBanner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//        mAdView.loadAd(adRequest);
        startRevMobSession();
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Main Menu");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        mCurrentLanguage = DataUtils.getINSTANCE(MainActivity.this).getLanguage();
        mbtnStart = (Button) findViewById(R.id.btn_start);
        mbtnStart.setText(mCurrentLanguage.getStart());
        Button mBtnHowToPlay = (Button) findViewById(R.id.btn_menu_howtoplay);
        Button mBtnSetting = (Button) findViewById(R.id.btn_menu_setting);
        Button mBtnRate = (Button) findViewById(R.id.btn_menu_rate);
        final Button mBtnSound = (Button) findViewById(R.id.btn_menu_sound);
        final Language language = DataUtils.getINSTANCE(MainActivity.this).getLanguage();
        mBtnHowToPlay.setText(language.getHowToPlay());
        mBtnSetting.setText(language.getSetting());
        mBtnRate.setText(language.getRate());
        mBtnSound.setText(language.getSound());
        mBtnHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(MainActivity.this).playSound(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, HowtoPlayActivity.class);
                startActivity(intent);
            }
        });
        mbtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(MainActivity.this).playSound(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, PlayerSetupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(MainActivity.this).playSound(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        mBtnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DataUtils.getINSTANCE(MainActivity.this).isEnableSound()) {
                    DataUtils.getINSTANCE(MainActivity.this).setIsEnableSound(true);
                    DataUtils.getINSTANCE(MainActivity.this).playSound(MainActivity.this);
                    mBtnSound.setText(language.getSound());
                } else {
                    DataUtils.getINSTANCE(MainActivity.this).setIsEnableSound(false);
                    mBtnSound.setText(language.getSoundOff());
                }

            }
        });
        
        mBtnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(MainActivity.this).playSound(MainActivity.this);
                Uri uri;
                Intent intent;
//                uri=Uri.parse("http://play.google.com/store/apps/details?id=com.superapp.kingscupdrinkingamefunpartyrock"); //GP
//                uri=Uri.parse("http://appvn.com/android/details?id=com.superapp.kingscupdrinkingamefunpartyrock"); //APPOTA
//                uri=Uri.parse("http://www.bestappsforphone.com"); //Samsung
                uri=Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=com.superapp.kingscupdrinkingamefun");//Kindle


                intent = new Intent(Intent.ACTION_VIEW, uri);
                MainActivity.this.startActivity(intent);
            }
        });
        
        if (DataUtils.getINSTANCE(MainActivity.this).isEnableSound()) {
            mBtnSound.setText(language.getSound());
        } else {
            mBtnSound.setText(language.getSoundOff());
        }
    }

    @Override
    public void onBackPressed() {
        if (count == 0 || Calendar.getInstance().getTimeInMillis() - count > 1000) {
            count = Calendar.getInstance().getTimeInMillis();
            Toast.makeText(this, DataUtils.getINSTANCE(this).getLanguage().getBackPress(), Toast.LENGTH_SHORT).show();
        } else {
            DataUtils.getINSTANCE(MainActivity.this).playSound(MainActivity.this);
            super.onBackPressed();
        }
    }

    public void startRevMobSession() {
        //RevMob's Start Session method:
        revmob = RevMob.startWithListener(this, new RevMobAdsListener() {
            @Override
            public void onRevMobSessionStarted() {
                loadBanner(); // Cache the banner once the session is started
                Log.i("RevMob","Session Started");
            }
            @Override
            public void onRevMobSessionNotStarted(String message) {
                //If the session Fails to start, no ads can be displayed.
                Log.i("RevMob","Session Failed to Start");
            }
        }, "584fc77f994c3f13170abb32");
        if (revmob!=null){
            loadBanner();
        }
        Log.i("Revmob","ahihi");
    }

    public void loadBanner(){
        banner = revmob.preLoadBanner(this, new RevMobAdsListener(){
            @Override
            public void onRevMobAdReceived() {
                showBanner();
                Log.i("RevMob","Banner Ready to be Displayed"); //At this point, the banner is ready to be displayed.
            }
            @Override
            public void onRevMobAdNotReceived(String message) {
                Log.i("RevMob","Banner Not Failed to Load");
            }
            @Override
            public void onRevMobAdDisplayed() {
                Log.i("RevMob","Banner Displayed");
            }
        });
    }

    public void showBanner(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (banner.getParent()==null){
                    ViewGroup view = (ViewGroup) findViewById(R.id.bannerLayout);
                    view.addView(banner);
                }
                banner.show(); //This method must be called in order to display the ad.

            }
        });
    }
}
