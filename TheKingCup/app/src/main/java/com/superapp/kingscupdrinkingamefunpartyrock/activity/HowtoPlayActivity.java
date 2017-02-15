package com.superapp.kingscupdrinkingamefunpartyrock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.superapp.kingscupdrinkingamefunpartyrock.R;
import com.superapp.kingscupdrinkingamefunpartyrock.utils.DataUtils;

/**
 * Created by ManhNV on 4/8/2016.
 */
public class HowtoPlayActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtoplay);
        TextView howtoplay= (TextView) findViewById(R.id.tv_howtoplay);
        howtoplay.setText(DataUtils.getINSTANCE(HowtoPlayActivity.this).getLanguage().getHowToPlayContent());
    }
}
