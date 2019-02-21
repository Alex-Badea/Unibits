package game;

import game.input.Streamable;

/**
 * Created by balex on 17.12.2016.
 */
public class Player {
    private final Streamable<Selection> inputMethod;
    private int score;

    ////
    protected Player(Streamable<Selection> inputMethod) {
        this.inputMethod = inputMethod;
        score = 0;
    }

    protected int getScore(){
        return score;
    }

    protected Selection move() {
        return inputMethod.getNext();
    }

    protected void addScore(int score){
        this.score += score;
    }

    public String toString() {
        return "Scor: " + score;
    }
}
