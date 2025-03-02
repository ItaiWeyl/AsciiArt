package image;

import java.awt.*;

/**
 * The ImageSplitter class is responsible for splitting an image into smaller sub-images based on a given
 * resolution.
 * Each sub-image is a portion of the original image, and the sub-images are organized into a grid.
 */
public class ImageSplitter {

    /**
     * Splits the given image into sub-images based on the desired resolution.
     *
     * @param image      The image to be split.
     * @param resolution The number of sub-images per row and column (resolution x resolution).
     * @return An array of sub-images that together recreate the original image.
     */
    public Image[][] splitImage(Image image, int resolution) {

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        // Calculate the size of each sub-image
        int subImageWidth = originalWidth / resolution;
        int subImageHeight = originalHeight / resolution;

        // Initialize a 2D array to hold the sub-images
        Image[][] subImages = new Image[resolution][originalHeight / subImageHeight];

        // Iterate over sub-image grid
        for (int col = 0; col < resolution; col++) {
            for (int row = 0; row < originalHeight / subImageHeight; row++) {

                // Create a sub-image pixel array
                Color[][] subPixelArray = new Color[subImageHeight][subImageWidth];

                // Fill the sub-image with the corresponding pixels
                for (int i = 0; i < subImageHeight; i++) {
                    for (int j = 0; j < subImageWidth; j++) {
                        int originalX = j + col * subImageWidth;
                        int originalY = i + row * subImageHeight;
                        subPixelArray[i][j] = image.getPixel(originalX, originalY);
                    }
                }

                // Create a sub-image object and store it
                subImages[col][row] = new Image(subPixelArray, subImageWidth, subImageHeight);
            }
        }

        return subImages;
    }
}
