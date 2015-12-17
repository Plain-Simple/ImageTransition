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
    private boolean pushOffScreen = true;
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
        int row_height = imgHeight / numRows;

        if(completion >= 1.0) {
            return endImage;
        } else if(completion <= 0.0) {
            return startImage;
        } else {
            // draw full rows, simply transferring from endImage to startImage
            int full_rows = (int) (completion * numRows);
            Rect src_end = new Rect(0, 0, imgWidth, full_rows * row_height);
            this_frame.drawBitmap(endImage, src_end, src_end, null);

            // calculate percentage of current row completed
            float row_completion = (completion - (full_rows / (float) numRows)) * (float) numRows;

            if (pushOffScreen) {
                // shift current row of start image left, taking 1 - row_completion as width
                Rect src_start = new Rect((int) (imgWidth * (1.0 - row_completion)), full_rows * row_height,
                        imgWidth, (full_rows + 1) * row_height);
                Rect dst_start = new Rect(0, src_start.top, src_start.width(), src_start.bottom);
                this_frame.drawBitmap(startImage, src_start, dst_start, null);

                // take leading portion from endImage and transfer to trailing portion of start image
                src_end = new Rect(0, full_rows * row_height, (int) (row_completion * imgWidth), (full_rows + 1) * row_height);
                Rect dst_end = new Rect(imgWidth - src_end.width(), src_end.top, imgWidth, src_end.bottom);
                this_frame.drawBitmap(endImage, src_end, dst_end, null);
            } else {
                // simply transfer from trailing portion of end image to start image
                src_end = new Rect(0, full_rows * row_height, (int) (row_completion * imgWidth), (full_rows + 1) * row_height);
                this_frame.drawBitmap(endImage, src_end, src_end, null);
            }
        }
        return workingFrame;
    }

    // renders and returns frame based on frameNumber in sequence
    public Bitmap getFrame(int frameNumber) {
        return getFrame(frameNumber / (float) totalFrames);
    }
}
