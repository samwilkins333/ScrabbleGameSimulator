package main;

import ScrabbleBase.Board.Location.TilePlacement;
import ScrabbleBase.Board.State.BoardStateUnit;
import ScrabbleBase.Board.State.Tile;
import ScrabbleBase.Generation.Generator;
import ScrabbleBase.Generation.Objects.ScoredCandidate;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static main.Configuration.RACK_CAPACITY;

public class Player {

  private LinkedList<Tile> rack;
  private int score = 0;

  public Player(LinkedList<Tile> rack) {
    this.rack = rack;
  }

  public ScoredCandidate play(BoardStateUnit[][] board, List<Tile> tileBag, boolean enableLogging) {
    if (enableLogging) {
      String serialized = this.rack.stream().map(tile -> String.valueOf(tile.getLetter())).collect(Collectors.joining(" "));
      System.out.printf("%s => ", serialized);
    }

    List<ScoredCandidate> candidates = Generator.Instance.computeAllCandidates(this.rack, board);

    if (candidates.size() == 0) {
      return null;
    }

    ScoredCandidate maximal = candidates.get(0);
    score += maximal.getScore();

    if (enableLogging) {
      System.out.println(maximal);
      System.out.println();
    }

    for (TilePlacement placement : maximal.getPlacements()) {
      Tile toPlay = placement.getTile();
      for (Tile tile : this.rack) {
        if (tile.getLetter() == toPlay.getLetter()) {
          this.rack.remove(tile);
          break;
        }
      }
      board[placement.getY()][placement.getX()].setTile(placement.getTile());
    }

    while (!tileBag.isEmpty() && this.rack.size() < RACK_CAPACITY) {
      this.rack.add(tileBag.remove((int) (Math.random() * tileBag.size())));
    }

    return maximal;
  }

  public boolean hasTiles() {
    return !this.rack.isEmpty();
  }

  public int getScore() {
    return score;
  }

}