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

import info.martinmarinov.snowstorm.rendering.ScreenParameters;
import info.martinmarinov.snowstorm.forces.Force;

public class Simulator {
    private final Force[] forces;

    public Simulator(Force ... forces) {
        this.forces = forces;
    }

    public void advance(Snowflake[] scene, ScreenParameters screenParameters, long durationNs) {
        for (Snowflake snowflake : scene) {
            applyForces(snowflake, durationNs);
            updateCoordinatesBasedOnVelocity(snowflake);
            resetSnowflakeIfTooFarOffScreen(snowflake, screenParameters);
        }
    }

    private void applyForces(Snowflake snowflake, long durationNs) {
        for (Force force : forces) {
            force.apply(snowflake, durationNs);
        }
    }

    private void updateCoordinatesBasedOnVelocity(Snowflake snowflake) {
        snowflake.x += snowflake.vx;
        snowflake.y += snowflake.vy;
        snowflake.z += snowflake.vz;
    }

    private void resetSnowflakeIfTooFarOffScreen(Snowflake snowflake, ScreenParameters screenParameters) {
        boolean resetFlake = snowflake.x > screenParameters.x + screenParameters.safeZoneSize ||
                snowflake.x < -screenParameters.safeZoneSize ||
                snowflake.y > screenParameters.y ||
                snowflake.y < -screenParameters.safeZoneSize ||
                snowflake.z < screenParameters.zClip ||
                snowflake.z > screenParameters.z;

        if (resetFlake) snowflake.resetFlake();
    }
}
