package image_char_matching;

import java.util.*;

/**
 * The SubImgCharMatcher class is responsible for matching a character from a given charset
 * to a brightness value of an image segment. It provides methods to find the character
 * closest in brightness, add or remove characters from the charset, and handle the brightness data.
 */
public class SubImgCharMatcher {

    private final TreeMap<Double, TreeSet<Character>> normalizedBrightnessMap;  // Normalized brightness map
    private final TreeMap<Double, TreeSet<Character>> rawBrightnessMap;  // Raw brightness map
    private double minBrightness = Double.MAX_VALUE;
    private double maxBrightness = Double.MIN_VALUE;
    private ComparisonMode comparisonMode;

    /**
     * Constructor for the SubImgCharMatcher class.
     * Initializes the brightness map and populates it with the given charset.
     *
     * @param charset An array of characters that will serve as the initial charset for matching.
     */
    public SubImgCharMatcher(char[] charset) {
        // Initialize TreeMaps with default natural ordering
        normalizedBrightnessMap = new TreeMap<>();
        rawBrightnessMap = new TreeMap<>();
        initializeBrightnessMap(charset);
    }

    /**
     * Finds the character from the charset based on the specified comparison mode.
     * If two characters have the same brightness, the character with the smallest ASCII value is returned.
     *
     * @param brightness The brightness value of the image segment.
     * @return The character from the charset that best matches the given brightness.
     */
    public char getCharByImageBrightness(double brightness) {
        Map.Entry<Double, TreeSet<Character>> lower = normalizedBrightnessMap.floorEntry(brightness);
        Map.Entry<Double, TreeSet<Character>> higher = normalizedBrightnessMap.ceilingEntry(brightness);

        Map.Entry<Double, TreeSet<Character>> closest;
        switch (comparisonMode) {
            case CLOSEST_HIGHER:
                closest = (higher != null) ? higher : lower;
                break;

            case CLOSEST_LOWER:
                closest = (lower != null) ? lower : higher;
                break;

            case CLOSEST_ABSOLUTE:
            default:
                if (lower == null) {
                    closest = higher;
                } else if (higher == null) {
                    closest = lower;
                } else {
                    closest = (Math.abs(lower.getKey() - brightness) <= Math.abs(higher.getKey() -brightness))
                            ? lower : higher;
                }
                break;
        }

        // Return the character according to the comparison mode
        return closest.getValue().first();
    }

    /**
     * Sets the comparison mode used for determining the best-matching character based on brightness.
     *
     * @param mode The comparison mode to be used (e.g., CLOSEST_HIGHER, CLOSEST_LOWER, CLOSEST_ABSOLUTE).
     */
    public void setComparisonMode(ComparisonMode mode) {
        if (mode != null){
            comparisonMode = mode;
        }
    }

    /**
     * Adds a character to the charset and associates it with its calculated brightness.
     * If the brightness already exists, the character is added to the corresponding set.
     *
     * @param c The character to be added to the charset.
     */
    public void addChar(char c) {
        double rawBrightness = calculateRawBrightness(c);
        rawBrightnessMap.computeIfAbsent(rawBrightness, k -> createAsciiSortedSet()).add(c);
        // Update min and max brightness if needed
        if (rawBrightness < minBrightness || rawBrightness > maxBrightness) {
            minBrightness = Math.min(minBrightness, rawBrightness);
            maxBrightness = Math.max(maxBrightness, rawBrightness);

            // Rebuild normalized brightness map
            BuildNormalizedBrightnessMap();
            return;
        }
        normalizedBrightnessMap.computeIfAbsent(calculateNormalizedBrightness(rawBrightness)
                , k -> createAsciiSortedSet()).add(c);
    }

    /**
     * Removes a character from the charset and its associated brightness.
     * If the character is the last one associated with its brightness, the brightness entry is removed.
     *
     * @param c The character to be removed from the charset.
     */
    public void removeChar(char c) {
        double rawBrightness = calculateRawBrightness(c);
        double normalizedBrightness = calculateNormalizedBrightness(rawBrightness);
        TreeSet<Character> charSetRawBrightness = rawBrightnessMap.get(rawBrightness);
        TreeSet<Character> charSetNormalizedBrightness = normalizedBrightnessMap.get(normalizedBrightness);

        if (charSetRawBrightness != null && charSetRawBrightness.remove(c)) {
            charSetNormalizedBrightness.remove(c); // Remove the char from normalized brightness map as well
            if (charSetRawBrightness.isEmpty()) {
                rawBrightnessMap.remove(rawBrightness);
                normalizedBrightnessMap.remove(rawBrightness);

                // Recalculate min and max brightness only if bounds are affected
                if (rawBrightness == minBrightness || rawBrightness == maxBrightness) {
                    minBrightness = Double.MAX_VALUE;
                    maxBrightness = Double.MIN_VALUE;

                    for (Double brightness : rawBrightnessMap.keySet()) {
                        minBrightness = Math.min(minBrightness, brightness);
                        maxBrightness = Math.max(maxBrightness, brightness);
                    }
                    // Rebuild normalized brightness map
                    BuildNormalizedBrightnessMap();
                }
            }
        }
    }

    /*
     * Initializes the brightness maps with the given charset.
     * Computes both raw and normalized brightness values for all characters.
     */
    private void initializeBrightnessMap(char[] charset) {
        for (char c : charset) {
            double rawBrightness = calculateRawBrightness(c);
            rawBrightnessMap.computeIfAbsent(rawBrightness, k -> createAsciiSortedSet()).add(c);

            minBrightness = Math.min(minBrightness, rawBrightness);
            maxBrightness = Math.max(maxBrightness, rawBrightness);
        }

        BuildNormalizedBrightnessMap();
    }

    /*
     * Rebuilds the brightness map using the raw brightness map.
     * Normalized brightness values are recalculated based on the current min and max brightness.
     */
    private void BuildNormalizedBrightnessMap() {
        normalizedBrightnessMap.clear();

        for (Map.Entry<Double, TreeSet<Character>> entry : rawBrightnessMap.entrySet()) {
            double rawBrightness = entry.getKey();
            double normalizedBrightness = calculateNormalizedBrightness(rawBrightness);

            for (char c : entry.getValue()) {
                normalizedBrightnessMap.computeIfAbsent(normalizedBrightness, k ->
                        createAsciiSortedSet()).add(c);
            }
        }
    }

    /*
     * Creates a TreeSet sorted by ASCII values.
     */
    private TreeSet<Character> createAsciiSortedSet() {
        return new TreeSet<>(Comparator.comparingInt(c -> (int) c));
    }

    /*
     * Calculates the raw brightness of a given character.
     * The raw brightness is based on the number of 'true' pixels in the character's representation matrix.
     */
    private double calculateRawBrightness(char c) {
        boolean[][] booleanValuesMatrix = CharConverter.convertToBoolArray(c);
        int numOfTrues = 0;

        for (boolean[] booleanValuesRow : booleanValuesMatrix) {
            for (boolean b : booleanValuesRow) {
                if (b) {
                    numOfTrues++;
                }
            }
        }

        return (double) numOfTrues / CharConverter.DEFAULT_PIXEL_RESOLUTION;
    }

    /*
     * Calculates the normalized brightness of a given raw brightness value.
     * Normalization is done using the current min and max brightness values.
     */
    private double calculateNormalizedBrightness(Double rawBrightness) {
        return (rawBrightness - minBrightness) / (maxBrightness - minBrightness);
    }
}
