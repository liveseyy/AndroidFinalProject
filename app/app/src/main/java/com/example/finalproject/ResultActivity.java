package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int winner = getIntent().getIntExtra("WINNER", 0);
        TextView resultText = findViewById(R.id.resultText);
        resultText.setText("Победил игрок " + winner + " \uD83C\uDF89");

        Button b = findViewById(R.id.buttonGoMain);
        b.setOnClickListener(v -> {
            finish();
        });
    }
}