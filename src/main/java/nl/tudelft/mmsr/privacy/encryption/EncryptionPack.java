package nl.tudelft.mmsr.privacy.encryption;

import java.util.ArrayList;

public class EncryptionPack {

    public ArrayList<byte[]> faces;
    public KeyPack keyPack;

    public EncryptionPack(ArrayList<byte[]> faces, KeyPack keyPack) {
        this.faces = faces;
        this.keyPack = keyPack;
    }
}