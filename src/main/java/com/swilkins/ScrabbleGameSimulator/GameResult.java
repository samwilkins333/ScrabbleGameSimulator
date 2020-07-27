package com.swilkins.ScrabbleGameSimulator;

import com.swilkins.ScrabbleBase.Board.State.BoardSquare;
import com.swilkins.ScrabbleBase.Generation.Candidate;

public class GameResult {

  private final BoardSquare[][] board;
  private final Candidate bestWord;
  private final int oneScore;
  private final int twoScore;
  private final int moveCount;

  public GameResult(BoardSquare[][] board, Candidate bestWord, int oneScore, int twoScore, int moveCount) {
    this.board = board;
    this.bestWord = bestWord;
    this.oneScore = oneScore;
    this.twoScore = twoScore;
    this.moveCount = moveCount;
  }

  public BoardSquare[][] getBoard() {
    return board;
  }

  public Candidate getBestWord() {
    return bestWord;
  }

  public int getOneScore() {
    return oneScore;
  }

  public int getTwoScore() {
    return twoScore;
  }

  public int getMoveCount() {
    return moveCount;
  }

}
