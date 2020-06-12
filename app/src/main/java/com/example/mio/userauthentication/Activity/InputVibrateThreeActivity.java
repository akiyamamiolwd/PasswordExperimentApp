package com.example.mio.userauthentication.Activity;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mio.userauthentication.Model.evaluation.VibrateInputType3Evaluation;
import com.example.mio.userauthentication.R;
import com.example.mio.userauthentication.util.FileReadAndWrite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputVibrateThreeActivity extends RecordSensorActivity {
    VibrateInputType3Evaluation vit3e;
    int hintType = 0;

    private Vibrator myVibrator;

    String [] buttonNames = {"button_num_1_to_5","button_num_6_to_0","button_arpha_a_to_e","button_arpha_f_to_j",
                            "button_arpha_k_to_o","button_arpha_p_to_t","button_arpha_u_to_z"};

    //ArrayList<String> buttonName = new ArrayList<String>();

    protected HashMap<String, Button> buttonInputs;
    protected Button buttonPush;

    protected TextView textviewInputs;
    protected TextView textviewHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_audio_one);
        this.testName = "MethodTest_Vibrate_Type_HintAndWait";

        myVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        textviewInputs = (TextView)this.findViewById(R.id.tv_inputs);
        textviewHint = (TextView)this.findViewById(R.id.tv_hint);

        vit3e = new VibrateInputType3Evaluation(5,4,1);

        textviewHint.setText(vit3e.getTestStartHint());
        initButtons();
    }


    void buttonInputsClick(int id, long inputTime){
        String buttonName = this.getResources().getResourceName(id);
        List<String> a = Arrays.asList(new String[] { "A", "B", "C" });

        String [] buttonChars = buttonInputs.get(buttonName).getText().toString().split(" ");
        //System.out


        vit3e.startVibrate(myVibrator,buttonName);

        vit3e.addPassInputDataInNum(inputTime,"1");

        disableAllNumButtons();

        if(vit3e.isAllGroupFinished()){
            textviewHint.setText(vit3e.getCurrentHint()+ vit3e.getEvaluateHint());
        }
        else {
            if(vit3e.isCurrentGroupFinished()){
                vit3e.nextGroup();
            }
            textviewHint.setText(vit3e.getCurrentHint());
        }
        textviewInputs.setText(vit3e.getCurPassInputStar());
    }

    public void initButtons(){
        buttonInputs = new HashMap<>();
        buttonPush = (Button) this.findViewById(R.id.button_push);

        for(String bn : buttonNames){
            String buttonID = "button_" + bn;
            //使用字符串查找Android资源
            int tmp = this.getResources().getIdentifier(buttonID,"id","com.example.mio.userauthentication");
            Button tmpButton = (Button)this.findViewById(tmp);
            buttonInputs.put(buttonID, tmpButton);
            tmpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long inputTime = System.currentTimeMillis();
                    buttonInputsClick(view.getId(),inputTime);
                }
            });
        }

        buttonPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long inputStartTime = System.currentTimeMillis();
                if(!vit3e.isTestStarted())
                {
                    //测试未开始
                    vit3e.testStart();
                    textviewHint.setText(vit3e.getCurrentHint());
                    enableAllNumButtons();
                }
                else if(vit3e.isAllGroupFinished()){
                    //所有组的测试均完成
                    if(vit3e.isHasEvaluated()){
                        finish();
                    }
                    else{
                        textviewHint.setText(vit3e.getEvaluatingHint());
                        HashMap<String,String> evalFiles = vit3e.evaluate();
                        writeEvalData(evalFiles);
                        textviewHint.setText(vit3e.getFinishHint());
                    }
                }

            }
        });

        disableAllNumButtons();
    }

    public void disableAllNumButtons(){
        for(Map.Entry<String,Button> b:buttonInputs.entrySet()){
            b.getValue().setEnabled(false);
        }
    }

    public void enableAllNumButtons(){
        for(Map.Entry<String,Button> b:buttonInputs.entrySet()){
            b.getValue().setEnabled(true);
        }
    }

    void writeEvalData(HashMap<String,String> files){
        for(String k : files.keySet()){
            FileReadAndWrite.writeFileWithPath(filePath,k,files.get(k));
        }
    }

}
