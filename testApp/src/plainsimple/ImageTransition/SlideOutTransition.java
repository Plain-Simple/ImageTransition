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
    // ending image
    private Bitmap endScreen;
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

    public SlideOutTransition(Bitmap startScreen, Bitmap endScreen, int numRows, int totalFrames) {
        this.startScreen = startScreen;
        workingFrame = startScreen.copy(Bitmap.Config.ARGB_8888, true);
        this.endScreen = endScreen;
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
        } else if (frameCounter == totalFrames) {
            transitionFinished = true;
            isPlaying = false;
        }
        return getFrame(frameCounter);
    }

    // renders and returns frame based on completion of sequence
    public Bitmap getFrame(float completion) { // todo: out of bounds error handling
        Canvas this_frame = new Canvas(workingFrame);
        int row_height = screenHeight / numRows;

        int full_rows = (int) (completion * numRows);
        if(full_rows > 0) {
            Bitmap subimage = Bitmap.createBitmap(endScreen, 0, 0, screenWidth, full_rows * row_height); // todo: use drawBitmap with rectangles
            this_frame.drawBitmap(subimage, 0, 0, null);
        }

        // calculate percentage of current row completed
        float row_completion = (completion - (full_rows / (float) numRows)) * (float) numRows;
        if(row_completion > 0) {
            Bitmap subimage = Bitmap.createBitmap(endScreen, (int) ((1.0f - row_completion) * screenWidth),
                    full_rows * row_height, (int) (row_completion * (float) screenWidth), row_height);
            this_frame.drawBitmap(subimage, (1.0f - row_completion) * (float) screenWidth, full_rows * row_height, null);
        }
        return workingFrame;
    }

    // renders and returns frame based on frameNumber in sequence
    public Bitmap getFrame(int frameNumber) {
        return getFrame(frameNumber / (float) totalFrames);
    }
}
