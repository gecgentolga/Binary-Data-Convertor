import java.text.DecimalFormat;

public class BinaryDataConverter {
    static long exponent;
    static double mantissa;
    static boolean normalised = true;

    public static int BinaryToSigned(String binary) {
        int length = binary.length();
        boolean isNegative = binary.charAt(0) == '1';
        int value = 0;


        for (int i = 0; i < length; i++) {
            if (binary.charAt(i) == '1') {
                value += (1 << (length - 1 - i));
            }
        }

        // Two's complement
        if (isNegative) {
            value -= (1 << length);
        }

        return value;
    }

    // Binary to Unsigned
    public static long BinaryToUnsigned(String binary) {
        long value = 0;
        int length = binary.length();
        for (int i = 0; i < length; i++) {
            if (binary.charAt(i) == '1') {
                value += (1L << (length - 1 - i));
            }
        }
        return value;
    }



    // Binary to Floating Point Number Conversion
    public static String BinaryToFloatingPointNumber(String binary, int size) {
        int exponentBitsNumber;
        String exponentBits = "";

        // Determine exponent bit size based on total size
        if (size == 1) {
            exponentBitsNumber = 4;
            exponentBits = binary.substring(1,5);
        } else if (size == 2) {
            exponentBitsNumber = 6;
            exponentBits = binary.substring(1,7);
        } else if (size == 3) {
            exponentBitsNumber = 8;
            exponentBits = binary.substring(1,9);
        } else if (size == 4) {
            exponentBitsNumber = 10;
            exponentBits = binary.substring(1,11);
        } else {
            throw new IllegalArgumentException("Invalid size for floating point conversion");
        }

        if (binary.length() != size * 8) {
            throw new IllegalArgumentException("Binary string length does not match the size specified");
        }

        // Extract sign bit
        int sign = (binary.charAt(0) == '1') ? -1 : 1;

        // Extract exponent part

        exponent = BinaryToUnsigned(exponentBits);


        int bias = (1 << (exponentBitsNumber - 1)) - 1;


        long actualExponent = exponent - bias;


        // Extract mantissa and round to even
        String fractionBits = binary.substring(1 + exponentBitsNumber);
        if (fractionBits.length() > 13) {
            fractionBits = roundToEven(fractionBits);
        }

        if(!exponentBits.contains("1")){ // Determining whether the value is Denormalized or Special
            mantissa = 0.0;
            normalised = false;
            if(!fractionBits.contains("1")){
                if(sign == -1) return "-0";
                if(sign == 1) return "0";
            }
        }
        else mantissa = 1.0;
        if(!exponentBits.contains("0")){
            if(!fractionBits.contains("1")){
                if(sign == -1) return "-∞";
                if(sign == 1) return "∞";
            }
            else return "NaN";
        }



        for (int i = 0; i < fractionBits.length(); i++) {
            if (fractionBits.charAt(i) == '1') {

                mantissa += Math.pow(2, -(i + 1));
            }
        }



        // Adjusting the decimal point of a number
        DecimalFormat scientificFormat = new DecimalFormat("0.#####E0");
        String formattedScientific = scientificFormat.format(sign * mantissa * Math.pow(2, actualExponent));
        if(normalised == false) formattedScientific = scientificFormat.format(sign * mantissa * Math.pow(2, 1-bias));

        // If the number requires scientific notation (contains 'E'), format as scientific
        if (Math.abs(sign * mantissa * Math.pow(2, actualExponent)) >= 1e5 || Math.abs(sign * mantissa * Math.pow(2, actualExponent)) <= 1e-5) {

            return formattedScientific;
        } else {
            // Otherwise, format as normal decimal with up to 5 decimals
            DecimalFormat normalFormat = new DecimalFormat("0.#####");
            String formattedNormal = normalFormat.format(sign * mantissa * Math.pow(2, actualExponent));

            return formattedNormal;
        }
    }

    // Round to Even Method
    private static String roundToEven(String mantissaBits) {

        String roundedMantissa = mantissaBits.substring(0, 13);
        char nextBit = mantissaBits.charAt(13);

        if (nextBit == '1') { // Apply round to even rule

            int lastBitIndex = roundedMantissa.lastIndexOf('0');
            if (lastBitIndex != -1) {
                StringBuilder sb = new StringBuilder(roundedMantissa);
                sb.setCharAt(lastBitIndex, '1');
                for (int i = lastBitIndex + 1; i < sb.length(); i++) {
                    sb.setCharAt(i, '0');
                }
                roundedMantissa = sb.toString();
            }
        }

        return roundedMantissa;

    }
}
