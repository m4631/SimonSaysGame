package com.example.mageroni.gameapp.Helpers;

import android.content.res.Resources;

import com.example.mageroni.gameapp.Models.GameRecord;
import com.example.mageroni.gameapp.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class for managing the state of the game and all its corresponding
 */

public class Game{
    private static  Game instance;
    private Random randomGenerator;

    public ArrayList<Integer> simonSteps;
    public GameRecord gameRecord;
    public int gameType;
    public boolean firstPlayerTurn;
    public int userStep;

    private Game() {}

    public static Game getInstance() {
        if(instance == null){
            instance = new Game();
        }
        return instance;
    }

    private void resetGame(){
        randomGenerator = new Random();
        simonSteps = new ArrayList<>();
    }

    public void cleanState(){
        firstPlayerTurn = true;
        userStep = 0;
    }

    public void startGame(String playerName, int gameType){
        this.gameType = gameType;
        gameRecord = new GameRecord(playerName);
        gameRecord.save();
        resetGame();
    }

    public int playMachine(){
        int machineChoice = randomGenerator.nextInt(4);
        simonSteps.add(machineChoice);
        return machineChoice;
    }

    public void prepareNextTurn(){
        firstPlayerTurn = !firstPlayerTurn;
        userStep = 0;
    }

    public void saveScore(){
        if(gameType == 0) {
            gameRecord.addToScore(1);
        }else{
            if(firstPlayerTurn) {
                gameRecord.addToScore(1, 0);
            }else{
                gameRecord.addToScore(0, 1);
            }
        }
    }

    public String getTurnMessage(Resources res){
        String showingText = res.getString(R.string.single_player_turn);
        if(gameType == 1){
            if(firstPlayerTurn){
                showingText = res.getString(R.string.multi_player_first_turn);
            }else{
                showingText = res.getString(R.string.multi_player_second_turn);
            }
        }
        return showingText;
    }

    public String getWinnerMessage(Resources res){
        String showingText;
        if(gameType == 0){
            if(firstPlayerTurn){
                showingText = gameRecord.getPlayerName() + " " + res.getString(R.string.win_message);
            }else{
                showingText = res.getString(R.string.machine_win_message);
            }
        }else{
            if(firstPlayerTurn){
                showingText = res.getString(R.string.second_player_win_message);
            }else{
                showingText = res.getString(R.string.first_player_win_message);
            }
        }
        showingText += " " + res.getString(R.string.max_score_team);
        showingText += " " + gameRecord.getMaxScore();
        return showingText;
    }
}
