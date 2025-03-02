package image_char_matching;

/**
 * Defines the comparison modes for determining the closest matching character
 * based on brightness values when mapping image brightness to characters.
 */
public enum ComparisonMode {
        /**
         * Selects the character with the smallest brightness value that is greater
         * than or equal to the specified brightness value.
         */
        CLOSEST_HIGHER,

        /**
         * Selects the character with the largest brightness value that is less
         * than or equal to the specified brightness value.
         */
        CLOSEST_LOWER,

        /**
         * Selects the character with the brightness value closest to the specified
         * brightness value, based on the absolute difference.
         */
        CLOSEST_ABSOLUTE
}
