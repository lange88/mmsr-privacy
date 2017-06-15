import java.awt.image.BufferedImage;
import java.io.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher; 
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import com.google.gson.Gson;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class ImageCipher
{
	private Cipher cipher;
	
	public ImageCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	}

	public EncryptionPack encrypt(byte [] image) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		byte [] keyBytes = new byte[16];
		new Random().nextBytes(keyBytes);
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		
		byte [] ivBytes = new byte[16];
		new Random().nextBytes(ivBytes);
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		
		this.cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte [] encryptedImage = this.cipher.doFinal(image);
		
		return new EncryptionPack(encryptedImage, new KeyPack(keyBytes, ivBytes));
	}
	
	public byte [] decrypt(byte [] image, KeyPack keyPack) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		SecretKeySpec key = new SecretKeySpec(keyPack.key, "AES");
		IvParameterSpec iv = new IvParameterSpec(keyPack.iv);
		
		this.cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte [] decryptedImage = this.cipher.doFinal(image);
		
		return decryptedImage;
		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException 
	{
		ImageCipher cipher = new ImageCipher();
		// get image
		BufferedImage initialImage = null;
		try {
			initialImage = ImageIO.read(new File("src/putin.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// convert image to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(initialImage, "png", baos);
		byte[] image = baos.toByteArray();
		
		// encrypt
		EncryptionPack pack = cipher.encrypt(image);
		
		// save key file - key and iv 
		Gson gson = new Gson();
		String keyPackString = gson.toJson(pack.keyPack);
		PrintWriter out = new PrintWriter("src/key.json");
		out.write(keyPackString);	
		out.close();
		
		// retrieve key file
		KeyPack kp = gson.fromJson(new FileReader("src/key.json"), KeyPack.class);
		
		// testing
		System.out.println(Arrays.toString(pack.keyPack.key));
		System.out.println(Arrays.toString(pack.keyPack.iv));
		System.out.println(Arrays.toString(kp.key));
		System.out.println(Arrays.toString(kp.iv));
		
		// decryption 
		byte [] decryptedImage = cipher.decrypt(pack.image, kp);
		
		
	}
}


