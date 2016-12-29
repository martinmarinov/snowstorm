/*
 * Copyright (c) 2016 Martin Marinov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package info.martinmarinov.snowstorm.tools;

/**
 * Provide a measurement of the frequency of calling #recordFrame
 * You can see the worst and the best FPS measure in order to judge the smoothness of the animation
 * Ideally #getMaxFps and #getMinFps should be equal
 */
public class FpsCounter {
    private static int RESET_MIN_EVERY_N_FRAMES = 30;

    private long lastFrame = System.nanoTime();
    private double minFps = Double.NaN;
    private double maxFps = Double.NaN;
    private int resetCounter = RESET_MIN_EVERY_N_FRAMES;

    public void recordFrame() {
        long thisFrame = System.nanoTime();
        long elapsed = thisFrame - lastFrame;
        double fps = 1_000_000_000.0 / elapsed;
        lastFrame = thisFrame;

        boolean doResetCounter = resetCounter-- == 0;
        if (fps < minFps || doResetCounter) {
            minFps = fps;
            resetCounter = RESET_MIN_EVERY_N_FRAMES;
        }
        if (fps > maxFps || doResetCounter) {
            maxFps = fps;
            resetCounter = RESET_MIN_EVERY_N_FRAMES;
        }
    }

    public double getMinFps() {
        return minFps;
    }

    public double getMaxFps() {
        return maxFps;
    }
}
