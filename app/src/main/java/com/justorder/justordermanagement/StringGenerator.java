package com.justorder.justordermanagement;

import java.math.BigInteger;
import java.security.SecureRandom;

public class StringGenerator {

    private SecureRandom random = new SecureRandom();

    public String createStringID(Integer radix) {
        return new BigInteger(130, random).toString(radix);
    }

    public String createStringID(Integer radix, Integer lastIndex) {
        return new BigInteger(130, random).toString(radix).substring(0,lastIndex);
    }

}
