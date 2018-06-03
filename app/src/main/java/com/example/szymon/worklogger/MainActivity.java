package com.example.szymon.worklogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {

    private TextView hoursLeft;
    private Engine engine;
    private boolean counting=false;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button edit = findViewById(R.id.editData);
        open("data");
        if(engine==null) engine=new Engine();
        hoursLeft=findViewById(R.id.hoursLeft);
        hoursLeft.setText(engine.TLString());
        edit.setOnClickListener(e -> openEdit());
        start=findViewById(R.id.startButton);
        start.setOnClickListener(e->startCounting());
        Button skip=findViewById(R.id.SkipOneDay);
        skip.setOnClickListener(e->dayFree());

        Log.i("hpw log", String.valueOf(Engine.HPW));

    }

    private void dayFree() {
        engine.skipDay();
        hoursLeft.setText(engine.TLString());
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
            hoursLeft.setText(engine.TLString());
        }
    }

    private void openEdit() {
        Intent intent = new Intent(this, EditData.class);
        startActivityForResult(intent, 0);
    }

    protected void onDestroy() {
        save();
        super.onDestroy();


    }

    private void save() {
        try {
            OutputStreamWriter out =
                    new OutputStreamWriter(openFileOutput("data", 0));
            Log.i("info log", String.valueOf(Engine.HPW+"\n"));
            out.write(String.valueOf(Engine.HPW+"\n"));
            out.write(String.valueOf(engine.getTL())+"\n");
            out.write(LocalDateTime.now().toString());
                Log.i("info log", String.valueOf(engine.getTL()));
            out.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void open(String fileName) {
        try {
            InputStream in = openFileInput(fileName);
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                Engine.HPW=Double.parseDouble(reader.readLine());
            Log.i("info log", String.valueOf(Engine.HPW));
                engine=new Engine(Long.parseLong(reader.readLine()),LocalDateTime.parse(reader.readLine()));
            Log.i("info log", String.valueOf(engine.getTL()));
                in.close();
        }
        catch (FileNotFoundException ignored) {}
        catch(Throwable t){
                Toast.makeText(this, "Data loading exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                engine.refreshTL();
                hoursLeft.setText(engine.TLString());
            }
        }
    }
}
