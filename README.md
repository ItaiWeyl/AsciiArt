# ASCII Art Generator

## **Overview**
The **ASCII Art Generator** is a command-line application that converts images into ASCII representations using customizable character sets. It allows users to interact with an image processing system, apply transformations, and generate ASCII art.  

The project includes a **PDF UML diagram** that outlines the system's architecture.  

---

## **Features**
- **Convert Images to ASCII Art** using different character sets.
- **Interactive Shell** for processing user commands.
- **Customizable Image Resolution** to control output detail.
- **Supports Multiple Output Modes** (Console and HTML).
- **Error Handling and Command Validation** for robustness.

---

## **Project Structure**
The entire project is contained inside the `src/` directory.

```
src/
│── ascii_art/             # Main application logic
│   ├── AsciiArtAlgorithm.java     # Handles the ASCII conversion algorithm
│   ├── CharsCommandsHandler.java  # Manages character set operations
│   ├── ImagesDataDrive.java       # Handles image data operations
│   ├── KeyboardInput.java         # Handles user input in the shell
│   ├── Shell.java                 # Entry point (Main class)
│   ├── ShellCommandFactory.java   # Processes and executes shell commands
│   ├── ShellException.java        # Custom exception for shell-related errors
│   ├── ShellExceptionsManager.java # Manages shell exceptions
│
│── ascii_output/          # Handles ASCII art output formats
│   ├── AsciiOutput.java         # Interface for ASCII output handling
│   ├── ConsoleAsciiOutput.java  # Outputs ASCII art to the console
│   ├── HtmlAsciiOutput.java     # Outputs ASCII art as an HTML file
│
│── image/                 # Image processing utilities
│   ├── BrightnessCalculator.java # Computes brightness levels for ASCII conversion
│   ├── Image.java               # Handles image loading and manipulation
│   ├── ImageEditor.java         # Applies transformations such as resolution adjustments
│   ├── ImagePadder.java         # Pads the image for proper alignment
│   ├── ImageSplitter.java       # Splits the image into sections for ASCII conversion
│
│── image_char_matching/   # ASCII character matching utilities
│   ├── CharConverter.java        # Converts image brightness to ASCII characters
│   ├── ComparisonMode.java       # Defines different comparison modes for character selection
│   ├── SubImgCharMatcher.java    # Matches image segments with ASCII characters
│
│── UML_Diagram.pdf        # UML diagram outlining system architecture

```

---

## **Shell Commands**
| Command        | Description |
|---------------|-------------|
| `chars`       | Displays the current set of characters used for ASCII conversion. |
| `add <char>`  | Adds a character to the character set. |
| `remove <char>` | Removes a character from the character set. |
| `res up`      | Doubles the image resolution. |
| `res down`    | Halves the image resolution. |
| `round abs`   | Sets character matching to absolute mode. |
| `round down`  | Prefers lower matching characters. |
| `round up`    | Prefers higher matching characters. |
| `output console` | Outputs ASCII art to the console. |
| `output html` | Saves ASCII art as an HTML file (`out.html`). |
| `asciiArt`    | Converts the image to ASCII art and displays it. |
| `exit`        | Exits the shell. |

---

## **How to Compile and Run**
### **Prerequisites**
- Java **JDK 8+**
- A valid image file (JPEG, PNG, etc.)

### **Compilation**
Since the project is inside the `src/` directory, compile using:
```sh
javac -d bin src/ascii_art/*.java src/ascii_output/*.java src/image/*.java src/image_char_matching/*.java
```

### **Running the Program**
After compiling, run the program with an image path:
```sh
java -cp bin ascii_art.Shell path/to/image.jpg
```

---

## **Example Usage**
```
>>> chars       # Shows available characters
>>> add @ * %   # Adds '@', '*', and '%' to the character set
>>> remove *    # Removes '*' from the character set
>>> res up      # Increases image resolution
>>> output html # Changes output format to HTML
>>> asciiArt    # Generates ASCII art
>>> exit        # Exits the shell
```
