package com.example.szymon.worklogger;

import android.util.Log;

import java.sql.Time;
import java.time.LocalDateTime;

public class Engine {
    public static double HPW=42.5;
    private Time TL=new Time(0);
    private long startTime;
    public static int workingDays=5;

    Engine(){
        refreshTL();
    }
    Engine(long timeLeft, LocalDateTime lastUse){
       if(lastUse.getDayOfYear()<LocalDateTime.now().getDayOfYear()||lastUse.getYear()<LocalDateTime.now().getYear()){
           LocalDateTime now = LocalDateTime.now();
           now.minusDays(7);
           if(now.isAfter(lastUse)||lastUse.getDayOfWeek().getValue()>=now.getDayOfWeek().getValue())   {
               refreshTL();
           } else {
               TL.setTime(timeLeft);
           }
       }else{
           TL.setTime(timeLeft);
       }
    }

    public void refreshTL() {
        TL.setTime((long) (HPW*1000*3600));
    }

    public void start(){
        startTime=System.currentTimeMillis();
    }
    public void stop(){
        TL.setTime(TL.getTime()-(System.currentTimeMillis()-startTime));
        if(TL.getTime()<0) TL.setTime(0);
        Log.i("info log", String.valueOf(TL.getTime()));
    }

    public long getTL() {
        return TL.getTime();
    }
    public void skipDay(){
        TL.setTime((long) (TL.getTime()-HPW/workingDays*1000*3600));
    }
    public String TLString(){
        if(TL.getTime()<0) TL.setTime(0);
       return String.valueOf(Math.round(getTL()/10.0/3600)/100.0);
    }

}
