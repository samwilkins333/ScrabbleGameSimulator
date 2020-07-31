package com.swilkins.ScrabbleGameSimulator;

import com.swilkins.ScrabbleBase.Generation.Generator;
import com.swilkins.ScrabbleBase.Vocabulary.PermutationTrie;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.swilkins.ScrabbleBase.Board.Configuration.logBoard;
import static com.swilkins.ScrabbleBase.Vocabulary.Trie.LOWERCASE;
import static com.swilkins.ScrabbleGameSimulator.Configuration.*;

public class GameHarness {

  public static void main(String[] args) {
    System.out.println("\nLoading trie...");
    URL dictionary = GameHarness.class.getResource(String.format("/%s", DICTIONARY));
    PermutationTrie trie = new PermutationTrie(LOWERCASE);
    trie.loadFrom(dictionary, String::trim);
    System.out.printf("Vocabulary contains %d words.\n", trie.size());
    System.out.printf("Trie contains %d nodes.\n", trie.getNodeSize());
    System.out.println("Done.\n");

    Generator generator = new Generator(trie, RACK_CAPACITY);

    System.out.println("Beginning game simulation...\n");

    long start = System.currentTimeMillis();
    List<GameResult> results = new ArrayList<>();
    for (int i = 0; i < GAME_ITERATIONS; i++) {
      System.out.printf("Game %d\n", i + 1);
      results.add(new Game().play(generator, ENABLE_LOGGING));
    }

    long duration = System.currentTimeMillis() - start;
    if (!ENABLE_LOGGING) {
      System.out.println();
    }
    System.out.printf("All games completed in (wall clock) %d seconds. Computing statistics...\n", duration / 1000);

    Optional<GameResult> maxWord = results.stream().max(Comparator.comparingInt(result -> result.getBestWord().getScore()));
    maxWord.ifPresent(gameResult -> {
      System.out.printf("\n1) Highest-scoring single play was %s.\n\n", gameResult.getBestWord());
      logBoard(gameResult.getBoard());
    });

    Optional<GameResult> maxOne = results.stream().max(Comparator.comparingInt(GameResult::getOneScore));
    Optional<GameResult> maxTwo = results.stream().max(Comparator.comparingInt(GameResult::getTwoScore));
    if (maxOne.isPresent() && maxTwo.isPresent()) {
      int maxScore = Math.max(maxOne.get().getOneScore(), maxTwo.get().getTwoScore());
      GameResult maxGame = maxScore == maxOne.get().getOneScore() ? maxOne.get() : maxTwo.get();
      System.out.printf("\n2) Highest-scoring player total was %d points.\n\n", maxScore);
      logBoard(maxGame.getBoard());
    }

    Optional<GameResult> maxLength = results.stream().max(Comparator.comparingInt(GameResult::getMoveCount));
    maxLength.ifPresent(gameResult -> {
      System.out.printf("\n3) Longest game required %d turns.\n\n", gameResult.getMoveCount());
      logBoard(gameResult.getBoard());
    });
  }

}