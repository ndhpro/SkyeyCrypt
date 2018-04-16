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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ndhpr
 */
public abstract class SymmetricAlgo extends Crypt implements KeyFile{
    private SecretKey key;
    private String keyFile;
    private boolean keyCheck;
    
    // abstract method - template pattern - template method
    abstract void initCipher();
    
    @Override
    public void setKey(String keyFile){
        this.keyFile = keyFile;
    }

    @Override
    public void keygen(){
        KeyGenerator keygenerator = null;
        try {
            keygenerator = KeyGenerator.getInstance(CIPHER_ALGO);
        } catch (NoSuchAlgorithmException ex) {
        }
        key = keygenerator.generateKey();
    }
    
    @Override
    public void saveKey(){ 
        File file = new File(keyFile);
        try (OutputStream f = new FileOutputStream(file)) {
            byte encoded[] = key.getEncoded();
            f.write(Base64.getEncoder().encodeToString(encoded).getBytes());
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            keyCheck = false;
        }
    }
    
    @Override
    public void readKey(){
        File file = new File(keyFile);
        byte[] tmp = null;
        try (InputStream f = new FileInputStream(file)) {
            tmp = new byte[f.available()];
            f.read(tmp);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        byte[] decodedKey = Base64.getDecoder().decode(tmp);
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, CIPHER_ALGO);
    }
    
    @Override
    protected void encrypt(){
        try {
            initCipher();
            keygen();
            saveKey();
            openFile(".en");
            initEncrypt(key);
            output.write(iv);
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
            initDecrypt(key);
            writeCipher();
        } catch (FileNotFoundException ex) {
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
        }
    }
    
    @Override
    protected boolean checkKey(){
        return keyCheck;
    }
}
