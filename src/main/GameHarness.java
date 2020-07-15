package main;

import ScrabbleBase.Board.Location.TilePlacement;
import ScrabbleBase.Board.State.BoardStateUnit;
import ScrabbleBase.Board.State.Multiplier;
import ScrabbleBase.Board.State.Tile;
import ScrabbleBase.Generation.Generator;
import ScrabbleBase.Generation.Objects.ScoredCandidate;
import ScrabbleBase.Vocabulary.Trie;
import resources.TrieFactory;

import java.util.*;
import java.util.stream.Collectors;

import static main.Configuration.BOARD_DIMENSIONS;
import static main.Configuration.RACK_CAPACITY;

public class GameHarness {

  public static void main(String[] args) {
    System.out.println("\nLoading trie...");
    Trie trie = new TrieFactory().loadFrom("ospd4.txt");
    System.out.printf("Vocabulary contains %d words.\n", trie.getWordCount());
    System.out.printf("Trie contains %d nodes.\n", trie.getNodeCount());
    System.out.println("Done.\n");

    System.out.println("Beginning game simulation...\n");

    long start = System.currentTimeMillis();
    List<GameResult> results = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      System.out.printf("Game %d\n", i + 1);
      results.add(new Game().play(false));
    }

    long duration = System.currentTimeMillis() - start;
    System.out.printf("\nAll games completed in (wall clock) %d seconds. Computing statistics...\n\n", duration / 1000);

    Optional<GameResult> maxWord = results.stream().max(Comparator.comparingInt(result -> result.getBestWord().getScore()));
    maxWord.ifPresent(gameResult -> System.out.printf("Highest-scoring single play was %s.\n", gameResult.getBestWord()));

    Optional<GameResult> maxOne = results.stream().max(Comparator.comparingInt(GameResult::getOneScore));
    Optional<GameResult> maxTwo = results.stream().max(Comparator.comparingInt(GameResult::getTwoScore));
    if (maxOne.isPresent() && maxTwo.isPresent()) {
      System.out.printf("Highest-scoring player total was %d points.\n", Math.max(maxOne.get().getOneScore(), maxTwo.get().getTwoScore()));
    }

    Optional<GameResult> maxLength = results.stream().max(Comparator.comparingInt(GameResult::getMoveCount));
    maxLength.ifPresent(gameResult -> System.out.printf("Longest game required %d turns.\n", gameResult.getMoveCount()));
  }

}