package main;

import ScrabbleBase.Board.State.BoardStateUnit;
import ScrabbleBase.Generation.Objects.ScoredCandidate;

public class GameResult {

  private final BoardStateUnit[][] board;
  private final ScoredCandidate bestWord;
  private final int oneScore;
  private final int twoScore;
  private final int moveCount;

  public GameResult(BoardStateUnit[][] board, ScoredCandidate bestWord, int oneScore, int twoScore, int moveCount) {
    this.board = board;
    this.bestWord = bestWord;
    this.oneScore = oneScore;
    this.twoScore = twoScore;
    this.moveCount = moveCount;
  }

  public BoardStateUnit[][] getBoard() {
    return board;
  }

  public ScoredCandidate getBestWord() {
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
