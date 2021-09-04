package com.deco2800.game.ui.textbox;

/**
 * To create a diverse storyline, interactions between the player and the characters throughout the map
 * will change on past interactions, mainly if they have defeated you or you have been defeated by them before.
 * To allow for this, an enum has been created to store all of the sets of dialogue that may occur.
 *
 * For each character, there will be dialogue based on if it is the first time the player has encountered the
 * NPC before and what happened in the last interaction. There is currently 3 sets that may occur,
 * the first encounter, the player has defeated the NPC or the NPC has defeated the player.
 */
public enum RandomDialogueSet {

    LOKI_OPENING(
            new Dialogue[] {
                    Dialogue.LOKI_FIRST
            },
            new Dialogue[] {
                    Dialogue.LOKI_DEFEAT_1,
                    Dialogue.LOKI_DEFEAT_2,
                    Dialogue.LOKI_DEFEAT_3,
                    Dialogue.LOKI_DEFEAT_4,
                    Dialogue.LOKI_DEFEAT_5,
            },
            new Dialogue[] {
                    Dialogue.LOKI_DEFEAT_1,
                    Dialogue.LOKI_DEFEAT_2,
                    Dialogue.LOKI_DEFEAT_3,
                    Dialogue.LOKI_DEFEAT_4,
                    Dialogue.LOKI_DEFEAT_5,
            }
    ),

    GARMR(
            new Dialogue[] {
                    Dialogue.GARMR_FIRST
            },
            new Dialogue[] {
                    Dialogue.GARMR_BEATEN_1,
                    Dialogue.GARMR_BEATEN_2,
                    Dialogue.GARMR_BEATEN_3,
                    Dialogue.GARMR_BEATEN_4,
            },
            new Dialogue[] {
                    Dialogue.GARMR_DEFEAT_1,
                    Dialogue.GARMR_DEFEAT_2,
            }
    );

    RandomDialogueSet(Dialogue[] firstEncounter, Dialogue[] bossDefeatedBefore,
                      Dialogue[] playerDefeatedBefore) {
        this.firstEncounter = firstEncounter;
        this.defeatedBossBefore = bossDefeatedBefore;
        this.defeatedPlayerBefore = playerDefeatedBefore;
    }

    /** Set of dialogue to be used if the player has not been used before. */
    private Dialogue[] firstEncounter;

    /** Set of dialogue to be used if the player has defeated by the NPC before. */
    private Dialogue[] defeatedBossBefore;

    /** Set of dialogue to be used if the player has been defeated by the NPC before. */
    private Dialogue[] defeatedPlayerBefore;

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
        int index = ((int)System.currentTimeMillis()) % size;
        return index > 0? index : index * -1;
    }
}
