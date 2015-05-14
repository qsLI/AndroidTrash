package com.air.ball;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by KL on 2015/5/9.
 */
public class DrawView extends View {
    public float currX = 40;
    public float currY = 50;
    public Paint p;

    public DrawView(Context context){
        super(context);
        p = new Paint();
        p.setColor(Color.RED);
    }

    public void changPainColor(int  color){
        p.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(currX,currY,15,p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.currX = event.getX();
        this.currY = event.getY();
        this.invalidate();
        return true;
    }
}
