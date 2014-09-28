This project is my project skeleton for jmonkeyengine 3 (jme3).

Use it :

* to experiment
* to store common game behavior (not part of a lib).
* as bootstarp for project, prototype, jam

[![demo 20140926](http://img.youtube.com/vi/Tw9oFsRqHwY/0.jpg)](http://www.youtube.com/watch?v=Tw9oFsRqHwY)

Project show, how I integrate:

* jmonkeyengine (3.0.10)
* gradle as build tool
* java8
* javafx (via jme3-jfx) for gui 
* logback (via slf4j) as logging framework
* dagger as dependency injection (better suited for android than guice)
* lombok as sugar syntax over java
* packaging (via [gradle-getdown-plugin](https://github.com/davidB/gradle-getdown-plugin))

# Download

* [jme3_skel-app-linux-i586.tgz](http://alchim31.free.fr/download/jme3_skel/bundles/jme3_skel-app-linux-i586.tgz)
* [jme3_skel-app-linux-x64.tgz](http://alchim31.free.fr/download/jme3_skel/bundles/jme3_skel-app-linux-x64.tgz)
* [jme3_skel-app-windows-i586.zip](http://alchim31.free.fr/download/jme3_skel/bundles/jme3_skel-app-windows-i586.zip)
* [jme3_skel-app-windows-x64.zip](http://alchim31.free.fr/download/jme3_skel/bundles/jme3_skel-app-windows-x64.zip)
* [jme3_skel-app.tgz](http://alchim31.free.fr/download/jme3_skel/bundles/jme3_skel-app.tgz) (no jre included)

(I need to collect info about gamepad, can you share with me the content of you app/log/joysticks.txt)

# GUI

I first experiment with lemur (see the lemur branch) + integration with MigLayout and create a welcome + settings pages.
But I switch to jdk8 and give a try to javafx. JavaFX is less integrated into jme but provide reusable knowledge (out of jme), more doc, tuto, css, ttf... and tools like SceneBuilder 2.0 to help you design GUI.

The main downside of javaFX+jME, the need to push "action" in javaFX's Thread or in jME's Thread.

The jme3_skel project provide the following pages :

* welcome page
* a fake game page (display action + every inputs)
* a scores page (TODO)
* a level selection page (TODO)
* options / settings page
  * video (resolution, vsync, anti-aliasing, fps, stats)
  * audio (master, music, sound)
  * control (TODO: edition)

Each page is an AppState + FXML (gui)

# To Build

I use gradle to build, and run the project.

## Requirements

* required java 8 installed and available in $PATH
* required you install [JME3-JFX](https://github.com/empirephoenix/JME3-JFX)

## Command Line

```
cd jme3_skel
gradle run
```

## IDE

I use eclipse 4.4 as main IDE, and netbeans 8 + jme's SDK build to edit asset.

# To Do

* add  doc : "howto", "why "
* implement control 's table load+edit+save
* missing pages
* packaging via getdown (W)