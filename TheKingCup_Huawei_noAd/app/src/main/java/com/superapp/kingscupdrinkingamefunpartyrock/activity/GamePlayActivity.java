package com.superapp.kingscupdrinkingamefunpartyrock.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.superapp.kingscupdrinkingamefunpartyrock.R;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
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
                nextCard();
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
}
