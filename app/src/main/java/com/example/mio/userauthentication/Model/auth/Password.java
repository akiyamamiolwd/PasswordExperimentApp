package com.example.mio.userauthentication.Model.auth;

import java.util.ArrayList;
import java.util.Iterator;

public class Password {
    int length = 0;
    ArrayList<Character> charactors = new ArrayList<>();


    public Password(){ }

    public Password(int length){
        this.length = length;
    }

    public void addPassChara(int i){
        charactors.add(Integer.toString(i).charAt(0));
    }

    public void addPassChara(char c){
        charactors.add(c);
    }

    boolean compare(Password pw){
        return charactors.equals(pw);
    }

    ArrayList<Boolean> compareEveryChar(Password pw){
        ArrayList<Boolean> tmp = new ArrayList<>();
        Iterator<Character> pw1 = charactors.iterator();
        Iterator<Character> pw2 = pw.getCharactors().iterator();
        while (pw1.hasNext()&&pw2.hasNext()){
            tmp.add(pw1.next().equals(pw2.next()));
        }
        return tmp;
    }

    public int getCurLength(){
        return charactors.size();
    }

    public ArrayList<Character> getCharactors() {
        return charactors;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getPasswordStringWithBlank(){
        return  getPasswordStringWithInter(" ");
    }

    public String getPasswordStringWithInter(String inter){
        StringBuffer sb = new StringBuffer(inter);
        for(char c : charactors){
            sb.append(c).append(inter);
        }
        return  sb.toString();
    }
}
