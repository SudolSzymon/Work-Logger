package com.example.szymon.worklogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class EditData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        Button save=findViewById(R.id.save);
        EditText hpw=findViewById(R.id.hoursPerWeak);
        save.setOnClickListener(e->quit(hpw.getText().toString()));
        Button cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(e-> cancel());
          }

    private void cancel() {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    private void quit(String hpw) {
        if(hpw.length()==0) hpw=String.valueOf(Engine.HPW);
            Engine.HPW=Double.parseDouble(hpw);
        Log.i("info log",hpw);
        setResult(RESULT_OK);
            this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
