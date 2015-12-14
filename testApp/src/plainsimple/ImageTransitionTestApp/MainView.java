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
    private Bitmap playButtonUp;
    private Bitmap playButtonDown;
    private boolean playButtonPressed = false;
    private int screenW;
    private int screenH;
    private Context context;
    private SlideOutTransition slideOut;

    public MainView(Context context) {
        super(context);
        this.context = context;
        titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
        slideOut = new SlideOutTransition(titleGraphic, 6, 100);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        screenW = w;
        screenH = h;
        Bitmap.createScaledBitmap(titleGraphic, w, h, false);
        slideOut = new SlideOutTransition(titleGraphic, 6, 100);
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
        //int event_action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!slideOut.isPlaying()) {
                    if(slideOut.hasFinished()) {
                        slideOut.reset();
                    }
                    Log.d("MainView Class", "Starting Transition");
                    slideOut.start();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                Log.d("MainView Class", "Stopping Transition");
                slideOut.stop();
                break;
        }
        //invalidate();
        return true;
    }
}
