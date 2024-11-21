package com.example.k;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.Random;

public class Invader {
    private int x, y;
    private int width, height;
    private int speed = 5; // Speed of invader movement
    private Bitmap bitmap; // Bitmap for invader image
    private static final Random random = new Random(); // Random object for selecting drawables
    private boolean destroyed; // Flag to indicate if the invader is destroyed

    public Invader(Context context, int x, int y, int[] drawableResources) {
        this.x = x;
        this.y = y;
        this.width = 120; // Increased width
        this.height = 90; // Increased height

        // Randomly select a drawable resource from the provided array
        int randomDrawable = drawableResources[random.nextInt(drawableResources.length)];

        // Load the invader image using the randomly selected drawable
        bitmap = BitmapFactory.decodeResource(context.getResources(), randomDrawable);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false); // Scale the bitmap if necessary

        // Initially, the invader is not destroyed
        destroyed = false;
    }

    public boolean isDestroyed() {
        return destroyed; // Check if the invader is destroyed
    }

    public void destroy() {
        destroyed = true; // Mark the invader as destroyed
    }

    public void update() {
        x += speed;
        if (x > 1080 || x < 0) { // Assuming 1080 is the screen width
            speed = -speed; // Change direction
            y += 20; // Move down
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        if (!destroyed) { // Draw only if not destroyed
            canvas.drawBitmap(bitmap, x, y, paint);
        }
    }

    // Getters for position and dimensions
    public int getX() {
        return x; // Getter for x
    }

    public int getY() {
        return y; // Getter for y
    }

    public int getWidth() {
        return width; // Getter for width
    }

    public int getHeight() {
        return height; // Getter for height
    }
}
