package com.example.alec.potatoapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PotatoList extends ArrayAdapter<PotatoCard> {

    private Activity context;
    private List<PotatoCard> potatoList;

    public PotatoList(Activity context, List<PotatoCard> potatoList) {
        super(context, R.layout.list_layout, potatoList);
        this.context = context;
        this.potatoList = potatoList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewResult = (TextView) listViewItem.findViewById(R.id.textViewResult);

        PotatoCard potatoCard = potatoList.get(position);

        textViewName.setText(potatoCard.getCardName());
        textViewResult.setText(potatoCard.getResult());

        return listViewItem;
    }
}
