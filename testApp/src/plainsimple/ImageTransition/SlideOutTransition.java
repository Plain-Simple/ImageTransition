package plainsimple.ImageTransition;

import android.graphics.*;

/**
 * Given starting image of screen, creates a "sliding out" animation
 * and feeds frames
 */
public class SlideOutTransition extends ImageTransition {

    // num rows to slide across screen
    private int numRows;
    // percentage a row should slide across screen before next row starts moving
    private float threshold;
    // whether start image shoulc be "pushed" off the screen
    private boolean pushOffScreen;

    public SlideOutTransition(Bitmap startImage, Bitmap endImage, int numRows, int totalFrames, boolean pushOffScreen) {
        super(startImage, endImage, totalFrames);
        this.numRows = numRows;
        this.pushOffScreen = pushOffScreen;
    }

    // draws frame based on completion of animation sequence onto
    // given canvas
    public void drawFrame(float completion, Canvas canvas) {
        int row_height = imgHeight / numRows;

        if(completion >= 1.0) {
            canvas.drawBitmap(endImage, 0, 0, null);
        } else if(completion <= 0.0) {
            canvas.drawBitmap(startImage, 0, 0, null);
        } else {
            // draw full rows, simply transferring from endImage to startImage
            int full_rows = (int) (completion * numRows);
            Rect end_src = new Rect(0, 0, imgWidth, full_rows * row_height);
            canvas.drawBitmap(endImage, end_src, end_src, null);

            // calculate percentage of current row completed
            float row_completion = (completion - (full_rows / (float) numRows)) * (float) numRows;

            if (pushOffScreen) {
                // shift current row of start image left, taking 1 - row_completion as width
                Rect start_src = new Rect((int) (imgWidth * row_completion), full_rows * row_height,
                        imgWidth, (full_rows + 1) * row_height);
                Rect start_dst = new Rect(0, start_src.top, start_src.width(), start_src.bottom);
                canvas.drawBitmap(startImage, start_src, start_dst, null);

                // take leading portion from endImage and transfer to trailing portion of start image
                end_src = new Rect(0, full_rows * row_height, (int) (row_completion * imgWidth), (full_rows + 1) * row_height);
                Rect end_dst = new Rect(imgWidth - end_src.width(), end_src.top, imgWidth, end_src.bottom);
                canvas.drawBitmap(endImage, end_src, end_dst, null);
            } else {
                // simply transfer trailing portion of end image to start image
                end_src = new Rect((int) ((1.0 - row_completion) * imgWidth), full_rows * row_height, imgWidth, (full_rows + 1) * row_height);
                canvas.drawBitmap(endImage, end_src, end_src, null);
            }
        }
    }
}
