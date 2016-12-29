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

import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.geom.Point2D;

public class AwtSnowflakeRenderer extends SnowFlake3DRenderer {
    private final static float[] GRADIENT_POSITIONS = new float[] {0.0f, 0.7f, 1.0f};
    private final static Color TRANSPARENT_WHITE = new Color(1.0f, 1.0f, 1.0f, 0.0f);
    private final Graphics2D g2d;

    public AwtSnowflakeRenderer(Graphics2D g2d, ScreenParameters screenParameters) {
        super(screenParameters);
        this.g2d = g2d;
    }

    /**
     * The snowflakes are drawn as squares with a circular gradient in the middle.
     * That's why they look circular and fuzzy. This is also pretty cheap since it doesn't require blurring and drawing
     * squares is fast.
     *
     * @param x top right corner of snowflake x coordinate
     * @param y top right corner of snowflake y coordinate
     * @param diameter diameter of snowflake
     * @param alpha calculated transparency
     */
    @Override
    protected void renderAt(double x, double y, double diameter, double alpha) {
        Color snowflakeColor = new Color(1.0f, 1.0f, 1.0f, (float) alpha);
        if (diameter > 7) {
            // Center of snowflake is white with opacity according to the value provided in alpha
            float radius = (float) (diameter / 2.0);
            Point2D.Float center = new Point2D.Float((float) (x + radius), (float) (y + radius));
            Paint paint = new RadialGradientPaint(center, radius, center, GRADIENT_POSITIONS,
                    new Color[]{snowflakeColor, snowflakeColor, TRANSPARENT_WHITE},
                    CycleMethod.NO_CYCLE);
            g2d.setPaint(paint);
            g2d.fillRect((int) x, (int) y, (int) diameter, (int) diameter);
        } else if (diameter > 3) {
            // for small snowflakes an oval works just fine
            g2d.setColor(snowflakeColor);
            g2d.fillOval((int) x, (int) y, (int) diameter, (int) diameter);
        } else {
            // just a rectangle would work for these
            g2d.setColor(snowflakeColor);
            g2d.fillRect((int) x, (int) y, (int) diameter, (int) diameter);
        }
    }
}
