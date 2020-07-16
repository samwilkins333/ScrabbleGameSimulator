package main;

import ScrabbleBase.Board.State.Multiplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Configuration {

  public static final int RACK_CAPACITY = 7;
  public static final int BOARD_DIMENSIONS = 15;
  public static final boolean ENABLE_LOGGING = false;
  public static final int GAME_ITERATIONS = 10;
  public static final String DICTIONARY = "ospd4.txt";

  public static final Map<Character, TileConfiguration> tileConfigurationMap = new HashMap<>();
  static {
    tileConfigurationMap.put('*', new TileConfiguration(2, 1));
    tileConfigurationMap.put('a', new TileConfiguration(9, 1));
    tileConfigurationMap.put('b', new TileConfiguration(2, 3));
    tileConfigurationMap.put('c', new TileConfiguration(2, 3));
    tileConfigurationMap.put('d', new TileConfiguration(4, 2));
    tileConfigurationMap.put('e', new TileConfiguration(12, 1));
    tileConfigurationMap.put('f', new TileConfiguration(2, 4));
    tileConfigurationMap.put('g', new TileConfiguration(3, 2));
    tileConfigurationMap.put('h', new TileConfiguration(2, 4));
    tileConfigurationMap.put('i', new TileConfiguration(9, 1));
    tileConfigurationMap.put('j', new TileConfiguration(1, 8));
    tileConfigurationMap.put('k', new TileConfiguration(1, 5));
    tileConfigurationMap.put('l', new TileConfiguration(4, 1));
    tileConfigurationMap.put('m', new TileConfiguration(2, 3));
    tileConfigurationMap.put('n', new TileConfiguration(6, 1));
    tileConfigurationMap.put('o', new TileConfiguration(8, 1));
    tileConfigurationMap.put('p', new TileConfiguration(2, 3));
    tileConfigurationMap.put('q', new TileConfiguration(1, 10));
    tileConfigurationMap.put('r', new TileConfiguration(6, 1));
    tileConfigurationMap.put('s', new TileConfiguration(4, 1));
    tileConfigurationMap.put('t', new TileConfiguration(6, 1));
    tileConfigurationMap.put('u', new TileConfiguration(4, 1));
    tileConfigurationMap.put('v', new TileConfiguration(2, 4));
    tileConfigurationMap.put('w', new TileConfiguration(2, 4));
    tileConfigurationMap.put('x', new TileConfiguration(1, 8));
    tileConfigurationMap.put('y', new TileConfiguration(2, 4));
    tileConfigurationMap.put('z', new TileConfiguration(1, 10));
  }

  static final class TileConfiguration {

    private final int frequency;
    private final int value;

    public TileConfiguration(int frequency, int value) {
      this.frequency = frequency;
      this.value = value;
    }

    public int getFrequency() {
      return frequency;
    }

    public int getValue() {
      return value;
    }

  }

  public static final List<Map<Integer, Multiplier>> locationMapping = new ArrayList<>();
  static {
    Map<Integer, Multiplier> local = new HashMap<>();
    local.put(0, new Multiplier(1, 3));
    local.put(3, new Multiplier(2, 1));
    local.put(7, new Multiplier(1, 3));
    locationMapping.add(local);

    local = new HashMap<>();
    local.put(1, new Multiplier(1, 2));
    local.put(5, new Multiplier(3, 1));
    locationMapping.add(local);

    local = new HashMap<>();
    local.put(2, new Multiplier(1, 2));
    local.put(6, new Multiplier(2, 1));
    locationMapping.add(local);

    local = new HashMap<>();
    local.put(0, new Multiplier(2, 1));
    local.put(3, new Multiplier(1, 2));
    local.put(7, new Multiplier(2, 1));
    locationMapping.add(local);

    local = new HashMap<>();
    local.put(4, new Multiplier(1, 2));
    locationMapping.add(local);

    local = new HashMap<>();
    local.put(1, new Multiplier(3, 1));
    local.put(5, new Multiplier(3, 1));
    locationMapping.add(local);

    local = new HashMap<>();
    local.put(2, new Multiplier(2, 1));
    local.put(6, new Multiplier(2, 1));
    locationMapping.add(local);

    local = new HashMap<>();
    local.put(0, new Multiplier(1, 3));
    local.put(3, new Multiplier(2, 1));
    local.put(7, new Multiplier(1, 2));
    locationMapping.add(local);
  }

}
