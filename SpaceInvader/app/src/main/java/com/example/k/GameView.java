package com.example.k;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log; // Ensure this is imported for logging
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.MotionEvent;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private boolean isPlaying;
    private boolean isGameOver; // Game over state
    private boolean isVictory; // Victory state
    private SurfaceHolder holder;
    private Paint paint;
    private Player player;
    private Bullet playerBullet;
    private List<Invader> invaders;
    private int score; // Variable to keep track of the score
    private GameState gameState; // Current game state

    private ScoreViewModel scoreViewModel; // Declare ScoreViewModel
    private boolean scoreSaved = false; // Flag to prevent multiple saves

    private int[] invaderDrawables = {
            R.drawable.alien,
            R.drawable.alienyellow,
            R.drawable.aliencyan,
            R.drawable.alienmagenta
    };

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        paint = new Paint();

        // Initialize ScoreViewModel
        scoreViewModel = new ViewModelProvider((MainActivity) context).get(ScoreViewModel.class);

        // Initialize player and bullet
        player = new Player(context);
        playerBullet = new Bullet();

        // Initialize invaders
        invaders = new ArrayList<>();
        int rows = 3; // Number of rows
        int cols = 4; // Number of columns
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                invaders.add(new Invader(context, col * 150 + 100, row * 100 + 100, invaderDrawables)); // Adjust spacing
            }
        }

        // Initialize score
        score = 0;

        // Set initial game state
        gameState = GameState.MENU;
    }

    @Override
    public void run() {
        while (isPlaying) {
            if (!holder.getSurface().isValid()) continue;
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK); // Background color

            // Draw based on the current game state
            switch (gameState) {
                case PLAYING:
                    updateAndDrawGame(canvas);
                    break;
                case GAME_OVER:
                case VICTORY:
                    drawGameOverOrVictory(canvas);
                    break;
                case MENU:
                    drawMenu(canvas);
                    break;
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void updateAndDrawGame(Canvas canvas) {
        // Update and draw player and bullet
        player.draw(canvas, paint);
        playerBullet.update();
        playerBullet.draw(canvas, paint);

        // Check for bullet and invader collisions
        List<Invader> toRemove = new ArrayList<>();
        for (Invader invader : invaders) {
            invader.update();
            invader.draw(canvas, paint);

            // Check collision between player's bullet and invader
            if (playerBullet.isActive() && checkCollision(playerBullet, invader)) {
                invader.destroy(); // Mark invader as destroyed
                toRemove.add(invader);  // Add to list for removal
                playerBullet.deactivate();  // Reset bullet
                score += 10; // Increase score when an invader is hit
                Log.d("GameView", "Score incremented: " + score); // Debug log
            }

            // Check if the invader hits the player's ship
            if (checkCollision(player, invader)) {
                isGameOver = true; // End game if invader hits player
                gameState = GameState.GAME_OVER; // Change game state to GAME_OVER
            }
        }

        // Remove invaders that were hit by the bullet
        invaders.removeAll(toRemove);

        // Check for victory state
        if (invaders.isEmpty()) {
            isVictory = true; // Set victory state if all invaders are destroyed
            gameState = GameState.VICTORY; // Change game state to VICTORY
        }

        // Draw the score
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("Score: " + score, 50, 100, paint); // Display score at the top left
    }

    private void drawMenu(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        String menuText = "Tap to Start";
        float menuTextWidth = paint.measureText(menuText);
        canvas.drawText(menuText, (getWidth() - menuTextWidth) / 2, getHeight() / 2, paint); // Centered text for menu
    }

    private void drawGameOverOrVictory(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setTextSize(100);

        if (gameState == GameState.GAME_OVER) {
            canvas.drawText("Game Over", 280, 960, paint); // Centered text for game over
            saveScoreIfNeeded(); // Save the score if the game is over
        } else if (gameState == GameState.VICTORY) {
            paint.setColor(Color.GREEN);
            canvas.drawText("Victory!", 320, 960, paint); // Centered text for victory
            saveScoreIfNeeded(); // Save the score if the game is won
        }

        // Instructions for restarting or going to the menu
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        String restartText = "Tap to Restart";
        String menuText = "Tap Below for Menu";

        // Center the text horizontally
        float restartTextWidth = paint.measureText(restartText);
        float menuTextWidth = paint.measureText(menuText);

        canvas.drawText(restartText, (getWidth() - restartTextWidth) / 2, 1080, paint); // Display restart option
        canvas.drawText(menuText, (getWidth() - menuTextWidth) / 2, 1150, paint); // Display menu option
    }

    private void saveScoreIfNeeded() {
        if (!scoreSaved) { // Check if the score has already been saved
            Log.d("GameView", "Saving Score: " + score); // Debug log
            if (score > 0) { // Ensure only positive scores are saved
                scoreViewModel.insert(new Score(score)); // Save score
                Log.d("GameView", "Score saved successfully: " + score); // Confirm saving
            } else {
                Log.d("GameView", "Score is zero or negative, not saving."); // Debug log for zero score
            }
            scoreSaved = true; // Set flag to true
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (gameState == GameState.GAME_OVER || gameState == GameState.VICTORY) {
                // Check if the touch event is within the restart area
                if (event.getY() < getHeight() - 200) { // This is the restart area
                    restartGame();
                } else {
                    // Check if touch is below the victory or game over message
                    if (event.getY() > 900) { // Adjust this value according to your UI
                        // Navigate to the ScoreDisplayActivity to see past scores
                        Context context = getContext();
                        Intent intent = new Intent(context, ScoreDisplayActivity.class);
                        context.startActivity(intent);
                    }
                }
            } else if (gameState == GameState.MENU) {
                // Start the game when tapping the menu
                startGame();
            } else if (gameState == GameState.PLAYING) {
                // Allow player to shoot and move only if the game is in play state
                playerBullet.shoot(player.getX() + player.getWidth() / 2, player.getY());
                player.setX((int) event.getX());
            }
        }
        return true;
    }

    private void restartGame() {
        // Reset game state
        isGameOver = false;
        isVictory = false;
        score = 0; // Reset score to 0
        invaders.clear(); // Clear existing invaders
        scoreSaved = false; // Reset score saved flag
        // Reinitialize invaders and player
        int rows = 6; // Number of rows
        int cols = 6; // Number of columns
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                invaders.add(new Invader(getContext(), col * 150 + 100, row * 100 + 100, invaderDrawables)); // Adjust spacing
            }
        }
        gameState = GameState.PLAYING; // Change game state to PLAYING
    }

    private void goToMenu() {
        // Reset game state to menu
        isGameOver = false;
        isVictory = false;
        score = 0; // Reset score to 0
        invaders.clear(); // Clear existing invaders
        scoreSaved = false; // Reset score saved flag
        gameState = GameState.MENU; // Change game state to MENU
    }

    private void startGame() {
        restartGame(); // Start the game by resetting it
    }

    // Collision detection method for player and invader
    private boolean checkCollision(Player player, Invader invader) {
        return player.getX() < invader.getX() + invader.getWidth() &&
                player.getX() + player.getWidth() > invader.getX() &&
                player.getY() < invader.getY() + invader.getHeight() &&
                player.getY() + player.getHeight() > invader.getY();
    }

    // Collision detection method for player's bullet and invader
    private boolean checkCollision(Bullet bullet, Invader invader) {
        return bullet.getX() > invader.getX() &&
                bullet.getX() < invader.getX() + invader.getWidth() &&
                bullet.getY() > invader.getY() &&
                bullet.getY() < invader.getY() + invader.getHeight();
    }
}
