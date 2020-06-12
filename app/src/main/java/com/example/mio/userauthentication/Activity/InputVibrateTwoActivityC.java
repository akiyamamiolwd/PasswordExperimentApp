package com.example.mio.userauthentication.Activity;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mio.userauthentication.R;

import java.util.HashMap;
import java.util.Map;

public class InputVibrateTwoActivityC extends AppCompatActivity {

    String hintNumPattern = "震动模式代表的数字如下表(0代表短震动，1代表长震动)：\n" +
                            "0: 0000  1: 0001  2: 0010\n" +
                            "3: 0011  4: 0100  5: 0101\n" +
                            "6: 0110  7: 0111  8: 1000\n" +
                            "9: 1001\n";
    String hintStart = "在输入密码时，每输入一位密码前,请先按Push按钮开始，然后根据震动提示来输入：\n" +
            "系统将通过震动给出一个随机数字，请您将要输入的密码数字加上系统提示的数字然后模上10，最终输入的数字为这个结果\n" +
            hintNumPattern +
            "准备好后请按PUSH按钮开始\n";


    private Vibrator myVibrator;

    int passLength = 4;
    int [] inputs;
    int [] inputsHint;
    int currentInput;
    protected long [][] hintPattern;
    protected HashMap<String, Button> buttonNums;
    protected Button buttonPush;
    protected TextView textviewInputs;
    protected TextView textviewHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_vibrate_two);

        myVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        textviewInputs = (TextView)this.findViewById(R.id.tv_inputs);
        textviewHint = (TextView)this.findViewById(R.id.tv_hint);

        initDatas();
        initHintPattern();
        initButtons();
    }

    public void initDatas(){
        //pattrenAdd = { 500 };
        //pattrenNotAdd = { 200, 200 };
        textviewHint.setText(hintStart);

        inputs = new int[passLength];
        inputsHint = new int[passLength];
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
                if(currentInput>passLength-1){
                    currentInput = 0;
                    textviewHint.setText(hintStart);
                }
                else{
                    //获取随机数
                    double r = Math.random();
                    long [] patternTmp = null;

                    int ra = new Double(r * 10).intValue();
                    patternTmp = hintPattern[ra];
                    inputsHint[currentInput] = ra;

                    //震动
                    myVibrator.vibrate(patternTmp, -1);
                    enableAllNumButtons();
                }
            }
        });

        disableAllNumButtons();
    }
    /**
     * 初始化提示密码输入的pattern，"." 代表短震动，"-"代表长震动。
     * 0：....
     * 1: ...-
     * 2: ..-.
     * 3: ..--
     * 4: .-..
     * 5: .-.-
     * 6: .--.
     * 7: .---
     * 8: -...
     * 9: -..-
     */
    protected void initHintPattern(){
        int [][]numPattern = {{0,0,0,0},{0,0,0,1},{0,0,1,0},{0,0,1,1},{0,1,0,0},{0,1,0,1},{0,1,1,0},{0,1,1,1},{1,0,0,0},{1,0,0,1}};
        long start = 0;
        long longPattern = 500;
        long shortPattern = 200;
        long sleepPattern = 100;
        hintPattern = new long[10][8];
        for(int i = 0; i < hintPattern.length; i++){
            hintPattern[i][0] = start;
            for(int j=0;j<numPattern[i].length;j++){
                hintPattern[i][j*2+1] = numPattern[i][j] == 1?longPattern:shortPattern;
                hintPattern[i][j*2+2] = sleepPattern;
            }
            /*
            hintPattern[i][1] = numPattern[i][0] == 1?longPattern:shortPattern;
            hintPattern[i][2] = sleepPattern;
            hintPattern[i][3] = numPattern[i][1] == 1?longPattern:shortPattern;
            hintPattern[i][4] = sleepPattern;
            hintPattern[i][5] = numPattern[i][2] == 1?longPattern:shortPattern;
            hintPattern[i][6] = sleepPattern;
            hintPattern[i][7] = numPattern[i][3] == 1?longPattern:shortPattern;
            */

        }
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
        String inputMes = "";
        String sysMes = "";
        String possiblePass = "";
        for(int i = 0;i<=currentInput;i++){
            showMes = showMes + "*" + "  ";
            sysMes = sysMes + inputsHint[i] + "  ";
            inputMes = inputMes + inputs[i] + "  ";
            possiblePass = possiblePass + (10 + inputs[i] - inputsHint[i])%10 + "  ";
        }
        textviewInputs.setText(showMes);

        String hintMes = hintStart+
                "您输入的数字为： "+inputMes+"\n" +
                "系统提示的数字为： "+sysMes+"\n" +
                "您的密码是:\n" + possiblePass;
        textviewHint.setText(hintMes);
    }


}
