package com.example.mio.userauthentication.Model.sensordata;

public class GyroscopeData {
    float x;
    float y;
    float z;
    int accuracy;
    double timeStamp;

    public GyroscopeData(){
        x=0;
        y=0;
        z=0;
        accuracy=0;
        timeStamp=(double)System.currentTimeMillis()*1e-3;
    }

    public GyroscopeData(float x, float y, float z, long startTime, int accuracy){
        this.x=x;
        this.y=y;
        this.z=z;
        this.timeStamp = System.currentTimeMillis() - startTime;
        this.timeStamp = this.timeStamp * 1e-3;
        this.accuracy = accuracy;
    }

    public GyroscopeData(float x, float y, float z, double timeStamp, int accuracy){
        this.x=x;
        this.y=y;
        this.z=z;
        this.timeStamp = timeStamp;
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return String.format("GYRO;%.3f;%f;%f;%f;%d;\n",this.timeStamp,this.x,this.y,this.z,this.accuracy);
        //return "GYRO;"+ timeStamp + ";" + this.x + ";"+ this.y + ";"+ this.z + ";"+ this.accuracy + ";\n" ;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(double timeStamp) {
        this.timeStamp = timeStamp;
    }

}
