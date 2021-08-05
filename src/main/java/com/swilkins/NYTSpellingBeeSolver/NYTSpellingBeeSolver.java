package com.swilkins.NYTSpellingBeeSolver;

import com.swilkins.ScrabbleBase.Vocabulary.PermutationTrie;
import com.swilkins.ScrabbleBase.Vocabulary.TrieNode;

import java.util.*;
import java.util.stream.Collectors;

public class NYTSpellingBeeSolver {

  public static void main(String[] argv) {
    if (argv.length == 0) {
      System.out.println("Please include at least one argument, a concatenation of the letters (for example, `dzanYle`) that will be used to generate candidate words. Capital letters are required. You may also include a minimum integer word length as a second argument, and a prefix cap (to show hints) as a third.");
      System.exit(1);
    }

    Set<String> words = new HashSet<>();
    Set<Character> letters = new HashSet<>();

    Set<String> required = new HashSet<>();
    for (char l : argv[0].toCharArray()) {
      char lower = Character.toLowerCase(l);
      if (Character.isUpperCase(l)) {
        required.add(String.valueOf(lower));
      }
      letters.add(lower);
    }

    int minimum = 0;
    if (argv.length > 1) {
      minimum = Math.max(Integer.parseInt(argv[1]), 0);
    }

    int prefixCap = -1;
    if (argv.length > 2) {
      prefixCap = Integer.parseInt(argv[2]);
    }

    System.out.print("Loading vocabulary...");
    PermutationTrie vocabulary  = new PermutationTrie();
    vocabulary.loadFrom(NYTSpellingBeeSolver.class.getResource("/ospd4.txt"), String::trim);
    System.out.println("Done.");

    System.out.printf("\nSolving for %s...", letters);
    traverse(vocabulary.getRoot(), letters, "", words);
    System.out.println("Done.");

    System.out.printf("\nFound %d candidates.\n", words.size());

    if (!required.isEmpty()) {
      System.out.printf("Filtering by required letters %s\n", required);
      List<String> requiredList = new ArrayList<>(required);
      words = words.parallelStream().filter(word -> requiredList.stream().allMatch(word::contains)).collect(Collectors.toSet());
      System.out.printf("Retained %d candidates.\n", words.size());
    }

    List<String> output = new ArrayList<>(words);
    output.sort(Comparator.comparingInt(String::length).reversed().thenComparing(String::compareTo));
    int previousLength = Integer.MAX_VALUE;
    for (String word : output) {
      if (word.length() < minimum) {
        break;
      }
      if (word.length() < previousLength) {
        System.out.printf("\n__ %d letters __\n\n", word.length());
        previousLength = word.length();
      }
      int diff = word.length() - prefixCap;
      if (prefixCap == -1 || diff <= 0) {
        System.out.println(word);
      } else {
        System.out.println(word.substring(0, prefixCap) + "_".repeat(diff));
      }
    }
  }

  public static void traverse(TrieNode node, Set<Character> letters, String accumulator, Set<String> words) {
    for (char l : letters) {
      TrieNode child = node.getChild(l);
      if (child != null) {
        String updated = accumulator + l;
        if (child.getTerminal()) {
          words.add(updated);
        }
        traverse(child, letters, updated, words);
      }
    }
  }

}
