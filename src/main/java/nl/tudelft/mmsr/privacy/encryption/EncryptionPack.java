package nl.tudelft.mmsr.privacy.encryption;

import nl.tudelft.mmsr.privacy.detection.FaceRectangle;

import java.util.ArrayList;

public class EncryptionPack {

    public ArrayList<FaceRectangle> faces;

    public KeyPack keyPack;

    public EncryptionPack(ArrayList<FaceRectangle> faces, KeyPack keyPack) {
        this.faces = faces;
        this.keyPack = keyPack;
    }
}