package ascii_art;

import image.Image;
import image.ImageEditor;
import image_char_matching.ComparisonMode;
import image_char_matching.SubImgCharMatcher;

/**
 * AsciiArtAlgorithm generates an ASCII representation of an image by splitting the image into sub-images,
 * calculating brightness for each sub-image, and mapping the brightness values to characters from a given
 * character set.
 */
public class AsciiArtAlgorithm {

    private final Image image;
    private final SubImgCharMatcher charMatcher;
    private final ImageEditor imageEditor;

    /**
     * Constructor for AsciiArtAlgorithm.
     * Initializes necessary components like image, resolution, character matcher, and image editor.
     *
     * @param image  The image to process
     * @param comparisonMode An enum used to define what rounding functions to use when comparing brightness
     */
    public AsciiArtAlgorithm(Image image,SubImgCharMatcher subImgCharMatcher, ImageEditor imageEditor,
                             ComparisonMode comparisonMode){
        this.image = image;
        this.charMatcher = subImgCharMatcher;
        this.imageEditor = imageEditor;
        subImgCharMatcher.setComparisonMode(comparisonMode);
    }

    /**
     * Runs the ASCII art algorithm by generating sub-images, calculating their brightness,
     * and mapping brightness values to corresponding characters.
     *
     * @return A 2D array representing the ASCII art
     */
    public char[][] run() {
        Image paddedImage = imageEditor.padImage(image);
        double[][] subImagesBrightness = getSubImagesBrightness(paddedImage);
        return getAsciiArt(subImagesBrightness);
    }

    /*
     * Retrieves the brightness values for sub-images, either from cached data or by calculating them.
     *
     * @return A 2D array containing brightness values for each sub-image
     */
    private double[][] getSubImagesBrightness(Image paddedImage) {
        // Try to retrieve cached brightness values
        double[][] subImagesBrightness = ImagesDataDrive.getImageData(image,
                imageEditor.getResolution());

        if (subImagesBrightness != null) {
            // If data is found, return it
            return subImagesBrightness;
        } else {
            // Split the padded image into sub-images
            Image[][] subImages = imageEditor.splitImage(paddedImage);
            subImagesBrightness = new double[subImages.length][subImages[0].length];
            for (int i = 0; i < subImagesBrightness.length; i++) {
                for (int j = 0; j < subImagesBrightness[i].length; j++) {
                    subImagesBrightness[i][j] = imageEditor.calculateBrightness(subImages[i][j]);
                }
            }

            // Cache the calculated brightness values for future use
            ImagesDataDrive.addImageData(image, imageEditor.getResolution(), subImagesBrightness);
        }

        return subImagesBrightness;
    }

    /*
     * Retrieves the ASCII art representation by mapping brightness values of sub-images to characters.
     * Either fetches the cached ASCII art or computes it by converting brightness values.
     *
     * @param subImagesBrightness A 2D array containing brightness values for each sub-image
     * @return A 2D array of characters representing the ASCII art
     */
    private char[][] getAsciiArt(double[][] subImagesBrightness) {
        char[][] asciiArt = new char[subImagesBrightness.length][subImagesBrightness[0].length];
        // Convert brightness values to characters
        for (int i = 0; i < asciiArt.length; i++) {
            for (int j = 0; j < asciiArt[i].length; j++) {
                asciiArt[i][j] = charMatcher.getCharByImageBrightness(subImagesBrightness[i][j]);
            }
        }
        return asciiArt;
    }
}
