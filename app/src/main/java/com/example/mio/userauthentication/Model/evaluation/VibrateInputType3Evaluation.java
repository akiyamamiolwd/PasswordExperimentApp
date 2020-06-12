package com.example.mio.userauthentication.Model.evaluation;

import android.os.Vibrator;

import com.example.mio.userauthentication.Model.auth.InputData;
import com.example.mio.userauthentication.Model.auth.Password;
import com.example.mio.userauthentication.Model.auth.PasswordInput;
import com.example.mio.userauthentication.Model.type.MethodType;
import com.example.mio.userauthentication.Model.type.VibrateType;
import com.example.mio.userauthentication.util.RandomOpreation;
import com.example.mio.userauthentication.util.VibratePattern;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class VibrateInputType3Evaluation {
    ArrayList<Password> passwordTest = new ArrayList<>();
    ArrayList<InputData> allInputData = new ArrayList<>();
    ArrayList<PasswordInput> passwordInputsData = new ArrayList<>();

    HashMap<String,long []> vibPattern = new HashMap<>();
    //事先存好提示的类型（要排除全0和只有一个1的情况）
    int [] hintPattern = null;

    //评测常数
    String methodType = MethodType.METHOD_TYPE_VIBRATE_2;
    double hintTime = (double)500*1E-3;
    String hintType = "";
    String hintNumPattern = "";
    //平均用户识别时间
    long avgRecognizeTime = 1000;

    //评测缓存
    //缓存状态
    boolean continueVib = false;
    int vibTimes = 0;
    //缓存InputData
    InputData tmpInput = new InputData();
    //是否回删
    boolean isBack = false;
    boolean hasEvaluated = false;

    //测试参数
    int passwordLen = 0;
    int testGroupNum = 0;
    int currentTestGroup = 0;

    //整体测试是否开始
    boolean testStarted = false;

    //当前测试组开始时间
    long currentTestGroupStartTime = 0;
    //测试开始时间
    long testStartTime = System.currentTimeMillis();

    public VibrateInputType3Evaluation(){
    }

    public VibrateInputType3Evaluation(int testGroupNum, int passwordLen, int hintType){
        passwordTest = RandomOpreation.generatePINPassTestCase(testGroupNum,passwordLen);
        this.testGroupNum = testGroupNum;
        this.passwordLen = passwordLen;
        this.currentTestGroup = 0;
        initHintType(hintType);
    }

    void initHintType(int hintType){

        switch (hintType){
            case VibrateType.VIBRATE_TYPE_BIN:{
                vibPattern = VibratePattern.getBinTypePattern();
                hintNumPattern = VibratePattern.hintBinPattern;
                hintTime = VibratePattern.getBinTypeLongestTime();
                this.hintType = VibrateType.VIBRATE_TYPE_BIN_STRING;
                break;
            }
            case VibrateType.VIBRATE_TYPE_SEL:{
                vibPattern = VibratePattern.getSelTypePattern();
                hintNumPattern = VibratePattern.hintSelPattern;
                hintTime = VibratePattern.getSelTypeLongestTime();
                this.hintType = VibrateType.VIBRATE_TYPE_SEL_STRING;
                break;
            }
            case VibrateType.VIBRATE_TYPE_MORS:{
                vibPattern = VibratePattern.getMorsTypePattern();
                hintNumPattern = VibratePattern.hintMorsPattern;
                hintTime = VibratePattern.getMorsTypeLongestTime();
                this.hintType = VibrateType.VIBRATE_TYPE_MORS_STRING;
                break;
            }
            default:vibPattern = VibratePattern.getBinTypePattern();
        }
    }

    int[]  generateHintPattern(){
        int[] hintPattern = new int[passwordLen];
        for(int i=0;i<passwordLen;i++){
            hintPattern[i] = RandomOpreation.getRandomNumInN(10);
        }
        return hintPattern;
    }

    public void startVibrate(final Vibrator vib,String inputName){
        vibTimes = 0;
        continueVib = true;

        int charNum = inputName.equals("button_arpha_u_to_z")?6:5;


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(avgRecognizeTime);
                    while (true){
                        if(continueVib){
                            vib.vibrate(200);
                            sleep(200);
                            vibTimes++;
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public long[] getCurVibPattern(){
        int curHintIndex = getCurPassInput().getCurLength();
        int vibP = hintPattern[curHintIndex];
        return vibPattern.get(Integer.toString(vibP));
    }

    public long[] getVibPattern(String vibKey){
        return vibPattern.get(vibKey);
    }

    public int getCurrentTestGroup() {
        return currentTestGroup;
    }

    public boolean isTestStarted() {
        return testStarted;
    }

    public boolean isHasEvaluated() {
        return hasEvaluated;
    }

    public PasswordInput getCurPassInput(){
        return passwordInputsData.get(currentTestGroup);
    }

    public String getInputHint(){
        String hint = "在输入密码时，每输入一位密码前,请先按Push按钮开始，然后根据震动提示来输入：\n" +
                "系统将通过震动给出一个随机数字，请您将要输入的密码数字加上系统提示的数字然后模上10，最终输入的数字为这个结果\n" +
                hintNumPattern +
                "输入完成后，单击PUSH继续下一位密码输入，直至密码输入完成。\n";
        return hint;
    }

    public String getEvaluatingHint(){
        String hint = "正在生成评测结果中......\n";
        return hint;
    }

    public String getEvaluateHint(){
        String hint = "所有测试已完成，按PUSH键进行评测\n";
        return hint;
    }

    public String getFinishHint(){
        String hint = "所有测试已完成，按PUSH键退出测试程序\n";
        return hint;
    }

    public String getTestStartHint(){
        String hintForm = "现在准备开始测试，我们将提供给您%d组密码，请您依次按照提示的方法输入。\n" +
                "准备好后，请按PUSH键开始\n";
        return String.format(hintForm,testGroupNum);
    }

    public String getCurrentHint(){
        String hintForm = getInputHint()+
                  "目前进行的测试组别是：第%d组\n" +
                  "您需要输入的密码是：%s\n" +
                  "系统提示类型为：%s\n" +
                  "您输入的数字是：%s\n";
        return String.format(hintForm,currentTestGroup+1,
                passwordTest.get(currentTestGroup).getPasswordStringWithBlank(),
                getCurPassInput().getInputHintCharWithBlank(),
                getCurPassInput().getPasswordStringWithBlank());
    }


    public String getCurPassInputStar(){
        StringBuffer sb = new StringBuffer(" ");
        for (int i = 0; i < getCurPassInput().getCurLength(); i ++){
            sb.append("* ");
        }
        return sb.toString();
    }

    public ArrayList<Boolean> isPassInputCorrect(int passwordIndex){
        System.out.println("test Pass Correction");
        PasswordInput inputP = passwordInputsData.get(passwordIndex);
        Password coP = passwordTest.get(passwordIndex);
        ArrayList<Boolean> correction = new ArrayList<>();
        for(int i=0;i<inputP.getCurLength();i++){
            int tmpPCI = Integer.parseInt(inputP.getCharactors().get(i).toString());
            int tmpPCT = Integer.parseInt(coP.getCharactors().get(i).toString());
            int tmpHint = Integer.parseInt(inputP.getInputHintByIndex(i));
            boolean correct = (tmpPCI == ((tmpPCT + tmpHint)%10));
            correction.add(correct);
            System.out.println(String.format("char %d: tmpPCI( %d ) == ( tmpPCT( %d ) + tmpHint( %d ) ) %% 10 : %b",
                                                i,tmpPCI,tmpPCT,tmpHint,correct));
        }
        return correction;
    }

    public boolean isCurrentGroupFinished(){
        return getCurPassInput().getCurLength() == passwordTest.get(currentTestGroup).getCurLength();
    }

    public boolean isAllGroupFinished(){
        return (currentTestGroup == (testGroupNum -1)) && isCurrentGroupFinished();
    }

    public void addPassInputDataInPush(long inputStartTime){
        tmpInput = new InputData();
        tmpInput.setMethodType(this.methodType);
        tmpInput.setHintType(this.hintType);
        tmpInput.setInputStartTime((double)(inputStartTime - testStartTime)*1E-3);
        tmpInput.setHintChar(Integer.toString(hintPattern[getCurPassInput().getCurLength()]));
        tmpInput.setHintTime(this.hintTime);

    }

    public void addPassInputDataInNum(long inputEndTime, String inputChar){
        tmpInput.setInputChar(inputChar);
        tmpInput.setInputEndTime((double)(inputEndTime - testStartTime)*1E-3);
        allInputData.add(tmpInput);
        getCurPassInput().addInputData(tmpInput);
    }

    public void nextGroup(){
        currentTestGroup++;
        newTestCase();
    }

    public void newTestCase(){
        currentTestGroupStartTime = System.currentTimeMillis();
        passwordInputsData.add(new PasswordInput(passwordLen));
        hintPattern = generateHintPattern();
    }

    public void testStart(){
        currentTestGroup = 0;
        newTestCase();
        testStarted = true;
    }

    public HashMap<String,String> evaluate(){
        hasEvaluated = true;
        HashMap<String,String> files = new HashMap<>();
        files.put("inputsData.txt",getInputsDatasContentEval());
        files.put("passwordEvalData.txt",getPassInputContentEval());

        return files;
    }

    String getPassInputContentEval(){
        StringBuffer sb = new StringBuffer("");
        for(int i = 0;i<passwordInputsData.size();i++){
            sb.append(getPassInputEval(i)).append("\n");
        }
        return sb.toString();
    }

    String getPassInputEval(int index){
        Password passTest = passwordTest.get(index);
        PasswordInput passInput = passwordInputsData.get(index);
        ArrayList<Boolean> everyCharCorrection = isPassInputCorrect(index);
        String passInputEvalContent = "";
        boolean passCorrection = true;
        for(Boolean f : everyCharCorrection){
            if(!f){
                passCorrection = false;
                break;
            }
        }
        //第几组测试，原密码，系统提示类型，输入密码，密码是否正确，密码输入时间，单次输入
        /*
        String format = "第%d组测试\n" +
                "原密码:%s\t输入密码:%s\t" +
                "系统提示类型:%s\t密码是否正确:%b\t密码输入时间:%.3f\n";*/
        String format = "第%d组测试\n" +
                "总体情况:\t%s\t%s\t%s\t%b\t%.3f\n";
        passInputEvalContent = String.format(format,index+1,
                passTest.getPasswordStringWithBlank(),passInput.getPasswordStringWithBlank(),
                passInput.getInputHintCharWithBlank(),passCorrection,
                passInput.calPassInputTime());
        for(int i = 0;i<passInput.getCurLength();i++){
            //元字符，输入字符，提示类型，提示字符，是否正确，输入时间
            String singleEval = "%c\t%c\t%s\t%s\t%b\t%.3f\n";
            passInputEvalContent += String.format(singleEval,
                    passTest.getCharactors().get(i),passInput.getCharactors().get(i),
                    hintType,passInput.getInputHintByIndex(i),
                    everyCharCorrection.get(i),passInput.getInputTimeByIndex(i));
        }
        return passInputEvalContent;
    }

    String getInputsDatasContentEval(){
        StringBuffer sb = new StringBuffer(InputData.getEvalDataTxtExplain());
        for(InputData a : allInputData){
            sb.append(a.getEvalDataTxt());
        }
        return sb.toString();
    }

}
