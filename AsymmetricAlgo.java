/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skyey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author ndhpr
 */
public abstract class AsymmetricAlgo extends Crypt implements KeyFile{
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String keyFile;
    
    // abstract method - template pattern - template method
    abstract void initCipher();
    
    @Override
    public void setKey(String keyFile){
        this.keyFile = keyFile;
    }

    @Override
    public void keygen(){
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CIPHER_ALGO);
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            // create public key and private key (1024 bits)
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException ex) {
        }
    }
    
    @Override
    public void saveKey(){ 
        File file = new File(keyFile);
        try (OutputStream f = new FileOutputStream(file)) {
            f.write(privateKey.getEncoded());
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }
    
    @Override
    public void readKey(){
        try {
            File file = new File(keyFile);
            byte[] tmp = null;
            try (InputStream f = new FileInputStream(file)) {
                tmp = new byte[f.available()];
                f.read(tmp);
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(tmp);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            privateKey = factory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
        }
    }
    
    private void initEncrypt(PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException{
        cipher = Cipher.getInstance(CIPHER_SPEC);
        cipher.init(Cipher.ENCRYPT_MODE, key); 
    }
    
    private void initDecrypt(PrivateKey key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
        cipher = Cipher.getInstance(CIPHER_SPEC);
        cipher.init(Cipher.DECRYPT_MODE, key);
    }
    
    @Override
    protected void encrypt(){
        try {
            initCipher();
            keygen();
            saveKey();
            openFile(".en");
            initEncrypt(publicKey);
            writeCipher();
        } catch (FileNotFoundException ex) {
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
        }
    }

    @Override
    protected void decrypt() {
        try {
            initCipher();
            readKey();
            openFile("");
            initDecrypt(privateKey);
            writeCipher();
        } catch (FileNotFoundException ex) {
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
        }
    }
}
