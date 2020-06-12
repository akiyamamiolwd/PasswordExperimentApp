package com.example.mio.userauthentication.util;

import java.util.ArrayList;
import java.util.HashMap;

public class VibratePattern {

    final static int longPattern = 500;
    final static int shortPattern = 200;
    final static int sleepPattern = 100;

    public static final String hintMorsPattern = "震动模式代表的数字如下表(0代表短震动，1代表长震动)：\n" +
            "0: 11111  1: 01111  2: 00111\n" +
            "3: 00011  4: 00001  5: 00000\n" +
            "6: 10000  7: 11000  8: 11100\n" +
            "9: 11110\n";

    public static final String hintBinPattern = "震动模式代表的数字如下表(0代表短震动，1代表长震动)：\n" +
            "0: 0000  1: 0001  2: 0010\n" +
            "3: 0011  4: 0100  5: 0101\n" +
            "6: 0110  7: 0111  8: 1000\n" +
            "9: 1001\n";

    public static final String hintSelPattern = "震动模式代表的数字如下表(0代表短震动，1代表长震动)：\n" +
            "0: 1    1: 00   2: 01\n" +
            "3: 10   4: 001  5: 010\n" +
            "6: 100  7: 011  8: 101\n" +
            "9: 110\n";

    public static double getBinTypeLongestTime(){
        int [] longestPattern = {0,1,1,1};
        long longestTime = sumTime(longestPattern,longPattern,shortPattern,sleepPattern);
        return (double)longestTime*1E-3;
    }

    public static double getMorsTypeLongestTime(){
        int [] longestPattern = {1,1,1,1,1};
        long longestTime = sumTime(longestPattern,longPattern,shortPattern,sleepPattern);
        return (double)longestTime*1E-3;
    }

    public static double getSelTypeLongestTime(){
        int [] longestPattern = {1,1,0};
        long longestTime = sumTime(longestPattern,longPattern,shortPattern,sleepPattern);
        return (double)longestTime*1E-3;
    }

    public static int [][] getBinTypePatternByInt(){
        int [][] binPattern = {{0,0,0,0},{0,0,0,1},{0,0,1,0},{0,0,1,1},{0,1,0,0},{0,1,0,1},{0,1,1,0},{0,1,1,1},{1,0,0,0},{1,0,0,1}};
        return binPattern;
    }

    public static int [][] getSelTypePatternByInt(){
        int [][] selPattern = {{1},{0,0},{0,1},{1,0},{0,0,1}, {0,1,0},{1,0,0},{0,1,1},{1,0,1},{1,1,0}};
        return selPattern;
    }

    public static int [][] getMorsTypePatternByInt(){
        int [][] morsPattern = {{1,1,1,1,1},{0,1,1,1,1},{0,0,1,1,1},{0,0,0,1,1},{0,0,0,0,1},
                {0,0,0,0,0},{1,0,0,0,0},{1,1,0,0,0},{1,1,1,0,0},{1,1,1,1,0}};
        return morsPattern;
    }

    public static long sumTime(int [] pattern,long longPattern,long shortPattern,long sleepPattern){
        long sum=0;
        for(int i : pattern){
            if(i==0) sum+=shortPattern;
            else sum+=longPattern;
        }
        sum+=sleepPattern*(pattern.length-1);
        return sum;
    }

    public static HashMap<String,long[]> getSelTypePattern(){
        int [][] selPattern = {{1},{0,0},{0,1},{1,0},{0,0,1}, {0,1,0},{1,0,0},{0,1,1},{1,0,1},{1,1,0}};
        int [] longestPattern = {1,1,0};
        return getVibratePatternTimeSame(longestPattern,longPattern,shortPattern,sleepPattern,selPattern);
    }

    public static HashMap<String,long[]> getMorsTypePattern(){
        int [][] morsPattern = {{1,1,1,1,1},{0,1,1,1,1},{0,0,1,1,1},{0,0,0,1,1},{0,0,0,0,1},
                               {0,0,0,0,0},{1,0,0,0,0},{1,1,0,0,0},{1,1,1,0,0},{1,1,1,1,0}};
        int [] longestPattern = {1,1,1,1,1};
        return getVibratePatternTimeSame(longestPattern,longPattern,shortPattern,sleepPattern,morsPattern);
    }

    public static HashMap<String,long[]> getBinTypePattern(){
        int [][] binPattern = {{0,0,0,0},{0,0,0,1},{0,0,1,0},{0,0,1,1},{0,1,0,0},{0,1,0,1},{0,1,1,0},{0,1,1,1},{1,0,0,0},{1,0,0,1}};
        int [] longestPattern = {0,1,1,1};
        return getVibratePatternTimeSame(longestPattern,longPattern,shortPattern,sleepPattern,binPattern);
    }

    public static HashMap<String,long[]> getVibratePatternTimeSame(int[] longestPattern,long longPattern,long shortPattern,long sleepPattern,int [][] numPattern){
        HashMap<String,long[]> hintPattern = new HashMap<>();
        long longestTime = sumTime(longestPattern,longPattern,shortPattern,sleepPattern);
        for(int i = 0; i < numPattern.length; i++){
            long [] tmpPattern = new long[numPattern[i].length*2];
            tmpPattern[0] = longestTime - sumTime(numPattern[i],longPattern,shortPattern,sleepPattern);
            for(int j=0;j < numPattern[i].length; j++){
                tmpPattern[j*2+1] = numPattern[i][j] == 1?longPattern:shortPattern;
                if(j*2+2 < tmpPattern.length)
                    tmpPattern[j*2+2] = sleepPattern;
            }
            hintPattern.put(Integer.toString(i),tmpPattern);
        }
        return hintPattern;
    }
}
