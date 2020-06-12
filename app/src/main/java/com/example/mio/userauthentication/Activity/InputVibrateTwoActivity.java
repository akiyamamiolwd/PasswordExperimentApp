package com.example.mio.userauthentication.Activity;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mio.userauthentication.Model.evaluation.VibrateInputType2Evaluation;
import com.example.mio.userauthentication.Model.type.VibrateType;
import com.example.mio.userauthentication.R;
import com.example.mio.userauthentication.util.FileReadAndWrite;

import java.util.HashMap;
import java.util.Map;

public class InputVibrateTwoActivity extends RecordSensorActivity {

    VibrateInputType2Evaluation vit2e;
    int hintType = 0;

    private Vibrator myVibrator;

    protected HashMap<String, Button> buttonNums;
    protected Button buttonPush;
    protected Button buttonBack;
    protected TextView textviewInputs;
    protected TextView textviewHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_vibrate_two);
        this.testName = "MethodTest_Vibrate_Type_HintAndAdd";

        myVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        Bundle b = this.getIntent().getExtras();
        if(b!=null){
            hintType = (int)b.get("TestType");
        }


        switch (hintType){
            case VibrateType.VIBRATE_TYPE_BIN:{
                this.testName += "BinType";
                break;
            }
            case VibrateType.VIBRATE_TYPE_SEL:{
                this.testName += "SelType";
                break;
            }
            case VibrateType.VIBRATE_TYPE_MORS:{
                this.testName += "MorsType";
                break;
            }
            default:break;
        }


        textviewInputs = (TextView)this.findViewById(R.id.tv_inputs);
        textviewHint = (TextView)this.findViewById(R.id.tv_hint);

        vit2e = new VibrateInputType2Evaluation(5,4,hintType);

        textviewHint.setText(vit2e.getTestStartHint());
        initButtons();
    }

    public void initButtons(){
        buttonNums = new HashMap<>();
        buttonPush = (Button) this.findViewById(R.id.button_push);
        buttonBack = (Button) this.findViewById(R.id.button_back);
        buttonBack.setEnabled(false);

        for(int i = 0; i <= 9; i++){
            String buttonID = "button_num_" + i;
            //使用字符串查找Android资源
            int tmp = this.getResources().getIdentifier(buttonID,"id","com.example.mio.userauthentication");
            Button tmpButton = (Button)this.findViewById(tmp);
            buttonNums.put(buttonID, tmpButton);
            tmpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long inputEndTime = System.currentTimeMillis();
                    String inputNum = ((Button)view).getText().toString();

                    vit2e.addPassInputDataInNum(inputEndTime,inputNum);


                    if(vit2e.isAllGroupFinished()){
                        textviewHint.setText(vit2e.getCurrentHint()+vit2e.getEvaluateHint());
                    }
                    else {
                        textviewHint.setText(vit2e.getCurrentHint());
                    }
                    textviewInputs.setText(vit2e.getCurPassInputStar());

                    disableAllNumButtons();
                    buttonPush.setEnabled(true);
                }
            });
        }

        buttonPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long inputStartTime = System.currentTimeMillis();
                if(!vit2e.isTestStarted())
                {
                    //测试未开始
                    vit2e.testStart();
                    textviewHint.setText(vit2e.getCurrentHint());
                }
                else if(vit2e.isAllGroupFinished()){
                    //所有组的测试均完成
                    if(vit2e.isHasEvaluated()){
                        finish();
                    }
                    else{
                        textviewHint.setText(vit2e.getEvaluatingHint());
                        HashMap<String,String> evalFiles = vit2e.evaluate();
                        writeEvalData(evalFiles);
                        textviewHint.setText(vit2e.getFinishHint());
                    }
                }
                else if(vit2e.isCurrentGroupFinished()){
                    //当前组的测试完成
                    vit2e.nextGroup();
                    textviewHint.setText(vit2e.getCurrentHint());
                    textviewInputs.setText(vit2e.getCurPassInputStar());
                }
                else{
                    //当前组的测试未完成
                    long [] patternTmp = vit2e.getCurVibPattern();
                    //震动
                    myVibrator.vibrate(patternTmp, -1);

                    vit2e.addPassInputDataInPush(inputStartTime);
                    enableAllNumButtons();
                    buttonPush.setEnabled(false);
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
