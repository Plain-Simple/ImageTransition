package plainsimple.ImageTransition;

import android.graphics.*;

/**
 * Given starting image of screen, creates a "sliding out" animation
 * and feeds frames
 */
public class SlideOutTransition {

    // starting image
    private Bitmap startImage;
    // actual working frame to be drawn on
    private Bitmap workingFrame;
    // ending image
    private Bitmap endImage;
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
    // whether start image shoulc be "pushed" off the screen
    private boolean pushOffScreen = false;
    // whether or not transition has been completed
    private boolean transitionFinished = false;
    // whether or not transition animation is currently playing
    private boolean isPlaying = false;
    // dimensions of image
    private int imgWidth;
    private int imgHeight;

    public boolean hasFinished() {
        return transitionFinished;
    }

    public boolean isPlaying() {
        return isPlaying;
    } // todo: settings

    public SlideOutTransition(Bitmap startImage, Bitmap endImage, int numRows, int totalFrames) {
        this.startImage = startImage;
        workingFrame = startImage.copy(Bitmap.Config.ARGB_8888, true);
        this.endImage = endImage;
        this.numRows = numRows;
        this.totalFrames = totalFrames;
        imgWidth = startImage.getWidth();
        imgHeight = startImage.getHeight();
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

    // resets frameCounter to zero and recopies startImage into workingFrame
    public void reset() {
        frameCounter = 0;
        workingFrame = startImage.copy(Bitmap.Config.ARGB_8888, true);
    }

    // renders and returns next frame in sequence
    public Bitmap nextFrame() { 
        if(frameCounter == 0) {
            start();
        }
        if (frameCounter < totalFrames) {
            frameCounter++;
            return getFrame(frameCounter);
        } else {
            stop();
            return endImage;
        }
    }

    // renders and returns frame based on completion of sequence
    public Bitmap getFrame(float completion) { // todo: out of bounds error handling
        Canvas this_frame = new Canvas(workingFrame);
        int row_height = imgHeight / numRows;

        if(completion >= 1.0) {
            return endImage;
        } else if(completion <= 0.0) {
            return startImage;
        } else {
            // draw full rows, simply transferring from endImage to startImage
            int full_rows = (int) (completion * numRows);
            Rect end_src = new Rect(0, 0, imgWidth, full_rows * row_height);
            this_frame.drawBitmap(endImage, end_src, end_src, null);

            // calculate percentage of current row completed
            float row_completion = (completion - (full_rows / (float) numRows)) * (float) numRows;

            if (pushOffScreen) {
                // shift current row of start image left, taking 1 - row_completion as width
                Rect start_src = new Rect((int) (imgWidth * row_completion), full_rows * row_height,
                        imgWidth, (full_rows + 1) * row_height);
                Rect start_dst = new Rect(0, start_src.top, start_src.width(), start_src.bottom);
                this_frame.drawBitmap(startImage, start_src, start_dst, null);

                // take leading portion from endImage and transfer to trailing portion of start image
                end_src = new Rect(0, full_rows * row_height, (int) (row_completion * imgWidth), (full_rows + 1) * row_height);
                Rect end_dst = new Rect(imgWidth - end_src.width(), end_src.top, imgWidth, end_src.bottom);
                this_frame.drawBitmap(endImage, end_src, end_dst, null);
            } else {
                // simply transfer trailing portion of end image to start image
                end_src = new Rect((int) ((1.0 - row_completion) * imgWidth), full_rows * row_height, imgWidth, (full_rows + 1) * row_height);
                this_frame.drawBitmap(endImage, end_src, end_src, null);
            }
        }
        return workingFrame;
    }

    // renders and returns frame based on frameNumber in sequence
    public Bitmap getFrame(int frameNumber) {
        return getFrame(frameNumber / (float) totalFrames);
    }
}
