package io.github.artsobol.shortlink.security.utils;

import io.github.artsobol.shortlink.exception.security.CryptoException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static io.jsonwebtoken.lang.Strings.toHex;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class TokenUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    private TokenUtils() {
        throw new AssertionError("Utility class");
    }

    public static String generateRawToken(int bytes) {
        byte[] buf = new byte[bytes];
        SECURE_RANDOM.nextBytes(buf);
        return ENCODER.encodeToString(buf);
    }

    public static String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(UTF_8));
            return toHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("crypto.sha256.notSupported", e);
        }
    }

    public static String hmacSha256Base64Url(String raw, String pepper) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    pepper.getBytes(UTF_8),
                    mac.getAlgorithm()
            );
            mac.init(secretKeySpec);
            byte[] hmac = mac.doFinal(raw.getBytes(UTF_8));
            return ENCODER.encodeToString(hmac);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("crypto.hmacSha256.notSupported", e);
        } catch (InvalidKeyException e) {
            throw new CryptoException("crypto.hmacSha256.invalidKey", e);
        }
    }
}
