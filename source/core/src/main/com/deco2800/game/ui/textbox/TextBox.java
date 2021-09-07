package com.deco2800.game.ui.textbox;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class TextBox extends Component {
    private static final Logger logger = LoggerFactory.getLogger(TextBox.class);

    /**
     * The message that the text box will display, based on the dialogue object stored
     * and which message it is on.
     */
    private String message = "";

    /** The substring of the message that will be displayed on the screen. */
    private String subMessage = "";

    /** Whether the text box is open or not. */
    private boolean isOpen = false;

    /** Used to determine if the line should be completed or the next message should be displayed. */
    private boolean skip = false;

    /** The index of the message in the Dialogue object. */
    private int index = 0;

    /** Index of the furthest character to be displayed of the substring of the message to be displayed. */
    private int subMessageIndex = 0;

    /** Dialogue object that the text box is currently on. */
    private Dialogue dialogue;

    /** Checks if the player has lifted a key to prevent the text box from skipping. */
    private boolean acceptingInput;

    /** Boolean to check if the delayed recursive call should be performed. */
    private boolean generateCharacter = true;

    public TextBox() {
        setDialogue(Dialogue.OPENING);
        acceptInput();
    }

    /**
     * @return message to be displayed on the text box
     */
    public String getSubMessage() {
        return subMessage;
    }

    /**
     * @return entire message to be displayed on the text box
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return text box is open
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Opens the text box.
     */
    public void setOpen() {
        logger.debug("Opening text box");
        isOpen = true;
        this.nextMessage();
    }

    /**
     * Toggles between the text box being open and closed.
     */
    public void toggleIsOpen() {
        if (isOpen) {
            this.setClosed();
        } else {
            this.setOpen();
        }
    }

    /**
     * Closes the terminal and clears the stored message.
     */
    public void setClosed() {
        logger.debug("Closing text box");
        isOpen = false;
        message = "";
        this.index = 0;
    }

    /**
     * Handles the escape key being pressed to close to text box.
     */
    public void handleEscape() {
        logger.debug("Handling escape key press");
        setClosed();
    }

    /**
     * If there is a next message, it start to be displayed in the text box, if there is no more messages
     * then the text box will be closed.
     */
    public void nextMessage() {
        skip = false;
        subMessageIndex = 0;
        if (this.index < dialogue.size()) {
            message = dialogue.getMessage(index);
            setSubMessage();
            this.index++;
        } else {
            this.setClosed();
        }
    }

    /**
     * The sub message displayed will be updated at a delay of 30ms each character. If the player
     * has pressed a button while it is appearing, then the entire line will ap pear.
     */
    public void setSubMessage() {
        if (skip || message.length() - 1 == subMessageIndex) {
            subMessageIndex = message.length() - 1;
            skip = true;
            subMessage = message;
        } else if (isOpen) {
            subMessage = message.substring(0, subMessageIndex);
            subMessageIndex++;
            if (generateCharacter) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        setSubMessage();
                        timer.cancel();
                    }
                }, 30);
            }
        }
    }

    /**
     * When the player provides an input while the message is appearing, it sets skip to true
     * which will display the entire message. If skip is true while it is pressed, the next
     * messaged will be displayed.
     */
    public void setSkip() {
        if (skip) {
            nextMessage();
        } else {
            skip = true;
        }
    }

    /**
     * Checks if the text box is accepting input from the player.
     * @return true if text box can altered with input, false otherwise
     */
    public boolean isAcceptingInput() {
        return this.acceptingInput;
    }

    public void acceptInput() {
        this.acceptingInput = true;
    }

    /**
     * Sets the dialogue to a certain message depending on cutscene that is to be displayed.
     *
     * @param dialogue the sequence of strings that are to be displayed in the text box
     */
    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
        this.index = 0;
        this.subMessageIndex = 0;
        this.acceptingInput = false;
        setOpen();
    }

    public void setNewCharactersOff() {
        this.generateCharacter = false;
    }
} 
