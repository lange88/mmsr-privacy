package nl.tudelft.mmsr.privacy.encryption;

import com.google.gson.Gson;
import nl.tudelft.mmsr.privacy.detection.FaceRectangle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;


/**
 * Created by jeroen on 6/28/17.
 */
public class AESEncryptionStrategy implements EncryptionStrategy {

    @Override
    public void encryptImageRegions(ArrayList<FaceRectangle> faceRectangles, String fileName, File RSAfile) {

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

        /* Perform RSA on IV & Key */
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
        // Create encryption key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        PublicKey publicKey = null;
        try {
            publicKey = kf.generatePublic(spec);
        } catch (InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
        try {
            //Initialize cipher
            RSAcipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        }

        try {
                /* Encrypt regions */
            pack.keyPack.iv = RSAcipher.doFinal(pack.keyPack.iv);
            pack.keyPack.key = RSAcipher.doFinal(pack.keyPack.key);
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
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
    }


}
