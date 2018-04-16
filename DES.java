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
public class DES extends SymmetricAlgo{
    @Override
    void initCipher(){
        CIPHER_SPEC = "DES/CBC/PKCS5Padding";
        CIPHER_ALGO = "DES";
        IV_LENGTH = 8;
    }
    
    @Override
    public boolean checkKey(){
        return true;
    }
}
