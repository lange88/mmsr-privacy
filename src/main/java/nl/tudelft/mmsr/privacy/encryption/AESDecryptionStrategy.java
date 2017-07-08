package nl.tudelft.mmsr.privacy.encryption;

import com.google.gson.Gson;
import nl.tudelft.mmsr.privacy.detection.FaceRectangle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

/**
 * Created by ptekieli on 7/1/17.
 */
public class AESDecryptionStrategy implements DecryptionStrategy{
    @Override
    public EncryptionPack decryptImageRegions(String pathToDecrypt, File RSAfile) {
        /* Prepare cipher */
        CipherOperations cipher = null;

        try {
            cipher = new CipherOperations();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        /* Based on indicated location, retrieve JSON file with essential data */
        Gson gson = new Gson();
        EncryptionPack encryptionPack = null;
        try {
            encryptionPack = gson.fromJson(new FileReader(pathToDecrypt), EncryptionPack.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* Decrypt IV & Key using RSA */
        Cipher RSAcipher = null;
        try {
            RSAcipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
        }
        // Read key file
        byte[] keyBytes = null;
        try {
            keyBytes = Files.readAllBytes(RSAfile.toPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Create decryption key
        PrivateKey privateKey = null;
        try {
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (InvalidKeySpecException ex) {
            ex.printStackTrace();
        }

        try {
            //Initialize cipher
            RSAcipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        }

        try {
            encryptionPack.keyPack.iv = RSAcipher.doFinal(encryptionPack.keyPack.iv);
            encryptionPack.keyPack.key = RSAcipher.doFinal(encryptionPack.keyPack.key);
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        }

        /* Test retrieved package */
        System.out.println(Arrays.toString(encryptionPack.keyPack.key));
        System.out.println(Arrays.toString(encryptionPack.keyPack.iv));
        System.out.println("There are: " + encryptionPack.countFaces() + " faces to recover.");

        /* Perform decryption */
        for (FaceRectangle faces : encryptionPack.faces) {
            try {
                faces.face = cipher.decryptFile(faces.face, encryptionPack.keyPack);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
        }

        return encryptionPack;
    }
}
