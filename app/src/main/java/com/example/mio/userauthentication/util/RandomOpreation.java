package com.example.mio.userauthentication.util;

import com.example.mio.userauthentication.Model.auth.Password;

import java.util.ArrayList;

public class RandomOpreation {

    /**
     * 生成一个0-N的随机整数
     * @param N 随机数的范围小于N
     * @return  随机数R
     */
    public static int getRandomNumInN(int N){
        return Double.valueOf(Math.random()*N).intValue();
    }

    /**
     * 生成一个均匀分布的的测试用PIN密码数组
     * @param testCaseNum   测试用的密码组数
     * @param passLen    密码长度
     * @return  均匀分布的的测试用数组
     */
    public static ArrayList<Password> generatePINPassTestCase(int testCaseNum, int passLen){
        ArrayList<Password> testPass = new ArrayList<>();
        int len = testCaseNum*passLen;
        int [] tmp = new int[len];
        for(int i=0;i < len; i++){
            tmp[i] = i % 10;
        }
        tmp = randomPostionOfIntArray(tmp);
        for(int j=0;j<testCaseNum;j++){
            Password tmpPass = new Password(passLen);
            for(int k=0;k<passLen;k++){
                tmpPass.addPassChara(tmp[j*passLen+k]);
            }
            testPass.add(tmpPass);
        }

        return testPass;
    }

    /**
     * 生成一个均匀分布的的测试用数组
     * @param testCaseNum   测试用例的容量，测试用的组数
     * @param maxTestNum    每组测试用例的数量
     * @return  均匀分布的的测试用数组
     */
    public static int[] generateVibrateTestCase(int testCaseNum,int maxTestNum){
        int len = testCaseNum*maxTestNum;
        int [] tmp = new int[len];
        for(int i=0;i < len; i++){
            tmp[i] = i % maxTestNum;
        }
        return tmp;
    }

    /**
     * 将一个整型数组的内容随机打乱，用于震动测试中随机生成要测试数字
     * @param a 需要打乱的数组
     * @return  打乱之后的数组
     */
    public static int[] randomPostionOfIntArray(int [] a){
        int len = a.length;
        do{
            int ra = getRandomNumInN(len);
            int tmp = a[ra];
            a[ra] = a[len-1];
            a[len-1] = tmp;
            len--;
        }while (len>0);

        return a;
    }
}
