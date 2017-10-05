package com.example.mageroni.gameapp.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "Games")
public class GameRecord extends Model implements Serializable {
    @Column(name = "Name")
    private String playerName;
    @Column(name = "First player score")
    private int score;
    @Column(name = "Second player score")
    private int score2;

    public GameRecord(String playerName){
        this.playerName = playerName;
        this.score = 0;
        this.score2 = 0;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void addToScore(int add){
        this.score2 += add;
    }

    public void addToScore(int addFirst, int addSecond){
        this.score += addFirst;
        this.score2 += addSecond;
    }

    public int getScore(){
        return score;
    }

    public int getScore2(){
        return score2;
    }

    public int getMaxScore(){
        return Math.max(score, score2);
    }
}
