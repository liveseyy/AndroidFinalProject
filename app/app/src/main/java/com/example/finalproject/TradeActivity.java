package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TradeActivity extends AppCompatActivity {

    private int currentPlayer = 1; // Текущий игрок
    private int player1Bid = 0;
    private int player2Bid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        TextView playerTurnText = findViewById(R.id.playerTurnText);
        EditText secondsInput = findViewById(R.id.secondsInput);
        Button tradeButton = findViewById(R.id.tradeButton);

        updateTurnText(playerTurnText);

        tradeButton.setOnClickListener(v -> {
            String input = secondsInput.getText().toString();
            int bid = input.isEmpty() ? 0 : Integer.parseInt(input);

            if (currentPlayer == 1) {
                player1Bid = bid;
                currentPlayer = 2;
            } else {
                player2Bid = bid;
                Intent intent = new Intent(TradeActivity.this, MusicPlayActivity.class);
                intent.putExtra("PLAYER1_BID", player1Bid);
                intent.putExtra("PLAYER2_BID", player2Bid);
                startActivity(intent);
                finish();
            }

            updateTurnText(playerTurnText);
            secondsInput.setText("");
        });
    }

    private void updateTurnText(TextView playerTurnText) {
        playerTurnText.setText("Ход игрока " + currentPlayer + ". Введите количество секунд:");
    }
}