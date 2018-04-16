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
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author ndhpr
 */
abstract public class Crypt {
    protected String CIPHER_SPEC;
    protected String CIPHER_ALGO;
    protected int IV_LENGTH;
    protected static final int BUFFER_SIZE = 1024;
    
    protected Cipher cipher;
    protected byte[] iv;
    
    protected InputStream input;
    protected OutputStream output;
    protected String fileName;
    protected String desFile;
    
    // abstract method
    protected abstract void setKey(String tmp);
    protected abstract void encrypt();
    protected abstract void decrypt();
    protected abstract boolean checkKey();
    
    protected void setFileName(String fileName){
        this.fileName = fileName;
    }
    
    protected void openFile(String tag) throws FileNotFoundException{
        // change file extension
        StringBuilder fixedName = new StringBuilder(fileName);
        fixedName.delete(fixedName.indexOf("."), fixedName.length());
        fixedName.append(tag);
        desFile = fixedName.toString();
        File source = new File(fileName);
        File dest = new File(desFile);
        input = new FileInputStream(source);
        output = new FileOutputStream(dest);
    }
    
    protected void initEncrypt(SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException{
        cipher = Cipher.getInstance(CIPHER_SPEC);
        cipher.init(Cipher.ENCRYPT_MODE, key); 
        iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
    }
    
    protected void initDecrypt(SecretKey key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
        iv = new byte[IV_LENGTH]; 
        input.read(iv);
        cipher = Cipher.getInstance(CIPHER_SPEC);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    }
    
    protected boolean checkFile(){
        File file = new File(desFile);
        return file.length() != 0;
    }
    
    public String getDesFile(){
        return desFile;
    }
    
    protected void writeCipher() throws IOException, IllegalBlockSizeException, BadPaddingException{
        // write wiht buffer_size = 1024 bytes = 1 kB
        byte[] buffer = new byte[BUFFER_SIZE];
        int numRead;
        byte[] ciphered = null;
        while ((numRead = input.read(buffer)) > 0) {
            ciphered = cipher.update(buffer, 0, numRead);
            if (ciphered != null) {
                output.write(ciphered);
            }
        }
        // write final block
        ciphered = cipher.doFinal();
        if (ciphered != null) {
            output.write(ciphered);
        }
        // close file
        input.close();
        output.close();
    }
}
