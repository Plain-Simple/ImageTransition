import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Given starting image of screen, creates a "sliding out" animation
 * and feeds frames
 */
public class SlideOutTransition {

    // starting image
    private Bitmap startScreen;
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
    private int screenWidth;
    private int screenHeight;

    public SlideOutTransition(Bitmap startScreen, int numRows, int totalFrames) {
        this.startScreen = startScreen;
        this.numRows = numRows;
        this.totalFrames = totalFrames;
        screenWidth = startScreen.getWidth();
        screenHeight = startScreen.getHeight();
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    // resets frameCounter to zero
    public void reset() {
        frameCounter = 0;
    }

    // renders and returns next frame in sequence
    public Bitmap nextFrame() {
        frameCounter++;
        return getFrame(frameCounter);
    }

    // renders and returns frame based on completion of sequence
    public Bitmap getFrame(float completion) throws IndexOutOfBoundsException {
        if(completion > 1.0 || completion < 0.0) {
            throw new IndexOutOfBoundsException("Invalid frame requested (" + (totalFrames * completion) + ")");
        } else {
            Canvas this_frame = new Canvas(startScreen);
            int row_height = screenWidth / numRows;

            int full_rows = (int) (completion * numRows);
            this_frame.drawRect(0, 0, screenWidth - 1, full_rows * row_height, paint);

            double row_completion = completion - (full_rows / numRows) * completion;
            this_frame.drawRect((float) (row_completion * screenWidth), full_rows * row_height,
                    screenWidth - 1, (full_rows + 1) / numRows * row_height, paint);
            return startScreen;
        }
    }

    // renders and returns frame based on frameNumber in sequence
    public Bitmap getFrame(int frameNumber) {
        return getFrame((float) (frameNumber / totalFrames));
    }
}
