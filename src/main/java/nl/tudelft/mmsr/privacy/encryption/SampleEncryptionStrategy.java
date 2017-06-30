package nl.tudelft.mmsr.privacy.encryption;

import com.google.gson.Gson;
import javafx.scene.image.Image;
import nl.tudelft.mmsr.privacy.detection.FaceRectangle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


/**
 * Created by jeroen on 6/28/17.
 */
public class SampleEncryptionStrategy implements EncryptionStrategy {

    @Override
    public void encryptImageRegions(ArrayList<FaceRectangle> faceRectangles, String fileName) {

        CipherOperations cipher = null;

        try {
            cipher = new CipherOperations();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        EncryptionPack pack = null;

        /* Encrypt regions */
        try {
            pack = cipher.encryptFile(faceRectangles);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        if (pack == null) {
            return;
        }

        /* Save KeyPack package - Key & Initialization Vector */
        Gson gson = new Gson();
        String keyPackString = gson.toJson(pack);
        PrintWriter out = null;
        try {
            out = new PrintWriter(fileName + ".encrypted.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.write(keyPackString);
        out.close();

        /* retrieve key file
        KeyPack kp = gson.fromJson(new FileReader("src/key.json"), KeyPack.class);

        // testing
        System.out.println(Arrays.toString(pack.keyPack.key));
        System.out.println(Arrays.toString(pack.keyPack.iv));
        System.out.println(Arrays.toString(kp.key));
        System.out.println(Arrays.toString(kp.iv));

        // decryption
        //byte [] decryptedImage = cipher.decrypt(pack.image, kp);*/

    }


}
