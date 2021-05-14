package com.deco2800.game.utils;

import java.util.function.Function;

public class StringDecorator<T> {
  public T object;
  public Function<T, String> printFn;

  public StringDecorator(T object, Function<T, String> printFn) {
    this.object = object;
    this.printFn = printFn;
  }

  @Override
  public String toString() {
    return printFn.apply(object);
  }
}
