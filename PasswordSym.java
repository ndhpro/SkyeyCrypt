/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skyey;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 *
 * @author ndhpro
 */
public abstract class PasswordSym extends Crypt{
    private static final String KEYGEN_SPEC = "PBKDF2WithHmacSHA1";
    private static final int AUTH_KEY_LENGTH = 8; 
    private static final int ITERATIONS = 32768;
    
    protected int SALT_LENGTH; 
    protected int KEY_LENGTH;
    
    private char[] password;
    private byte[] salt;
    private boolean passwordCheck;
    
    // abstract method - template pattern - template method
    abstract void initCipher();
    
    @Override
    protected void setKey(String password){
        this.password = password.toCharArray();
    }
    
    @Override
    public boolean checkKey(){
        return passwordCheck;
    }
    
    private class Keys {
        public final SecretKey encryption, authentication;
        public Keys(SecretKey encryption, SecretKey authentication) {
            this.encryption = encryption;
            this.authentication = authentication;
        }
    }
    
    // generate salt
    private byte[] generateSalt(int length) {
        Random r = new SecureRandom();
        salt = new byte[length];
        r.nextBytes(salt);
        return salt;
    }

    // generate key
    private Keys keygen() {
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(KEYGEN_SPEC);
        } catch (NoSuchAlgorithmException impossible) { return null; }
        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH + AUTH_KEY_LENGTH * 8);
        SecretKey tmp = null;
        try {
            tmp = factory.generateSecret(spec);
        } catch (InvalidKeySpecException impossible) { }
        byte[] fullKey = tmp.getEncoded();
        // authentication key to check key
        SecretKey authKey = new SecretKeySpec( Arrays.copyOfRange(fullKey, 0, AUTH_KEY_LENGTH), CIPHER_ALGO);
        // encryption key 
        SecretKey encKey = new SecretKeySpec( Arrays.copyOfRange(fullKey, AUTH_KEY_LENGTH, fullKey.length), CIPHER_ALGO);
        return new Keys(encKey, authKey);
    }
    
    @Override
    protected void encrypt(){
        try {
            initCipher();
            openFile(".en");
            // generate key
            salt = generateSalt(SALT_LENGTH);
            Keys keys = keygen();
            initEncrypt(keys.encryption);
            // write header
            output.write(salt);
            output.write(keys.authentication.getEncoded());
            output.write(iv);
            // write encrypted data
            writeCipher();
        } catch (FileNotFoundException ex) {   
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException | IOException ex) {            
        }
    }

    @Override
    protected void decrypt(){
        try {
            initCipher();
            openFile("");
            // read salt and authentication key
            salt = new byte[SALT_LENGTH];
            input.read(salt);
            Keys keys = keygen();
            byte[] authRead = new byte[AUTH_KEY_LENGTH];
            input.read(authRead);
            // check password
            passwordCheck = true;
            if (!Arrays.equals(keys.authentication.getEncoded(), authRead)) {
                passwordCheck = false;
                return ;
            }
            initDecrypt(keys.encryption);
            // write decrypted data
            writeCipher();
        } catch (FileNotFoundException ex) {
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
        }
    }
}
