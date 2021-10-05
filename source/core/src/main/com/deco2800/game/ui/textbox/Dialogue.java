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
                    "\nonly thing that won't attack back."),
            new Message(false, "User Q or Right Click to activate your" +
                    "\nweapons power, each weapon has one" +
                    "\nand they're all different. You should test" +
                    "\nit out before heading off into battle.")
    }),

    TUTORIAL_ENEMIES(new Message[]{
            new Message(false, "Be careful of some of the enemies here." +
                    "\nI've seen them all with my very own eyes."),
            new Message(false, "The warriors will chase you down till their" +
                    "\ndeaths while the archers snipe" +
                    "\nyou from the shadows.")
    }),

    TUTORIAL_TRAPS(new Message[]{
            new Message(false, "The place is littered with traps," +
                    "\nbe careful where you're stepping.")
    }),

    TUTORIAL_EXIT(new Message[]{
            new Message(false, "Good luck warrior, I hope to never" +
                    "\nsee you again.")
    }),

    ELF_INTRODUCTION_FIRST(new Message[]{
            new Message(false, "Welcome!, can we all welcome our" +
                    "\nnew prisoner everyone."),
            new Message(true, "I won't be staying here very long."),
            new Message(false, "I'm not too sure about that." +
                    "\nThe Gods have locked you here for a" +
                    "\nreason and I intend to keep it that way."),
            new Message(false, "Ragnarok is nearly here and you'll" +
                    "\nbe trapped here when it arrives."),
            new Message(true, "We'll see about that."),
    }),

    ELF_INTRODUCTION_VICTORY_1(new Message[]{
            new Message(false, "I see you that you're back. This time" +
                    "\nyou're staying in this prison."),
            new Message(true, "Didn't work out very well last time."),
            new Message(false, "I underestimated you last time but my" +
                    "\nsoldiers and I aren't holding back this time."),
    }),

    ELF_INTRODUCTION_VICTORY_2(new Message[]{
            new Message(false, "I see you never reached your destination." +
                    "\nI'm disappointed I didn't get to kill you."),
            new Message(true, "I'm going to keep it that way."),
            new Message(false, "Prepare yourself for a different ending."),
    }),

    ELF_INTRODUCTION_DEFEATED_1(new Message[]{
            new Message(false, "Still trying? Can't you rot" +
                    "\nwith the other prisoners"),
            new Message(true, "I'm not going to stop until I reach Valhalla."),
            new Message(false, "A lifetime of misery I suppose."),
    }),

    ELF_INTRODUCTION_DEFEATED_2(new Message[]{
            new Message(false, "Back for more? You wouldn't even come" +
                    "\nclose to Valhalla."),
            new Message(true, "I've seen your tricks. This time I'm prepared."),
            new Message(false, "Even my elves could kill you. I'd be" +
                    "\nsurprised to meet you again."),
    }),

    ELF_ENCOUNTER_FIRST(new Message[]{
            new Message(false, "I'm surprised to see you here." +
                    "\nMy elves have disappointed me but it's" +
                    "\nnow time I had some fun."),
            new Message(true, "Can't wait to kill you like I did" +
                    "\nwith the rest of your guards."),
    }),

    ELF_ENCOUNTER_VICTORY_1(new Message[]{
            new Message(false, "I won't let you get away this time." +
                    "\nI'll make sure you stay here forever."),
            new Message(true, "Didn't work very well last time."),
    }),

    ELF_ENCOUNTER_VICTORY_2(new Message[]{
            new Message(false, "Looks like the curse worked." +
                    "\nGood luck getting through all of us."),
            new Message(true, "I've already killed you." +
                    "\nThe others will have the same fate."),
    }),

    ELF_ENCOUNTER_DEFEATED_1(new Message[]{
            new Message(false, "It's a surprise to see you here"),
            new Message(true, "It'll be a surprise when I kill you this time."),
            new Message(false, "You amuse me warrior.")
    }),

    ELF_ENCOUNTER_DEFEATED_2(new Message[]{
            new Message(false, "Nice to see you again." +
                    "\nI had a blast last time. How about you?"),
            new Message(true, "This time will be different."),
    }),

    LOKI_INTRODUCTION_FIRST(new Message[]{
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

    LOKI_INTRODUCTION_VICTORY_1(new Message[]{
            new Message(false, "I underestimated you but now I" +
                    "\ncan get my revenge."),
    }),

    LOKI_INTRODUCTION_VICTORY_2(new Message[]{
            new Message(false, "You bested me last time but we now" +
                    "\nknow what we're dealing with."),
    }),

    LOKI_INTRODUCTION_VICTORY_3(new Message[]{
            new Message(false, "I'm still not very impressed."),
    }),

    LOKI_INTRODUCTION_DEFEATED_1(new Message[]{
            new Message(false, "I told you it was a fools errand." +
                    "\nStay here and give up on your ideas" +
                    "\nof making it past me."),
    }),

    LOKI_INTRODUCTION_DEFEATED_2(new Message[]{
            new Message(false, "No matter how many times you try," +
                    "\nyour fate will always be the same."),
    }),

    LOKI_INTRODUCTION_DEFEATED_3(new Message[]{
            new Message(false, "Come now, have you not embarrassed" +
                    "\nyourself enough? Accept your fate and stay."),
    }),

    LOKI_INTRODUCTION_DEFEATED_4(new Message[]{
            new Message(false, "Still trying? How quaint."),
    }),

    LOKI_INTRODUCTION_DEFEATED_5(new Message[]{
            new Message(false, "Good attempt, are you really going" +
                    "\n to try again?"),
    }),

    LOKI_ENCOUNTER_FIRST(new Message[]{
            new Message(false, "It's nice to see you in the flesh... " +
                    "\nFor now"),
    }),

    LOKI_ENCOUNTER_VICTORY_1(new Message[]{
            new Message(false, "I'm glad I get to have my revenge."),
    }),

    LOKI_ENCOUNTER_VICTORY_2(new Message[]{
            new Message(false, "The other Gods must have been playing" +
                    "\ntricks on me. Not this time though."),
    }),

    LOKI_ENCOUNTER_DEFEATED_1(new Message[]{
            new Message(false, "In what manner would you like to die this time?"),
    }),

    LOKI_ENCOUNTER_DEFEATED_2(new Message[]{
            new Message(false, "I'm baffled by how you made it this far." +
                    "\nNo matter, you'll make it no further than here."),
    }),

    ODIN_INTRODUCTION_FIRST(new Message[]{
            new Message(false, "Greetings Warrior, it is impressive that you" +
                    "\nhave made it this far however now you face" +
                    "\na god of pure power and so this is where" +
                    "\nyou shall fail"),
            new Message(false, "Perhaps after being put down quickly you" +
                    "\nwill finally accept your fate and finally" +
                    "\nand finally give up on your fools quest"),
            new Message(false, "Come, let us fight and I will send you back" +
                    "\n to where you belong")
    }),

    ODIN_INTRODUCTION_DEFEAT_1(new Message[]{
            new Message(false, "Why have you come back for a second time?" +
                    "\nthe result will only be the same. You - sent" +
                    "\nback to the beginning.")

    }),

    ODIN_INTRODUCTION_DEFEAT_2(new Message[]{
            new Message(false, "Your persistence is noteworthy. It is" +
                    "\nhowever folly as well. You shall not make it" +
                    "\nthrough"),
            new Message(false, "Give up now.")
    }),

    ODIN_INTRODUCTION_DEFEAT_3(new Message[]{
            new Message(false, "Just how many times will you try? Failing" +
                    "\neach time. Your hubris led you here, and yet" +
                    "\nyou cling to it still")
    }),

    ODIN_INTRODUCTION_VICTORY_1(new Message[]{
            new Message(false, "Why are you back? Just because you" +
                    "\ncan?")

    }),

    ODIN_INTRODUCTION_VICTORY_2(new Message[]{
            new Message(false, "What are you doing here again?" +
                    "\n Valhalla awaits.")
    }),

    ODIN_INTRODCTION_VICTORY_3(new Message[]{
            new Message(false, "Are you really still that eager to battle?" +
                    "\n even with Valhalla now yours to roam?" +
                    "\n So be it.")
    }),

    THOR_INTRODUCTION_FIRST(new Message[]{
            new Message(false, "Ahh, I see that you've made it past my" +
                    "\nbrother. I can't say I'm too surprised though"),
            new Message(false, "He was never really one for real fights," +
                    "\nhe always much preferred making others" +
                    "\nfight and lurking around instead."),
            new Message(false, "I on the other hand, excel at battle" +
                    "\nCome! Test your will against the power " +
                    "\nof a true god and see how you fare!")
    }),

    THOR_INTRODUCTION_DEFEAT_1(new Message[]{
            new Message(false, "What purpose does it serve to try again?" +
                    "\nI cannot be bested in combat. Learn to" +
                    "\naccept your limits"),
            new Message(false, "It is unbecoming of you to keep trying")
    }),

    THOR_INTRODUCTION_DEFEAT_2(new Message[]{
            new Message(false, "What? Back again? Allow me to send you back" +
                    "\nto whence you came.")
    }),

    THOR_INTRODUCTION_DEFEAT_3(new Message[]{
            new Message(false, "Your stubbornness is noteworthy, it is" +
                    "\nhowever also ugly. Learn to accept when you are" +
                    "\nbested.")
    }),

    THOR_INTRODUCTION_VICTORY_1(new Message[]{
            new Message(false, "I see that you've come back for a second" +
                    "\ntime. An error on your part."),
            new Message(false, "I shall make sure you don't defeat me again.")
    }),

    THOR_INTRODUCTION_VICTORY_2(new Message[]{
            new Message(false, "Ahhh you wish to fight me again?" +
                    "\nCome, you'll find me a willing opponent.")
    }),

    THOR_INTRODUCTION_VICTORY_3(new Message[]{
            new Message(false, "Still trying to make it to Valhalla?" +
                    "\nyou'll have to make it past me again.")
    }),

    THOR_INTRODUCTION_VICTORY_4(new Message[]{
            new Message(false, "We really need to stop meeting like this," +
                "\nmaybe instead we can meet in Valhalla before long."),
            new Message(false, "Until then however, we fight!")
    }),

    THOR_INTRODUCTION_VICTORY_5(new Message[]{
            new Message(false, "Ah, hello once again warrior. Did you wish" +
                    "\nto clash with me once again?" +
                    "\nVery well, I am happy to oblige.")
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

    private final Message[] messages;

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
    private final boolean mainCharacter;

    /**
     * The message that will be displayed by the character.
     */
    private final String message;

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
