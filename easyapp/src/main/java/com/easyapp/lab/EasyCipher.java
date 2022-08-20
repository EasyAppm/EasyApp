package com.easyapp.lab;
import com.easyapp.cipher.CesarCipher;
import com.easyapp.core.TypeValidator;
import com.easyapp.util.SimpleBase64;

public class EasyCipher {

    public static String encrypt(String text, int cesarKey) {
        return encrypt(text, cesarKey, false);
    }

    public static String encrypt(String text, int cesarKey, boolean reverse) {
        return encrypt(text, cesarKey, reverse, 0);
    }

    public static String encrypt(String text, int cesarKey, boolean reverse, int amountBase) {
        String value = CesarCipher.encrypt(TypeValidator.nonNull(text), cesarKey);
        if (amountBase > 0) {
            for (int i = 1; i <= amountBase; i++) {
                value = SimpleBase64.encodeToString(value);
            }
        }
        if (reverse) {
            return new StringBuilder(value).reverse().toString();
        } else {
            return value;
        }
    }

    public static String decrypt(String text, int cesarKey) {
        return decrypt(text, cesarKey, false);
    }

    public static String decrypt(String text, int cesarKey, boolean reverse) {
        return decrypt(text, cesarKey, reverse, 0);
    }
    
    public static String decrypt(String text, int cesarKey, boolean reverse, int amountBase) {
        String value = reverse ? new StringBuilder(text).reverse().toString() : text;
        if (amountBase > 0) {
            for (int i = 1; i <= amountBase; i++) {
                value = SimpleBase64.decodeToString(value);
            }
        }
        return CesarCipher.decrypt(value, cesarKey);
    }
}
