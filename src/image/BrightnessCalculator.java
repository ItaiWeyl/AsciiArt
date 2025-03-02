package image;

/**
 * This class is responsible for calculating the brightness of an image.
 * The brightness is computed using a weighted sum of the RGB values of each pixel.
 * The formula for brightness is:
 * brightness = (Red * 0.2126) + (Green * 0.7152) + (Blue * 0.0722)
 * The result is normalized by dividing by maximum possible RGB sum and the gray variety sum of all pixels.
 */
public class BrightnessCalculator {
    private static final double RED_FACTOR = 0.2126;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double BLUE_FACTOR = 0.0722;

    // Maximum RGB value (255) used for normalization
    private static final double MAX_RGB = 255;

    /**
     * Calculates the brightness of an subImage.
     * The brightness is determined by summing up the weighted RGB values of each pixel
     * in the subImage and normalizing the result.
     * @param subImage The subImage for which the brightness needs to be calculated.
     * @return A double representing the normalized brightness of the subImage (range 0.0 to 1.0).
     */
    public double calculateBrightness(Image subImage) {
        double grayVarietySum = 0;

        // Iterate through each pixel of the subImage and compute the weighted sum of its RGB components
        for (int i = 0; i < subImage.getHeight(); i++) {
            for (int j = 0; j < subImage.getWidth(); j++) {
                // Retrieve the pixel color and compute its contribution to the brightness
                grayVarietySum += subImage.getPixel(i, j).getRed() * RED_FACTOR +
                        subImage.getPixel(i, j).getGreen() * GREEN_FACTOR +
                        subImage.getPixel(i, j).getBlue() * BLUE_FACTOR;
            }
        }

        // Normalize the sum by the total number of pixels and the max possible value of RGB
        return grayVarietySum / (subImage.getHeight() * subImage.getWidth() * MAX_RGB);
    }
}
