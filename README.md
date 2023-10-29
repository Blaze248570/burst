# JBurst

### What is it?
JBurst is a non-revolutionary addition to [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing) modeled from [HaxeFlixel](https://haxeflixel.com) that adds sprites with animation and simple transformation methods.

### Why did I make it?
I always found it frustrating how JLabel only really supported static images.

(This was once intended to be a very basic game engine, but I've since lost interest)

### Notes
- Layout managers cause the sprites to behave wierdly, so they usually have to be deactivated.

- I worked on this in my free time during high school so it may have problems here and there. If you find one, report it! I may look into it.

## *Notice*
I would like to convert this project to use Apache Maven, but I've had a lot of trouble with it. 
If anyone has experience or would like to assist me, help would be greatly appreciated.

## How do I use it?
It's as simple as:
```java
int x = 50, y = 50;
int frameWidth = 100, frameHeight = 150;
int[] frameIndices = new int[] {0, 1, 2, 3};
int frameRate = 30;
boolean looped = true;

JBurstSprite sprite = new JBurstSprite(x, y);
sprite.loadAnimatedGraphic("Image file location", frameWidth, frameHeight);
sprite.animation.add("idle", frameIndices, framerate, looped);
sprite.animation.play("idle");

// Then just add this sprite to whatever container you're using
```

### Dependencies
- JBurst uses [json-simple-1.1.1](https://github.com/fangyidong/json-simple) for json support.

*Note:* JBurst is compiled with VS Code's [Language Support for Java(TM) by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java) using `JRE 17.0.8.1`.
I've done my best to ensure that older versions can use it as well, so please mention version issues if you come across them.

### Demos
- An example program using JBurst can be found within demo.zip

- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing)

## Helping
There are things that I still do not know about Swing and about Java. So, if you have any suggestions (or warnings) let me hear them.

# HaxeFlixel
If you enjoy using this, give [HaxeFlixel](https://haxeflixel.com) a shot. A large amount of JBurst's animation system is modeled on their game engine, so it would feel wrong not to mention them.
