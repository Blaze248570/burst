# JBurst

### What is it?
JBurst is a non-revolutionary addition to [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing) modeled from [HaxeFlixel](https://haxeflixel.com) that adds sprites with animation and simple transformation methods.

### Why did I make it?
I always found it frustrating how JLabel only really supported static images.

(This was once intended to be a very basic game engine, but I've since realized Swing really isn't cut out for it.)

### Does it work?
For the most part. Layout managers treat the sprites wierdly, so they usually have to be deactivated.

I worked on this in my free time during high school so it may have problems here and there.
If you find one, report it! I'll look into it.

## How do I use it?
It's as simple as:
```java
int x = 50, y = 50;
int frameHeight = 100, frameHeight = 150;
int[] frameIndices = new int[] {0, 1, 2, 3};
int frameRate = 30;
boolean looped = true;

JBurstSprite sprite = new JBurstSprite(x, y);
sprite.loadAnimatedGraphic("Image file location", frameWidth, frameHeight);
sprite.animation.add("idle", frameIndices, framerate, looped);
sprite.animation.play("idle");

// Then just add this sprite to whatever container you're using
```
The template folder also provides an example program using JBurst, and a Swing tutorial can be at
[Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing).\
*Note:* I compiled this using `JDK 18.0.1`, but I'm doing my best to make sure older versions can use it as well.
If I missed something, mention it.

## Helping
I am a novice programmer. There are things that I do not know about Swing and about Java.
So, if you have any suggestions (or warnings) let me hear them.

# HaxeFlixel
If you enjoy using this, give [HaxeFlixel]((https://haxeflixel.com)) a shot.
The base of this engine is modeled on theirs, so they're very similar on a basic level.
It uses an awesome language known as [Haxe](https://haxe.org) and is *completely* open-source.\
Plus, Flixel is an *actual* engine, so you can use it to compile *actual* games.
