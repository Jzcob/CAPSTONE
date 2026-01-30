package me.jacob.capstone.Utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class PasswordUtils {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a random salt for a new user.
     * @return 16-byte random salt
     */
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password using the provided salt.
     * @param password The plain text password (e.g. "admin123")
     * @param salt The user's specific salt
     * @return The hashed bytes
     */
    public static byte[] hashPassword(String password, byte[] salt) {
        char[] passwordChars = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, ITERATIONS, KEY_LENGTH);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        } finally {
            // Security best practice: wipe the password from memory
            Arrays.fill(passwordChars, '\0');
        }
    }

    /**
     * Verifies if a provided password matches the stored hash.
     * @param providedPassword The plain text input from the login box
     * @param salt The salt retrieved from the database
     * @param storedHash The hash retrieved from the database
     * @return true if they match, false otherwise
     */
    public static boolean verifyPassword(String providedPassword, byte[] salt, byte[] storedHash) {
        byte[] calculatedHash = hashPassword(providedPassword, salt);
        return Arrays.equals(calculatedHash, storedHash);
    }
}