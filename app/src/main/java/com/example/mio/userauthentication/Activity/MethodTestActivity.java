package com.example.mio.userauthentication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mio.userauthentication.Model.type.VibrateType;
import com.example.mio.userauthentication.R;

public class MethodTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_test);
    }

    public void buttonNormalClick(View view) {
        Intent intent= new Intent();
        intent.setClass(MethodTestActivity.this, InputNomalActivity.class);
        startActivity(intent);
    }

    public void buttonVibrateType1Click(View view) {
        Intent intent= new Intent();
        intent.setClass(MethodTestActivity.this, InputVibrateOneActivity.class);
        startActivity(intent);
    }

    public void buttonVibrateType2BinClick(View view) {
        Intent intent= new Intent();
        intent.putExtra("TestType", VibrateType.VIBRATE_TYPE_BIN);
        startVibrateType2Activity(intent);
    }

    public void buttonVibrateType2MorsClick(View view) {
        Intent intent= new Intent();
        intent.putExtra("TestType", VibrateType.VIBRATE_TYPE_MORS);
        startVibrateType2Activity(intent);
    }

    public void buttonVibrateType2SelClick(View view) {
        Intent intent= new Intent();
        intent.putExtra("TestType", VibrateType.VIBRATE_TYPE_SEL);
        startVibrateType2Activity(intent);
    }

    public void buttonVibrateType3Click(View view) {
        /*
        Intent intent= new Intent();
        intent.setClass(MethodTestActivity.this, InputVibrateTwoActivity.class);
        startActivity(intent);

         */
    }

    protected void startVibrateType2Activity(Intent intent){
        intent.setClass(MethodTestActivity.this, InputVibrateTwoActivity.class);
        startActivity(intent);
    }

}
