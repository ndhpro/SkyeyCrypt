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
public interface KeyFile {
    abstract public void keygen();
    abstract public void saveKey();
    abstract public void readKey();
    abstract public void setKey(String keyFile);
}
