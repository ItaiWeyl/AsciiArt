package image;

import ascii_art.ShellException;

/**
 * The ImageEditor class provides various image processing functions, such as padding the image,
 * splitting it into smaller sub-images, and calculating its brightness.
 * This class uses helper classes such as BrightnessCalculator, ImageSplitter, and ImagePadder
 * to perform these specific image processing tasks.
 */
public class ImageEditor {

    private final Image paddedImage; // The image after padding
    private int resolution; // Resolution for splitting the image

    /**
     * Instance of {@link BrightnessCalculator} used to calculate the brightness of sub-images.
     */
    public BrightnessCalculator brightnessCalculator;

    /**
     * Instance of {@link ImageSplitter} used to split the image into smaller sub-images.
     */
    public ImageSplitter imageSplitter;

    /**
     * Instance of {@link ImagePadder} used to pad the image for processing.
     */
    public ImagePadder imagePadder;


    /**
     * Constructs an ImageEditor instance with the specified image and resolution.
     *
     * @param image      The original image to be processed.
     * @param resolution The resolution used for splitting the image.
     */
    public ImageEditor(Image image, int resolution) {
        // The original image to process
        this.resolution = resolution;
        this.brightnessCalculator = new BrightnessCalculator();
        this.imageSplitter = new ImageSplitter();
        this.imagePadder = new ImagePadder();
        paddedImage = padImage(image);
    }

    /**
     * Pads the original image using the functionality provided by ImagePadder.
     * This method updates the `paddedImage` field with the padded version of the original image.
     */
    public Image padImage(Image originalImage) {
        return imagePadder.paddImage(originalImage);
    }

    /**
     * Splits the padded image into smaller sub-images based on the specified resolution.
     *
     * @return A 2D array of sub-images after splitting.
     * @throws IllegalStateException if the padded image has not been initialized.
     */
    public Image[][] splitImage(Image paddedImage) {
        return imageSplitter.splitImage(paddedImage, resolution);
    }

    /**
     * Calculates the brightness of the specified sub-image using its pixel data.
     *
     * @param subImage The sub-image whose brightness is to be calculated.
     * @return The brightness value of the sub-image, represented as a double between 0 and 1.
     */
    public double calculateBrightness(Image subImage) {
        return brightnessCalculator.calculateBrightness(subImage);
    }

    /**
     * Retrieves the resolution used for splitting the image.
     *
     * @return The resolution value.
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Updates the resolution used for splitting the image.
     *
     * @param resolution The new resolution value.
     */
    public void setResolution(int resolution) throws ShellException {
        int maxRes = paddedImage.getWidth();
        int minRes = Math.max(1, paddedImage.getWidth() / paddedImage.getHeight());
        if (resolution < minRes || resolution > maxRes) {
            throw new ShellException("Resolution out of range: " + resolution, new Throwable());
        }

        this.resolution = resolution;
    }
}
