package com.deco2800.game.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class GameTimeTest {
  GameTime gameTime;

  @BeforeAll
  static void beforeAll() {
    Gdx.graphics = mock(Graphics.class);
    when(Gdx.graphics.getDeltaTime()).thenReturn(10f);
  }

  @BeforeEach
  void beforeEach() {
    gameTime = new GameTime();
  }

  @Test
  void shouldGiveCorrectDelta() {
    assertEquals(10f, gameTime.getDeltaTime());
    assertEquals(10f, gameTime.getRawDeltaTime());
  }

  @Test
  void shouldScaleDown() {
    gameTime.setTimeScale(0.5f);
    assertEquals(5f, gameTime.getDeltaTime());
    assertEquals(10f, gameTime.getRawDeltaTime());
  }

  @Test
  void shouldScaleUp() {
    gameTime.setTimeScale(2f);
    assertEquals(20f, gameTime.getDeltaTime());
    assertEquals(10f, gameTime.getRawDeltaTime());
  }

  @Test
  void shouldScaleToZero() {
    gameTime.setTimeScale(0f);
    assertEquals(0f, gameTime.getDeltaTime());
    assertEquals(10f, gameTime.getRawDeltaTime());
  }
}