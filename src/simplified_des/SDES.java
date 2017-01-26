/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplified_des;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author someone2
 */
public class SDES {
    private String plainText;
    private String encryptedText;
    private String password;
    private char temp[] = new char[10];
    private char key[] = new char[10];
    private char key1[] = new char[8];
    private char key2[] = new char[8];
    
    public void SDES() {
        Scanner reader = new Scanner(System.in); 
        System.out.println("Give a 10-bit password: ");
        password = reader.nextLine();
        
        System.out.println("Give plain text: ");
        plainText = reader.nextLine();     
    }
    
    //generate the keys key1 and key2
    public void generateKeys(String password) {
        if(password.length() != 10) {
            System.out.println("Wrong password.");
            return ;
        }   
        key = password.toCharArray();
        
        
        // permutation P10
        char permutation10[] = new char[10];
        permutation10[0] = key[2];
        permutation10[1] = key[4];
        permutation10[2] = key[1];
        permutation10[3] = key[6];
        permutation10[4] = key[3];
        permutation10[5] = key[9];
        permutation10[6] = key[0];
        permutation10[7] = key[8];
        permutation10[8] = key[7];
        permutation10[9] = key[5];
        
        
        char left[] = new char[5];
        char right[] = new char[5];
        
        //both split to 5 bits and LS1(1-bit left rotation)
        for(int i=0; i<10; i++) {
            if(i<5){
                left[i] = permutation10[(i+1)%5];
                //System.out.println(left[i]);
            } else {
                right[i-5] = permutation10[((i-4)%5)+5];
                //System.out.println(right[i-5]);
            }
        }
        System.out.println("");
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        sb.append(right);
        
        char ls1[] = new char[10];
        ls1 = sb.toString().toCharArray();
        
        // permutation P8
        char permutation8[] = new char[8];
        permutation8[0] = ls1[5];
        permutation8[1] = ls1[2];
        permutation8[2] = ls1[6];
        permutation8[3] = ls1[3];
        permutation8[4] = ls1[7];
        permutation8[5] = ls1[4];
        permutation8[6] = ls1[9];
        permutation8[7] = ls1[8];
        
        key1 = permutation8;
        
        
        //perform LS2, 2-bit left shift 
        char left2[] = new char[5];
        char right2[] = new char[5];
        for(int i=0; i<10; i++) {
            if(i<5){
                left2[i] = ls1[(i+2)%5];
               //System.out.println(left2[i]);
            } else {
                right2[i-5] = ls1[((i-3)%5)+5];
                //System.out.println(right2[i-5]);
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(left2);
        sb2.append(right2);
        
        char ls2[] = new char[10];
        ls2 = sb2.toString().toCharArray();
        
        // permutation P8
        char second_permutation8[] = new char[8];
        second_permutation8[0] = ls2[5];
        second_permutation8[1] = ls2[2];
        second_permutation8[2] = ls2[6];
        second_permutation8[3] = ls2[3];
        second_permutation8[4] = ls2[7];
        second_permutation8[5] = ls2[4];
        second_permutation8[6] = ls2[9];
        second_permutation8[7] = ls2[8];
        
        key2 = second_permutation8;
        for(int i=0; i<8; i++){
            System.out.println(key2[i]);
        }
    }
    
    public String encrypt(String plainText) {
        char subPlainText[] = new char[8];
        StringBuilder cipherBuilder = new StringBuilder();
        for(int i=0; i<plainText.length(); i=i+8) {
            String valueString=plainText.substring(i, i+8);
            
            while(valueString.length() < 8) {
                valueString = "0"+valueString;
            }
            
            subPlainText = valueString.toCharArray();       
            
            char subCipherText[];
            subCipherText = IP(subPlainText);            
            subCipherText = FK(subCipherText,key1);            
            subCipherText = SW(subCipherText);            
            subCipherText = FK(subCipherText, key2);            
            subCipherText = IP_1(subCipherText);            
            cipherBuilder.append(subCipherText);
        }    
        return cipherBuilder.toString();
    }
    
    public String decrypt(String cipherText) {
        char subCipherText[] = new char[8];
        StringBuilder plainBuilder = new StringBuilder();
        for (int i = 0; i < cipherText.length(); i = i + 8) {
            String valueString = cipherText.substring(i, i + 8);

            while (valueString.length() < 8) {
                valueString = "0" + valueString;
            }

            subCipherText = valueString.toCharArray();

            char subPlainText[];
            subPlainText = IP(subCipherText);
            subPlainText = FK(subPlainText, key2);
            subPlainText = SW(subPlainText);
            subPlainText = FK(subPlainText, key1);
            subPlainText = IP_1(subPlainText);
            plainBuilder.append(subPlainText);
        }
        return plainBuilder.toString();
    }    
    
    //permutation
    private char[] IP(char[] temp){
            char ip[] = new char[8];
		
	    ip[0] = temp[1];
	    ip[1] = temp[5];
	    ip[2] = temp[2];
	    ip[3] = temp[0];
	    ip[4] = temp[3];
	    ip[5] = temp[7];
	    ip[6] = temp[4];
	    ip[7] = temp[6];
		
	    return ip;
    }
    
    //inverse permutation
    public char[] IP_1( char temp[] ) {
		char ip_1[] = new char[8];
		
		ip_1[0] = temp[3];
		ip_1[1] = temp[0];
		ip_1[2] = temp[2];
		ip_1[3] = temp[4];
		ip_1[4] = temp[6];
		ip_1[5] = temp[1];
		ip_1[6] = temp[7];
		ip_1[7] = temp[5];
		
		return ip_1;
	}
    
    //the switch function.
    private char[] SW(char[] temp) {
        char sw[] = new char[8];
        
        //interchanges the left and right 4 bits of the 8-bit input
        for(int i=0; i<4; i++) {
           sw[i] = temp[i+4];
           sw[i+4] = temp[i];
        }
        
        return sw;       
    }
    
    //the fk function
    private char[] FK(char[] temp, char[] key) {
        char fk[] = new char[8];
        char left[] = new char[4];
        char right[] = new char[4];

        // split the 8-bits to 4-bits arrays
        for (int i = 0; i < 4; i++) {
                left[i] = temp[i];
                right[i] = temp[i+4];            
        }
        
        //perform E/P
        char ep[] = EP(right);
        
        //perform XOR with the K1
        char xor1[] = XOR(ep,key);
        
        //split the xor-result to 4-bit arrays
        char leftXor[] = new char[4];
        char rightXor[] = new char[4];
        
        for (int i=0; i < 4; i++) {
                leftXor[i] = xor1[i];
                rightXor[i] = xor1[i+4];            
        }
        
        //run the SBoxes
        char resultSB0[] = S0(leftXor);
        char resultSB1[] = S1(rightXor);
        
        StringBuilder sb = new StringBuilder();
        sb.append(resultSB0);
        sb.append(resultSB1);
        
        char resultSBox[] = sb.toString().toCharArray();
        
        //perform P4
        char p4[] = P4(resultSBox);
        //perform the final XOR
        char xor2[] = XOR(left,p4);
        
        sb = new StringBuilder();
        sb.append(xor2);
        sb.append(right);   
        
        //output
        fk=sb.toString().toCharArray();
        
        return fk;
    }

    //the E/P function
    private char[] EP(char[] temp) {
        char result[] = new char[8];
        
        result[0] = temp[3];
        result[1] = temp[0];
        result[2] = temp[1];
        result[3] = temp[2];
        result[4] = temp[1];
        result[5] = temp[2];
        result[6] = temp[3];
        result[7] = temp[0];
        
        
        return result;
    }

    private char[] XOR(char[] temp, char[] temp2) {
        char result[] = new char[temp.length];
        
        for(int i=0; i<temp.length; i++) {
            if(temp[i] == temp2[i]) {
                result[i] = '0';
            } else {
                result[i] = '1';
            }
        }
        
        return result;
   }
    
    private char[] S0(char temp[]) {

        int SB0[][] = {
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 2}};
        
        //The first and fourth input bits are treated as a 2-bit number
        //that specify a row of the S-box and the second and third input bits 
        //specify a column of the Sbox.
        char row[] = new char[2]; 
        char column[] = new char[2];
        
        row[0] = temp[0];
        row[1] = temp[3];
        column[0] = temp[1];
        column[1] = temp[2];
        
        
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb1.append(row[0]);
        sb1.append(row[1]);
        sb2.append(column[0]);
        sb2.append(column[1]);
                
        int sElement = SB0[Integer.parseInt(sb1.toString() , 2)][Integer.parseInt(sb2.toString(), 2)];
        String stringElement = Integer.toBinaryString(sElement);
        if(stringElement.length()==1) stringElement = "0"+stringElement;
        
        char result[] = stringElement.toCharArray();
        
        return result;
    }
    
    private char[] S1(char temp[]) {

        int SB1[][] = {
            {0, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3}};

        //The first and fourth input bits are treated as a 2-bit number
        //that specify a row of the S-box and the second and third input bits 
        //specify a column of the Sbox.
        char row[] = new char[2];
        char column[] = new char[2];

        row[0] = temp[0];
        row[1] = temp[3];
        column[0] = temp[1];
        column[1] = temp[2];
        
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb1.append(row[0]);
        sb1.append(row[1]);
        sb2.append(column[0]);
        sb2.append(column[1]);

        int sElement = SB1[Integer.parseInt(sb1.toString(), 2)][Integer.parseInt(sb2.toString(), 2)];
        String stringElement = Integer.toBinaryString(sElement);
        if (stringElement.length() == 1) {
            stringElement = "0" + stringElement;
        }

        char result[] = stringElement.toCharArray();

        return result;
    }
    
    private char[] P4(char temp[]) {
        char result[] = new char[4];

        result[0] = temp[1];
        result[1] = temp[3];
        result[2] = temp[2];
        result[3] = temp[0];

        return result;
    }
    
}
