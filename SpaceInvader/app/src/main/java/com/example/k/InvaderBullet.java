package com.example.k;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class InvaderBullet {
    private int x, y;
    private int width, height;
    private boolean active;
    private int speed = 5; // Bullet speed
    private Bitmap bitmap; // Bitmap for bullet image

    public InvaderBullet(Context context) {
        this.width = 120; // Increased bullet width
        this.height = 240; // Increased bullet height
        this.active = false;

        // Load the bullet image (you can create a drawable for the bullet)
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.alienbullet); // Replace with your drawable resource
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false); // Scale the bitmap if necessary
    }

    public void shoot(int startX, int startY) {
        this.x = startX - width / 2; // Center the bullet horizontally
        this.y = startY;
        this.active = true; // Set bullet as active
    }

    public void update() {
        if (active) {
            y += speed; // Move the bullet downward
            if (y > 1920) { // Assuming 1920 is the screen height
                active = false; // Deactivate bullet when it goes off-screen
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        if (active) {
            // Draw the bullet using the bitmap
            canvas.drawBitmap(bitmap, x, y, paint);
        }
    }

    public boolean isActive() {
        return active;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width; // Getter for bullet width
    }

    public int getHeight() {
        return height; // Getter for bullet height
    }
}
