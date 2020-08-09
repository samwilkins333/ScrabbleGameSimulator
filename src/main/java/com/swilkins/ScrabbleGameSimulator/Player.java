package com.swilkins.ScrabbleGameSimulator;

import com.swilkins.ScrabbleBase.Board.Location.TilePlacement;
import com.swilkins.ScrabbleBase.Board.State.BoardSquare;
import com.swilkins.ScrabbleBase.Board.State.Tile;
import com.swilkins.ScrabbleBase.Generation.Generator;
import com.swilkins.ScrabbleBase.Generation.Candidate;
import com.swilkins.ScrabbleBase.Generation.GeneratorResult;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.swilkins.ScrabbleBase.Generation.Generator.getDefaultOrdering;
import static com.swilkins.ScrabbleGameSimulator.Configuration.RACK_CAPACITY;

public class Player {

  private LinkedList<Tile> rack;
  private int score = 0;

  public Player(LinkedList<Tile> rack) {
    this.rack = rack;
  }

  public Candidate play(Generator generator, BoardSquare[][] board, List<Tile> tileBag, boolean enableLogging) {
    if (enableLogging) {
      String serialized = this.rack.stream().map(tile -> String.valueOf(tile.getLetter())).collect(Collectors.joining(" "));
      System.out.printf("%s => ", serialized);
    }

    GeneratorResult result = generator.compute(this.rack, board).orderBy(getDefaultOrdering());

    if (result.isEmpty()) {
      return null;
    }

    Candidate maximal = result.get(0);
    score += maximal.getScore();

    if (enableLogging) {
      System.out.println(maximal);
      System.out.println();
    }

    for (TilePlacement placement : maximal.getPrimary()) {
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