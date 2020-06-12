package com.example.mio.userauthentication.Activity;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mio.userauthentication.Model.evaluation.NormalInputTypeEvaluation;
import com.example.mio.userauthentication.Model.evaluation.VibrateInputType2Evaluation;
import com.example.mio.userauthentication.Model.type.VibrateType;
import com.example.mio.userauthentication.R;
import com.example.mio.userauthentication.util.FileReadAndWrite;

import java.util.HashMap;
import java.util.Map;

public class InputNomalActivity extends RecordSensorActivity {

    NormalInputTypeEvaluation nite;
    int hintType = 0;

    private Vibrator myVibrator;

    protected HashMap<String, Button> buttonNums;
    protected Button buttonPush;

    protected TextView textviewInputs;
    protected TextView textviewHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_normal);
        this.testName = "MethodTest_Normal_Type";

        myVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        textviewInputs = (TextView)this.findViewById(R.id.tv_inputs);
        textviewHint = (TextView)this.findViewById(R.id.tv_hint);

        nite = new NormalInputTypeEvaluation(5,4);

        textviewHint.setText(nite.getTestStartHint());
        initButtons();
    }

    public void initButtons(){
        buttonNums = new HashMap<>();
        buttonPush = (Button) this.findViewById(R.id.button_push);

        for(int i = 0; i <= 9; i++){
            String buttonID = "button_num_" + i;
            //使用字符串查找Android资源
            int tmp = this.getResources().getIdentifier(buttonID,"id","com.example.mio.userauthentication");
            Button tmpButton = (Button)this.findViewById(tmp);
            buttonNums.put(buttonID, tmpButton);
            tmpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long inputTime = System.currentTimeMillis();
                    String inputNum = ((Button)view).getText().toString();

                    nite.addPassInputDataInNum(inputTime,inputNum);

                    if(nite.isAllGroupFinished()){
                        textviewHint.setText(nite.getCurrentHint()+ nite.getEvaluateHint());
                    }
                    else {
                        if(nite.isCurrentGroupFinished()){
                            nite.nextGroup();
                        }
                        textviewHint.setText(nite.getCurrentHint());
                    }
                    textviewInputs.setText(nite.getCurPassInputStar());
                }
            });
        }

        buttonPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long inputStartTime = System.currentTimeMillis();
                if(!nite.isTestStarted())
                {
                    //测试未开始
                    nite.testStart();
                    textviewHint.setText(nite.getCurrentHint());
                    enableAllNumButtons();
                }
                else if(nite.isAllGroupFinished()){
                    //所有组的测试均完成
                    if(nite.isHasEvaluated()){
                        finish();
                    }
                    else{
                        textviewHint.setText(nite.getEvaluatingHint());
                        HashMap<String,String> evalFiles = nite.evaluate();
                        writeEvalData(evalFiles);
                        textviewHint.setText(nite.getFinishHint());
                    }
                }

            }
        });

        disableAllNumButtons();
    }

    public void disableAllNumButtons(){
        for(Map.Entry<String,Button> b:buttonNums.entrySet()){
            b.getValue().setEnabled(false);
        }
    }

    public void enableAllNumButtons(){
        for(Map.Entry<String,Button> b:buttonNums.entrySet()){
            b.getValue().setEnabled(true);
        }
    }

    void writeEvalData(HashMap<String,String> files){
        for(String k : files.keySet()){
            FileReadAndWrite.writeFileWithPath(filePath,k,files.get(k));
        }
    }

}
