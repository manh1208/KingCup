package com.superapp.kingscupdrinkingamefunpartyrock.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.superapp.kingscupdrinkingamefunpartyrock.R;
import com.superapp.kingscupdrinkingamefunpartyrock.utils.DataUtils;

public class SettingActivity extends AppCompatActivity {
    private final Context mContext = SettingActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(DataUtils.getINSTANCE(mContext).getLanguage().getSetting());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btnCardRule = (Button) findViewById(R.id.btn_setting_rule);
        btnCardRule.setText(DataUtils.getINSTANCE(mContext).getLanguage().getSettingCardRule());
        btnCardRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(mContext).playSound(mContext);
                Intent intent = new Intent(SettingActivity.this, CardRuleActivity.class);
                startActivity(intent);

            }
        });
        Button btnLanguage = (Button) findViewById(R.id.btn_setting_language);
        btnLanguage.setText(DataUtils.getINSTANCE(mContext).getLanguage().getSettingLanguage());
        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(mContext).playSound(mContext);
                Intent intent = new Intent(SettingActivity.this, LanguageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
