package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionActivity extends AppCompatActivity {
    TextView player1ScoreTextView;
    TextView player2ScoreTextView;
    TextView questionResultTextView;

    private final int MAX_STAGES = 3;

    private int currentStage;
    private int player1Score;
    private int player2Score;
    private int currentPlayer;


    ArrayList<Button> answerButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        currentStage = getIntent().getIntExtra("currentStage", 0);
        player1Score = getIntent().getIntExtra("player1Score", 0);
        player2Score = getIntent().getIntExtra("player2Score", 0);
        currentPlayer = getIntent().getIntExtra("currentPlayer", 1);
        Log.d("BackToQuestion", String.format("%s %s %s %s", currentStage, player1Score, player2Score, currentPlayer));

        player1ScoreTextView = findViewById(R.id.player1Score);
        player1ScoreTextView.setText(String.format("%s / %s", player1Score, MAX_STAGES));
        player2ScoreTextView = findViewById(R.id.player2Score);
        player2ScoreTextView.setText(String.format("%s / %s", player2Score, MAX_STAGES));

        questionResultTextView = findViewById(R.id.questionResult);
        questionResultTextView.setText(String.format("Игрок №%s,\nкто издаёт такой звук?", currentPlayer));

        HashMap<String, Boolean> answers = new HashMap<String, Boolean>();
        answers.put(getIntent().getStringExtra("TRUE_ANSWER"), true);
        answers.put(getIntent().getStringExtra("ANSWER2"), false);
        answers.put(getIntent().getStringExtra("ANSWER3"), false);
        answers.put(getIntent().getStringExtra("ANSWER4"), false);

        answerButtons.add(findViewById(R.id.answer1));
        answerButtons.add(findViewById(R.id.answer2));
        answerButtons.add(findViewById(R.id.answer3));
        answerButtons.add(findViewById(R.id.answer4));
        Collections.shuffle(answerButtons);
        Log.d("asdasd", answers.toString());
        String[] answersNames = answers.keySet().toArray(new String[0]);
        for (int i = 0; i < 4; i++) {
            Button answerButton = answerButtons.get(i);
            String answerName = answersNames[i];
            boolean isTrueAnswer = Boolean.TRUE.equals(answers.get(answerName));
            answerButton.setOnClickListener(v -> checkAnswer(answerButton, isTrueAnswer));
            answerButton.setText(answerName);
        }
    }

    private void checkAnswer(Button answerButton, boolean isCorrect) {

        for (Button b: answerButtons) {
            b.setEnabled(false);
        }

        if (isCorrect) {
            answerButton.setBackgroundColor(getColor(R.color.right_answer));
            questionResultTextView.setText("Верно \uD83D\uDE42");

            if (currentPlayer == 1) {
                player1Score += 1;
                player1ScoreTextView.setText(String.format("%s / %s", player1Score, MAX_STAGES));
            } else {
                player2Score += 1;
                player2ScoreTextView.setText(String.format("%s / %s", player2Score, MAX_STAGES));
            }
        }
        else {
            answerButton.setBackgroundColor(getColor(R.color.bad_answer));
            questionResultTextView.setText("Не правильно \uD83D\uDE22");
        }

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                backToMusic();
            }
        }, 1500);

    }

    private void backToMusic() {
        if (player1Score == 3 || player2Score == 3) {
            int playerWinnerNumber = player1Score > player2Score ? 1 : 2;
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("WINNER", playerWinnerNumber);
            startActivity(intent);
        }
        else {
            currentStage += 1;
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            Intent resultIntent = new Intent();
            resultIntent.putExtra("some_key", "String data");
            resultIntent.putExtra("currentStage", currentStage);
            resultIntent.putExtra("player1Score", player1Score);
            resultIntent.putExtra("player2Score", player2Score);
            resultIntent.putExtra("currentPlayer", currentPlayer);
            resultIntent.putExtra("PLAYER1_BID", getIntent().getIntExtra("PLAYER1_BID", 1));
            resultIntent.putExtra("PLAYER2_BID", getIntent().getIntExtra("PLAYER2_BID", 1));
            setResult(RESULT_OK, resultIntent);
        }

        finish();
    }
}