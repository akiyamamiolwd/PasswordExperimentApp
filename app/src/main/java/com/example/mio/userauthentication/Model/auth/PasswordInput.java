package com.example.mio.userauthentication.Model.auth;

import java.util.ArrayList;

public class PasswordInput extends Password {
    ArrayList<InputData> inputDatas = new ArrayList<>();

    double passwordInputTime = 0;

    public PasswordInput(){
        super();
    }

    public PasswordInput(int length){
        super(length);
    }

    public void addInputData(InputData data){
        this.inputDatas.add(data);
        this.charactors.add(data.inputChar.charAt(0));
        calPassInputTime();
    }

    public void addInputDataBack(InputData data){
        this.inputDatas.get(inputDatas.size()).setBack(true);
        this.inputDatas.add(data);
        this.charactors.add(data.inputChar.charAt(0));
        calPassInputTime();
    }

    public double getInputTimeByIndex(int index){
        //可能存在密码回删的情况，用双指针定位
        double time = 0;
        //i是理想位置指针
        int i = 0;
        //j是实际位置指针
        int j = 0;
        while (i<=index){
            if(i == index){
                time += inputDatas.get(j).inputTime;
            }
            if(inputDatas.get(j).isBack()){
                j++;
            }
            else {
                i++;
                j++;
            }
        }
        return time;
    }

    public String getInputHintByIndex(int index){
        //可能存在密码回删的情况，用双指针定位
        //i是理想位置指针
        int i = 0;
        //j是实际位置指针
        int j = 0;
        while (i<index){
            if(inputDatas.get(j).isBack()){
                j++;
            }
            else {
                i++;
                j++;
            }
        }
        return inputDatas.get(j).hintChar;
    }

    public String getInputHintCharWithBlank(){
        return getInputHintCharWithInter(" ");
    }

    public String getInputHintCharWithInter(String inter){
        StringBuffer tmp = new StringBuffer(inter);
        for(InputData data : inputDatas){
            if(!data.isBack())
                tmp.append(data.getHintChar()).append(inter);
        }
        return tmp.toString();
    }

    public double calPassInputTime(){
        double tmp = 0;
        for(InputData ind : inputDatas){
            tmp += ind.getInputTime();
        }
        this.passwordInputTime = tmp;
        return this.passwordInputTime;
    }

    public double calPassInputTimeBySE(){
        double tmpS = inputDatas.get(0).inputStartTime;
        double tmpE = inputDatas.get(inputDatas.size()-1).inputEndTime;
        this.passwordInputTime = tmpE - tmpS;
        return this.passwordInputTime;
    }

}
