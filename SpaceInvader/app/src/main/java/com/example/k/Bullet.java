package com.example.k;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bullet {
    private int x, y;
    private boolean isActive;
    private int speed = 15;  // Bullet speed

    public Bullet() {
        isActive = false;
    }

    public void update() {
        if (isActive) {
            y -= speed;
            if (y < 0) isActive = false;  // Reset bullet if it goes off screen
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        if (isActive) {
            paint.setColor(Color.WHITE);
            canvas.drawRect(x - 5, y, x + 5, y + 20, paint);
        }
    }

    public void shoot(int startX, int startY) {
        if (!isActive) {  // Only allow shooting if no active bullet exists
            x = startX;
            y = startY;
            isActive = true;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }
}
