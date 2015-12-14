package plainsimple.ImageTransition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Given starting image of screen, creates a "sliding out" animation
 * and feeds frames
 */
public class SlideOutTransition {

    // starting image
    private Bitmap startScreen;
    // actual working frame to be drawn on
    private Bitmap workingFrame;
    // current frame
    private int frameCounter = 0;
    // total frames in animation
    private int totalFrames;
    // num rows to slide across screen
    private int numRows;
    // color of rows to overlay
    private Paint paint;
    // percentage a row should slide across screen before next row starts moving
    private float threshold;
    // whether or not transition has been completed
    private boolean transitionFinished = false;
    private boolean isPlaying = false;

    private int screenWidth;
    private int screenHeight;

    public boolean hasFinished() {
        return transitionFinished;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public SlideOutTransition(Bitmap startScreen, int numRows, int totalFrames) {
        this.startScreen = startScreen.copy(Bitmap.Config.ARGB_8888, true);
        workingFrame = startScreen.copy(Bitmap.Config.ARGB_8888, true);
        this.numRows = numRows;
        this.totalFrames = totalFrames;
        screenWidth = startScreen.getWidth();
        screenHeight = startScreen.getHeight();
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    public void start() {
        isPlaying = true;
    }

    // sets transitionFinished to true
    public void stop() {
        transitionFinished = true;
        isPlaying = false;
    }

    // resets frameCounter to zero and recopies startScreen into workingFrame
    public void reset() {
        frameCounter = 0;
        workingFrame = startScreen.copy(Bitmap.Config.ARGB_8888, true);
    }

    // renders and returns next frame in sequence
    public Bitmap nextFrame() { // todo: require start?
        if (frameCounter < totalFrames) {
            frameCounter++;
            if (frameCounter + 1 == totalFrames) {
                transitionFinished = true;
                isPlaying = false;
            }
        }
        return getFrame(frameCounter);
    }

    // renders and returns frame based on completion of sequence
    public Bitmap getFrame(float completion) throws IndexOutOfBoundsException {
        if (completion > 1.0 || completion < 0.0) {
            throw new IndexOutOfBoundsException("Invalid frame requested (" + (totalFrames * completion) + ")");
        } else {
            Canvas this_frame = new Canvas(workingFrame);
            int row_height = screenWidth / numRows; // todo: screen size is off
            Log.d("TransitionClass", "numRows = " + numRows + " screenWidth = " + screenWidth + " row_height = " + row_height);
            // calculate and draw full rows
            int full_rows = (int) (completion * numRows);
            this_frame.drawRect(0, 0, screenWidth, full_rows * row_height, paint);

            // calculate percentage of current row completed
            float row_completion = (completion - (full_rows / (float) numRows)) * (float) numRows;
            this_frame.drawRect((1.0f - row_completion) * (float) screenWidth, full_rows * row_height,
                    screenWidth, (full_rows + 1) * row_height, paint);
            return workingFrame;
        }
    }

    // renders and returns frame based on frameNumber in sequence
    public Bitmap getFrame(int frameNumber) {
        return getFrame(frameNumber / (float) totalFrames);
    }
}
