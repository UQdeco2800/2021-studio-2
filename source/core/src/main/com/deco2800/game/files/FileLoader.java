package com.deco2800.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for reading Java objects from Json files.
 *
 * <p>A generic method is provided already, but methods for reading specific classes can be added
 * for more control.
 */
public class FileLoader {
  private static final Logger logger = LoggerFactory.getLogger(FileLoader.class);
  static final Json json = new Json();

  /**
   * Read generic Java classes from a Json file. Properties in the Json file will override class
   * defaults.
   *
   * @param type class type
   * @param filename file to read from
   * @return instance of class, may be null
   */
  public static <T> T loadClass(Class<T> type, String filename) {
    FileHandle file = Gdx.files.internal(filename);
    T object = null;
    try {
      object = json.fromJson(type, file);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
    if (object == null) {
      logger.error(
        "Error creating {} class instance from {}", type.getSimpleName(), file.path());
    }
    return object;
  }
}
