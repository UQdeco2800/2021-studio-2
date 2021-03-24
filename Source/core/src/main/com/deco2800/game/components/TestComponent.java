package com.deco2800.game.components;

public class TestComponent extends Component {

  @Override
  public void create() {
    entity.getEvents().trigger("created");
    entity.getEvents().trigger("something", "Hello world");
  }
}
