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
import info.martinmarinov.snowstorm.tools.ControlledRandom;

public class SceneGenerator {
    public static Snowflake[] newDefaultScene(int snowFlakes, ScreenParameters screenParameters) {
        Snowflake[] snowflakes = new Snowflake[snowFlakes];

        for (int i = 0; i < snowFlakes; i++) {
            snowflakes[i] = new RandomSafeZoneSnowflake(screenParameters);
        }

        return snowflakes;
    }
    private static class RandomSafeZoneSnowflake extends Snowflake {
        private final ScreenParameters screenParameters;

        private RandomSafeZoneSnowflake(ScreenParameters screenParameters) {
            this.screenParameters = screenParameters;
            initFlake();
        }

        /**
         * When initializing snowflake, ensure they are evenly spread around the screen, including the safe offscreen zones
         */
        public void initFlake() {
            x = ControlledRandom.random() * (screenParameters.x + 2 * screenParameters.safeZoneSize) - screenParameters.safeZoneSize;
            y = ControlledRandom.random() * (screenParameters.y + 2 * screenParameters.safeZoneSize) - screenParameters.safeZoneSize;
            z = ControlledRandom.random() * screenParameters.z;
            size = ControlledRandom.randomGaussian() + 3;
            if (size < 1) size = 1;

            resetVelocity();
        }

        /**
         * When resetting snowflake, only put them offscreen in the safe zones.
         * This allows them to gain some realistic velocities by the time they are user visible
         */
        @Override
        public void resetFlake() {
            x = ControlledRandom.random() * (screenParameters.x + 2 * screenParameters.safeZoneSize) - screenParameters.safeZoneSize;
            y = ControlledRandom.random() * (- screenParameters.safeZoneSize);
            z = ControlledRandom.random() * screenParameters.z;

            resetVelocity();
        }

        private void resetVelocity() {
            vx = ControlledRandom.random() * 10 - 5;
            vy = ControlledRandom.random() * 10;
            vz = ControlledRandom.random() * 10 - 5;
        }
    }
}
