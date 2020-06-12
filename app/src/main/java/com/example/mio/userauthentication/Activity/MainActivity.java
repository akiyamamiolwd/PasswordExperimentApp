package com.example.mio.userauthentication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mio.userauthentication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonMethodTestClick(View view) {
        Intent intent= new Intent();
        intent.setClass(MainActivity.this, MethodTestActivity.class);
        startActivity(intent);
    }

    public void buttonInputTestClick(View view) {
        Intent intent= new Intent();
        intent.setClass(MainActivity.this, InputTestActivity.class);
        startActivity(intent);
    }

    public void buttonTestClick(View view) {
        Intent intent= new Intent();
        intent.setClass(MainActivity.this, RecordSensorActivity.class);
        startActivity(intent);
    }
}
