package com.superapp.kingscupdrinkingamefunpartyrock.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.superapp.kingscupdrinkingamefunpartyrock.R;
import com.superapp.kingscupdrinkingamefunpartyrock.adapter.PlayerAdapter;
import com.superapp.kingscupdrinkingamefunpartyrock.model.Player;
import com.superapp.kingscupdrinkingamefunpartyrock.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayerSetupActivity extends AppCompatActivity {
    private final Context mContext = PlayerSetupActivity.this;
    private ListView mlvPlayer;
    private PlayerAdapter playerAdapter;
    private List<Player> players;
    private int NumOfPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(DataUtils.getINSTANCE(mContext).getLanguage().getPlayerSetup());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(getApplicationContext()).playSound(getApplicationContext());
                onBackPressed();
            }
        });
       getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getActionBar();
        NumOfPlayer = 0;
        players = new ArrayList<Player>();
        mlvPlayer = (ListView) findViewById(R.id.lv_player);
        playerAdapter = new PlayerAdapter(this,R.layout.listview_player_item,players,PlayerSetupActivity.this);
        mlvPlayer.setAdapter(playerAdapter);
        Button btnStartGame = (Button) findViewById(R.id.btn_player_start);
        btnStartGame.setText(DataUtils.getINSTANCE(PlayerSetupActivity.this).getLanguage().getStart());
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(getApplicationContext()).playSound(getApplicationContext());
                if (players.size()>0) {
                    DataUtils.getINSTANCE(PlayerSetupActivity.this).setPlayers(players);
                    Intent intent = new Intent(PlayerSetupActivity.this, GamePlayActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(PlayerSetupActivity.this, DataUtils.getINSTANCE(mContext).getLanguage().getPickAtleast(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.player_setup, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.mn_add_player){
            DataUtils.getINSTANCE(getApplicationContext()).playSound(getApplicationContext());
            NumOfPlayer ++;
            String sPlayer = DataUtils.getINSTANCE(mContext).getLanguage().getPlayer() +" ";
            Player player = new Player(NumOfPlayer,sPlayer+NumOfPlayer);
            players.add(player);
            playerAdapter.update(players);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DataUtils.getINSTANCE(this).playSound(this);
        Intent intent = new Intent(PlayerSetupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
