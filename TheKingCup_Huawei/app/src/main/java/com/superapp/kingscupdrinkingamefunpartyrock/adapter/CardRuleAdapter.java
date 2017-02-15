package com.superapp.kingscupdrinkingamefunpartyrock.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.superapp.kingscupdrinkingamefunpartyrock.R;
import com.superapp.kingscupdrinkingamefunpartyrock.model.Card;

import java.util.List;

/**
 * Created by ManhNV on 4/13/2016.
 */
public class CardRuleAdapter extends ArrayAdapter<Card> {
    private Context mContext;
    private int resource;
    private List<Card> cards;
    private Activity mActivity;

    public CardRuleAdapter(Context context, int resource, List<Card> objects, Activity activity) {
        super(context, resource, objects);
        this.mContext = context;
        this.resource = resource;
        this.cards = objects;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Card getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cards.get(position).getId();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_cardrule_item, parent, false);
        }
        Card card = cards.get(position);
        TextView tvCardCharacter = (TextView) convertView.findViewById(R.id.tv_card_character);
        tvCardCharacter.setText(card.getCharacter());
        TextView tvCardName = (TextView) convertView.findViewById(R.id.tv_card_name);
        tvCardName.setText(card.getName());
        TextView tvCardSubject = (TextView) convertView.findViewById(R.id.tv_card_subject);
        tvCardSubject.setText(card.getCardContent().split(":")[0]);
        return convertView;
    }
}
