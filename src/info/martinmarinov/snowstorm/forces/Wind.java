/*
 * Copyright (c) 2016 Martin Marinov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package info.martinmarinov.snowstorm.forces;

import info.martinmarinov.snowstorm.tools.ControlledRandom;
import info.martinmarinov.snowstorm.rendering.ScreenParameters;
import info.martinmarinov.snowstorm.physics.Snowflake;

/**
 * Wind is applied to snowflakes and changes randomly.
 * The space is divided into #SECTIONS to the third power sections
 * each one has different wind speed, so particles in each section
 * experience same wind forces.
 *
 * This creates the effect of
 */
public class Wind implements Force {
    private final static double MEAN_WIND_SPEED = 1; // pixels per second
    private final static int SECTIONS = 5;

    private final static int DONT_DO_WIND_CHANGE_CALL_DELAY_MIN = 50_000;
    private final static int DONT_DO_WIND_CHANGE_CALL_DELAY_MAX = 300_000;

    private final double[][][] sectionsX = new double[SECTIONS][SECTIONS][SECTIONS];
    private final double[][][] sectionsY = new double[SECTIONS][SECTIONS][SECTIONS];
    private final double[][][] sectionsZ = new double[SECTIONS][SECTIONS][SECTIONS];

    // section size in pixels
    private final int screenX, screenY, screenZ;

    private int nextWindChange = newRandomWindChangeDelay();

    public Wind(ScreenParameters screenParameters) {
        screenX = screenParameters.x / SECTIONS;
        screenY = screenParameters.y / SECTIONS;
        screenZ = screenParameters.z / SECTIONS;

        randomizeWind();
    }

    @Override
    public void apply(Snowflake snowflake, long durationNs) {
        int xSection = ((int) snowflake.x) / screenX;
        int ySection = ((int) snowflake.y) / screenY;
        int zSection = ((int) snowflake.z) / screenZ;

        if (xSection >= 0 && xSection < SECTIONS &&
                ySection >= 0 && ySection < SECTIONS &&
                zSection >= 0 && zSection < SECTIONS) {
            double durationCoeff = durationNs / 1_000_000_000.0;

            snowflake.vx += sectionsX[xSection][ySection][zSection] * durationCoeff;
            snowflake.vy += sectionsY[xSection][ySection][zSection] * durationCoeff;
            snowflake.vz += sectionsZ[xSection][ySection][zSection] * durationCoeff;
        }

        if (nextWindChange-- < 0) {
            // randomly change wind after a random timeout

            randomizeWind();
            nextWindChange = newRandomWindChangeDelay();
        }
    }

    private void randomizeWind() {
        for (int x = 0; x < SECTIONS; x++) {
            for (int y = 0; y < SECTIONS; y++) {
                for (int z = 0; z < SECTIONS; z++) {
                    sectionsX[x][y][z] = 2 * ControlledRandom.randomGaussian() * MEAN_WIND_SPEED;
                    sectionsY[x][y][z] = 2 * ControlledRandom.randomGaussian() * MEAN_WIND_SPEED;
                    sectionsZ[x][y][z] = 2 * ControlledRandom.randomGaussian() * MEAN_WIND_SPEED;
                }
            }
        }
    }

    private static int newRandomWindChangeDelay() {
        return ControlledRandom.randomInt(DONT_DO_WIND_CHANGE_CALL_DELAY_MAX - DONT_DO_WIND_CHANGE_CALL_DELAY_MIN) + DONT_DO_WIND_CHANGE_CALL_DELAY_MIN;
    }
}
