package com.deco2800.game.ui.textbox;

/**
 * This enum will contain all of the dialogue to be displayed in text boxes and cutscenes.
 *
 * Each Dialogue constant can be added by adding a new attribute under the current list.
 * Each constant is made up of an Array of Messages, each Message will contain a boolean to
 * determine if the main character is talking as well as the message.
 *
 * To add a new set of dialogue, the template will look like
 *
 * DIALOGUE_NAME(new Message[]{
 *     new Message(MAIN_CHARACTER?, MESSAGE1),
 *     new Message(MAIN_CHARACTER?, MESSAGE2),
 *     new Message(MAIN_CHARACTER?, MESSAGE3),
 *     new Message(MAIN_CHARACTER?, MESSAGE4),
 *     new Message(MAIN_CHARACTER?, MESSAGE5),
 * }
 *
 * This will allow for the text box to alternate between the characters talking as well as
 * change in the text box graphically.
 *
 */
public enum Dialogue {

    OPENING(new Message[]{
            new Message(false, "Welcome warrior... \nRagnarok is nearly here, finally our escape." ),
            new Message(true, "Where am I?"),
            new Message(true, "Let me out or I'll make you."),
            new Message(false, "I'm stuck here with you."),
            new Message(false, "Please help me enter Valhalla \nI can teach you how to get out."),
    }),

    THE_ROCK(new Message[]{
            new Message(false, "Welcome warrior... \nRagnarok is nearly here, finally our escape." ),
            new Message(false, "Where am I?"),
            new Message(false, "Let me out or I'll make you."),
            new Message(false, "I'm stuck here with you."),
            new Message(false, "Please help me enter Valhalla \nI can teach you how to get out."),
    }),

    TESTER(new Message[]{
            new Message(false, "Hello"),
            new Message(true, "Hello"),
            new Message(true, "Hello"),
            new Message(false, "Hello"),
            new Message(false, "Hello"),
            new Message(false, "Hello"),
            new Message(false, "Hello"),
            new Message(false, "Hello"),

    });

    private Message[] messages;

    /**
     * Constructor to create the dialogue enum object, used to take and store the array list passed in.
     *
     * @param messages an ArrayList of Message representing each new line of text to be displayed.
     */
    Dialogue(Message[] messages){
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
     * @return
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

    /** If the main character is talking mainCharacter is true, else it is false. */
    private boolean mainCharacter;

    /** The message that will be displayed by the character. */
    private String message;

    public Message(boolean mainCharacter, String message) {
        this.mainCharacter = mainCharacter;
        this.message = message;
    }

    /**
     * Checks if the character who is talking is the main character.
     * @return boolean to show if the main character is talking
     */
    public boolean isMainCharacter() {
        return mainCharacter;
    }

    /**
     * Returns the message that will be displayed in the text box.
     * @return String containing the message to be displayed
     */
    public String getMessage() {
        return message;
    }

}
