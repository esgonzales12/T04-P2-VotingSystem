package org.teamfour.system.data;

public class CipherData {
    private final String encryptionAlgorithm;
    private final String encryptionKey;
    private final String tokenIssuer;
    private final String tokenSecret;

    public CipherData(String encryptionAlgorithm, String encryptionKey, String tokenIssuer, String tokenSecret) {
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.encryptionKey = encryptionKey;
        this.tokenIssuer = tokenIssuer;
        this.tokenSecret = tokenSecret;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    @Override
    public String toString() {
        return "CipherData{" +
                "encryptionAlgorithm='" + encryptionAlgorithm + '\'' +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", tokenIssuer='" + tokenIssuer + '\'' +
                ", tokenSecret='" + tokenSecret + '\'' +
                '}';
    }
}
