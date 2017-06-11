package com.justorder.justordermanagement;

import java.math.BigInteger;
import java.security.SecureRandom;

public class StringGenerator {

    private SecureRandom random = new SecureRandom();

    public String createStringID(Integer lastIndex) {
        return new BigInteger(130, random).toString(10).substring(0,lastIndex);
    }

}
