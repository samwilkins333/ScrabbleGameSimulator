package resources;

import ScrabbleBase.Generation.Generator;
import ScrabbleBase.Vocabulary.Trie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static main.Configuration.RACK_CAPACITY;

public class TrieFactory {

  public Trie loadFrom(String dictionary) {
    Trie trie = new Trie();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(getClass().getResource(String.format("dictionaries/%s", dictionary)).getFile()));
      String word;
      while ((word = reader.readLine()) != null) {
        trie.addWord(word.trim());
      }
      Generator.Instance.setRoot(trie.getRoot());
      Generator.Instance.setRackCapacity(RACK_CAPACITY);
      return trie;
    } catch (FileNotFoundException e) {
      System.out.printf("Unable to locate dictionary file \"%s\"\n", dictionary);
    } catch (IOException e) {
      System.out.printf("Encountered error while reading from \"%s\"\n", dictionary);
    }
    return trie;
  }

}
