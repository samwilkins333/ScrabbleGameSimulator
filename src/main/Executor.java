package main;

import ScrabbleBase.Board.Location.TilePlacement;
import ScrabbleBase.Board.State.BoardStateUnit;
import ScrabbleBase.Board.State.Multiplier;
import ScrabbleBase.Board.State.Tile;
import ScrabbleBase.Generation.Generator;
import ScrabbleBase.Generation.Objects.ScoredCandidate;
import ScrabbleBase.Vocabulary.Trie;
import resources.TrieFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static main.Configuration.BOARD_DIMENSIONS;
import static main.Configuration.RACK_CAPACITY;

public class Executor {

  public static void main(String[] args) {

    System.out.println("\nLoading trie...");
    Trie trie = new TrieFactory().loadFrom("ospd4.txt");
    System.out.printf("Vocabulary contains %d words.\n", trie.getWordCount());
    System.out.println("Done.\n");

    System.out.println("Populating tile bag...");
    List<Tile> tileBag = new ArrayList<>();
    for (Map.Entry<Character, Configuration.TileConfiguration> entry : Configuration.tileConfigurationMap.entrySet()) {
      for (int i = 0; i < entry.getValue().getFrequency(); i++) {
        tileBag.add(new Tile(entry.getKey(), entry.getValue().getValue(), null));
      }
    }
    System.out.println(tileBag.stream().map(tile -> String.valueOf(tile.getLetter())).collect(Collectors.joining(" ")));
    System.out.println("Done.\n");

    System.out.println("Initializing board multipliers...");
    BoardStateUnit[][] board = new BoardStateUnit[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
    for (int y = 0; y < BOARD_DIMENSIONS; y++) {
      List<String> multipliers = new ArrayList<>();
      for (int x = 0; x < BOARD_DIMENSIONS; x++) {
        int _x = Math.min(x, BOARD_DIMENSIONS - 1 - x);
        int _y = Math.min(y, BOARD_DIMENSIONS - 1 - y);
        Multiplier special = Configuration.locationMapping.get(_y).get(_x);
        Multiplier resolved = special != null ? special : new Multiplier();
        board[y][x] = new BoardStateUnit(resolved, null);
        multipliers.add(String.format("%s:%s", resolved.getLetterValue(), resolved.getWordValue()));
      }
      System.out.println(String.join(" ", multipliers));
    }
    System.out.println("Done.\n");

    System.out.println("Filling racks...");
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
    System.out.println("Done.\n");

    int impasseCount = 0;
    Player current = playerTwo;

    System.out.println("Beginning game...");

    int i = 0;
    while (impasseCount < 2 && current.hasTiles()) {
      current = current.equals(playerOne) ? playerTwo : playerOne;
      System.out.printf("\n*** Move %d ***\n\n", ++i);
      boolean played = current.play(board, tileBag);
      if (!played) {
        impasseCount++;
      } else {
        impasseCount = 0;
      }
      log(board, playerOne.getScore(), playerTwo.getScore(), tileBag.size());
    }

    System.out.println("\nGame over.");
  }

  private static void log(BoardStateUnit[][] board, int oneScore, int twoScore, int remainingTileCount) {
    for (int y = 0; y < BOARD_DIMENSIONS; y++) {
      List<String> letters = new ArrayList<>();
      for (int x = 0; x < BOARD_DIMENSIONS; x++) {
        Tile played = board[y][x].getTile();
        letters.add(played != null ? String.valueOf(played.getLetter()) : "_");
      }
      System.out.println(String.join(" ", letters));
    }
    System.out.printf("\nTile bag count: %d\n\n", remainingTileCount);
    System.out.printf("Player 1: %d\n", oneScore);
    System.out.printf("Player 2: %d\n", twoScore);
  }

  private static class Player {

    private LinkedList<Tile> rack;
    private int score = 0;

    public Player(LinkedList<Tile> rack) {
      this.rack = rack;
    }

    public boolean play(BoardStateUnit[][] board, List<Tile> tileBag) {
      String serialized = this.rack.stream().map(tile -> String.valueOf(tile.getLetter())).collect(Collectors.joining(" "));
      System.out.printf("%s => ", serialized);

      List<ScoredCandidate> candidates = Generator.Instance.computeAllCandidates(this.rack, board);

      if (candidates.size() == 0) {
        return false;
      }

      ScoredCandidate maximal = candidates.get(0);
      score += maximal.getScore();

      System.out.println(maximal);
      System.out.println();

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

      return true;
    }

    public boolean hasTiles() {
      return !this.rack.isEmpty();
    }

    public int getScore() {
      return score;
    }

  }

}