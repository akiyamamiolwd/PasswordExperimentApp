package com.example.mio.userauthentication.Activity;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mio.userauthentication.Model.evaluation.VibrateInputType1Evaluation;
import com.example.mio.userauthentication.R;

import java.util.HashMap;
import java.util.Map;

public class InputVibrateOneActivityC extends RecordSensorActivity {

    VibrateInputType1Evaluation vit1e = new VibrateInputType1Evaluation(5,4);

    String hintStart = "在输入密码前,请先选择一个数(0到9)默记下来，然后根据震动提示来输入：\n" +
            "一次长震代表：最终输入的数字为，您要输入的密码数字加上您事先选择的数字然后模上10；\n" +
            "两次短震代表：最终输入的数字为，您要输入的密码数字；\n" +
            "准备好后请按PUSH按钮开始";

    private Vibrator myVibrator;
    //与输入流程相关的特定类型
    long [] pattrenAdd = { 0, 500 };
    long [] pattrenNotAdd = { 0, 200, 100, 200 };
    //密码输入相关的内容
    int [] inputs;
    int [] inputsPattern;
    int currentInput;

    protected HashMap<String, Button> buttonNums;
    protected Button buttonPush;
    protected TextView textviewInputs;
    protected TextView textviewHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_vibrate_one);
        this.testName = "MethodTest_Vibrate_Type1";

        myVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        textviewInputs = (TextView)this.findViewById(R.id.tv_inputs);
        textviewHint = (TextView)this.findViewById(R.id.tv_hint);
        initDatas();
        initButtons();
    }

    public void initDatas(){
        textviewHint.setText(hintStart);

        int inputLen = 4;
        inputs = new int[inputLen];
        inputsPattern = new int[inputLen];
        currentInput = 0;
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
                    String inputNum = ((Button)view).getText().toString();
                    inputs[currentInput] = Integer.parseInt(inputNum);
                    showInputs();
                    currentInput ++;
                    disableAllNumButtons();
                }
            });
        }

        buttonPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentInput>3){
                    currentInput = 0;
                    textviewHint.setText(hintStart);
                }
                else{
                    //获取随机数
                    double r = Math.random();
                    long [] patternTmp = null;
                    if(r>0.5){
                        patternTmp = pattrenAdd;
                        inputsPattern[currentInput] = 1;
                    }
                    else{
                        patternTmp = pattrenNotAdd;
                        inputsPattern[currentInput] = 0;
                    }
                    //震动
                    myVibrator.vibrate(patternTmp, -1);
                    enableAllNumButtons();
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

    public void showInputs(){
        String showMes = "";
        String patternMes = "";
        String inputMes = "";
        String possiblePass = "";
        for(int i = 0;i<=currentInput;i++){
            showMes = showMes + "*" + "  ";
            patternMes = patternMes + inputsPattern[i] + "  ";
            inputMes = inputMes + inputs[i] + "  ";
        }

        for(int i = 0;i<=9;i++){
            possiblePass += "选择的数字为"+i+"： ";
            for(int j = 0;j<=currentInput;j++){
                int tmp = (10 - inputsPattern[j] * i + inputs[j])%10;
                possiblePass = possiblePass + tmp + "  ";
            }
            possiblePass +="\n";
        }
        textviewInputs.setText(showMes);

        String hintMes = "您输入的数字为： "+inputMes+"\n" +
                "系统提示的震动类型为(1代表加，0代表不加)：\n" +
                patternMes+"\n" +
                "您的密码可能是:\n" + possiblePass;
        textviewHint.setText(hintMes);
    }

}
