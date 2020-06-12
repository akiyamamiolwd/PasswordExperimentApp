package com.example.mio.userauthentication.Model.auth;

public class InputData {
    //输入时间
    double inputTime=0;
    //输入的字符
    String inputChar="";
    //方法的类型
    String methodType="";
    //提示的类型
    String hintType="";
    //提示的字符
    String hintChar="";
    //提示用时
    double hintTime = 0;
    //识别用时
    double recognizeTime=0;
    //按下Push键的时刻
    double inputStartTime = 0;
    //按下数字键的时刻
    double inputEndTime = 0;
    //是否被删除
    boolean isBack = false;

    public InputData(){ }

    public double getInputTime() {
        return inputTime;
    }

    public void calInputTime(){
        inputTime = inputEndTime - inputStartTime;
        calRecognizeTime();
    }

    public String getInputChar() {
        return inputChar;
    }

    public void setInputChar(String inputChar) {
        this.inputChar = inputChar;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getHintType() {
        return hintType;
    }

    public void setHintType(String hintType) {
        this.hintType = hintType;
    }

    public String getHintChar() {
        return hintChar;
    }

    public void setHintChar(String hintChar) {
        this.hintChar = hintChar;
    }

    public double getHintTime() {
        return hintTime;
    }

    public void setHintTime(double hintTime) {
        this.hintTime = hintTime;
        calRecognizeTime();
    }

    public double getInputStartTime() {
        return inputStartTime;
    }

    public void setInputStartTime(double inputStartTime) {
        this.inputStartTime = inputStartTime;
        calInputTime();
    }

    public double getInputEndTime() {
        return inputEndTime;
    }

    public void setInputEndTime(double inputEndTime) {
        this.inputEndTime = inputEndTime;
        calInputTime();
    }

    public void calRecognizeTime(){
        recognizeTime = inputTime - hintTime;
    }

    public double getRecognizeTime() {
        return recognizeTime;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public static String getEvalDataTxtExplain(){
        return "//方法名\t输入开始时间\t输入结束时间\t" +
                "提示时间\t输入时间\t识别时间\t" +
                "提示类型\t提示字符\t输入字符\t是否为回删\n";
    }

    public String getEvalDataTxt(){
        //需要的数据
        String format = "%s\t" +
                "%.3f\t%.3f\t" +
                "%.3f\t%.3f\t%.3f\t" +
                "%s\t%s\t%s\t%b\t\n";
        return String.format(format,this.methodType,
                this.inputStartTime,this.inputEndTime,
                this.hintTime,this.inputTime,this.recognizeTime,
                this.hintType,this.hintChar,this.inputChar,this.isBack);
    }
}
