package com.example.mio.userauthentication.Model.auth;

public class VibrateTestData {
    int vibrateType = 0;

    long [] vibratePattern = null;

    int vibrateChar = 0;

    double vibrateTime = 0;

    //按下Push键的时刻
    double inputStartTime = 0;

    //按下数字键的时刻
    double inputEndTime = 0;

    double inputTime = 0;

    double recognizeTime = 0;

    int inputChar = 0;

    boolean correction = true;

    public VibrateTestData(int vibrateType){
        this.vibrateType = vibrateType;
    }

    public int getVibrateType() {
        return vibrateType;
    }

    public void setVibrateType(int vibrateType) {
        this.vibrateType = vibrateType;
    }

    public long[] getVibratePattern() {
        return vibratePattern;
    }

    public void setVibratePattern(long[] vibratePattern) {
        this.vibratePattern = vibratePattern;
    }

    public int getVibrateChar() {
        return vibrateChar;
    }

    public void setVibrateChar(int vibrateChar) {
        this.vibrateChar = vibrateChar;
    }

    public double getVibrateTime() {
        return vibrateTime;
    }

    public void setVibrateTime(double vibrateTime) {
        this.vibrateTime = vibrateTime;
    }

    public double getInputTime() {
        return inputTime;
    }

    public void setInputTime(double inputTime) {
        this.inputTime = inputTime;
    }

    public int getInputChar() {
        return inputChar;
    }

    public void setInputChar(int inputChar) {
        this.inputChar = inputChar;
    }

    public double getInputStartTime() {
        return inputStartTime;
    }

    public void setInputStartTime(double inputStartTime) {
        this.inputStartTime = inputStartTime;
    }

    public double getInputEndTime() {
        return inputEndTime;
    }

    public void setInputEndTime(double inputEndTime) {
        this.inputEndTime = inputEndTime;
    }

    public boolean isCorrection() {
        return correction;
    }

    public boolean testCorrection(){
        this.correction = (this.inputChar==this.vibrateChar);
        return this.correction;
    }

    public void calRecognizeTime(){
        this.recognizeTime = this.inputTime - this.vibrateTime;
    }

    public double getRecognizeTime() {
        return recognizeTime;
    }

    public String getEvalInfo1(){
        return String.format("%d;%d;%.3f;%.3f;%.3f;%.3f;%.3f;%b;\n",this.vibrateChar,this.inputChar,this.inputStartTime,this.inputEndTime,this.inputTime,this.vibrateTime,this.recognizeTime,this.correction);
    }

    public String getEvalInfo(){
        return String.format("%d\t%d\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%b;\n",this.vibrateChar,this.inputChar,this.inputStartTime,this.inputEndTime,this.inputTime,this.vibrateTime,this.recognizeTime,this.correction);
    }
}
