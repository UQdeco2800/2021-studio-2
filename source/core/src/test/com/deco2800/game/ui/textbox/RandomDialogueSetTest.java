package com.deco2800.game.ui.textbox;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RandomDialogueSetTest {

    @Test
    void getRandomFirstEncounterTest() {
        assertEquals(Dialogue.TEST_1, RandomDialogueSet.TEST.getRandomFirstEncounter());
    }

    @Test
    void getOrderedDialogueTest() {
        assertEquals(Dialogue.TEST_1, RandomDialogueSet.TEST.getOrderedDialogue(0));
    }

    @Test
    void getOrderedDialogueTest2() {
        assertEquals(Dialogue.TEST_2, RandomDialogueSet.TEST.getOrderedDialogue(1));
    }

    @Test
    void getOrderedDialogueSizeTest() {
        assertEquals(2, RandomDialogueSet.TEST.getOrderedDialogueSize());
    }

    @Test
    void getRandomBossDefeatedBeforeTest() {
        Dialogue random = RandomDialogueSet.TEST.getRandomBossDefeatedBefore();
        assertTrue(random == Dialogue.TEST_1);
    }

    @Test
    void getRandomPlayerDefeatedBeforeTest() {
        Dialogue random = RandomDialogueSet.TEST.getRandomPlayerDefeatedBefore();
        assertTrue(random == Dialogue.TEST_1 || random == Dialogue.TEST_2);
    }
}
