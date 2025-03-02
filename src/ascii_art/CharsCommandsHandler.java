package ascii_art;

import image_char_matching.SubImgCharMatcher;

import java.util.TreeSet;

/**
 * The CharsCommandsHandler class serves as part of the ShellCommand factory
 * and is responsible for handling the logic of adding, removing, and showing characters.
 * It interacts with {@link SubImgCharMatcher} for character-based operations
 * and uses {@link ShellExceptionsManager} to handle exceptions.
 */

public class CharsCommandsHandler {
    private static final int FIRST_ASCII_VALUE_TO_ADD = 32;
    private static final int LAST_ASCII_VALUE_TO_ADD = 126;
    private final ShellExceptionsManager shellExceptionsManager;
    private final SubImgCharMatcher subImgCharMatcher;
    private final TreeSet<Character> charSet;

    /**
     * Constructs a CharsCommandsHandler instance with the specified exception manager, character matcher,
     * and character set.
     *
     * @param shellExceptionsManager The manager responsible for handling exceptions related to
     * character operations.
     * @param subImgCharMatcher The matcher used for character-based operations on sub-images.
     * @param charSet The set of characters to be managed for adding or removing operations.
     */
    public CharsCommandsHandler(ShellExceptionsManager shellExceptionsManager, SubImgCharMatcher
            subImgCharMatcher, TreeSet<Character> charSet) {
        this.shellExceptionsManager = shellExceptionsManager;
        this.subImgCharMatcher = subImgCharMatcher;
        this.charSet = charSet;
    }

    /**
     * Adds or removes characters based on the provided command.
     *
     * @param lineList A string array containing the command and the character(s) to add or remove.
     * @param isAdd    A boolean flag indicating whether to add (true) or remove (false) characters.
     * @throws ShellException If the command is invalid or an error occurs during processing.
     */
    public void AddOrRemoveChars(String[] lineList, boolean isAdd) throws ShellException{
        try {
            String toAddOrRemove = lineList[1];

            if (toAddOrRemove.equals("all")) {
                handleAllCharsCommand(isAdd);
            } else if (toAddOrRemove.equals("space")) {
                handleSpaceCharCommand(isAdd);
            } else if (toAddOrRemove.equals("-")) {
                handleSingleCharCommand("-", isAdd);
            } else if (toAddOrRemove.contains("-")) {
                handleRangeCommand(toAddOrRemove, isAdd);
            } else {
                handleSingleCharCommand(toAddOrRemove, isAdd);
            }
        } catch (IndexOutOfBoundsException e) {
            shellExceptionsManager.throwAddRemoveCharException(isAdd);
        }
    }

    /*
     * Handles the adding and removing of a single character. Verifying that it has a valid ascii value
     * in the given range (32 - 126).
     */
    private void handleSingleCharCommand(String s, boolean isAdd) throws ShellException {
        if (s.length() != 1){
            shellExceptionsManager.throwAddRemoveCharException(isAdd);
        }

        char charToAdd = s.charAt(0);
        if ((int) charToAdd < FIRST_ASCII_VALUE_TO_ADD || charToAdd > LAST_ASCII_VALUE_TO_ADD) {
            shellExceptionsManager.throwAddRemoveCharException(isAdd);
            return;
        }
        if (isAdd){
            addSingleCharacter(charToAdd);
            return;
        }
        removeSingleCharacter(charToAdd);
    }

    /*
     * Adds a single character to both the charSet and the subImgCharMatcher.
     */
    private void addSingleCharacter(char charToAdd) {
        charSet.add(charToAdd);
        subImgCharMatcher.addChar(charToAdd);
    }

    /*
     * Removes a single character from both the charSet and the subImgCharMatcher.
     */
    private void removeSingleCharacter(char charToRemove) {
        if (charSet.contains(charToRemove)) {
            charSet.remove(charToRemove);
            subImgCharMatcher.removeChar(charToRemove);
        }
    }

    /*
     * Handles the adding and removing of the space character (' ').
     */
    private void handleSpaceCharCommand(boolean isAdd) {
        if (isAdd) {
            charSet.add(' ');
            subImgCharMatcher.addChar(' ');
            return;
        }
        charSet.remove(' ');
        subImgCharMatcher.removeChar(' ');
    }

    /*
     * Handles the adding / removing of all the characters. Calls the appropriate function according to the
     * boolean argument - add all or remove all.
     */
    private void handleAllCharsCommand(boolean isAdd) {
        if (isAdd) {
            addAllAsciiChars();
            return;
        }
        removeAllAsciiChars();
    }

    /*
     * Handles the range add/remove command, processing the specified range of characters.
     * It checks that the range is valid and either adds or removes characters in the specified range.
     * Throws a ShellException if there is an error during range processing - invalid characters or range.
     */
    private void handleRangeCommand(String toAddRemove, boolean isAdd) throws ShellException {
        try {
            String[] partsToAddRemove = toAddRemove.split("-");
            char startChar = partsToAddRemove[0].charAt(0);
            char endChar = partsToAddRemove[1].charAt(0);
            if (partsToAddRemove.length == 2 && isAsciiInRange(startChar) && isAsciiInRange(endChar)) {
                if (isAdd) {
                    addRange(startChar, endChar);
                } else {
                    removeRange(startChar, endChar);
                }
            }
            else {
                throw new ShellException(null);
            }

        } catch (Exception e) {
            shellExceptionsManager.throwAddRemoveCharException(isAdd);
        }
    }

    /*
     * Adds characters within the specified range to both the charSet and the subImgCharMatcher.
     */
    private void addRange(char startChar, char endChar) {
        if (startChar <= endChar) {
            for (char c = startChar; c <= endChar; c++) {
                charSet.add(c);
                subImgCharMatcher.addChar(c);
            }
        } else {
            for (char c = startChar; c >= endChar; c--) {
                charSet.add(c);
                subImgCharMatcher.addChar(c);
            }
        }
    }

    /*
     * Removes characters within the specified range from both the charSet and the subImgCharMatcher.
     */
    private void removeRange(char startChar, char endChar) {
        if (startChar <= endChar) {
            for (char c = startChar; c <= endChar; c++) {
                if (charSet.contains(c)) {
                    charSet.remove(c);
                    subImgCharMatcher.removeChar(c);
                }
            }
        } else {
            for (char c = startChar; c >= endChar; c--) {
                if (charSet.contains(c)) {
                    charSet.remove(c);
                    subImgCharMatcher.removeChar(c);
                }
            }
        }
    }

    /*
     * Adds all ASCII characters within the defined range to both the charSet and the subImgCharMatcher.
     */
    private void addAllAsciiChars() {
        for (int i = FIRST_ASCII_VALUE_TO_ADD; i <= LAST_ASCII_VALUE_TO_ADD; i++) {
            char c = (char) i;
            charSet.add(c);
            subImgCharMatcher.addChar(c);
        }
    }

    /*
     * Removes all ASCII characters within the defined range from both the charSet and the subImgCharMatcher.
     */
    private void removeAllAsciiChars() {
        for (int i = FIRST_ASCII_VALUE_TO_ADD; i <= LAST_ASCII_VALUE_TO_ADD; i++) {
            char c = (char) i;
            charSet.remove(c);
            subImgCharMatcher.removeChar(c);
        }
    }

    /*
     * Checks if a character is within the valid ASCII range for adding/removing characters.
     */
    private boolean isAsciiInRange(char c) {
        return FIRST_ASCII_VALUE_TO_ADD <= (int) c && (int) c <= LAST_ASCII_VALUE_TO_ADD;
    }

    /**
     * Prints all characters currently in the character set.
     * The characters are printed in ascending order.
     */
    public void printAllChars() {
        for (Character c : charSet) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
}
