package org.teamfour.system;

import org.teamfour.logging.LogBase;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.teamfour.system.data.CipherData;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.data.SystemFiles;
import org.teamfour.system.enums.Authority;
import org.teamfour.util.JsonUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SystemBase extends LogBase {
    private final CipherData cipherData;
    private final Metadata metadata;

    public SystemBase() {
        super(SystemBase.class.getName());
        cipherData = JsonUtil.getJsonObj(CipherData.class, SystemFiles.CIPHER);
        metadata = JsonUtil.getJsonObj(Metadata.class, SystemFiles.META);
    }
    protected JWTVerifier verifier() {
        return  JWT.require(Algorithm.HMAC256(cipherData.getTokenSecret()))
                .withIssuer(cipherData.getTokenIssuer())
                .build();
    }

    protected String issueToken(String username, Authority authority) {
        return JWT.create()
                .withIssuer(cipherData.getTokenIssuer())
                .withClaim("AUTHORITY", authority.toString())
                .sign(Algorithm.HMAC256(cipherData.getTokenSecret()));
    }

    protected String hash(String input) {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            log.warn("NO SHA-256 ALGORITHM FOUND");
            log.warn(e.getMessage());
            return null;
        }
        return hexString.toString();
    }

    protected String encrypt(String value) {
        byte[] encryptedValue;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    cipherData.getEncryptionKey().getBytes(),
                    cipherData.getEncryptionAlgorithm());
            Cipher cipher = Cipher.getInstance(cipherData.getEncryptionAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            encryptedValue = cipher.doFinal(value.getBytes());
        } catch (Exception e) {
            log.warn("UNABLE TO ENCRYPT");
            log.warn(e.getMessage());
            return null;
        }
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    protected String decrypt(String value) {
        byte[] decryptedValue;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    cipherData.getEncryptionKey().getBytes(),
                    cipherData.getEncryptionAlgorithm());
            Cipher cipher = Cipher.getInstance(cipherData.getEncryptionAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedValue = Base64.getDecoder().decode(value);
            decryptedValue = cipher.doFinal(decodedValue);
        } catch (Exception e) {
            log.warn("UNABLE TO DECRYPT");
            log.warn(e.getMessage());
            return null;
        }
        return new String(decryptedValue);
    }
}
