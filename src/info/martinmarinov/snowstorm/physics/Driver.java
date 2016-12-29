/*
 * Copyright (c) 2016 Martin Marinov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package info.martinmarinov.snowstorm.physics;

import info.martinmarinov.snowstorm.rendering.Painter;
import info.martinmarinov.snowstorm.rendering.ScreenParameters;
import info.martinmarinov.snowstorm.tools.ConstantFpsThread;

public class Driver extends ConstantFpsThread {
    private final static double DEFAULT_FPS = 60.0;

    private final Snowflake[] scene;
    private final Painter painter;
    private final Simulator simulator;
    private final ScreenParameters screenParameters;

    public Driver(Snowflake[] scene, Painter painter, Simulator simulator, ScreenParameters screenParameters, double fps) {
        super(fps);
        this.scene = scene;
        this.painter = painter;
        this.simulator = simulator;
        this.screenParameters = screenParameters;
    }

    public Driver(Snowflake[] scene, Painter painter, Simulator simulator, ScreenParameters screenParameters) {
        this(scene, painter, simulator, screenParameters, DEFAULT_FPS);
    }

    @Override
    protected void doWork() {
        simulator.advance(scene, screenParameters, delayNs);
        painter.drawScene(scene, screenParameters);
    }
}
