package nl.tudelft.mmsr.privacy.encryption;

import com.google.gson.Gson;
import nl.tudelft.mmsr.privacy.detection.FaceRectangle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by ptekieli on 7/1/17.
 */
public class AESDecryptionStrategy implements DecryptionStrategy{
    @Override
    public EncryptionPack decryptImageRegions(String pathToDecrypt) {
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
