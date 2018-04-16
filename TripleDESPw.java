/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skyey;

/**
 *
 * @author ndhpro
 */
public class TripleDESPw extends PasswordSym{
    @Override
    void initCipher(){
        CIPHER_SPEC = "DESede/CBC/PKCS5Padding";
        CIPHER_ALGO = "DESede";
        SALT_LENGTH = 8;
        KEY_LENGTH = 192;
        IV_LENGTH = 8;
    }
}
