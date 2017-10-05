package com.example.mageroni.gameapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.example.mageroni.gameapp.Helpers.GameRecordAdapter;
import com.example.mageroni.gameapp.Models.GameRecord;

import java.util.ArrayList;
import java.util.List;

public class LeadershipBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadership_board);

        ActiveAndroid.initialize(this);

        List<GameRecord> records = new ArrayList<>(); //new Select().from(GameRecord.class).execute();

        GameRecordAdapter adapter = new GameRecordAdapter(this, records);
        ListView listView = (ListView) findViewById(R.id.leadersList);
        listView.setAdapter(adapter);

        listView.setAdapter(new ArrayAdapter<>(this, R.layout.text_view_item, records));
    }
}
