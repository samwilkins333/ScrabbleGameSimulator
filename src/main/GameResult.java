package main;

import ScrabbleBase.Generation.Objects.ScoredCandidate;

public class GameResult {

  private final ScoredCandidate bestWord;
  private final int oneScore;
  private final int twoScore;
  private final int moveCount;

  public GameResult(ScoredCandidate bestWord, int oneScore, int twoScore, int moveCount) {
    this.bestWord = bestWord;
    this.oneScore = oneScore;
    this.twoScore = twoScore;
    this.moveCount = moveCount;
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
