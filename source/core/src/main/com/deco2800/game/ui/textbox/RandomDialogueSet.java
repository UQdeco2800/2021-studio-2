package com.deco2800.game.ui.textbox;

/**
 * To create a diverse storyline, interactions between the player and the characters throughout the map
 * will change on past interactions, mainly if they have defeated you or you have been defeated by them before.
 * To allow for this, an enum has been created to store all of the sets of dialogue that may occur.
 * <p>
 * For each character, there will be dialogue based on if it is the first time the player has encountered the
 * NPC before and what happened in the last interaction. There is currently 3 sets that may occur,
 * the first encounter, the player has defeated the NPC or the NPC has defeated the player.
 */
public enum RandomDialogueSet {

    TUTORIAL(
            new Dialogue[]{
                    Dialogue.TUTORIAL_FIRST
            },
            new Dialogue[]{
                    Dialogue.TUTORIAL_FIRST
            },
            new Dialogue[]{
                    Dialogue.TUTORIAL_FIRST
            },
            new Dialogue[]{
                    Dialogue.TUTORIAL_MOVE,
                    Dialogue.TUTORIAL_DASH,
                    Dialogue.TUTORIAL_ATTACK,
                    Dialogue.TUTORIAL_ENEMIES,
                    Dialogue.TUTORIAL_TRAPS,
                    Dialogue.TUTORIAL_EXIT
            }
    ),

    ELF_INTRODUCTION(
            new Dialogue[]{
                    Dialogue.ELF_INTRODUCTION_FIRST
            },
            new Dialogue[]{
                    Dialogue.ELF_INTRODUCTION_VICTORY_1,
                    Dialogue.ELF_INTRODUCTION_VICTORY_2,
            },
            new Dialogue[]{
                    Dialogue.ELF_INTRODUCTION_DEFEATED_1,
                    Dialogue.ELF_INTRODUCTION_DEFEATED_2,
            },
            null
    ),

    ELF_ENCOUNTER(
            new Dialogue[]{
                    Dialogue.ELF_ENCOUNTER_FIRST
            },
            new Dialogue[]{
                    Dialogue.ELF_ENCOUNTER_VICTORY_1,
                    Dialogue.ELF_ENCOUNTER_VICTORY_2,
            },
            new Dialogue[]{
                    Dialogue.ELF_ENCOUNTER_DEFEATED_1,
                    Dialogue.ELF_ENCOUNTER_DEFEATED_2,
            },
            null
    ),

    LOKI_INTRODUCTION(
            new Dialogue[]{
                    Dialogue.LOKI_INTRODUCTION_FIRST
            },
            new Dialogue[]{
                    Dialogue.LOKI_INTRODUCTION_VICTORY_1,
                    Dialogue.LOKI_INTRODUCTION_VICTORY_2,
                    Dialogue.LOKI_INTRODUCTION_VICTORY_3,
            },
            new Dialogue[]{
                    Dialogue.LOKI_INTRODUCTION_DEFEATED_1,
                    Dialogue.LOKI_INTRODUCTION_DEFEATED_2,
                    Dialogue.LOKI_INTRODUCTION_DEFEATED_3,
                    Dialogue.LOKI_INTRODUCTION_DEFEATED_4,
                    Dialogue.LOKI_INTRODUCTION_DEFEATED_5,
            },
            null
    ),

    LOKI_ENCOUNTER(
            new Dialogue[]{
                    Dialogue.LOKI_ENCOUNTER_FIRST
            },
            new Dialogue[]{
                    Dialogue.LOKI_ENCOUNTER_VICTORY_1,
                    Dialogue.LOKI_ENCOUNTER_VICTORY_2,
            },
            new Dialogue[]{
                    Dialogue.LOKI_ENCOUNTER_DEFEATED_1,
                    Dialogue.LOKI_ENCOUNTER_DEFEATED_2,
            },
            null
    ),

    THOR_INTRODUCTION(
            new Dialogue[]{
                    Dialogue.THOR_INTRODUCTION_FIRST
            },
            new Dialogue[]{
                    Dialogue.THOR_INTRODUCTION_VICTORY_1,
                    Dialogue.THOR_INTRODUCTION_VICTORY_2,
                    Dialogue.THOR_INTRODUCTION_VICTORY_3,

            },
            new Dialogue[]{
                    Dialogue.THOR_INTRODUCTION_DEFEAT_1,
                    Dialogue.THOR_INTRODUCTION_DEFEAT_2,
                    Dialogue.THOR_INTRODUCTION_DEFEAT_3,
            },
            null
    ),

    THOR_ENCOUNTER(
            new Dialogue[]{
                    Dialogue.THOR_ENCOUNTER_FIRST
            },
            new Dialogue[]{
                    Dialogue.THOR_ENCOUNTER_VICTORY_1,
                    Dialogue.THOR_ENCOUNTER_VICTORY_2,
                    Dialogue.THOR_ENCOUNTER_VICTORY_3,
                    Dialogue.THOR_ENCOUNTER_VICTORY_4,
                    Dialogue.THOR_ENCOUNTER_VICTORY_5,

            },
            new Dialogue[]{
                    Dialogue.THOR_ENCOUNTER_DEFEAT_1,
                    Dialogue.THOR_ENCOUNTER_DEFEAT_2,
                    Dialogue.THOR_ENCOUNTER_DEFEAT_3,
            },
            null
    ),

    ODIN_INTRODUCTION(
            new Dialogue[]{
                    Dialogue.ODIN_INTRODUCTION_FIRST
            },
            new Dialogue[]{
                    Dialogue.ODIN_INTRODUCTION_VICTORY_1,
                    Dialogue.ODIN_INTRODUCTION_VICTORY_2,
                    Dialogue.ODIN_INTRODUCTION_VICTORY_3,
            },
            new Dialogue[]{
                    Dialogue.ODIN_INTRODUCTION_DEFEAT_1,
                    Dialogue.ODIN_INTRODUCTION_DEFEAT_2,
                    Dialogue.ODIN_INTRODUCTION_DEFEAT_3,
            },
            null
    ),

    ODIN_ENCOUNTER(
            new Dialogue[]{
                    Dialogue.ODIN_ENCOUNTER_FIRST
            },
            new Dialogue[]{
                    Dialogue.ODIN_ENCOUNTER_VICTORY_1,
                    Dialogue.ODIN_ENCOUNTER_VICTORY_2,
                    Dialogue.ODIN_ENCOUNTER_VICTORY_3,
            },
            new Dialogue[]{
                    Dialogue.ODIN_ENCOUNTER_DEFEAT_1,
                    Dialogue.ODIN_ENCOUNTER_DEFEAT_2,
                    Dialogue.ODIN_ENCOUNTER_DEFEAT_3,
            },
            null
    ),

    TEST(
            new Dialogue[]{
                    Dialogue.TEST_1
            },
            new Dialogue[]{
                    Dialogue.TEST_1
            },
            new Dialogue[]{
                    Dialogue.TEST_1,
                    Dialogue.TEST_2
            },
            new Dialogue[]{
                    Dialogue.TEST_1,
                    Dialogue.TEST_2
            }
    );

    RandomDialogueSet(Dialogue[] firstEncounter, Dialogue[] bossDefeatedBefore,
                      Dialogue[] playerDefeatedBefore, Dialogue[] orderedDialogue) {
        this.firstEncounter = firstEncounter;
        this.defeatedBossBefore = bossDefeatedBefore;
        this.defeatedPlayerBefore = playerDefeatedBefore;
        this.orderedDialogue = orderedDialogue;
    }

    /**
     * Set of dialogue to be used if the player has not been used before.
     */
    private final Dialogue[] firstEncounter;

    /**
     * Set of dialogue to be used if the player has defeated by the NPC before.
     */
    private final Dialogue[] defeatedBossBefore;

    /**
     * Set of dialogue to be used if the player has been defeated by the NPC before.
     */
    private final Dialogue[] defeatedPlayerBefore;

    /**
     * Sets of dialogue that are within the same area but are not displayed right after each other.
     */
    private final Dialogue[] orderedDialogue;

    /**
     * Gets a random Dialogue object from the set for first encounters.
     *
     * @return Dialogue enum constant containing dialogue sequences
     */
    public Dialogue getRandomFirstEncounter() {
        return firstEncounter[randomIndex(firstEncounter.length)];
    }

    /**
     * Gets a random Dialogue object from the set where the player has defeated the NPC before.
     *
     * @return Dialogue enum constant containing dialogue sequences
     */
    public Dialogue getRandomBossDefeatedBefore() {
        return defeatedBossBefore[randomIndex(defeatedBossBefore.length)];
    }

    /**
     * Gets a random Dialogue object from the set where the player has been defeated by the NPC before.
     *
     * @return Dialogue enum constant containing dialogue sequences
     */
    public Dialogue getRandomPlayerDefeatedBefore() {
        return defeatedPlayerBefore[randomIndex(defeatedPlayerBefore.length)];
    }

    /**
     * Random index generator to get a random set of text.
     *
     * @param size the number of possible dialogue sequences
     * @return returns the random index
     */
    private static int randomIndex(int size) {
        int index = ((int) System.currentTimeMillis()) % size;
        return index > 0 ? index : index * -1;
    }

    /**
     * Gets the set of dialogue at the index specified, used for sets of dialogue
     * that will be used sequentially.
     *
     * @param index the positional order of where the dialogue will come
     * @return returns the dialogue in the ordered array
     */
    public Dialogue getOrderedDialogue(int index) {
        return orderedDialogue[index];
    }

    /**
     * Returns the size of the ordered dialogue so all of the elements will be accounted for.
     *
     * @return integer size of the array
     */
    public int getOrderedDialogueSize() {
        return orderedDialogue.length;
    }
}
