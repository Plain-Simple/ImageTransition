package plainsimple.ImageTransition;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Superclass
 */
public abstract class ImageTransition {
    
    // starting image
    protected Bitmap startImage;
    // actual working frame to be drawn on
    protected Bitmap workingFrame;
    // ending image
    protected Bitmap endImage;
    // current frame
    protected int frameCounter = 0;
    // total frames in animation
    protected int totalFrames;
    // whether or not transition has been completed
    protected boolean transitionFinished = false;
    // whether or not transition animation is currently playing
    protected boolean isPlaying = false;
    // dimensions of image
    protected int imgWidth;
    protected int imgHeight;

    public boolean hasFinished() {
        return transitionFinished;
    }
    public boolean isPlaying() {
        return isPlaying;
    }

    public ImageTransition(Bitmap startImage, Bitmap endImage, int totalFrames) {
        this.startImage = startImage;
        workingFrame = startImage.copy(Bitmap.Config.ARGB_8888, true);
        this.endImage = endImage;
        this.totalFrames = totalFrames;
        imgWidth = startImage.getWidth();
        imgHeight = startImage.getHeight();
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

    // renders and returns frame as a Bitmap based on completion of
    // animation sequence
    public Bitmap getFrame(float completion) {
        Canvas this_frame = new Canvas(workingFrame);
        drawFrame(completion, this_frame);
        return workingFrame;
    }

    // renders and returns frame based on completion of sequence
    public abstract void drawFrame(float completion, Canvas canvas);

    // renders and returns frame based on frameNumber in sequence
    public Bitmap getFrame(int frameNumber) {
        return getFrame(frameNumber / (float) totalFrames);
    }
}
