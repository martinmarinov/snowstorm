/*
 * Copyright (c) 2017 Martin Marinov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package info.martinmarinov.snowstorm;

import info.martinmarinov.snowstorm.forces.AirDrag;
import info.martinmarinov.snowstorm.forces.GravityForce;
import info.martinmarinov.snowstorm.forces.Wind;
import info.martinmarinov.snowstorm.physics.Driver;
import info.martinmarinov.snowstorm.physics.SceneGenerator;
import info.martinmarinov.snowstorm.physics.Simulator;
import info.martinmarinov.snowstorm.physics.Snowflake;
import info.martinmarinov.snowstorm.rendering.AwtCanvasPainter;
import info.martinmarinov.snowstorm.rendering.ScreenParameters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main3D {
    private final static int SNOW_FLAKES_COUNT = 1000;
    private final static double STEREO_SEPARATION = 5;

    public static void main(String args[]) throws IOException {
        GraphicsDevice device = getGraphicsDevice();

        JFrame frame = new JFrame("Snow Storm");
        BufferedImage background = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("winterPic.jpg"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        device.setFullScreenWindow(frame);
        frame.pack();

        Rectangle bounds = device.getDefaultConfiguration().getBounds();
        ScreenParameters screenParameters = new ScreenParameters((int)(bounds.getWidth() / 2), (int) (bounds.getHeight()));
        Snowflake[] scene = SceneGenerator.newDefaultScene(SNOW_FLAKES_COUNT, screenParameters);
        AwtCanvasPainter painterLeft = AwtCanvasPainter.withBackground(background, STEREO_SEPARATION);
        AwtCanvasPainter painterRight = AwtCanvasPainter.withBackground(background, -STEREO_SEPARATION);
        Simulator simulator = new Simulator(new GravityForce(), new Wind(screenParameters), new AirDrag());

        Driver driver = new Driver(scene, simulator, screenParameters, painterLeft, painterRight);

        frame.setLayout(new GridLayout(1, 2));
        frame.add(painterLeft);
        frame.add(painterRight);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        painterLeft.setUp();
        painterRight.setUp();

        driver.start();
    }

    private static GraphicsDevice getGraphicsDevice() {
        // Get last available monitor, very likely an external one
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        return screenDevices[screenDevices.length-1];
    }
}
