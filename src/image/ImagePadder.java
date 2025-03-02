package image;

import java.awt.*;

/**
 * The ImagePadder class provides functionality to pad an image by resizing its dimensions to the next
 * power of 2.
 * The original image is centered within a padded area filled with white pixels.
 */
public class ImagePadder {

    /**
     * Pads the given image by resizing it to the next power of 2 for both width and height.
     * The original image is centered in the padded area, which is filled with white pixels by default.
     *
     * @param imageToPad The image to be padded.
     * @return A new image with the padded dimensions.
     */
    public Image paddImage(Image imageToPad) {
        if (imageToPad == null) {
            return null;
        }

        int originalWidth = imageToPad.getWidth();
        int originalHeight = imageToPad.getHeight();

        // Calculate the new dimensions, which are the next powers of 2
        int paddedWidth = nextPowerOfTwo(originalWidth);
        int paddedHeight = nextPowerOfTwo(originalHeight);

        // Create a new pixel array with the new dimensions
        Color[][] paddedPixelArray = new Color[paddedHeight][paddedWidth];

        // Fill the new array with white pixels as the default padding
        for (int i = 0; i < paddedHeight; i++) {
            for (int j = 0; j < paddedWidth; j++) {
                paddedPixelArray[i][j] = Color.WHITE;
            }
        }

        // Copy the original image pixels into the center of the new array
        int verticalPadding = (paddedHeight - originalHeight) / 2;
        int horizontalPadding = (paddedWidth - originalWidth) / 2;

        for (int i = 0; i < originalHeight; i++) {
            for (int j = 0; j < originalWidth; j++) {
                paddedPixelArray[i + verticalPadding][j + horizontalPadding] = imageToPad.getPixel(i, j);
            }
        }

        // Return a new Image object with the padded pixel array
        return new Image(paddedPixelArray, paddedWidth, paddedHeight);
    }

    /*
     * Calculates the next power of 2 greater than or equal to the given number.
     * If the number is already a power of 2, it returns the same number.
     *
     * @param n The number to calculate the next power of 2 for.
     * @return The next power of 2 greater than or equal to n.
     */
    private int nextPowerOfTwo(int n) {
        if ((n & (n - 1)) == 0) {
            return n; // n is already a power of 2
        }
        int power = 1;
        while (power < n) {
            power *= 2;
        }
        return power;
    }
}
