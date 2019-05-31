package com.example.bluetoothapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class GooseTip extends AppCompatActivity {

    private ImageButton cancel_btn;
    public static GooseTip instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        getSupportActionBar().hide();
        setContentView(R.layout.activity_goose_tip);
        cancel_btn = (ImageButton) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
