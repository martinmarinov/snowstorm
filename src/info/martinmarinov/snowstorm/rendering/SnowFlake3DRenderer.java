/*
 * Copyright (c) 2016 Martin Marinov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package info.martinmarinov.snowstorm.rendering;

/**
 * This class uses an orthographic 3d projection to draw the snowflakes on the screen
 * Actual graphics related functions should be implemented in #renderAt
 */
public abstract class SnowFlake3DRenderer {
    private final double perspective;
    private final int halfScreenX;
    private final int halfScreenY;

    public SnowFlake3DRenderer(ScreenParameters screenParameters) {
        // perspective is the focal plane on which everything is projected
        // it is drawn on the z axis
        // perspective is set to screen z/2 so we project at middle of the view frustum
        perspective = screenParameters.z / 2;

        halfScreenX = screenParameters.x / 2;
        halfScreenY = screenParameters.y / 2;
    }

    public void renderSnowflake(double x, double y, double radius, double z, ScreenParameters screenParameters) {
        if (z < screenParameters.z && z > screenParameters.zClip) { // ensure we only render if in front of us
            double pX = projectX(x - radius, z);
            double pY = projectY(y - radius, z);
            double pX2 = projectX(x + radius, z);
            double estimatedDiameter = pX2 - pX;

            if (pX2 >= 0 && pX < screenParameters.x && pY + estimatedDiameter >= 0 && pY < screenParameters.y) {
                // only render if the snowflake can be projected on the screen
                renderAt(pX, pY, estimatedDiameter, getSnowflakeAplha(z, screenParameters));
            }
        }
    }

    /**
     * Actually draw the snowflake on the screen
     * @param x top right corner of snowflake x coordinate
     * @param y top right corner of snowflake y coordinate
     * @param diameter diameter of snowflake
     * @param alpha calculated transparency
     */
    protected abstract void renderAt(double x, double y, double diameter, double alpha);

    /**
     *  Make snowflakes at zClip fully transparent and snowflakes at z fully opaque.
     *  This simulates focus (if something is too close it is a bit blurry, in this case transparent)
     *  Anything in between is linearly interpolated.
     */
    private double getSnowflakeAplha(double z, ScreenParameters screenParameters) {
        return (z - screenParameters.zClip) / (screenParameters.z - screenParameters.zClip);
    }

    private double projectX(double x, double z) {
        return perspective * (x - halfScreenX) / z + halfScreenX;
    }

    private double projectY(double y, double z) {
        return perspective * (y - halfScreenY) / z + halfScreenY;
    }
}
