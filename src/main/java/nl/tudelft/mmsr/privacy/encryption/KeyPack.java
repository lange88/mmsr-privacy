package nl.tudelft.mmsr.privacy.encryption;

/**
 * Created by ptekieli on 6/30/17.
 */
public class KeyPack {
    public byte [] key;
    public byte [] iv;

    public KeyPack(byte [] key, byte [] iv) {
        this.key = key;
        this.iv = iv;
    }
}
