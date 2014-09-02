This project is my project skeleton for jmonkeyengine 3 (jme3).
I use it to experiment and to store common game behavior (not part of a lib).

[![demo 20140902](http://img.youtube.com/vi/TGXrvvhBb_c/0.jpg)](http://www.youtube.com/watch?v=TGXrvvhBb_c)

Project show, how I integrate:

* jmonkeyengine
* gradle as build tool
* java8
* javafx (via jme3-jfx) for gui 
* logback (via slf4j) as logging framework
* dagger as dependency injection (better suited for android than guice)
* lombok as sugar syntax over java
* packaging (via getdown + jre provided) WIP

## GUI

I first experiment with lemur (see the lemur branch) + integration with MigLayout and create a welcome + settings pages.
But I switch to jdk8 and give a try to javafx. JavaFX is less integrated into jme but provide reusable knowledge (out of jme), more doc, tuto,... and tools like SceneBuilder 2.0 to help you design GUI.

The main downside of javaFX+jME, the need to push "action" in javaFX's Thread or in jME's Thread.

The jme3_skel project provide the following pages :

* welcome page
* a fake game page (TODO)
* a scores page (TODO)
* a level selection page (TODO)
* options / settings page
  * video (resolution, vsync, anti-aliasing, fps, stats)
  * audio (master, music, sound)
  * control (TODO)

Each page is an AppState + FXML (gui)

# To Build

I use gradle to build, and run the project (the next build tool for jme 3.1).

## Requirements

* required java 8 installed and available in $PATH
* required you install [JME3-JFX](https://github.com/empirephoenix/JME3-JFX)
* required to have jme3 in local maven repository. I use [jme3_lib2repo](https://github.com/davidB/jme3_lib2repo) to install jme3

## Command Line

```
cd jme3_skel
gradle run
```

## IDE

I use eclipse 4.4 as main IDE, and netbeans 8 + jme's SDK build to edit asset.
