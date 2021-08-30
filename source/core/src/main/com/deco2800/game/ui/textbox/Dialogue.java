package com.deco2800.game.ui.textbox;

public enum Dialogue {

    /**
     * This is currently being used as a placeholder but the elements in the enum will be used to display
     * all of the text that is to be used in a particular sequence, in this case, the opening.
     */
    OPENING(new String[]{"You're running out of time... \nQuickly! Press any key once " +
            "you're ready \nto see the next " + "message.",
            "Your WASD keys will help guide you warrior \ninto Valhalla",
            "Press ESCAPE if you're urgent to \ncarry on with your journey to Valhalla.", "Good Luck"}),

    THE_ROCK(new String[]{"Ragnarok is nearing warrior.", "You mustn't hesitate nor die an unworthy death.",
            "If you want a seat at Valhalla...", "Earn it"}),

    TEST(new String[]{"Message 1", "Message 2", "Message 3"});

    /** An array list used to store the strings in a dialogue sequence. */
    private String[] text;

    /**
     * Constructor to create the dialogue enum object, used to take and store the array list passed in.
     *
     * @param text an ArrayList of Strings representing each new line of text to be displayed.
     */
    Dialogue(String[] text){
        this.text = text;
    }

    /**
     * Returns the line of the dialogue at the index that has been passed in as an argument.
     *
     * @param index the index of the line to be retrieved
     * @return String at the index
     */
    public String getMessage(int index) {
        return text[index];
    }

    /**
     * Returns the number of different lines in the dialogue.
     * @return
     */
    public int size() {
        return text.length;
    }
}
