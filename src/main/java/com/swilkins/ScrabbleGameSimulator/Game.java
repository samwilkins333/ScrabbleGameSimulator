package com.swilkins.ScrabbleGameSimulator;

import com.swilkins.ScrabbleBase.Board.State.BoardSquare;
import com.swilkins.ScrabbleBase.Board.State.Tile;
import com.swilkins.ScrabbleBase.Generation.Candidate;
import com.swilkins.ScrabbleBase.Generation.Generator;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.swilkins.ScrabbleBase.Board.Configuration.*;
import static com.swilkins.ScrabbleGameSimulator.Configuration.RACK_CAPACITY;

public class Game {

  public GameResult play(Generator generator, boolean enableLogging) {
    if (enableLogging) {
      System.out.println("\nPopulating tile bag...");
    }
    List<Tile> tileBag = getStandardTileBag();
    if (enableLogging) {
      System.out.println(tileBag.stream().map(tile -> String.valueOf(tile.getLetter())).collect(Collectors.joining(" ")));
      System.out.println("Done.\n");
    }

    if (enableLogging) {
      System.out.println("Initializing board multipliers...");
    }
    BoardSquare[][] board = getStandardBoard();
    if (enableLogging) {
      System.out.println("Done.\n");
    }

    if (enableLogging) {
      System.out.println("Filling racks...");
    }
    LinkedList<Tile> rackOne = new LinkedList<>();
    while (rackOne.size() < RACK_CAPACITY) {
      rackOne.add(tileBag.remove((int) (Math.random() * tileBag.size())));
    }
    Player playerOne = new Player(rackOne);

    LinkedList<Tile> rackTwo = new LinkedList<>();
    while (rackTwo.size() < RACK_CAPACITY) {
      rackTwo.add(tileBag.remove((int) (Math.random() * tileBag.size())));
    }
    Player playerTwo = new Player(rackTwo);
    if (enableLogging) {
      System.out.println("Done.\n");
    }

    int impasseCount = 0;
    Player current = playerTwo;

    if (enableLogging) {
      System.out.println("Beginning game...");
    }

    Candidate bestWord = null;
    int i = 0;
    while (impasseCount < 2 && current.hasTiles()) {
      i++;
      current = current.equals(playerOne) ? playerTwo : playerOne;
      if (enableLogging) {
        System.out.printf("\n*** Move %d ***\n\n", i);
      }
      Candidate played = current.play(generator, board, tileBag, enableLogging);
      if (played == null) {
        impasseCount++;
      } else {
        impasseCount = 0;
        if (bestWord == null || played.getScore() > bestWord.getScore()) {
          bestWord = played;
        }
      }
      if (enableLogging) {
        logBoard(board);
        System.out.printf("\nTile bag count: %d\n\n", tileBag.size());
        System.out.printf("Player 1: %d\n", playerOne.getScore());
        System.out.printf("Player 2: %d\n", playerTwo.getScore());
      }
    }

    if (enableLogging) {
      System.out.println("\nGame over.\n");
    }

    return new GameResult(board, bestWord, playerOne.getScore(), playerTwo.getScore(), i);
  }

}
