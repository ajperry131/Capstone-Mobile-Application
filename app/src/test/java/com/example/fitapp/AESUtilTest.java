package com.example.fitapp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AESUtilTest {
    @Test
    public void givenString_whenEncrypt_thenSuccess()
            throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        String originalString = "testtext";

        String encryptedString = AESUtil.encrypt(originalString);
        String decryptedString = AESUtil.decrypt(encryptedString);

        assertEquals(originalString, decryptedString);
    }

    @Test
    public void givenKey_whenEncrypt_thenSuccess()
            throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        String firstOriginalString = "testtext1";
        String firstKey = "first";
        String encryptedString1 = AESUtil.encryptWithKey(firstOriginalString, firstKey);
        String decryptedString1 = AESUtil.decryptWithKey(encryptedString1, firstKey);

        String secondOriginalString = "testtext2";
        String secondKey = "second";
        String encryptedString2 = AESUtil.encryptWithKey(secondOriginalString, secondKey);
        String decryptedString2 = AESUtil.decryptWithKey(encryptedString2, secondKey);

        assertEquals(firstOriginalString, decryptedString1);
        assertEquals(secondOriginalString, decryptedString2);
        assertNotEquals(firstOriginalString, decryptedString2);
        assertNotEquals(secondOriginalString, decryptedString1);
    }
}