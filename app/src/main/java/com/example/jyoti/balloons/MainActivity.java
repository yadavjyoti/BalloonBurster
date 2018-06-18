package com.example.jyoti.balloons;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jyoti.balloons.utilis.HighScoreHelper;
import com.example.jyoti.balloons.utilis.SimpleAlertDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Balloonimage.BalloonListener {
    private static final int BALLOON_PER_LEVEL = 100;
    private ViewGroup mContentView;
    private static int MIN_ANIMATION_DURATION = 1000;
    private static int MAX_ANIMATION_DURATION = 8000;
    private static int no_of_pins = 5;

    private ArrayList<Integer> mBalloonColor = new ArrayList<>();
    private int mnextColor, mscreenHeight, mscreenWidth;
    private int mLevel = 0, pinsUsed = 0, mScore = 0;
    TextView mScoreDisplay;
    private Button goButton;
    private List<Balloonimage> mBalloon = new ArrayList<>();
    private int mPoppedBalloon = 0;
    private BalloonLauncher launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //updating color of balloons
        mBalloonColor.add(Color.argb(255, 255, 0, 0));
        mBalloonColor.add(Color.argb(255, 0, 255, 0));
        mBalloonColor.add(Color.argb(255, 0, 0, 255));

        mContentView = (ViewGroup) findViewById(R.id.activity_main);

        // ViewTreeObserver is used to register listeners that can be notified of global changes in the view tree
        ViewTreeObserver viewtreeObserver = mContentView.getViewTreeObserver();
        if (viewtreeObserver.isAlive()) {
            viewtreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mscreenHeight = mContentView.getHeight();
                    mscreenWidth = mContentView.getWidth();
                }
            });
        }

        mScoreDisplay = (TextView) findViewById(R.id.score_display);
        goButton = (Button) findViewById(R.id.go_button);
        mScoreDisplay.setText(String.valueOf(mScore));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startGame() {
        mScoreDisplay.setText(String.valueOf(mScore));
        goButton.setText("Stop Game");
        launcher = new BalloonLauncher();
        launcher.execute();
    }

//    public void goButtonClickHandler(View view) {
//        if (goButton.getText().equals("Stop Game")) {
//            gameOver(false);
//        } else if (goButton.getText().equals("Play Game")) {
//            startGame();
//        }
//    }

    private class BalloonLauncher extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            for (int i = 0; i <= BALLOON_PER_LEVEL; i++) {
//              Get a random horizontal position for the next balloon
                Random random = new Random(new Date().getTime());
                int xPosition = random.nextInt(mscreenWidth - 200);
                publishProgress(xPosition);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            if (goButton.getText().equals("Stop Game")) {
                launchBalloon(xPosition);
            }
        }
    }

    private void launchBalloon(int x) {
        Balloonimage balloon = new Balloonimage(this, mBalloonColor.get(mnextColor), 150);
        mBalloon.add(balloon);
        if (mnextColor + 1 == mBalloonColor.size()) {
            mnextColor = 0;
        } else {
            mnextColor++;
        }


//      Set balloon vertical position and dimensions, add to container
        balloon.setX(x);
        balloon.setY(mscreenHeight + balloon.getHeight());
        mContentView.addView(balloon);

//      Let 'er fly
        int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (mLevel * 1000));
        balloon.releaseBalloon(mscreenHeight, duration);
    }

    @Override
    public void popBalloon(Balloonimage balloon, boolean userTouch, int color) {
        mPoppedBalloon++;
        mContentView.removeView(balloon);
        mBalloon.remove(balloon);

        if (userTouch) {
            if (color == Color.argb(255, 255, 0, 0)) {
                mScore += 5;
            } else if (color == Color.argb(255, 0, 255, 0)) {
                mScore += 10;
            } else if (color == Color.argb(255, 0, 0, 255)) {
                mScore += 15;
            }
        } else {
            pinsUsed++;
            if (pinsUsed == no_of_pins) {
                gameOver(true);
                return;
            } else {
                Toast.makeText(this, "Missed that one", Toast.LENGTH_SHORT).show();
            }
        }
        mScoreDisplay.setText(String.valueOf(mScore));
    }

    private void gameOver(boolean allPinsUsed) {
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
        for (Balloonimage balloon : mBalloon) {
            balloon.setPoped(true);
            mContentView.removeView(balloon);
        }
        mPoppedBalloon=0;
        pinsUsed=0;
        mBalloon.clear();
        goButton.setText("Play Game");
        mScore=0;

        if (allPinsUsed) {
            if (HighScoreHelper.isTopScore(this, mScore)) {
                HighScoreHelper.setTopScore(this, mScore);
                SimpleAlertDialog dialog = SimpleAlertDialog.newInstance("HIGH SCORE", String.format("YOUR NEW HIGH SCORE IS %d", mScore));
                dialog.show(getSupportFragmentManager(), null);
            }
        }
    }
}
