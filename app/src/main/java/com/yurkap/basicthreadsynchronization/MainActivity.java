package com.yurkap.basicthreadsynchronization;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        Button stop = findViewById(R.id.stop);
        Button start = findViewById(R.id.start);
        Button clear = findViewById(R.id.clear);

        BasicThreadViewModel model = new ViewModelProvider(this).get(BasicThreadViewModel.class);

        model.getValue().observe(this, integer -> textView.setText(String.valueOf(integer)));
        model.setValue(0);


        model.getButtonStateListener().observe(this, aBoolean -> {
            if (aBoolean) {
                Log.d(TAG, "Button State 1: " + true);
                model.start();
                start.setEnabled(false);
                stop.setEnabled(true);
            } else {
                model.stop();
                start.setEnabled(true);
                stop.setEnabled(false);
                Log.d(TAG, "Button State 2: " + false);
            }
        });

        model.init();

        start.setOnClickListener(v -> {
            if (!model.isRunning()) {
                model.start();
                model.init();
            }
        });

        stop.setOnClickListener(v -> model.stop());

        clear.setOnClickListener(v -> {
            model.stop();
            model.clearData(0);
        });
    }
}