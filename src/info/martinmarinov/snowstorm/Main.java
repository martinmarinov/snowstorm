/*
 * Copyright (c) 2016 Martin Marinov
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
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    private final static int SNOW_FLAKES_COUNT = 1000;

    public static void main(String args[]) throws IOException {
        JFrame frame = new JFrame("Snow Storm");
        BufferedImage background = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("winterPic.jpg"));

        ScreenParameters screenParameters = new ScreenParameters(background.getWidth(), background.getHeight());
        Snowflake[] scene = SceneGenerator.newDefaultScene(SNOW_FLAKES_COUNT, screenParameters);
        AwtCanvasPainter painter = new AwtCanvasPainter(background);
        Simulator simulator = new Simulator(new GravityForce(), new Wind(screenParameters), new AirDrag());

        Driver driver = new Driver(scene, painter, simulator, screenParameters);
        frame.add(painter);

        frame.setSize(screenParameters.x, screenParameters.y);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        painter.setUp();
        driver.start();
    }
}
