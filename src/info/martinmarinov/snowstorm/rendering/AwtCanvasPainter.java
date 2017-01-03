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

import info.martinmarinov.snowstorm.tools.FpsCounter;
import info.martinmarinov.snowstorm.physics.Snowflake;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class AwtCanvasPainter extends Canvas implements Painter {
    private final static boolean DEBUG_SHOW_FPS = false; // change this to true to see what the current frame rate is
    private final static AlphaComposite LOW_OPACITY = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
    private final static Toolkit DEFAULT_TOOLKIT = Toolkit.getDefaultToolkit();

    private final FpsCounter fpsCounter = new FpsCounter();
    private final BufferedImage background;
    private final double xoffset;

    public static AwtCanvasPainter withBackground(BufferedImage background) {
        return withBackground(background, 0.0);
    }

    public static AwtCanvasPainter withBackground(BufferedImage background, double xoffset) {
        return new AwtCanvasPainter(background, xoffset);
    }

    private AwtCanvasPainter(BufferedImage background, double xoffset) {
        this.background = background;
        this.xoffset = xoffset;
    }

    public void setUp() {
        createBufferStrategy(2);
        setIgnoreRepaint(true);
    }

    @Override
    public void drawScene(Snowflake[] scene, ScreenParameters screenParameters) {
        if (DEBUG_SHOW_FPS) fpsCounter.recordFrame();

        BufferStrategy strategy = getBufferStrategy();
        do {
            do {
                Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
                SnowFlake3DRenderer snowFlake3DRenderer = new AwtSnowflakeRenderer(g2d, screenParameters);

                drawBackground(g2d, screenParameters);
                for (Snowflake snowflake : scene) {
                    snowFlake3DRenderer.renderSnowflake(snowflake.x + xoffset, snowflake.y, snowflake.size, snowflake.z, screenParameters);
                }

                if (DEBUG_SHOW_FPS) {
                    g2d.setColor(Color.white);
                    g2d.drawString(String.format("%.2f min FPS", fpsCounter.getMinFps()), 20, 20);
                    g2d.drawString(String.format("%.2f max FPS", fpsCounter.getMaxFps()), 20, 35);
                }

                g2d.dispose();

            } while (strategy.contentsRestored());

            strategy.show();
            DEFAULT_TOOLKIT.sync(); // Seems like this is necessary on Ubuntu for smooth animation
        } while (strategy.contentsLost());
    }

    /**
     * Draw background with some opacity. This actually creates motion blur effect
     * since previous positions of the snowflakes are still partially visible underneath
     */
    private void drawBackground(Graphics2D g2d, ScreenParameters screenParameters) {
        Composite defaultComposite = g2d.getComposite();
        g2d.setComposite(LOW_OPACITY);
        g2d.drawImage(background, 0, 0, screenParameters.x, screenParameters.y, null);
        g2d.setComposite(defaultComposite);
    }
}
