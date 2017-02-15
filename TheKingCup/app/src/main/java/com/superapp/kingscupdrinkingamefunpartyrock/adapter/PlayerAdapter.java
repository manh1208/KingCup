package com.superapp.kingscupdrinkingamefunpartyrock.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.superapp.kingscupdrinkingamefunpartyrock.R;
import com.superapp.kingscupdrinkingamefunpartyrock.model.Player;
import com.superapp.kingscupdrinkingamefunpartyrock.utils.DataUtils;

import java.util.List;

/**
 * Created by ManhNV on 4/11/2016.
 */
public class PlayerAdapter extends ArrayAdapter<Player> {
    private Context mContext;
    private int resource;
    private List<Player> players;
    private Activity mActivity;

    public PlayerAdapter(Context context, int resource, List<Player> objects,Activity activity) {
        super(context, resource, objects);
        this.mContext = context;
        this.resource = resource;
        this.players = objects;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Player getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return players.get(position).getId();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_player_item, parent, false);
        }
        final Player player = players.get(position);
        TextView tvPlayer = (TextView) convertView.findViewById(R.id.tv_player_name);
        tvPlayer.setText(player.getName());
        Button btnDelete = (Button) convertView.findViewById(R.id.btn_player_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(mContext).playSound(mContext);
                players.remove(position);
                notifyDataSetChanged();
            }
        });
        Button btnEdit = (Button) convertView.findViewById(R.id.btn_player_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.getINSTANCE(mContext).playSound(mContext);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_player_edit, null);
                final EditText etPlayerName = (EditText) view.findViewById(R.id.dialog_player_name);
                etPlayerName.setText(player.getName());
                builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        players.get(position).setName(etPlayerName.getText().toString());
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.create();
                builder.show();
            }
        });

        return convertView;
    }

    public void update(List<Player> players) {
        this.players = players;
        notifyDataSetChanged();
    }
}
