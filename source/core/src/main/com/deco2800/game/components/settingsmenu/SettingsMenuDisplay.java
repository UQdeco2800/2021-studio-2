package com.deco2800.game.components.settingsmenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.GdxGame.ScreenType;
import com.deco2800.game.UI.UIComponent;
import com.deco2800.game.services.ServiceLocator;

public class SettingsMenuDisplay extends UIComponent {
  private final GdxGame game;

  public SettingsMenuDisplay(GdxGame game) {
    super();
    this.game = game;
  }


  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    Label title = new Label("Settings", skin);
    Table settingsTable = makeSettingsTable();
    Table menuBtns = makeMenuBtns();

    Table table = new Table();
    table.setFillParent(true);

    table.add(title).expandX().top().padTop(20f);

    table.row().padTop(30f);
    table.add(settingsTable).expandX().expandY();

    table.row();
    table.add(menuBtns).fillX();

    stage.addActor(table);
  }

  private Table makeSettingsTable() {
    // Create components
    Label fpsLabel = new Label("FPS:", skin);
    TextField fpsText = new TextField("", skin);

    Label fullScreenLabel = new Label("Fullscreen:", skin);
    CheckBox fullScreenCheck = new CheckBox("", skin);

    Label uiScaleLabel = new Label("UI Scale:", skin);
    Slider uiScaleSlider = new Slider(0.2f, 2f, 0.1f, false, skin);
    Label uiScaleValue = new Label("1x", skin);

    // Position Components on table
    Table table = new Table();

    table.add(fpsLabel).right().padRight(15f);
    table.add(fpsText).width(100).left();

    table.row().padTop(10f);
    table.add(fullScreenLabel).right().padRight(15f);
    table.add(fullScreenCheck).left();

    table.row().padTop(10f);
    table.add(uiScaleLabel).right().padRight(15f);
    table.add(uiScaleSlider).width(100).left();
    table.add(uiScaleValue).left().padLeft(5f);

    // Events on inputs
    uiScaleSlider.addListener((Event event) -> {
      float value = uiScaleSlider.getValue();
      uiScaleValue.setText(String.format("%.2fx", value));
      return true;
    });

    return table;
  }

  private Table makeMenuBtns() {
    TextButton exitBtn = new TextButton("Exit", skin);
    TextButton applyBtn = new TextButton("Apply", skin);

    exitBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent changeEvent, Actor actor) {
        exitMenu();
      }
    });

    applyBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent changeEvent, Actor actor) {
        applyChanges();
      }
    });

    Table table = new Table();
    table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
    table.add(applyBtn).expandX().right().pad(0f, 0f, 15f, 15f);
    return table;
  }

  private void applyChanges() {
    // TODO: Save settings
  }

  private void exitMenu() {
    game.setScreen(ScreenType.MainMenu);
  }

  @Override
  protected void draw(SpriteBatch batch) {
    stage.getRoot().draw(batch, 1f);
  }

  @Override
  public void update() {
    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }
}
