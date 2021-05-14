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
  public static <T> T readClass(Class<T> type, String filename) {
    return readClass(type, filename, Location.Internal);
  }

  public static <T> T readClass(Class<T> type, String filename, Location location) {
    FileHandle file = getFileHandle(filename, location);
    T object;
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

  public static void writeClass(Object object, String filename) {
    writeClass(object, filename, Location.External);
  }

  public static void writeClass(Object object, String filename, Location location) {
    FileHandle file = getFileHandle(filename, location);
    assert file != null;
    file.writeString(json.prettyPrint(object), false);
  }

  private static FileHandle getFileHandle(String filename, Location location) {
    switch (location) {
      case Classpath:
        return Gdx.files.classpath(filename);
      case Internal:
        return Gdx.files.internal(filename);
      case Local:
        return Gdx.files.local(filename);
      case External:
        return Gdx.files.external(filename);
      case Absolute:
        return Gdx.files.absolute(filename);
      default:
        return null;
    }
  }

  public enum Location {
    Classpath, Internal, Local, External, Absolute
  }
}
