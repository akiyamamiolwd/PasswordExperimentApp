package com.example.mio.userauthentication.Activity;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mio.userauthentication.Model.auth.VibrateTestData;
import com.example.mio.userauthentication.Model.type.VibrateType;
import com.example.mio.userauthentication.R;
import com.example.mio.userauthentication.util.FileReadAndWrite;
import com.example.mio.userauthentication.util.RandomOpreation;
import com.example.mio.userauthentication.util.VibratePattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VibrateTestActivity extends RecordSensorActivity {

    int testCase[];

    //提示数字类型
    String hintNumPattern ="";
    String hintStart = "";

    int vibrateType = 0;
    private Vibrator myVibrator;

    double longestVibTime =0;
    long markTime = System.currentTimeMillis();
    int currentTestCase = 0;
    long testStartTime =System.currentTimeMillis();

    ArrayList<VibrateTestData> testDatas = new ArrayList<VibrateTestData>();
    protected HashMap<String,long[]> vibratePattern;

    //控件
    protected HashMap<String, Button> buttonNums;
    protected Button buttonPush;
    protected Button buttonBack;
    protected TextView textviewInputs;
    protected TextView textviewHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate_test);
        this.testName = "VibrateTest";

        myVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        Bundle b = this.getIntent().getExtras();
        if(b!=null){
            vibrateType = (int)b.get("TestType");
        }

        switch (vibrateType){
            case VibrateType.VIBRATE_TYPE_BIN:{
                vibratePattern = VibratePattern.getBinTypePattern();
                this.testName += "BinType";
                hintNumPattern = VibratePattern.hintBinPattern;
                longestVibTime = VibratePattern.getBinTypeLongestTime();
                break;
            }
            case VibrateType.VIBRATE_TYPE_SEL:{
                vibratePattern = VibratePattern.getSelTypePattern();
                this.testName += "SelType";
                hintNumPattern = VibratePattern.hintSelPattern;
                longestVibTime = VibratePattern.getSelTypeLongestTime();
                break;
            }
            case VibrateType.VIBRATE_TYPE_MORS:{
                vibratePattern = VibratePattern.getMorsTypePattern();
                this.testName += "MorsType";
                hintNumPattern = VibratePattern.hintMorsPattern;
                longestVibTime = VibratePattern.getMorsTypeLongestTime();
                break;
            }
            default:vibratePattern = VibratePattern.getBinTypePattern();
        }

        textviewInputs = (TextView)this.findViewById(R.id.tv_inputs);
        textviewHint = (TextView)this.findViewById(R.id.tv_hint);

        initDatas();
        initButtons();
    }


    public void initDatas(){
        hintStart = "在测试震动识别时，请先按Push按钮开始，\n" +
                "系统将通过震动给出一个随机数字，请您将要输入识别并输入这个数字：\n" +
                hintNumPattern +
                "准备好后请按PUSH按钮开始。\n";
        textviewHint.setText(hintStart);
        currentTestCase = 0;

        testCase = RandomOpreation.generateVibrateTestCase(2,10);
        testCase = RandomOpreation.randomPostionOfIntArray(testCase);
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
                    long inputTime = System.currentTimeMillis() - markTime;
                    String inputNum = ((Button)view).getText().toString();
                    int inputChar = Integer.parseInt(inputNum);

                    VibrateTestData vtdTmp = testDatas.get(currentTestCase);
                    vtdTmp.setInputChar(inputChar);
                    vtdTmp.setInputTime((double)inputTime*1E-3);
                    vtdTmp.testCorrection();
                    vtdTmp.calRecognizeTime();
                    vtdTmp.setInputEndTime((double)(inputTime + markTime - testStartTime)*1E-3);

                    showInputs();
                    currentTestCase ++;

                    disableAllNumButtons();
                    buttonPush.setEnabled(true);
                }
            });
        }

        buttonPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentTestCase < testCase.length){
                    int vibChar = testCase[currentTestCase];
                    long[] patternTmp = vibratePattern.get(Integer.toString(vibChar));
                    //获取系统时间
                    markTime = System.currentTimeMillis();
                    //震动
                    myVibrator.vibrate(patternTmp, -1);

                    VibrateTestData vtdTmp = new VibrateTestData(vibrateType);
                    vtdTmp.setVibrateChar(vibChar);
                    vtdTmp.setVibrateTime(longestVibTime);
                    vtdTmp.setInputStartTime((double)(markTime - testStartTime)*1E-3);
                    vtdTmp.setVibratePattern(patternTmp);

                    testDatas.add(vtdTmp);

                    enableAllNumButtons();
                }
                else{
                    writeEvalData();

                    String t = textviewHint.getText().toString();
                    t += "结果已保存，按back键退出本次测试\n";
                    textviewHint.setText(t);
                    buttonBack.setEnabled(true);
                }
                buttonPush.setEnabled(false);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        disableAllNumButtons();
    }

    protected void writeEvalData(){
        String evalFileName = "TestEval_" + vibrateType + ".txt";
        StringBuffer evalFileContent = new StringBuffer("");
        evalFileContent.append("//格式为：震动字符\t输入字符\t开始输入时刻\t结束输入时刻\t输入用时\t震动用时\t用户识别用时\t是否正确\n");
        for(VibrateTestData vtd : testDatas){
            evalFileContent.append(vtd.getEvalInfo());
        }
        FileReadAndWrite.writeFileWithPath(filePath,evalFileName,evalFileContent.toString());
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
        for(int i = 0;i<=currentTestCase;i++){
            showMes = showMes + "*" + "  ";
            sysMes = sysMes + testDatas.get(i).getVibrateChar() + "  ";
            inputMes = inputMes + testDatas.get(i).getInputChar() + "  ";
        }
        textviewInputs.setText(showMes);

        String hintMes = hintStart+
                "您输入的数字为： "+inputMes+"\n" +
                "系统提示的数字为： "+sysMes+"\n" ;

        if(currentTestCase == testCase.length-1){
            hintMes+="测试已经完成，请按PUSH键输出结果\n";
        }
        textviewHint.setText(hintMes);
    }
}
