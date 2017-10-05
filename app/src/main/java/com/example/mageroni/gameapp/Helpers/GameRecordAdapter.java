package com.example.mageroni.gameapp.Helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mageroni.gameapp.Models.GameRecord;
import com.example.mageroni.gameapp.R;

import java.util.List;

public class GameRecordAdapter extends ArrayAdapter<GameRecord> {

    public GameRecordAdapter(Context context, List<GameRecord> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        GameRecord gameRecord = getItem(position);
        if (convertView == null) {
            convertView =
            LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.teamName);
        TextView firstScore = (TextView) convertView.findViewById(R.id.firstScore);
        TextView secondScore = (TextView) convertView.findViewById(R.id.secondScore);

        if(gameRecord != null){
            name.setText(gameRecord.getPlayerName());
            firstScore.setText(gameRecord.getScore());
            secondScore.setText(gameRecord.getScore2());
        }

        return convertView;
    }

}
