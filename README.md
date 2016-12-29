# SnowStorm
[![YouTube video](https://img.youtube.com/vi/_4E33QT61qk/0.jpg)](https://www.youtube.com/watch?v=_4E33QT61qk)

Beautiful physics based snow simulation in pure Java 7 with AWT. No dependencies just download and run!

The simulation features a very simple 3D physics and rendering engine. All particles experience:
* Gravity
* Air drag
* Variable wind speed in different sections

The wind effect can create vortexes that carry particles around in a very realistic manner.

*Disclaimer:* This is a just-for-fun and possibly educational project created from scratch in an afternoon. So no unit test, no seriousness, no attempt to make it 100% physically accurate. Just enjoy the snowflakes, they are beautiful!

# Download

[Click to download!](release/SnowStorm.jar?raw=true)

If you have Java installed, double click the downloaded file. No additional installation required.

Works on all operating systems that support Java Runtime Environment (JRE): Linux, OS X, Windows

# Compiling

This is a plain IntelliJ IDEA project. Since the project doesn't have any dependencies you can just run it!

# Tweaks

All Java constants. Adjust in source and re-compile. No interactive controls implemented.

* Framerate - Default is 60 fps. Could be adjusted in *info.martinmarinov.snowstorm.physics.Driver*. I recommend setting it to 30 fps if you have any performance issues.
* Number of snowflakes - Default is 1000. Could be adjusted in *info.martinmarinov.snowstorm.Main*. Reduce if you have performance issues.
* A lot of tweaks regarding snowflake size, distribution and initial speed in *info.martinmarinov.snowstorm.physics.SceneGenerator*
* Tweaks regarding speed of gravity, air resistance and wind direction in all classes under package *info.martinmarinov.snowstorm.forces*

# Background and licensing

The code is released under the MIT license without any warranty. Please read the LICENSE file for more information.

The background image is royalty free provided by [PIXELS](https://www.pexels.com/photo/trees-house-winter-night-24639/).
