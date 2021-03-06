package com.deco2800.game.ui.textbox;

import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;
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

    /**
     * The substring of the message that will be displayed on the screen.
     */
    private String subMessage = "";

    /**
     * Whether the text box is open or not.
     */
    private boolean isOpen = false;

    /**
     * Used to determine if the line should be completed or the next message should be displayed.
     */
    private boolean skip = false;

    /**
     * The index of the message in the Dialogue object.
     */
    private int index = 0;

    /**
     * Index of the furthest character to be displayed of the substring of the message to be displayed.
     */
    private int subMessageIndex = 0;

    /**
     * Set of dialogue that can be randomised depending on previous NPC interactions.
     */
    private RandomDialogueSet randomDialogueSet;

    /**
     * Index of the sequence of ordered dialogue.
     */
    private int orderedDialogueIndex = 0;

    /**
     * Dialogue object that the text box is currently on.
     */
    private Dialogue dialogue;

    /**
     * Checks if the player has lifted a key to prevent the text box from skipping.
     */
    private boolean acceptingInput;

    /**
     * Boolean to check if the delayed recursive call should be performed.
     */
    private boolean generateCharacter = true;

    /**
     * Boolean to check if the main character text box should be displayed or the enemy.
     */
    private boolean mainCharacterShowing = true;

    /**
     * Checks if the set of dialogue is ordered or random.
     */
    private boolean showBars = false;

    public TextBox() {
        setRandomFirstEncounter(RandomDialogueSet.TUTORIAL);
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
        if (isOpen) {
            logger.debug("Closing text box");
            message = "";
            generateCharacter = false;
            hideBars();
            isOpen = false;
            this.index = 0;
        }
    }

    /**
     * Opens the text box.
     */
    public void setOpen() {
        logger.debug("Opening text box");
        isOpen = true;
        generateCharacter = true;
        showBars();
        nextMessage();
    }

    /**
     * Shows the black cutscene bars.
     */
    public void showBars() {
        this.showBars = true;
        ServiceLocator.getTimeSource().pauseEnemies();
    }

    /**
     * Hides the black cutscene bars.
     */
    public void hideBars() {
        this.showBars = false;
        ServiceLocator.getTimeSource().unpauseEnemies();
    }

    /**
     * Returns if the black cutscene bars should be shown or not.
     *
     * @return true if the bars should be shown, false if not
     */
    public boolean shouldShowBars() {
        return this.showBars;
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
            mainCharacterShowing = dialogue.isMainCharacter(index);
            setSubMessage();
            this.index++;
        } else {
            this.index = 0;
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
     *
     * @return true if text box can altered with input, false otherwise
     */
    public boolean isAcceptingInput() {
        return this.acceptingInput;
    }

    /**
     * When walking into an entity that triggers a text box to appear, repeatedly holding down a
     * key would instantly cycle through all of the messages. To prevent this, the player needs to
     * press start accepting keyboard input.
     */
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
    }

    /**
     * Sets the dialogue set to the set of dialogue determined by the NPC that was reached.
     *
     * @param dialogueSet the sequence of strings that are to be displayed in the text box
     */
    public void setRandomDefeatDialogueSet(RandomDialogueSet dialogueSet) {
        this.randomDialogueSet = dialogueSet;
        orderedDialogueIndex = 0;
        setDialogue(randomDialogueSet.getRandomPlayerDefeatedBefore());
        setOpen();
    }

    /**
     * Sets the dialogue set to the set of dialogue determined by the NPC that was reached.
     *
     * @param dialogueSet the sequence of strings that are to be displayed in the text box
     */
    public void setRandomBeatenDialogueSet(RandomDialogueSet dialogueSet) {
        this.randomDialogueSet = dialogueSet;
        orderedDialogueIndex = 0;
        setDialogue(randomDialogueSet.getRandomBossDefeatedBefore());
        setOpen();
    }

    /**
     * Sets the dialogue set to the set of dialogue determined by the NPC that was reached.
     *
     * @param dialogueSet the sequence of strings that are to be displayed in the text box
     */
    public void setRandomFirstEncounter(RandomDialogueSet dialogueSet) {
        this.randomDialogueSet = dialogueSet;
        orderedDialogueIndex = 0;
        setDialogue(randomDialogueSet.getRandomFirstEncounter());
        setOpen();
    }

    /**
     * This method will be called repeatedly to trigger an ordered sequence of dialogue.
     *
     * @param dialogueSet the set of dialogue that dialogue will be retrieved from
     */
    public void setOrderedDialogue(RandomDialogueSet dialogueSet) {
        // If the dialogue set is new, it will change the count back to 0 to retrieve the first message
        if (dialogueSet != this.randomDialogueSet) {
            this.randomDialogueSet = dialogueSet;
            orderedDialogueIndex = 0;
        }
        // Checks to see if the last message has been already read.
        if (randomDialogueSet.getOrderedDialogueSize() <= orderedDialogueIndex) {
            orderedDialogueIndex = 0;
        }
        setDialogue(randomDialogueSet.getOrderedDialogue(orderedDialogueIndex));
        orderedDialogueIndex++;
        setOpen();
    }

    public RandomDialogueSet getRandomDialogueSet() {
        return this.randomDialogueSet;
    }

    /**
     * Checks which character will be displaying the text,
     * returns true if the main character is talking, false otherwise
     *
     * @return boolean to determine who is talking
     */
    public boolean isMainCharacterShowing() {
        return mainCharacterShowing;
    }

    /**
     * Prevents the timer from recursively calling the method to generate a new character.
     */
    public void setNewCharactersOff() {
        this.generateCharacter = false;
    }
} 
