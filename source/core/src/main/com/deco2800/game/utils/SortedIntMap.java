package com.deco2800.game.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A map sorted by the value of key with O(1) iteration. put/get/contains are O(n). useful when we
 * don't modify values often, but want to iterate quickly.
 * @param <V>
 */
public class SortedIntMap<V> implements Iterable<V> {
  private final IntArray keys;
  private final Array<V> values;

  public SortedIntMap(int capacity) {
    keys = new IntArray(true, capacity);
    values = new Array<>(true, capacity);
  }

  public boolean contains(int key) {
    return keys.contains(key);
  }

  public boolean contains(V value) {
    return values.contains(value, true);
  }

  public void put(int key, V value) {
    if (keys.size == 0) {
      insertAt(0, key, value);
      return;
    }

    for (int i = 0; i < keys.size; i++) {
      if (keys.get(i) >= key) {
        insertAt(i, key, value);
        return;
      }
    }

    keys.add(key);
    values.add(value);
  }

  public V get(int key) {
    int index = keys.indexOf(key);
    if (index == -1) {
      return null;
    }
    return values.get(index);
  }

  public void clear() {
    keys.clear();
    values.clear();
  }

  private void insertAt(int i, int key, V value) {
    keys.insert(i, key);
    values.insert(i, value);
  }

  @Override
  public Iterator<V> iterator() {
    return values.iterator();
  }

  @Override
  public void forEach(Consumer<? super V> action) {
    values.forEach(action);
  }

  @Override
  public Spliterator<V> spliterator() {
    return values.spliterator();
  }
}
