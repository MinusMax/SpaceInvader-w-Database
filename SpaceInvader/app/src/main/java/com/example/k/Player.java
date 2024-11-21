package com.example.k;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {
    private int x, y;
    private int width, height;
    private Bitmap bitmap;

    public Player(Context context) {
        // Load the original bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        // Set the desired width and height (adjust these values as needed)
        width = bitmap.getWidth() / 4; // Assuming the original width is 4 times larger
        height = bitmap.getHeight() / 4; // Assuming the original height is 4 times larger

        // Scale the bitmap to the new size
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        // Set initial position
        x = 400; // Initial X position
        y = 1000; // Initial Y position (adjust as needed)
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    // Getters for x, y, width, and height
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Setter for X position
    public void setX(int x) {
        this.x = x;
    }

    // Setter for Y position
    public void setY(int y) {
        this.y = y; // Add this method to allow setting the Y position
    }
}
