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
public class AESPw extends PasswordSym{
    @Override
    void initCipher(){
        CIPHER_SPEC = "AES/CBC/PKCS5Padding";
        CIPHER_ALGO = "AES";
        SALT_LENGTH = 16;
        KEY_LENGTH = 128;
        IV_LENGTH = 16;
    }
}
