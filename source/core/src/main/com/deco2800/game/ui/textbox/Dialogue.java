package com.deco2800.game.ui.textbox;

import java.util.ArrayList;
import java.util.Arrays;

public enum Dialogue {

    /**
     * This is currently being used as a placeholder but the elements in the enum will be used to display
     * all of the text that is to be used in a particular sequence, in this case, the opening.
     */
    OPENING(new String[]{"Hello, the dialogue has been created in an enum\n" +
                    "but could also use a file to store lines and \nread them. " +
                    "This allows for all the dialogue to \nbe kept in the same place.",
            "At the moment, the text can be hard to see\n" +
                    "so it might be useful to add a box around it.\n" +
                    "We could even have the character when he is talking.",
            "The size of the font could be changed but\n" +
                    "changing the line shifts should be done as well.",
            "It is quite easy to add to the dialogue,\n" +
                    "It is all done in one big array in sequence.",
            "The dialogue should be triggered on events\n" +
                    "such as entering a new area or defeating a boss.\n" +
                    "All we would need to do is set the UI component\n" +
                    "to the new Dialogue content and display the text",
            "There is definitely more to change so let me know."}),

    HELLO(new String[]{"Welcome, hello! This is the second value in the enum and will be used\n" +
            "mostly to test things, I'm not too sure if the text should be \nadded by hard coding the strings" +
            "or should we use a text file that the game will read off.", "Good Luck."});

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
