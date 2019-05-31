package com.example.topbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private  Topbar topbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topbar = (Topbar) findViewById(R.id.topbar);

        topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(MainActivity.this, "left button click", Toast.LENGTH_LONG).show();
            }

            @Override
            public void rightClick() {
                Toast.makeText(MainActivity.this, "right button click", Toast.LENGTH_LONG).show();
            }
        });

    }
}
