package TestingGrounds.Utilities;

import java.security.SecureRandom;

public class TokenGenerator {
    private static final String BASE36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        return generatePart(3) + "-" + generatePart(4);
    }

    private static String generatePart(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(BASE36.charAt(random.nextInt(BASE36.length())));
        }
        return builder.toString();
    }
}