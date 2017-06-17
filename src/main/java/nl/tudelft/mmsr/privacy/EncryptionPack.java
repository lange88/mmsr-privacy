package nl.tudelft.mmsr.privacy;

public class EncryptionPack {

	public byte [] image;
	public KeyPack keyPack;
	
	public EncryptionPack(byte [] image, KeyPack keyPack) {
		this.image = image;
		this.keyPack = keyPack;
	}
}

