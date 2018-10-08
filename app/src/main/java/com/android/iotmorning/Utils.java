package com.android.iotmorning;

import java.util.Random;


public class Utils {

    public static String getUniqueId() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(4);
        sb.append("Experiment_");
        for (int i = 0; i < 4; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }

}
