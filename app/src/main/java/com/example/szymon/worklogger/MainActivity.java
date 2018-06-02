package com.example.szymon.worklogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView hoursLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button edit = findViewById(R.id.EditData);
        open("data");
        hoursLeft=findViewById(R.id.hoursLeft);
        hoursLeft.setText(String.valueOf(Engine.HPW));
        edit.setOnClickListener(e -> openEdit());
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
            Log.i("info log", String.valueOf(Engine.HPW));
            out.write(String.valueOf(Engine.HPW));
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
                hoursLeft.setText(String.valueOf(Engine.HPW));
            }
        }
    }
}
