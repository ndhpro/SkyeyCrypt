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
public class CryptFactory{
    public static Crypt getAlgorithm(boolean AES, boolean RSA, boolean DES, boolean Password){
        if (Password == true)
            if (AES == true) return new AESPw(); else
                if (DES == true) return new DESPw(); else
                    return new TripleDESPw();
        else
            if (AES == true) return new AES(); else
                if (DES == true) return new DES(); else
                    if (RSA == true) return new RSA(); else
                        return new TripleDES();
    }
}
