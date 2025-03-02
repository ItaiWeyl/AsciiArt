package ascii_art;

import image.Image;

import java.util.HashMap;

/**
 * ImagesDataDrive is a utility class that stores the mapping between an image and an integer
 * to a corresponding 2D array of doubles.
 */
public final class ImagesDataDrive {

    // A static HashMap with a custom key class that holds an Image and an int
    private static final HashMap<ImageIntKey, double[][]> IMAGES = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private ImagesDataDrive() {}

    /*
     * A custom key class to store the combination of an Image and an int.
     */
    private record ImageIntKey(Image image, int value) {}

    /**
     * Adds an image and its corresponding data (double[][]) to the HashMap.
     *
     * @param image The image to be used as a key.
     * @param value An integer to be used alongside the image.
     * @param data  The 2D array to be stored as a value for the key.
     */
    public static void addImageData(Image image, int value, double[][] data) {
        ImageIntKey key = new ImageIntKey(image, value);
        IMAGES.put(key, data);
    }

    /**
     * Retrieves the image data corresponding to the provided image and integer key (resolution).
     *
     * @param image The image used as a key.
     * @param value The integer used as part of the key - representing resolution.
     * @return The 2D array of doubles associated with the key, or null if no data is found.
     */
    public static double[][] getImageData(Image image, int value) {
        ImageIntKey key = new ImageIntKey(image, value);
        return IMAGES.get(key);
    }
}
