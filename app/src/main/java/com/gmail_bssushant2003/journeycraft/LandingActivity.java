package com.gmail_bssushant2003.journeycraft;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button buttonLetsGo = findViewById(R.id.button_lets_go);
        buttonLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingActivity.this,SendOTPActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}