package com.example.mio.userauthentication.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mio.userauthentication.Model.sensordata.AccelData;
import com.example.mio.userauthentication.Model.sensordata.GravityData;
import com.example.mio.userauthentication.Model.sensordata.GyroscopeData;
import com.example.mio.userauthentication.R;
import com.example.mio.userauthentication.util.FileReadAndWrite;

import java.util.ArrayList;
import java.util.Date;

public class RecordSensorActivity extends AppCompatActivity {

    String filePath = "";
    String accelFile = "data_accel.txt";
    String gyroscopeFile = "data_gyroscope.txt";
    String gravityFile = "data_gravity.txt";

    Date startTime;
    String testName = "RecordSensorTest";

    ArrayList<AccelData> accelData = new ArrayList<AccelData>();
    ArrayList<GyroscopeData> gyroscopeData = new ArrayList<GyroscopeData>();
    ArrayList<GravityData> gravityData = new ArrayList<GravityData>();

    SensorManager sensorManager;
    MySensorEventListener mySensorEventListener = new MySensorEventListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //动态申请读写权限
        FileReadAndWrite.verifyStoragePermissions(this);
        //获取传感器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    protected void initFileName(){
        startTime = new Date();
        String timeString = startTime.toString().replaceAll(":","");
        filePath = "UserAuthTest/" + testName + "_" + timeString + "/";
    }

    @Override
    protected void onResume() {
        initFileName();

        //注册监听传感器
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mySensorEventListener,accelSensor,sensorManager.SENSOR_DELAY_FASTEST);

        Sensor gravSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(mySensorEventListener,gravSensor,sensorManager.SENSOR_DELAY_FASTEST);

        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(mySensorEventListener,gyroSensor,sensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //暂时退出时如果有未写完的数据需要先写入文件
        if(accelData.size()>0){
            writeSensorData(Sensor.TYPE_ACCELEROMETER);
        }
        if(gravityData.size()>0){
            writeSensorData(Sensor.TYPE_GRAVITY);
        }
        if(gyroscopeData.size()>0){
            writeSensorData(Sensor.TYPE_GYROSCOPE);
        }
        sensorManager.unregisterListener(mySensorEventListener);
        super.onPause();
    }

    public void buttonWriteTest(View view) {
        FileReadAndWrite.writeFile("TestFile.txt","This is a test file");
    }

    //写传感器数据
    protected void writeSensorData(int sensorType){
        //System.out.println("Now write sensor data:");
        String tmpfile;
        StringBuffer tmpContent = new StringBuffer("");
        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:{
                tmpfile = accelFile;
                for(AccelData ad : accelData){
                    tmpContent.append(ad.toString());
                }
                accelData.clear();
                break;
            }
            case Sensor.TYPE_GYROSCOPE:{
                tmpfile = gyroscopeFile;
                for(GyroscopeData gyd : gyroscopeData){
                    tmpContent.append(gyd.toString());
                }
                gyroscopeData.clear();
                break;
            }
            case  Sensor.TYPE_GRAVITY:{
                tmpfile = gravityFile;
                for(GravityData grd : gravityData){
                    tmpContent.append(grd.toString());
                }
                gravityData.clear();
                break;
            }
            default:{
                tmpfile = "";
            }
        }
        if(!tmpfile.equals("")){
            FileReadAndWrite.writeFileWithPath(filePath, tmpfile, tmpContent.toString());
        }
    }

    private final class MySensorEventListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            //System.out.println("new Sensor data is come!!");
            int accuracy = event.accuracy;
            double tmpTime = System.currentTimeMillis() - startTime.getTime();
            tmpTime = tmpTime * 1E-3;
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:{
                    if(accelData.size()>1000){
                        writeSensorData(Sensor.TYPE_ACCELEROMETER);
                    }
                    accelData.add(new AccelData(x,y,z,tmpTime,accuracy));
                    break;
                }
                case Sensor.TYPE_GYROSCOPE:{
                    if(gyroscopeData.size()>1000){
                        writeSensorData(Sensor.TYPE_GYROSCOPE);
                    }
                    gyroscopeData.add(new GyroscopeData(x,y,z,tmpTime,accuracy));
                    break;
                }
                case  Sensor.TYPE_GRAVITY:{
                    if(gravityData.size()>1000){
                        writeSensorData(Sensor.TYPE_GRAVITY);
                    }
                    gravityData.add(new GravityData(x,y,z,tmpTime,accuracy));
                    break;
                }
                default:{

                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
