package nl.tudelft.mmsr.privacy.encryption;

import nl.tudelft.mmsr.privacy.detection.FaceRectangle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class CipherOperations
{
    private Cipher cipher;

    public CipherOperations() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    public EncryptionPack encryptFile(ArrayList<FaceRectangle> faceRectangles) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte [] keyBytes = new byte[16];
        new Random().nextBytes(keyBytes);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        byte [] ivBytes = new byte[16];
        new Random().nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        this.cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        for (FaceRectangle face : faceRectangles) {
            face.face = this.cipher.doFinal(face.face);
        }

        return new EncryptionPack(faceRectangles, new KeyPack(keyBytes, ivBytes));
    }

    public byte [] decryptFile(byte [] image, KeyPack keyPack) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SecretKeySpec key = new SecretKeySpec(keyPack.key, "AES");
        IvParameterSpec iv = new IvParameterSpec(keyPack.iv);

        this.cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte [] decryptedImage = this.cipher.doFinal(image);

        return decryptedImage;

    }
} 