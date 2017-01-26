/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplified_des;

/**
 *
 * @author someone2
 */
public class Simplified_des {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SDES s = new SDES();
        s.generateKeys("1100011110");
        String cipher = s.encrypt("00101000");
        System.out.println("Cipher: "+cipher);
        System.out.println("Decryption: "+s.decrypt(cipher));
    }
    
}
