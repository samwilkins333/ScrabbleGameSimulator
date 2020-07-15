package resources;

import ScrabbleBase.Vocabulary.Trie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TrieFactory {

  public Trie loadFrom(String dictionary) {
    Trie trie = new Trie();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(getClass().getResource(String.format("dictionaries/%s", dictionary)).getFile()));
      String word;
      while ((word = reader.readLine()) != null) {
        if (word.length() > 0) {
          trie.addWord(word.trim());
        }
      }
      return trie;
    } catch (FileNotFoundException e) {
      System.out.printf("Unable to locate dictionary file \"%s\"\n", dictionary);
    } catch (IOException e) {
      System.out.printf("Encountered error while reading from \"%s\"\n", dictionary);
    }
    return trie;
  }

}
