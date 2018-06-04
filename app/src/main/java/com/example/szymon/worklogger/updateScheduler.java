package com.example.szymon.worklogger;

import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class updateScheduler {
    private Engine engine;
    private TextView display;
    private MainActivity main;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> updateHandler;

    updateScheduler(Engine engine) {
        this.engine = engine;
        main=MainActivity.getInstance();
        this.display=main.getHoursLeft();
    }
    public void saveEverySecond() {
        final Runnable saver = () -> update();

        updateHandler = scheduler.scheduleAtFixedRate(saver, 0, 5, SECONDS);
    }

    private void update() {
        engine.update();
        display.setText(engine.TLString());
        Log.i("display update","called update");
        Log.i("scheduler update","engine works");

    }

    public void stop() {
        updateHandler.cancel(true);
    }
}