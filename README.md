# ImageTransition
A library with methods for transitioning between Bitmaps (Android)

# Notes
This repo contains a testApp for testing the library source files. Sources
for the image transition classes can be found in testApp/src/plainsimple/imagetransition
One of the things we are working on is making this project into a full Android library.

# Usage
- All transition classes inherit ImageTransition, a superclass which provides the main
functionalities for animating
- SlideInTransition and SlideOutTransition are currently the two available animations:
  - startImage specifies the image to start with
  - endImage specifies the image to transition to
  - numRows specifies the number of rows the screen should split into as the transition occurs
  - totalFrames specifies over how many frames the animation takes place
  - threshold specifies how far across the screen one row should transition before the next begins
  - pushOffScreen specifies whether transitioning rows should push startImage off the screen as
  they progress
- To use the built-in animation:
  - Construct an instance of a transition
  - Call the start() method to begin. This sets isPlaying to true
  - The nextFrame() method returns a Bitmap with the next frame of the animation rendered. You can
  also use the drawFrame() method and provide a Canvas to draw onto directly
  - Once the animation has finished, isPlaying() will return false
  - The animation can be reset with the reset() method