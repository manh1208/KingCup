package com.superapp.kingscupdrinkingamefunpartyrock.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.banner.RevMobBanner;
import com.superapp.kingscupdrinkingamefunpartyrock.R;
import com.superapp.kingscupdrinkingamefunpartyrock.application.AnalyticsApplication;
import com.superapp.kingscupdrinkingamefunpartyrock.model.Language;
import com.superapp.kingscupdrinkingamefunpartyrock.model.Player;
import com.superapp.kingscupdrinkingamefunpartyrock.utils.DataUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ManhNV on 4/9/2016.
 */
public class GamePlayActivity extends Activity {
    private final Context mContext = GamePlayActivity.this;
    private HashMap<Integer,String> mCards;
    private List<Integer> mCardsImage;
    private int mCurrent;
    private ImageView mCard;
    private TextView mCardSubject;
    private TextView mCardContent;
    private Button mNextCard;
    private boolean mButtonStatus;
    private Language mCurrentLanguage;
    private ImageView mCardBack;
    private TextView mPlayer;
    private List<Player> mplayers;
    private TextView mNumCard;
//    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private Tracker mTracker;
    private RevMob revmob;
    private RevMobBanner banner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Game Play");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//        mAdView.loadAd(adRequest);
        startRevMobSession();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        requestNewInterstitial();

        mplayers = DataUtils.getINSTANCE(GamePlayActivity.this).getPlayers();
        initial();

        reset();
    }

    private void initial(){
        mCurrentLanguage = DataUtils.getINSTANCE(GamePlayActivity.this).getLanguage();
        mCards = DataUtils.getINSTANCE(GamePlayActivity.this).getCards();
        mCardsImage = new ArrayList<Integer>(mCards.keySet());
        mCard = (ImageView) findViewById(R.id.iv_card);
        mCardSubject = (TextView) findViewById(R.id.tv_card_subject);
        mCardContent = (TextView) findViewById(R.id.tv_card_content);
        mNextCard = (Button) findViewById(R.id.btn_next_card);
        mCardBack = (ImageView) findViewById(R.id.iv_cardback);
        mPlayer = (TextView) findViewById(R.id.tv_player);
        mNumCard = (TextView) findViewById(R.id.tv_numofcard);
        if (mButtonStatus){
          showNewCard();
        }else{
           nextCard();
        }
        mNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(getApplicationContext()).playSound(getApplicationContext());
                int rand = DataUtils.random(1, 3);
                Log.d("Random", rand + "");
                if (rand == 1) {
                    requestNewInterstitial();
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                DataUtils.getINSTANCE(getApplicationContext()).playSound(getApplicationContext());
                                nextCard();
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                mInterstitialAd.show();

                            }
                        });
                    } else {
                        nextCard();
                    }

                } else {
                    nextCard();
                }


            }
        });
        mCardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataUtils.getINSTANCE(getApplicationContext()).playSound(getApplicationContext());
                if (mCurrent < mCardsImage.size()) {
                    showNewCard();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GamePlayActivity.this);

                    builder.setTitle(DataUtils.getINSTANCE(GamePlayActivity.this).getLanguage().getNotiTitle());
                    builder.setMessage(DataUtils.getINSTANCE(GamePlayActivity.this).getLanguage().getNotiPlayAgain());

                    builder
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    reset();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(GamePlayActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    builder.create();
                    builder.show();
                    // Toast.makeText(GamePlayActivity.this, "Hết bài bà nó rồi", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void reset(){
        Collections.shuffle(mCardsImage);
        mCurrent = 0;
        nextCard();
    }

    private void showNewCard(){
        mCard.setVisibility(View.VISIBLE);
        mCardSubject.setVisibility(View.VISIBLE);
        mCardContent.setVisibility(View.VISIBLE);
        mCardBack.setVisibility(View.INVISIBLE);
        mPlayer.setVisibility(View.INVISIBLE);
        mNextCard.setVisibility(View.VISIBLE);
        mNumCard.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCard.setImageDrawable(this.getDrawable(mCardsImage.get(mCurrent)));
        }else{
            mCard.setImageDrawable(this.getResources().getDrawable(mCardsImage.get(mCurrent)));
        }
        String selectedCard = mCards.get(mCardsImage.get(mCurrent));
        int numCard = Integer.parseInt(selectedCard.substring(0, 2));
        String str = mCurrentLanguage.getCards().get(numCard-1).getCardContent();
        String subject="";
        String content="";
        if (str.contains(":")){
            String[] s = str.split(":");
            subject = s[0];
            content = s[1];
        }else{
            subject = "Subject";
            content = str;
        }
        mCardContent.setText(content);
        mCardSubject.setText(subject);
        mNextCard.setText(DataUtils.getINSTANCE(mContext).getLanguage().getNext());
        mCurrent++;
//        if (mCurrent==mCardsImage.size()){
//            reset();
//            Toast.makeText(GamePlayActivity.this, "Reset Gamge", Toast.LENGTH_SHORT).show();
//        }
        mButtonStatus = true;


    }
    private void nextCard(){
        mNumCard.setText(mCardsImage.size() - mCurrent + "");
        mNumCard.setVisibility(View.VISIBLE);
        mCard.setVisibility(View.INVISIBLE);
        mCardSubject.setVisibility(View.INVISIBLE);
        mCardContent.setVisibility(View.INVISIBLE);
        mCardBack.setVisibility(View.VISIBLE);
        mPlayer.setVisibility(View.VISIBLE);
        mNextCard.setVisibility(View.INVISIBLE);
        String sPick =" "+ DataUtils.getINSTANCE(mContext).getLanguage().getPick();
        mPlayer.setText(mplayers.get(mCurrent % mplayers.size()).getName() + sPick);
        mButtonStatus = false;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GamePlayActivity.this);

        builder.setTitle(DataUtils.getINSTANCE(this).getLanguage().getNotiTitle());
        builder.setMessage(DataUtils.getINSTANCE(this).getLanguage().getNotiLeaveGame());

        builder
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(GamePlayActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create();
        builder.show();

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
