package com.deco2800.game.UI;

import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.Renderable;

public abstract class UIComponent extends RenderComponent implements Renderable {
  private static final int UI_LAYER = 2;

  @Override
  public int compareTo(Renderable o) {
    return Float.compare(getZIndex(), o.getZIndex());
  }

  @Override
  public int getLayer() {
    return UI_LAYER;
  }

}
