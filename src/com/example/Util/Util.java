package com.example.Util;

import java.util.Arrays;

public class Util {
    public static int getTimeInInt(String time)
    {
        String[] timeSplits = time.split(":");

        StringBuilder temp = new StringBuilder();

        for (String s : timeSplits) {
            temp.append(s);
        }

        return Integer.parseInt(temp.toString());
    }
}
