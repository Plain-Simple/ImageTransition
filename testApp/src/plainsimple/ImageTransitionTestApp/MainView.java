package plainsimple.ImageTransitionTestApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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

    public MainView(Context context) {
        super(context);
        this.context = context;
        titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        screenW = w;
        screenH = h;
        Bitmap.createScaledBitmap(titleGraphic, w, h, false);
    }

    // draws titleGraphic centered on screen
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(titleGraphic, 0, 0, null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        //int event_action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }

    // starts the game activity
    private void launchGameIntent() {
        Intent game_intent = new Intent(context, MainActivity.class);
        context.startActivity(game_intent);
    }
}
