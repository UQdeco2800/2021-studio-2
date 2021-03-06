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

    TUTORIAL_OTHER(new Message[]{
            new Message(false, "If you don't care what I'm saying, you" +
                    "\ncan press ESCAPE to finish the cutscenes."),
            new Message(false, "The pause button is also on ESCAPE if you" +
                    "\nneed a break.")
    }),

    TUTORIAL_DASH(new Message[]{
            new Message(false, "To get around quickly, press SHIFT" +
                    "\nto sprint around and CAPS_LOCK to dash." +
                    "\nTake it easy though, you'll tire yourself out."),
            new Message(false, "Get back to me once you're ready" +
                    "\nto escape.")
    }),

    TUTORIAL_TRAPS(new Message[]{
            new Message(false, "The place is littered with traps," +
                    "\nbe careful where you're stepping."),
            new Message(false, "I'll meet you in the next room and show" +
                    "\nyou what I'm talking about.")
    }),

    TUTORIAL_CRATES(new Message[]{
            new Message(false, "You better get use to injuries." +
                    "\nThere are crates spread across the worlds." +
                    "\nBreak them for a health potion."),
    }),

    TUTORIAL_ATTACK(new Message[]{
            new Message(false, "Use SPACE BAR to swing the axe at" +
                    "\nthe gate. Careful though, this is the" +
                    "\nonly thing that won't attack back."),
            new Message(false, "There are some weapons you'll find along the way." +
                    "\nUse them to your advantage and utilise the power" +
                    "\nthey possess."),
            new Message(false, "The Greatsword has a powerful AOE attack" +
                    "\nwhen pressing Q."),
            new Message(false, "The scepter is capable of firing a powerful" +
                    "\nblast when pressing ALT"),
            new Message(false, "Lastly, Thor's hammer allows can call" +
                    "\nan AOE attack on Q and a deadly weapon" +
                    "\nthrow on ALT."),
            new Message(false, "You'll know what I'm talking about" +
                    "\nwhen you wield them yourself." +
                    "\nFor now, practice on some crates before taking" +
                    "\nout the guard.")
    }),

    TUTORIAL_ENEMIES(new Message[]{
            new Message(false, "Be careful of some of the enemies here." +
                    "\nI've seen them all with my very own eyes."),
            new Message(false, "The warriors will chase you down till their" +
                    "\ndeaths while the archers snipe" +
                    "\nyou from the shadows.")
    }),

    TUTORIAL_EXIT(new Message[]{
            new Message(false, "Between the worlds, you'll find a teleport pad." +
                    "\nThese are how the Gods get around."),
            new Message(false, "You'll need your hands stained with blood" +
                    "\nbefore you're deemed worthy to enter."),
            new Message(true, "I understand. Nothing will get in my way."),
            new Message(false, "Last thing, if you want to leave \nbefore defeating all " +
                    "the enemies \nor take a break between fights \npress ESC to see the pause menu."),
            new Message(false, "Good luck warrior, I hope to never" +
                    "\nsee you again."),
            new Message(true, "Thank you. Same to you."),
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
                    "\nto try again?"),
    }),

    LOKI_ENCOUNTER_FIRST(new Message[]{
            new Message(false, "It's nice to see you in the flesh... " +
                    "\nFor now."),
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

    LOKI2_INTRODUCTION_FIRST(new Message[]{
            new Message(true, "What? I thought the teleport pad would" +
                    "\nget me out of here. It seems like I've only" +
                    "\ngone further."),
            new Message(true, "I have a higher chance of dying from" +
                    "\nthe heat than anyone in my way."),
            new Message(false, "Ignorant fool..."),
            new Message(true, "What?! I saw you die at my hands."),
            new Message(true, "Can't wait to kill you again.")
    }),

    LOKI2_INTRODUCTION_VICTORY_1(new Message[]{
            new Message(true, "Killing Lokis may now be my new" +
                    "\nfavourite hobby."),
            new Message(true, "One down, another one to go."),
    }),

    LOKI2_INTRODUCTION_VICTORY_2(new Message[]{
            new Message(false, "I'm surprised you made it through my" +
                    "\ndecoy again."),
            new Message(false, "It means that I get to have my " +
                    "\nrevenge however."),
    }),

    LOKI2_INTRODUCTION_VICTORY_3(new Message[]{
            new Message(false, "Come find me again. I know you can't" +
                    "\nwait to kill me."),
    }),

    LOKI2_INTRODUCTION_DEFEATED_1(new Message[]{
            new Message(false, "How did you get past my decoy again." +
                    "\nI must not be like I use to be."),
            new Message(false, "Still good enough to kill you however.")
    }),

    LOKI2_INTRODUCTION_DEFEATED_2(new Message[]{
            new Message(true, "I'm prepared this time, the heat" +
                    "\nwon't get to me."),
            new Message(true, "Where are you Loki? Are you afraid" +
                    "\nof a rematch?."),
    }),

    LOKI2_INTRODUCTION_DEFEATED_3(new Message[]{
            new Message(true, "Spawn decoys, transform and fire." +
                    "\nEasy."),
            new Message(true, "Same as the clown I just killed."),
    }),

    LOKI2_ENCOUNTER_FIRST(new Message[]{
            new Message(false, "I see you've reached me again" +
                    "\nbut have you really? Am I just another clone?"),
            new Message(true, "Are you afraid? I've seen what you" +
                    "\ncan do and I'm far from impressed."),
            new Message(false, "Fool! This is going to be fun isn't it." +
                    "\nWatching you die didn't seem as fun."),
            new Message(true, "So it is you. I'll make sure I savour" +
                    "\nthe moment."),
            new Message(false, "Is it? Who knows."),
    }),

    LOKI2_ENCOUNTER_VICTORY_1(new Message[]{
            new Message(true, "Killing Loki. It never gets old."),
            new Message(false, "I don't recall that happening." +
                    "\nI must've been asleep while a decoy was watching over."),
    }),

    LOKI2_ENCOUNTER_VICTORY_2(new Message[]{
            new Message(false, "Can't wait to get my revenge." +
                    "\nI knew you wouldn't get past the others."),
    }),

    LOKI2_ENCOUNTER_DEFEATED_1(new Message[]{
            new Message(true, "Hiding behind your decoys once again." +
                    "\nYou can't hide forever."),
    }),

    LOKI2_ENCOUNTER_DEFEATED_2(new Message[]{
            new Message(false, "Interesting. You seem to keep getting back" +
                    "\nto me then dying."),
            new Message(false, "Are you finding this fun?"),
            new Message(true, "I will when I finally kill you."),
    }),


    ODIN_INTRODUCTION_FIRST(new Message[]{
            new Message(true, "So this is where I belong, the golden statues" +
                    "\nand pristine white walls." +
                    "\nI could get use to this but I am not yet" +
                    "\nworthy for a place here."),
            new Message(true, "I cannot live here with those who belittled me" +
                    "\nand locked me away." +
                    "\nThis will be mine. And mine only."),
    }),

    ODIN_INTRODUCTION_DEFEAT_1(new Message[]{
            new Message(true, "I couldn't see from the shine of the walls." +
                    "\nHis eye patch must've helped him."),
    }),

    ODIN_INTRODUCTION_DEFEAT_2(new Message[]{
            new Message(true, "Good skills for an old man. I'm impressed."),
    }),

    ODIN_INTRODUCTION_DEFEAT_3(new Message[]{
            new Message(true, "I was taught the elderly should be" +
                    "\nrespected. Not this time."),
    }),

    ODIN_INTRODUCTION_VICTORY_1(new Message[]{
            new Message(true, "The old man didn't know what was coming for him." +
                    "\nI can't wait to relive this joy."),
    }),

    ODIN_INTRODUCTION_VICTORY_2(new Message[]{
            new Message(true, "More time in this golden palace." +
                    "\nI could live here forever."),
    }),

    ODIN_INTRODUCTION_VICTORY_3(new Message[]{
            new Message(true, "The curse on me may have been a blessing." +
                    "\nBattles for the rest of my life."),
    }),

    ODIN_ENCOUNTER_FIRST(new Message[]{
            new Message(false, "Greetings Warrior, it is impressive that you" +
                    "\nhave made it this far however now you face" +
                    "\na god of pure power and so this is where" +
                    "\nyou shall fail."),
            new Message(false, "Perhaps after being put down quickly you" +
                    "\nwill finally accept your fate and finally" +
                    "\ngive up on your fools quest."),
            new Message(false, "Come, let us fight and I will send you back" +
                    "\nto where you belong")
    }),

    ODIN_ENCOUNTER_DEFEAT_1(new Message[]{
            new Message(false, "Why have you come back for a second time?" +
                    "\nthe result will only be the same. You - sent" +
                    "\nback to the beginning.")

    }),

    ODIN_ENCOUNTER_DEFEAT_2(new Message[]{
            new Message(false, "Your persistence is noteworthy. It is" +
                    "\nhowever folly as well. You shall not make it" +
                    "\nthrough"),
            new Message(false, "Give up now.")
    }),

    ODIN_ENCOUNTER_DEFEAT_3(new Message[]{
            new Message(false, "Just how many times will you try? Failing" +
                    "\neach time. Your hubris led you here, and yet" +
                    "\nyou cling to it still")
    }),

    ODIN_ENCOUNTER_VICTORY_1(new Message[]{
            new Message(false, "Why are you back? Just because you" +
                    "\ncan?")

    }),

    ODIN_ENCOUNTER_VICTORY_2(new Message[]{
            new Message(false, "What are you doing here again?" +
                    "\n Valhalla awaits.")
    }),

    ODIN_ENCOUNTER_VICTORY_3(new Message[]{
            new Message(false, "Are you really still that eager to battle?" +
                    "\n even with Valhalla now yours to roam?" +
                    "\n So be it.")
    }),

    ODIN_KILLED_FIRST(new Message[]{
            new Message(false, "You have defeated the best Asgard has to offer!" +
                    "\n No mortal is worthier than you. Perhaps no god..." +
                    "\n Valhalla awaits you.")
    }),

    ODIN_KILLED_DEFEAT_1(new Message[]{
            new Message(false, "Revenge must taste sweet." +
                    "\n Valhalla awaits you.")
    }),

    ODIN_KILLED_DEFEAT_2(new Message[]{
            new Message(false, "They say revenge is best served cold." +
                    "\n They won't say that anymore." +
                    "\n Valhalla awaits you.")
    }),

    ODIN_KILLED_DEFEAT_3(new Message[]{
            new Message(false, "Must be your lucky day." +
                    "\n Walk through the gates before your luck changes.")
    }),

    ODIN_KILLED_VICTORY_1(new Message[]{
            new Message(false, "Beaten by a mortal twice." +
                    "\n Maybe, I should retire...")
    }),

    ODIN_KILLED_VICTORY_2(new Message[]{
            new Message(true, "Is this the king of gods?" +
                    "\n The gods must be weak!")
    }),

    ODIN_KILLED_VICTORY_3(new Message[]{
            new Message(true, "King of gods?" +
                    "\n I have seen peasants who fight better")
    }),


    THOR_INTRODUCTION_FIRST(new Message[]{
            new Message(true, "I'm finally free from Loki, the heat from Hell" +
                    "\nand the chains within the prison." +
                    "\nI'm nearly there. I can feel it"),
            new Message(true, "It feels like home here, what I'm use to." +
                    "\nThe soft, grassy fields and the chilling breeze"),
            new Message(true, "I wonder who else stands before me on" +
                    "\nmy journey. They aren't ready to face my wrath.")
    }),

    THOR_INTRODUCTION_DEFEAT_1(new Message[]{
            new Message(true, "This place feels different," +
                    "\nnot like home but I'm ready this time."),
    }),

    THOR_INTRODUCTION_DEFEAT_2(new Message[]{
            new Message(true, "Thor may have bested me this time," +
                    "\nbut he's incompetent, unwilling to learn."),
    }),

    THOR_INTRODUCTION_DEFEAT_3(new Message[]{
            new Message(true, "I've seen his skills, he got lucky last time."),
    }),

    THOR_INTRODUCTION_VICTORY_1(new Message[]{
            new Message(true, "It was a breeze last time," +
                    "\nI don't see it being any different."),
    }),

    THOR_INTRODUCTION_VICTORY_2(new Message[]{
            new Message(true, "Finally back in my element," +
                    "\nThor is nothing compared to me."),
    }),

    THOR_INTRODUCTION_VICTORY_3(new Message[]{
            new Message(true, "I have to go through this again..." +
                    "\nWhat a waste of time."),
    }),

    THOR_ENCOUNTER_FIRST(new Message[]{
            new Message(false, "Ahh, I see that you've made it past my" +
                    "\nbrother. I can't say I'm too surprised though"),
            new Message(false, "He was never really one for real fights," +
                    "\nhe always much preferred making others" +
                    "\nfight and lurking around instead."),
            new Message(false, "I on the other hand, excel at battle" +
                    "\nCome! Test your will against the power " +
                    "\nof a true god and see how you fare!")
    }),

    THOR_ENCOUNTER_DEFEAT_1(new Message[]{
            new Message(false, "What purpose does it serve to try again?" +
                    "\nI cannot be bested in combat. Learn to" +
                    "\naccept your limits"),
            new Message(false, "It is unbecoming of you to keep trying")
    }),

    THOR_ENCOUNTER_DEFEAT_2(new Message[]{
            new Message(false, "What? Back again? Allow me to send you back" +
                    "\nto whence you came.")
    }),

    THOR_ENCOUNTER_DEFEAT_3(new Message[]{
            new Message(false, "Your stubbornness is noteworthy, it is" +
                    "\nhowever also ugly. Learn to accept when you are" +
                    "\nbested.")
    }),

    THOR_ENCOUNTER_VICTORY_1(new Message[]{
            new Message(false, "I see that you've come back for a second" +
                    "\ntime. An error on your part."),
            new Message(false, "I shall make sure you don't defeat me again.")
    }),

    THOR_ENCOUNTER_VICTORY_2(new Message[]{
            new Message(false, "Ahhh you wish to fight me again?" +
                    "\nCome, you'll find me a willing opponent.")
    }),

    THOR_ENCOUNTER_VICTORY_3(new Message[]{
            new Message(false, "Still trying to make it to Valhalla?" +
                    "\nyou'll have to make it past me again.")
    }),

    THOR_ENCOUNTER_VICTORY_4(new Message[]{
            new Message(false, "We really need to stop meeting like this," +
                    "\nmaybe instead we can meet in Valhalla before long."),
            new Message(false, "Until then however, we fight!")
    }),

    THOR_ENCOUNTER_VICTORY_5(new Message[]{
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
        return messages[index].getMessageContents();
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
    private final String messageContents;

    public Message(boolean mainCharacter, String messageContents) {
        this.mainCharacter = mainCharacter;
        this.messageContents = messageContents;
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
    public String getMessageContents() {
        return messageContents;
    }

}
