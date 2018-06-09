package com.example.szymon.worklogger;

import android.util.Log;

import java.time.LocalDateTime;

public class Engine {
    public static double HPW=42.5;
    private static int workingDays = 5;
    private long startTime;
    private Long TL = 0L;
    private updateScheduler updater;
    Engine(){
        refreshTL();
        updater=new updateScheduler(this);
    }
    Engine(long timeLeft, LocalDateTime lastUse){
       if(lastUse.getDayOfYear()<LocalDateTime.now().getDayOfYear()||lastUse.getYear()<LocalDateTime.now().getYear()){
           LocalDateTime now = LocalDateTime.now();
           now.minusDays(7);
           if(now.isAfter(lastUse)||lastUse.getDayOfWeek().getValue()>=now.getDayOfWeek().getValue())   {
               refreshTL();
           } else {
               TL = timeLeft;
           }
       }else{
           TL = timeLeft;
       }
       updater=new updateScheduler(this);

    }

    public void refreshTL() {
        TL = (long) (HPW * 1000 * 3600);
    }

    public void start(){
        startTime=System.currentTimeMillis();
        updater.scheduleUpdates();
    }
    public void stop(){
        updater.stop();
        update();
    }

    public void update() {
        TL -= System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        if (TL < 0L) {
            TL = 0L;
        }
        Log.i("update  tl log", TL.toString());
    }

    public long getTL() {
        return TL;
    }
    public void skipDay(){
        TL = (long) (TL - HPW / workingDays * 1000 * 3600);
    }
    public String TLString(){
        if (TL < 0L) TL = 0L;
       return String.valueOf(Math.round(getTL()/10.0/3600)/100.0);
    }

}
