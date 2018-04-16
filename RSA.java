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
public class RSA extends AsymmetricAlgo{
    @Override
    void initCipher(){
        CIPHER_SPEC = "RSA/ECB/PKCS1Padding";
        CIPHER_ALGO = "RSA";
    }
    
    @Override
    public boolean checkKey(){
        return true;
    }
}
