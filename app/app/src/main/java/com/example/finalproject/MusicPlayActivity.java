package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.finalproject.tools.Translator;


public class MusicPlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private int playTime;
    int player1Bid;
    int player2Bid;

    Translator translator = new Translator();

    private int currentStage = 1;
    private int player1Score = 0;
    private int player2Score = 0;
    private int currentPlayer = 1;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    onQuestionActivityResult(result);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        startMusic();
    }

    private void startMusic() {
        Field[] fields = R.raw.class.getFields();
        List<Field> randomAnswers = new ArrayList<>();
        Random random = new Random();
        while (randomAnswers.size() < 4) {
            int randomIndex = random.nextInt(fields.length);
            Field randomItem = fields[randomIndex];
            if (!randomAnswers.contains(randomItem)) {
                randomAnswers.add(randomItem);
            }
        }
        Log.d("randomAnswers", randomAnswers.toString());
        Field trueAnswer = randomAnswers.get(0);
        String trueAnswerName = translator.translateToRussian(trueAnswer.getName());
        int trueAnswerResourceID;
        try {
            trueAnswerResourceID = trueAnswer.getInt(trueAnswer);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        TextView playingMusicTimerTextView = findViewById(R.id.playingMusicTimer);
        TextView tradeWinnerMessage = findViewById(R.id.tradeWinnerMessage);

        playTime = getMusicPlayTime();
        mediaPlayer = MediaPlayer.create(this, trueAnswerResourceID);
        mediaPlayer.start();

        String winnerTradePlayerNumber = player1Bid == playTime ? "1" : "2";

        String tradeWinnerMessageText;
        tradeWinnerMessageText = String.format(
                "Победила ставка игрока №%s\nСлушай \uD83C\uDFB5",
                winnerTradePlayerNumber,
                playTime / 1000
        );
        tradeWinnerMessage.setText(tradeWinnerMessageText);

        playingMusicTimerTextView.setText(String.format("%s", playTime / 1000));
        new CountDownTimer(playTime, 1000) {
            public void onTick(long millisUntilFinished) {
                playingMusicTimerTextView.setText(String.format("%s", (millisUntilFinished + 1000) / 1000));
            }
            public void onFinish() {
                mediaPlayer.stop();
                mediaPlayer.release();

                Intent intent = new Intent(MusicPlayActivity.this, QuestionActivity.class);
                intent.putExtra("TRUE_ANSWER", trueAnswerName);
                intent.putExtra("ANSWER2", translator.translateToRussian(randomAnswers.get(1).getName()));
                intent.putExtra("ANSWER3", translator.translateToRussian(randomAnswers.get(2).getName()));
                intent.putExtra("ANSWER4", translator.translateToRussian(randomAnswers.get(3).getName()));

                intent.putExtra("currentStage", currentStage);
                intent.putExtra("player1Score", player1Score);
                intent.putExtra("player2Score", player2Score);
                intent.putExtra("currentPlayer", currentPlayer);

                mStartForResult.launch(intent);
            }
        }.start();
    }

    public void onQuestionActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            currentStage = intent.getIntExtra("currentStage", 0);
            player1Score = intent.getIntExtra("player1Score", 0);
            player2Score = intent.getIntExtra("player2Score", 0);
            currentPlayer = intent.getIntExtra("currentPlayer", 1);
            Log.d("BackToMusicPlay", String.format("%s %s %s %s", currentStage, player1Score, player2Score, currentPlayer));

            startMusic();
        }
        else finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /**
     * При торге за кол-во секунд прослушивания конечный результат выбирается случайно
     */
    private int getMusicPlayTime() {
        int player1Bid = getIntent().getIntExtra("PLAYER1_BID", 0);
        int player2Bid = getIntent().getIntExtra("PLAYER2_BID", 0);
        ArrayList<Integer> playersBids = new ArrayList<Integer>();
        playersBids.add(player1Bid);
        playersBids.add(player2Bid);
        Collections.shuffle(playersBids);
        return playersBids.get(0) * 1000; // Время в миллисекундах
    }
}