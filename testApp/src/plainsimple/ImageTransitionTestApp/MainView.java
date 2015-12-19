package plainsimple.ImageTransitionTestApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import plainsimple.ImageTransition.SlideOutTransition;

/**
 * Created by Stefan on 10/17/2015.
 */
public class MainView extends View {

    private Bitmap titleGraphic;
    private Bitmap testGraphic;
    private int screenW;
    private int screenH;
    private SlideOutTransition slideOut;

    public MainView(Context context) {
        super(context);
        titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
        testGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.test_screen);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        screenW = w;
        screenH = h;
        titleGraphic = Bitmap.createScaledBitmap(titleGraphic, screenW, screenH, false); // todo: use matrix to resize image
        testGraphic = Bitmap.createScaledBitmap(testGraphic, screenW, screenH, false);
        slideOut = new SlideOutTransition(titleGraphic, testGraphic, 6, 100, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(slideOut.isPlaying()) {
            canvas.drawBitmap(slideOut.nextFrame(), 0, 0, null);
        } else {
            canvas.drawBitmap(titleGraphic, 0, 0, null);
        }
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!slideOut.isPlaying()) {
                    if(slideOut.hasFinished()) {
                        slideOut.reset();
                    }
                    slideOut.start();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                slideOut.stop();
                break;
        }
        return true;
    }
}
