package nl.tudelft.mmsr.privacy.encryption;


/**
 * Created by ptekieli on 7/1/17.
 */
public interface DecryptionStrategy {
    EncryptionPack decryptImageRegions(String pathToDecrypt);
}
