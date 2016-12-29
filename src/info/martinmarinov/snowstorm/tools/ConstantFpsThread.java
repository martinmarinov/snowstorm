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
 * This class ensures #doWork is called at the given frame rate regardless of how long it takes
 * The class ensures average fps is respected while also ensuring maximum consistency between frames
 * This results in smooth animations
 */
public abstract class ConstantFpsThread extends Thread {
    protected final long delayNs;

    public ConstantFpsThread(double fps) {
        this.delayNs = Math.round(1_000_000_000.0 / fps);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            doWork();
            long now = System.nanoTime();
            long nextFrame = (now / delayNs) * delayNs + delayNs;

            do {
                if (!safeSleep(nextFrame - System.nanoTime())) return;
            } while(System.nanoTime() < nextFrame);
        }
    }

    private static boolean safeSleep(long toSleepNs) {
        long toSleepMs = (toSleepNs + 999_999L) / 1_000_000L;
        if (toSleepMs > 0) {
            try {
                Thread.sleep(toSleepMs);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }

    protected abstract void doWork();
}
