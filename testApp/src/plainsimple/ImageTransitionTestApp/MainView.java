package plainsimple.ImageTransitionTestApp;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import plainsimple.ImageTransition.SlideInTransition;
import plainsimple.ImageTransition.SlideOutTransition;

/**
 * Created by Stefan on 10/17/2015.
 */
public class MainView extends View {

    private Bitmap mainGraphic;
    private Bitmap testGraphic;
    private SlideOutTransition slideOut;
    private SlideInTransition slideIn;
    private boolean onMainScreen = true;

    public MainView(Context context) {
        super(context);
        mainGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.main_graphic);
        testGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.test_screen);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mainGraphic = Bitmap.createScaledBitmap(mainGraphic, w, h, false); // todo: use matrix to resize image
        testGraphic = Bitmap.createScaledBitmap(testGraphic, w, h, false);
        // the main graphic will slide out to test graphic
        slideOut = new SlideOutTransition(mainGraphic, testGraphic, 6, 100, 0.5f, false);
        // the test graphic will slide in to main graphic
        slideIn = new SlideInTransition(testGraphic, mainGraphic, 6, 100, 0.5f, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (slideIn.isPlaying()) {
            canvas.drawBitmap(slideIn.nextFrame(), 0, 0, null);
        } else if (slideOut.isPlaying()) {
            canvas.drawBitmap(slideOut.nextFrame(), 0, 0, null);
        } else if (onMainScreen) {
            canvas.drawBitmap(mainGraphic, 0, 0, null);
        } else if (!onMainScreen) {
            canvas.drawBitmap(testGraphic, 0, 0, null);
        }
        if (slideIn.hasFinished()) {
            onMainScreen = true;
            slideIn.reset();
        }
        if (slideOut.hasFinished()) {
            onMainScreen = false;
            slideOut.reset();
        }
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onMainScreen && !slideOut.isPlaying()) {
                    slideOut.start();
                } else if (!onMainScreen && !slideIn.isPlaying()) {
                    slideIn.start();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
