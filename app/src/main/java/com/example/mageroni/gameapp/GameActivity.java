package com.example.mageroni.gameapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mageroni.gameapp.Helpers.Game;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ArrayList<ObjectAnimator> listAnimators;
    private Button[] buttonReferences;
    private Game game;
    private static final int SinglePlayer = 0;
    private static final int MultiPlayer = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setUpEnvironment();
    }

    private void setUpEnvironment(){
        game = Game.getInstance();
        game.gameType = Integer.parseInt(getIntent().getStringExtra("gameType"));
        buttonReferences = new Button[4];
        setScoreLabels();
        getButtonsReference();
        welcomeDialog();
    }

    private void cleanState(){
        listAnimators = new ArrayList<>();
        game.cleanState();
        setScoreLabels();
        resetScores();
    }

    public void startGame(View view){
        String playerName = getIntent().getStringExtra("playerName");
        if(playerName == null) playerName = "";
        game.startGame(playerName, game.gameType);
        cleanState();
        doFirstMove();
    }

    private void doFirstMove(){
        if(game.gameType == SinglePlayer) {
            machineTurn();
        }
    }

    public void machineTurn(){
        int chosenButton = game.playMachine();
        addAnimator(chosenButton);
        showSteps();
    }

    private void addAnimator(int chosenButton){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(buttonReferences[chosenButton], "alpha", 1f, 0.25f);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(1);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        listAnimators.add(objectAnimator);
    }

    private void showSteps(){
        AnimatorSet animatorSet = getAnimatorSet();
        Animator[] temp = listAnimators.toArray(new ObjectAnimator[game.simonSteps.size()]);
        animatorSet.playSequentially(temp);
        animatorSet.start();
    }

    private AnimatorSet getAnimatorSet(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(600);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                goNexTurn();
                Toast.makeText(GameActivity.this, game.getTurnMessage(getResources()), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        return animatorSet;
    }

    public void userTurn(View view){
        if(game.gameType == SinglePlayer){
            if(!game.firstPlayerTurn){
                singleUser(view);
            }
        }else if(game.gameType == MultiPlayer){
            manualPlayer(view);
        }
    }

    private void singleUser(View view){
        if(game.userStep < game.simonSteps.size()){
            verifyMove(view);
        }
        if(game.userStep == game.simonSteps.size()){
            scorePoint();
            goNexTurn();
            if(game.gameType == SinglePlayer) machineTurn();
        }
    }

    private void manualPlayer(View view){
        if(game.userStep < game.simonSteps.size()){
            verifyMove(view);
        }else if (game.userStep == game.simonSteps.size()){
            manualStepAdd(view);
        }
    }

    private void manualStepAdd(View view){
        game.simonSteps.add( Integer.parseInt(view.getTag().toString()) );
        scorePoint();
        goNexTurn();
    }

    private void verifyMove(View view){
        int correctChoice = game.simonSteps.get(game.userStep);

        if(buttonReferences[correctChoice] != view){
            endGameDialog();
        }

        game.userStep++;
    }

    private void scorePoint(){
        game.saveScore();
        showScore();
    }

    private void showScore(){
        TextView score1 = (TextView) findViewById(R.id.score01);
        TextView score2 = (TextView) findViewById(R.id.score02);
        if(game.gameType == SinglePlayer){
            score2.setText(String.valueOf(game.gameRecord.getScore2()));
        }else{
            score1.setText(String.valueOf(game.gameRecord.getScore()));
            score2.setText(String.valueOf(game.gameRecord.getScore2()));
        }
    }

    private void goNexTurn(){
        game.prepareNextTurn();
        if(game.gameType == MultiPlayer) {
            if(game.firstPlayerTurn) {
                Toast.makeText(this, getResources().getString(R.string.multi_player_first_turn), Toast.LENGTH_SHORT)
                        .show();
            }else{
                Toast.makeText(this, getResources().getString(R.string.multi_player_second_turn), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void exitGame(View view){
        finish();
    }

    /*Interacting with UI**/
    private void setScoreLabels(){
        //Getting all the reference
        TextView singleUserLabel = (TextView) findViewById(R.id.scoreSingleLabel);
        TextView firstUserLabel = (TextView) findViewById(R.id.score01Label);
        TextView secondUserLabel = (TextView) findViewById(R.id.score02Label);
        TextView score1 = (TextView) findViewById(R.id.score01);

        //Resetting before start
        singleUserLabel.setVisibility(View.INVISIBLE);
        firstUserLabel.setVisibility(View.INVISIBLE);
        secondUserLabel.setVisibility(View.INVISIBLE);
        score1.setVisibility(View.INVISIBLE);

        //Putting conditions
        if(game.gameType == SinglePlayer){
            singleUserLabel.setVisibility(View.VISIBLE);
        }else{
            firstUserLabel.setVisibility(View.VISIBLE);
            secondUserLabel.setVisibility(View.VISIBLE);
            score1.setVisibility(View.VISIBLE);
        }
    }

    private void getButtonsReference(){
        buttonReferences[0] = (Button) findViewById(R.id.simonButton01);
        buttonReferences[1] = (Button) findViewById(R.id.simonButton02);
        buttonReferences[2] = (Button) findViewById(R.id.simonButton03);
        buttonReferences[3] = (Button) findViewById(R.id.simonButton04);
    }

    private void resetScores(){
        TextView score1 = (TextView) findViewById(R.id.score01);
        TextView score2 = (TextView) findViewById(R.id.score02);
        score1.setText("0");
        score2.setText("0");
    }
    /*End of interacting with UI**/

    /*Dialogs creation**/
    private void endGameDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.end_game_title));
        dialog.setMessage(game.getWinnerMessage(getResources()))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.replay_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GameActivity.this.startGame(null);
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.exit_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GameActivity.this.finish();
                            }
                        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void welcomeDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.game_instructions));
        dialog.setMessage(getResources().getString(R.string.game_instructions_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGame(null);
                            }
                        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
    /*End of Dialogs creation**/
}
