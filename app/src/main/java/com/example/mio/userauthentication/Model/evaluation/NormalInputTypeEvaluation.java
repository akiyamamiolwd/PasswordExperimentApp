package com.example.mio.userauthentication.Model.evaluation;

import com.example.mio.userauthentication.Model.auth.InputData;
import com.example.mio.userauthentication.Model.auth.Password;
import com.example.mio.userauthentication.Model.auth.PasswordInput;
import com.example.mio.userauthentication.Model.type.MethodType;
import com.example.mio.userauthentication.util.RandomOpreation;

import java.util.ArrayList;
import java.util.HashMap;

public class NormalInputTypeEvaluation {
    ArrayList<Password> passwordTest = new ArrayList<>();
    ArrayList<InputData> allInputData = new ArrayList<>();
    ArrayList<PasswordInput> passwordInputsData = new ArrayList<>();

    HashMap<String,long []> vibPattern = new HashMap<>();

    //评测常数
    String methodType = MethodType.METHOD_TYPE_NORMAL;
    double hintTime = 0;
    String hintType = "NoHint";

    //评测缓存
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

    public NormalInputTypeEvaluation(){
        initVibPattern();
    }

    public NormalInputTypeEvaluation(int testGroupNum, int passwordLen){
        passwordTest = RandomOpreation.generatePINPassTestCase(testGroupNum,passwordLen);
        this.testGroupNum = testGroupNum;
        this.passwordLen = passwordLen;
        this.currentTestGroup = 0;
        initVibPattern();
    }

    void initVibPattern(){
        long [] pattrenAdd = { 0, 500 };
        long [] pattrenNotAdd = { 0, 200, 100, 200 };
        vibPattern.put("0",pattrenNotAdd);
        vibPattern.put("1",pattrenAdd);
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
        String hint = "请正常的输入密码：\n";
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
                  "您输入的数字是：%s\n";
        return String.format(hintForm,currentTestGroup+1,
                passwordTest.get(currentTestGroup).getPasswordStringWithBlank(),
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
        PasswordInput inputP = passwordInputsData.get(passwordIndex);
        Password coP = passwordTest.get(passwordIndex);
        ArrayList<Boolean> correction = new ArrayList<>();
        for(int i=0;i<inputP.getCurLength();i++){
            int tmpPCI = Integer.parseInt(inputP.getCharactors().get(i).toString());
            int tmpPCT = Integer.parseInt(coP.getCharactors().get(i).toString());
            correction.add(tmpPCI == tmpPCT);
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

    }

    public void addPassInputDataInNum(long inputTime, String inputChar){
        tmpInput = new InputData();
        tmpInput.setMethodType(this.methodType);
        tmpInput.setHintType(this.hintType);
        tmpInput.setInputStartTime((double)(inputTime - testStartTime)*1E-3);
        tmpInput.setHintChar("");
        tmpInput.setHintTime(this.hintTime);
        tmpInput.setInputChar(inputChar);
        tmpInput.setInputEndTime((double)(inputTime - testStartTime)*1E-3);
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
                passInput.calPassInputTimeBySE());
        for(int i = 0;i<passInput.getCurLength();i++){
            //元字符，输入字符，提示类型，是否正确，输入时间
            String singleEval = "%c\t%c\t%s\t%b\t%.3f\n";
            passInputEvalContent += String.format(singleEval,
                    passTest.getCharactors().get(i),passInput.getCharactors().get(i),
                    passInput.getInputHintByIndex(i),everyCharCorrection.get(i),
                    passInput.getInputTimeByIndex(i));
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
