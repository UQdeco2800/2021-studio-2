package com.deco2800.game.ui.textbox;

/**
 * This enum will contain all of the dialogue to be displayed in text boxes and cutscenes.
 * Each Dialogue constant can be added by adding a new attribute under the current list.
 * Each constant is made up of an Array of Messages, each Message will contain a boolean to
 * determine if the main character is talking as well as the message.
 * To add a new set of dialogue, the template will look like:
 * DIALOGUE_NAME(new Message[]{
 * new Message(MAIN_CHARACTER?, MESSAGE1),
 * new Message(MAIN_CHARACTER?, MESSAGE2),
 * new Message(MAIN_CHARACTER?, MESSAGE3),
 * new Message(MAIN_CHARACTER?, MESSAGE4),
 * new Message(MAIN_CHARACTER?, MESSAGE5),
 * }
 * This will allow for the text box to alternate between the characters talking as well as
 * change in the text box graphically.
 */
public enum Dialogue {

    TUTORIAL_FIRST(new Message[]{
            new Message(false, "Hey psst," +
                    "\nit's nice to see a new face here."),
            new Message(true, "What! Where am I?"),
            new Message(false, "Keep it down! He'll hear you."),
            new Message(true, "Who'll hear me!?"),
            new Message(false, "Quiet! Or we're both done."),
            new Message(false, "Loki. What did you do to him anyway?"),
            new Message(true, "Nothing. I need to get out of here." +
                    "\nRagnarok hasn't come yet has it?" +
                    "\nI need to get out of here."),
            new Message(false, "Good luck with that." +
                    "\nWhy do you think I'm still here."),
            new Message(true, "Cause you're not me."),
            new Message(false, "You remind me a lot of my younger self" +
                    "\nI'll help you. Come over to me."),
    }),

    TUTORIAL_REPEAT(new Message[]{
            new Message(false, "Welcome back. Sounds like everything went well."),
            new Message(false, "Beaten too hard that you've forgotten" +
                    "\nhow to walk?")
    }),

    TUTORIAL_MOVE(new Message[]{
            new Message(false, "Your legs may not be like they" +
                    "\nused to be. Use the WASD to get around." +
                    "\nOnce you're ready, walk back over to me.")
    }),

    TUTORIAL_DASH(new Message[]{
            new Message(false, "To get around quickly, press SHIFT" +
                    "\nto sprint around and CAPS_LOCK to dash." +
                    "\nTake it easy though, you'll tire yourself out."),
            new Message(false, "Get back to me once you're ready" +
                    "\nto escape.")
    }),

    TUTORIAL_ATTACK(new Message[]{
            new Message(false, "Use SPACE BAR to swing the axe at" +
                    "\nthe gate. Careful though, this is the" +
                    "\nonly thing that won't attack back.")
    }),

    TUTORIAL_EXIT(new Message[]{
            new Message(false, "Good luck warrior, I hope to never" +
                    "\nsee you again.")
    }),

    LOKI_FIRST(new Message[]{
            new Message(false, "Ahhh, you've awakened warrior."),
            new Message(false, "Did you think that you with your hubris and" +
                    "\npride could just so easily join the ranks of" +
                    "\nthose in Valhalla?"),
            new Message(false, "Nay I have placed you here in this cell that" +
                    "\nyou might reflect on your deeds and learn" +
                    "\nsome humility."),
            new Message(false, "If you do however think that you can face off" +
                    "\nagainst me and those on the journey to me," +
                    "\nthen the door lies open."),
            new Message(false, "Know this though, it is a fools errand and" +
                    "\nwith every failure you only prove all the" +
                    "\nmore unworthy of Valhalla."),
            new Message(false, "Better to sit here and think on your deeds."),

    }),

    LOKI_DEFEAT_1(new Message[]{
            new Message(false, "I told you it was a fools errand." +
                    "\nStay here and give up on your ideas" +
                    "\nof making it to me."),
    }),

    LOKI_DEFEAT_2(new Message[]{
            new Message(false, "No matter how many times you try," +
                    "\nyour fate will always be the same."),
    }),

    LOKI_DEFEAT_3(new Message[]{
            new Message(false, "Come now, have you not embarrassed" +
                    "\nyourself enough? Accept your fate and stay."),
    }),

    LOKI_DEFEAT_4(new Message[]{
            new Message(false, "Still trying? How quaint."),
    }),

    LOKI_DEFEAT_5(new Message[]{
            new Message(false, "Good attempt, are you really going" +
                    "\n to try again?"),
    }),

    GARMR_FIRST(new Message[]{
            new Message(true, "What is this foul beast that blocks my way?"),
            new Message(true, "I shall slay you just as easily as" +
                    "\nI would any other wild animal."),
    }),

    GARMR_DEFEAT_1(new Message[]{
            new Message(true, "This time you shall not even manage to" +
                    "\nscratch me, let alone bite."),
    }),

    GARMR_DEFEAT_2(new Message[]{
            new Message(true, "You can only remain guard for so long," +
                    "\neventually I will make it past."),
            new Message(false, "Grrrrrr")
    }),

    GARMR_BEATEN_1(new Message[]{
            new Message(true, "Ah, still obediently guarding the entrance?" +
                    "\nCome, I shall cast you aside just as" +
                    "\nquickly as the last time."),
    }),

    GARMR_BEATEN_2(new Message[]{
            new Message(true, "Your bark is definitely worst than your bite."),
            new Message(false, "Grrrrrr")
    }),

    GARMR_BEATEN_3(new Message[]{
            new Message(true, "Fierce only in looks, why do you yet still" +
                    "\nstand guard? I shall remove you" +
                    "\nfrom your post yet again."),
            new Message(false, "Grrrrrr")
    }),

    GARMR_BEATEN_4(new Message[]{
            new Message(true, "Hopefully it will be as fun as last time mutt."),
    }),

    TEST_1(new Message[]{
            new Message(true, "Test 1 Message 1"),
            new Message(false, "Test 1 Message 2"),
            new Message(true, "Test 1 Message 3"),
            new Message(true, "Test 1 Message 4")
    }),

    TEST_2(new Message[]{
            new Message(true, "Test 2 Message 1"),
            new Message(false, "Test 2 Message 2"),
            new Message(false, "Test 2 Message 3")
    });

    private Message[] messages;

    /**
     * Constructor to create the dialogue enum object, used to take and store the array list passed in.
     *
     * @param messages an ArrayList of Message representing each new line of text to be displayed.
     */
    Dialogue(Message[] messages) {
        this.messages = messages;
    }

    /**
     * Returns the line of the dialogue at the index that has been passed in as an argument.
     *
     * @param index the index of the line to be retrieved
     * @return String at the index
     */
    public String getMessage(int index) {
        return messages[index].getMessage();
    }

    /**
     * Returns if the main character is speaking at the message index provided.
     *
     * @param index the index of the message to be retrieved
     * @return boolean that is true if the main character is speaking, false otherwise
     */
    public boolean isMainCharacter(int index) {
        return messages[index].isMainCharacter();
    }

    /**
     * Returns the number of different lines in the dialogue.
     *
     * @return returns the number of messages in the dialogue sequence
     */
    public int size() {
        return messages.length;
    }
}

/**
 * Class used to store a message in the form of a string as well as who the speaker will be.
 * This will be used to determine whether a normal text box should be used or an enemy text box.
 */
class Message {

    /**
     * If the main character is talking mainCharacter is true, else it is false.
     */
    private boolean mainCharacter;

    /**
     * The message that will be displayed by the character.
     */
    private String message;

    public Message(boolean mainCharacter, String message) {
        this.mainCharacter = mainCharacter;
        this.message = message;
    }

    /**
     * Checks if the character who is talking is the main character.
     *
     * @return boolean to show if the main character is talking
     */
    public boolean isMainCharacter() {
        return mainCharacter;
    }

    /**
     * Returns the message that will be displayed in the text box.
     *
     * @return String containing the message to be displayed
     */
    public String getMessage() {
        return message;
    }

}
