package com.deco2800.game.files;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class FileLoaderTest {

    @Test
    void loadFromEmptyFile() {
        TestStats test =
                FileLoader.readClass(
                        TestStats.class, "test/files/empty.json");
        assertNotNull(test);
        assertEquals(1, test.stat1);
        assertEquals(2, test.stat2);
    }

    @Test
    void loadFromMissingFile() {
        TestStats test =
                FileLoader.readClass(
                        TestStats.class, "test/files/missing.json");
        assertNull(test);
    }

    @Test
    void loadFromInvalidFile() {
        TestStats test =
                FileLoader.readClass(
                        TestStats.class, "test/files/invalid.json");
        assertNull(test);
    }
}
