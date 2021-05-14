package com.deco2800.game.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import org.junit.jupiter.api.Test;

class StringDecoratorTest {
  @Test
  void shouldReturnGivenString() {
    Function<Integer, String> customToString = (Integer n) -> "hello";
    StringDecorator<Integer> decorator = new StringDecorator<>(5, customToString);

    assertEquals("hello", decorator.toString());
  }

  @Test
  void shouldPassCorrectObject() {
    Function<Integer, String> customToString = (Integer n) -> Integer.toString(n * 2);

    StringDecorator<Integer> decorator = new StringDecorator<>(5, customToString);
    assertEquals("10", decorator.toString());

    decorator = new StringDecorator<>(10, customToString);
    assertEquals("20", decorator.toString());
  }
}