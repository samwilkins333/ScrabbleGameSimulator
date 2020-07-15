package main;

import ScrabbleBase.Vocabulary.Trie;
import resources.TrieFactory;

import java.util.*;

import static main.Configuration.ENABLE_LOGGING;

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
      results.add(new Game().play(ENABLE_LOGGING));
    }

    long duration = System.currentTimeMillis() - start;
    if (!ENABLE_LOGGING) {
      System.out.println();
    }
    System.out.printf("All games completed in (wall clock) %d seconds. Computing statistics...\n", duration / 1000);

    Optional<GameResult> maxWord = results.stream().max(Comparator.comparingInt(result -> result.getBestWord().getScore()));
    maxWord.ifPresent(gameResult -> {
      System.out.printf("\n1) Highest-scoring single play was %s.\n\n", gameResult.getBestWord());
      Game.logBoard(gameResult.getBoard());
    });

    Optional<GameResult> maxOne = results.stream().max(Comparator.comparingInt(GameResult::getOneScore));
    Optional<GameResult> maxTwo = results.stream().max(Comparator.comparingInt(GameResult::getTwoScore));
    if (maxOne.isPresent() && maxTwo.isPresent()) {
      int maxScore = Math.max(maxOne.get().getOneScore(), maxTwo.get().getTwoScore());
      GameResult maxGame = maxScore == maxOne.get().getOneScore() ? maxOne.get() : maxTwo.get();
      System.out.printf("\n2) Highest-scoring player total was %d points.\n\n", maxScore);
      Game.logBoard(maxGame.getBoard());
    }

    Optional<GameResult> maxLength = results.stream().max(Comparator.comparingInt(GameResult::getMoveCount));
    maxLength.ifPresent(gameResult -> {
      System.out.printf("\n3) Longest game required %d turns.\n\n", gameResult.getMoveCount());
      Game.logBoard(gameResult.getBoard());
    });
  }

}