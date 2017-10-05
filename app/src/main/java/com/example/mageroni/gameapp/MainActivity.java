package com.example.mageroni.gameapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonStartGame(View view){
        String playerName = getPlayerName();
        if(playerName.isEmpty()){
            nameIsRequiredDialog();
            return;
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerName", playerName);
        intent.putExtra("gameType", getGameType());
        startActivity(intent);
    }

    private String getPlayerName(){
        EditText editText = (EditText) findViewById(R.id.playerNameEntry);
        return editText.getText().toString();
    }

    private void nameIsRequiredDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.missing_parameters_title));
        dialog.setMessage(getResources().getString(R.string.missing_parameters_message)).setCancelable(true);

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private String getGameType(){
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.gameTypeOptions);
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        return radioButton.getTag().toString();
    }

}
