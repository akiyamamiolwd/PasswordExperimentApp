package com.example.mio.userauthentication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mio.userauthentication.Model.type.VibrateType;
import com.example.mio.userauthentication.R;

public class InputTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_test);
    }

    public void buttonVibrateTypeMorsClick(View view) {
        Intent intent= new Intent();
        intent.putExtra("TestType", VibrateType.VIBRATE_TYPE_MORS);
        startTestActivity(intent);
    }

    public void buttonVibrateTypeBinClick(View view) {
        Intent intent= new Intent();
        intent.putExtra("TestType", VibrateType.VIBRATE_TYPE_BIN);
        startTestActivity(intent);
    }

    public void buttonVibrateTypeSelClick(View view) {
        Intent intent= new Intent();
        intent.putExtra("TestType", VibrateType.VIBRATE_TYPE_SEL);
        startTestActivity(intent);
    }

    protected void startTestActivity(Intent intent){
        intent.setClass(InputTestActivity.this, VibrateTestActivity.class);
        startActivity(intent);
    }

}
