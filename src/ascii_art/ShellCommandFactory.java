package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageEditor;
import image_char_matching.ComparisonMode;
import image_char_matching.SubImgCharMatcher;

import java.util.Objects;
import java.util.TreeSet;

/**
 * Factory class responsible for processing and executing shell commands.
 * It handles various commands related to managing the character set,
 * changing resolution, output format, and generating ASCII art from images.
 */
public class ShellCommandFactory {
    private static final String SHOW_CHARS_SET = "chars";
    private static final String ADD_CHAR = "add";
    private static final String REMOVE_CHAR = "remove";
    private static final String CHANGE_RESOLUTION = "res";
    private static final String ROUND = "round";
    private static final String OUTPUT = "output";
    private static final String ASCII_ART = "asciiArt";
    private static final String HTML_OUTPUT_FILE_PATH = "out.html";
    private static final String HTML_FONT_FORMAT = "Courier New";
    private static final int MINIMAL_CHARSET_SIZE = 2;

    private final TreeSet<Character> charSet;
    private final ImageEditor imageEditor;
    private final SubImgCharMatcher subImgCharMatcher;
    private ComparisonMode comparisonMode;
    private AsciiOutput asciiOutput;
    private final Image image;
    private final ShellExceptionsManager shellExceptionsManager;
    private final CharsCommandsHandler charsCommandsHandler;

    /**
     * Constructs a ShellCommandFactory with the specified components.
     *
     * @param charSet           The set of characters used for ASCII art.
     * @param imageEditor       The image editor used to modify the image.
     * @param subImgCharMatcher The matcher used to find suitable characters for sub-images.
     * @param comparisonMode    The comparison mode for sub-image character matching.
     * @param asciiOutput       The output handler to display ASCII art.
     * @param image             The image to be processed.
     * @param shellExceptions   An object to handle shell-related exceptions.
     */
    public ShellCommandFactory(TreeSet<Character> charSet, ImageEditor imageEditor,
                               SubImgCharMatcher subImgCharMatcher,
                               ComparisonMode comparisonMode,
                               AsciiOutput asciiOutput,
                               Image image, ShellExceptionsManager shellExceptions) {
        this.charSet = charSet;
        this.imageEditor = imageEditor;
        this.subImgCharMatcher = subImgCharMatcher;
        this.comparisonMode = comparisonMode;
        this.asciiOutput = asciiOutput;
        this.image = image;
        this.shellExceptionsManager = shellExceptions;
        charsCommandsHandler = new CharsCommandsHandler(shellExceptions,
                subImgCharMatcher, charSet);
    }

    /**
     * Processes the given input line to determine and execute the corresponding shell command.
     *
     * @param inputLine The command input from the user.
     * @throws ShellException If an invalid command is given or an error occurs during processing.
     */
    public void getCommand(String inputLine) throws ShellException {
        String[] lineList = inputLine.split(" ");
        String command = lineList[0];

        switch (command) {
            case SHOW_CHARS_SET:
                doCharsCommand();
                break;  // Don't forget the break statement to prevent falling through to next case.

            case ADD_CHAR:
                doAddRemoveCommand(lineList, true);  // true indicates add operation
                break;

            case REMOVE_CHAR:
                doAddRemoveCommand(lineList, false);  // false indicates remove operation
                break;

            case CHANGE_RESOLUTION:
                doChangeResolutionCommand(lineList);
                break;

            case ROUND:
                doRoundCommand(lineList);
                break;

            case OUTPUT:
                doChangeOutputCommand(lineList);
                break;

            case ASCII_ART:
                doAsciiArtAlgorithmCommand();
                break;

            default:
                shellExceptionsManager.throwIllegalCommandException();
        }
    }

    /*
     * Executes the ASCII art algorithm to convert the image into ASCII art.
     * Throws an exception if the character set is too small to generate meaningful output.
     */
    private void doAsciiArtAlgorithmCommand() throws ShellException {
        if (charSet.size() < MINIMAL_CHARSET_SIZE) {
            shellExceptionsManager.throwCharSetTooSmallException();
        }
        AsciiArtAlgorithm algorithmToRun = new AsciiArtAlgorithm(image, subImgCharMatcher,
                imageEditor, comparisonMode);
        char[][] asciiArt = algorithmToRun.run();
        asciiOutput.out(asciiArt);
    }

    /*
     * Changes the output format between console and HTML based on the user input.
     * If the format is not recognized, throws an exception.
     */
    private void doChangeOutputCommand(String[] lineList) throws ShellException {
        try {
            String outputConsole = lineList[1];
            if (outputConsole.equals("html") && asciiOutput instanceof HtmlAsciiOutput ||
                    outputConsole.equals("console") && asciiOutput instanceof ConsoleAsciiOutput) {
                return;
            }
            if (outputConsole.equals("html")) {
                asciiOutput = new HtmlAsciiOutput(HTML_OUTPUT_FILE_PATH, HTML_FONT_FORMAT);
            } else if (outputConsole.equals("console")) {
                asciiOutput = new ConsoleAsciiOutput();
            } else {
                shellExceptionsManager.throwOutputFormatException();
            }
        } catch (IndexOutOfBoundsException e) {
            shellExceptionsManager.throwOutputFormatException();
        }
    }

    /*
     * Changes the rounding mode for image character comparison based on user input.
     * Throws an exception if the round mode format is incorrect.
     */
    private void doRoundCommand(String[] lineList) throws ShellException {
        try {
            String roundMode = lineList[1];
            switch (roundMode) {
                case "abs":
                    comparisonMode = ComparisonMode.CLOSEST_ABSOLUTE;
                    break;

                case "down":
                    comparisonMode = ComparisonMode.CLOSEST_LOWER;
                    break;

                case "up":
                    comparisonMode = ComparisonMode.CLOSEST_HIGHER;
                    break;

                default:
                    shellExceptionsManager.throwRoundFormatException();
            }
        } catch (IndexOutOfBoundsException e) {
            shellExceptionsManager.throwRoundFormatException();
        }
    }

    /*
     * Processes the add or remove character command. Depending on the command,
     * it calls the appropriate method to add or remove characters from the charSet.Throws a ShellException
     *  if there is an error during processing, such as an invalid character range or invalid command.
     */
    private void doAddRemoveCommand(String[] lineList, boolean isAdd) throws ShellException {
        charsCommandsHandler.AddOrRemoveChars(lineList, isAdd);
    }

    /*
     * Changes the resolution of the image based on the input command. It either increases or
     * decreases the resolution. Throws a ShellException if an invalid resolution command is provided.
     */
    private void doChangeResolutionCommand(String[] lineList) throws ShellException {
        try {
            if (lineList.length == 1) {
                System.out.printf("Resolution set to %s" + "\n", imageEditor.getResolution());
                return;
            }
            if (lineList[1].equals("up")) {
                imageEditor.setResolution(imageEditor.getResolution() * 2);
            } else if (lineList[1].equals("down")) {
                imageEditor.setResolution(imageEditor.getResolution() / 2);
            } else {
                throw new ShellException("Invalid resolution postfix",
                        new Throwable("Invalid resolution postfix"));
            }
        }
        // In case a ShellException was thrown from imageEditor.setResolution.
        catch (ShellException e) {
            if (Objects.equals(e.getCause().getMessage(), "Invalid resolution postfix")) {
                shellExceptionsManager.throwResolutionFormatException();
            }
            shellExceptionsManager.throwResolutionRangeException();
        }
    }

    /*
     * Prints all the characters in the charSet to the console and calls the subImgCharMatcher's print method.
     */
    private void doCharsCommand() {
        charsCommandsHandler.printAllChars();
    }
}


