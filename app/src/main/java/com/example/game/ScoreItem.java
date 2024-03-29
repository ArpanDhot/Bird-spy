package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ScoreItem extends Position implements GameObject {

    private Rect rectangle;
    private Context context;
    private Random random;

    private Bitmap scoreSprite;

    private Bitmap scoreCountSprite;
    private Paint scoreCountPaint;

    private Bitmap scoreDisplayCountSprite;
    private Paint scoreDisplayCountPaint;

    private Bitmap highScoreSprite;
    private Paint highScorePaint;

    private int highScore=0;


    private int scoreCount = 0;
    private int scoreDisplayCount = 0;


    /**
     * In order for the update to work with the velocity I need to pass point and that store the point values to x and y.
     * If I pass x and y then set point it will give errors
     *
     * @param rectangle
     * @param point
     */
    public ScoreItem(Rect rectangle, Point point, Context context) {
        this.rectangle = rectangle;
        this.setxPos(point.x);
        this.setyPos(point.y);

        this.context = context;

        random = new Random();

        //Setting up the birdSprites
        scoreSprite = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.strawberry);
        scoreCountSprite = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.scorecount);
        scoreDisplayCountSprite = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.scoredisplay);
        highScoreSprite = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.trophy);

        //Setting up the Paint for item count as well as the item display
        scoreCountPaint = new Paint();
        scoreCountPaint.setColor(Color.BLACK);
        scoreCountPaint.setStyle(Paint.Style.FILL);
        scoreCountPaint.setTextSize(40);

        scoreDisplayCountPaint = new Paint();
        scoreDisplayCountPaint.setColor(Color.WHITE);
        scoreDisplayCountPaint.setStyle(Paint.Style.FILL);
        scoreDisplayCountPaint.setTextSize(35);

        highScorePaint = new Paint();
        highScorePaint.setColor(Color.BLACK);
        highScorePaint.setStyle(Paint.Style.FILL);
        highScorePaint.setTextSize(40);

        //Setting up the points of the rectangle shape. This will draw the four points of the rectangle
        //left, top, right, bottom
        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);

        //Firebase
        try{
            FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        highScore =  dataSnapshot.getValue(Integer.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });}catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    /**
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {

        //This method allows to scale the image size
        //Spawn item sprite
        Bitmap resizedBitmap0 = Bitmap.createScaledBitmap(scoreSprite, 40, 40, true);
        //Display item, sprite
        Bitmap resizedBitmap1 = Bitmap.createScaledBitmap(scoreCountSprite, 90, 80, true);
        Bitmap resizedBitmap2 = Bitmap.createScaledBitmap(scoreDisplayCountSprite, 110, 110, true);
        Bitmap resizedBitmap3 = Bitmap.createScaledBitmap(highScoreSprite, 120, 90, true);

        //drawing the Bitmap and text on to the canvas
        //Spawn item sprite
        canvas.drawBitmap(resizedBitmap0, this.getxPos()-20, this.getyPos()-20, null);//I have subtracted the width and high from the x and y. So the stripe will align with the rectangle below and the collision will work perfectly

        //Display ite, sprite
        canvas.drawBitmap(resizedBitmap1, 140,32, null);
        canvas.drawText("" + scoreCount, 175, 100, scoreCountPaint);
        canvas.drawBitmap(resizedBitmap2, 230,20, null);
        canvas.drawText("" + scoreDisplayCount, 265, 95, scoreDisplayCountPaint);
        canvas.drawBitmap(resizedBitmap3, 1500,35, null);
        canvas.drawText("" + highScore, 1550, 90, highScorePaint);

    }


    @Override
    public void update() {

    }


    /**
     * To update to a random new location
     */
    public void updateMovement() {

        int X = random.nextInt(1000);
        int Y = random.nextInt(700);
        this.setxPos(X);
        this.setyPos(Y);
        rectangle.set((this.getxPos() - rectangle.width() / 2), (this.getyPos() - rectangle.height() / 2), (this.getxPos() + rectangle.width() / 2), (this.getyPos() + rectangle.height() / 2));


    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rect rectangle) {
        this.rectangle = rectangle;
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }

    public int getScoreDisplayCount() {
        return scoreDisplayCount;
    }

    public void setScoreDisplayCount(int scoreDisplayCount) {
        this.scoreDisplayCount = scoreDisplayCount;
    }

    /**
     * Remouvi questo metodo perche non fa nulla
     * @param n1
     * @param n2
     */
    @Override
    public void updatePos(int n1, int n2) {
        int min;
        if (n1 > n2)
            min = n2;
        else
            min = n1;
    }
}
