package com.example.szymon.worklogger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static MainActivity instance=null;
    private TextView hoursLeft;
    private Engine engine;
    private boolean counting=false;
    private Button start;
    public static MainActivity getInstance(){
        return instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.activity_main);
        Button edit = findViewById(R.id.editData);
        hoursLeft=findViewById(R.id.hoursLeft);
        edit.setOnClickListener(e -> openEdit());
        start=findViewById(R.id.startButton);
        start.setOnClickListener(e->startCounting());
        Button skip=findViewById(R.id.SkipOneDay);
        skip.setOnClickListener(e->dayFree());
        open();
        if(engine==null) engine=new Engine();
        reviwe();
        updateDisplay();
        Log.i("hpw log", String.valueOf(Engine.HPW));
    }

    private void reviwe() {
        if (counting) {
            counting = false;
            startCounting();
        }
    }

    private void dayFree() {
        engine.skipDay();
        updateDisplay();
    }

    private void startCounting() {
        if(!counting){
            counting=true;
            engine.start();
            start.setText(R.string.stop);
        } else {
            counting=false;
            engine.stop();
            start.setText(R.string.start);
            updateDisplay();
        }
    }

    public void updateDisplay() {
        hoursLeft.setText(engine.TLString());
        Log.i("display update","called update");
    }

    public TextView getHoursLeft() {
        return hoursLeft;
    }

    private void openEdit() {
        Intent intent = new Intent(this, EditData.class);
        startActivityForResult(intent, 0);
    }

    protected void onDestroy() {
        save();
        super.onDestroy();


    }

    @Override
    protected void onPause() {
        save();
        super.onPause();
    }

    void save() {
        try {
            OutputStreamWriter out =
                    new OutputStreamWriter(openFileOutput("data", 0));
            Log.i("sva hpw log", String.valueOf(Engine.HPW + "\n"));
            out.write(String.valueOf(Engine.HPW+"\n"));
            out.write(String.valueOf(engine.getTL())+"\n");
            out.write(LocalDateTime.now().toString() + "\n");
            Log.i("save tl log", String.valueOf(engine.getTL()));
            out.write(String.valueOf(System.currentTimeMillis()) + "\n");
            Log.i("save time log", String.valueOf(System.currentTimeMillis()));

            Log.i("save state log", String.valueOf(counting));
            out.write(String.valueOf(counting) + "\n");
            out.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void open() {
        try {
            Log.i("info log", "started reading file");
            InputStream in = openFileInput("data");
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                Engine.HPW=Double.parseDouble(reader.readLine());
            Log.i("hpw log", String.valueOf(Engine.HPW));
            Long lastTL = Long.valueOf(reader.readLine());
            LocalDateTime lastUse = LocalDateTime.parse(reader.readLine());
            Log.i("engine tl log", String.valueOf(lastTL));
            Long exitTime = Long.parseLong(reader.readLine());
            Log.i("exit time log", String.valueOf(exitTime));
            counting = Boolean.valueOf(reader.readLine());
            Log.i("counting log", String.valueOf(counting));
            if (counting) lastTL = adjustTL(lastTL, exitTime);
            engine = new Engine(lastTL, lastUse);
                in.close();
        } catch (FileNotFoundException ignored) {
        } catch (Throwable t) {
            Log.e("error log", t.toString(), t);
                Toast.makeText(this, "Data loading exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
    }

    private Long adjustTL(Long lastTL, Long exitTime) {
        Log.i("adjusting log", "adjusting tl");
        Long difference = System.currentTimeMillis() - exitTime;
        if (difference > 0 && difference < 3600 * 16) lastTL -= difference;
        return lastTL;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                engine.refreshTL();
                updateDisplay();
            }
        }
    }


}
