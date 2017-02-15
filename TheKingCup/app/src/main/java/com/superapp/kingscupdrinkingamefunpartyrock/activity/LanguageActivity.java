package com.superapp.kingscupdrinkingamefunpartyrock.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.banner.RevMobBanner;
import com.superapp.kingscupdrinkingamefunpartyrock.R;
import com.superapp.kingscupdrinkingamefunpartyrock.utils.DataUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ManhNV on 4/8/2016.
 */
public class LanguageActivity extends Activity {
    private List<String> mLanguages;
//    private AdView mAdView;
    private long count;
    private RevMob revmob;
    private RevMobBanner banner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        startRevMobSession();
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//        mAdView.loadAd(adRequest);
        
        createList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.listview_language_item,R.id.lv_language_item,mLanguages);
        ListView listView = (ListView) findViewById(R.id.lv_language);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataUtils.getINSTANCE(getApplicationContext()).playSound(getApplicationContext());
                DataUtils.getINSTANCE(LanguageActivity.this).setmCurrentLanguage(position+1);
                Intent intent = new Intent(LanguageActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createList(){
        mLanguages = DataUtils.getINSTANCE(LanguageActivity.this).getListLanguageName();
    }

    @Override
    public void onBackPressed() {
        if (count == 0 || Calendar.getInstance().getTimeInMillis() - count > 1000) {
            count = Calendar.getInstance().getTimeInMillis();
            Toast.makeText(this, "Press the back button again to exit", Toast.LENGTH_SHORT).show();
        } else {
            DataUtils.getINSTANCE(LanguageActivity.this).playSound(LanguageActivity.this);
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
