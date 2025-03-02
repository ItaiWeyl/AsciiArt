package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image.ImageEditor;
import image_char_matching.ComparisonMode;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Shell class represents the command-line interface (CLI) for processing an image and converting it into
 * ASCII art. It allows users to interact with the image processing system by entering commands.
 * The class initializes necessary components such as the image loader, image editor, and the command factory
 * for handling user commands.The main function processes the image path passed via command-line arguments
 * and starts the shell for user interaction.
 */
public class Shell {
    // Default set of characters to be used in ASCII art representation.
    private static final char[] DEFAULT_CHAR_SET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int DEFAULT_RESOLUTION = 2;
    private static final String EXIT_COMMAND = "exit";
    private static final String INPUT_STREAM_MARK = ">>> ";

    /**
     * Main method to start the Shell with the provided image path and generate the corresponding ASCII art.
     *
     * @param args Command-line arguments, where the first argument should be the image path.
     * @throws IOException if there is an issue reading the image file.
     */
    public static void main(String[] args) throws IOException {
        try {
            // Ensure that an image path is provided as a command-line argument.
            if (args.length == 0) {
                throw new IllegalArgumentException(
                        "Error: No image path provided. Please specify the image file path as an argument.");
            }

            String imagePath = args[0];

            // Initialize and run the shell with the specified image path and character set.
            Shell shell = new Shell();
            shell.run(imagePath);
        } catch (IllegalArgumentException e) {
            // Print the error message when no image path is provided.
            System.err.println(e.getMessage());
        }
    }


    /**
     * public Constructor to initialize the Shell.
     */
    public Shell() {}


    /**
     * Starts the interactive shell for processing user commands.
     * Users can input commands to manipulate the image and generate ASCII art.
     * @param imagePath - The path of the image to be processed as class Image.
     */
    public void run(String imagePath) throws IOException {

        // Load the image to be processed.
        Image image = new Image(imagePath);

        // Initialize the character matcher for sub-image matching.
        ShellCommandFactory commandFactory = getShellCommandFactory(image);

        // Display the prompt for user input.
        System.out.print(INPUT_STREAM_MARK);

        // Read the user's command.
        String command = KeyboardInput.readLine();

        // Keep running until the user types the exit command.
        while (!command.equals(EXIT_COMMAND)) {
            try {
                // Attempt to execute the command.
                commandFactory.getCommand(command);
            }
            // Ignore exceptions thrown by the command processing (i.e., invalid commands).
            catch (ShellException ignored) {
            }

            // Prompt for the next command.
            System.out.print(INPUT_STREAM_MARK);
            command = KeyboardInput.readLine();

        }
    }

    /**
     * Initializes and returns a fully configured ShellCommandFactory instance.
     * The factory handles shell commands for processing images and generating ASCII art.
     * It sets up the necessary components such as image, character matcher,
     * and output handler.
     *
     * @param image The image to be processed for ASCII art generation.
     * @return A fully configured ShellCommandFactory instance.
     */
    private static ShellCommandFactory getShellCommandFactory(Image image) {
        SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);

        // Create an image editor to apply transformations based on the given resolution.
        ImageEditor imageEditor = new ImageEditor(image, DEFAULT_RESOLUTION);

        // Initialize the ASCII output handler to display results in the console.
        AsciiOutput asciiOutput = new ConsoleAsciiOutput();

        // Set the default comparison mode for the character matcher.
        ComparisonMode comparisonMode = ComparisonMode.CLOSEST_ABSOLUTE;

        // Initialize the command factory to handle commands and pass the necessary components.
        // Convert the default character set into a TreeSet to ensure unique characters and sorted order.
        TreeSet<Character> treeCharSet = new TreeSet<>();
        for (char c : DEFAULT_CHAR_SET) {
            treeCharSet.add(c);
        }
        return new ShellCommandFactory(treeCharSet, imageEditor,
                subImgCharMatcher, comparisonMode, asciiOutput, image, new ShellExceptionsManager());
    }
}
