package practice.util;

import practice.exception.SecureTokenConvertingException;

public class SecureTokenConverter {

    public static String convert (String secureToken) throws SecureTokenConvertingException {
        if (secureToken.equals("GoodToken")) {
            return "Processed Good Token";
        } else {
            throw new SecureTokenConvertingException("Not a valid token");
        }

    }
}
