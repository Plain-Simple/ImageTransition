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
    private float threshold = 0.5f;
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
        int threshold_width = (int) (imgWidth * threshold);

        if(completion >= 1.0) {
            canvas.drawBitmap(endImage, 0, 0, null);
        } else if(completion <= 0.0) {
            canvas.drawBitmap(startImage, 0, 0, null);
        } else {
            // count total thresholds on the screen, including in last row
            float total_thresholds = (numRows - 1) + 1.0f / threshold;
            float num_thresholds = total_thresholds * completion;
            for(int i = 0; i < numRows && i <= num_thresholds; i++) { 
                // represents section of the row on canvas that is in transition
                Rect src = new Rect(imgWidth - (int) ((num_thresholds - i) * threshold_width), i * row_height,
                        imgWidth, (i + 1) * row_height);
                // limit to width of screen
                if(src.width() > imgWidth) {
                    src.left = 0;
                }
                if(pushOffScreen) {
                    // pixels from startImage to be shifted left
                    Rect start_src = new Rect(src.width(), src.top, imgWidth, src.bottom);
                    // new location of shifted pixels
                    Rect start_dst = new Rect(0, start_src.top, start_src.width(), start_src.bottom);
                    canvas.drawBitmap(startImage, start_src, start_dst, null);

                    // pixels from endImage to be drawn on canvas
                    Rect end_src = new Rect(0, src.top, src.width(), src.bottom);
                    // new location of pixels on canvas
                    Rect end_dst = new Rect(src.left, src.top, src.right, src.bottom);
                    canvas.drawBitmap(endImage, end_src, end_dst, null);
                } else {
                    // simply transfer pixels from endImage to startImage. No change necessary.
                    canvas.drawBitmap(endImage, src, src, null);
                }
            }
        }
    }
}
