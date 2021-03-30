package com.deco2800.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class FileLoaderTest {
    private static final Logger logger = LoggerFactory.getLogger(FileLoaderTest.class);

    @Test
    void loadFromValidFile() {
        FileHandle file = Gdx.files.internal("test/files/valid.json");
        logger.info(file.readString());
        TestStats test = FileLoader.loadClassFromJson(TestStats.class, file);
        assertNotNull(test);
        assertEquals(test.stat1, 3);
        assertEquals(test.stat2, 4);
    }

    @Test
    void loadFromEmptyFile() {
        TestStats test =
                FileLoader.loadClassFromJson(
                  TestStats.class, Gdx.files.internal("test/files/empty.json"));
        assertNotNull(test);
        assertEquals(test.stat1, 1);
        assertEquals(test.stat2, 2);
    }

    @Test
    void loadFromMissingFile() {
        TestStats test =
                FileLoader.loadClassFromJson(
                  TestStats.class, Gdx.files.internal("test/files/missing.json"));
        assertNull(test);
    }

    @Test
    void loadFromInvalidFile() {
        TestStats test =
                FileLoader.loadClassFromJson(
                  TestStats.class, Gdx.files.internal("test/files/invalid.json"));
        assertNull(test);
    }
}
