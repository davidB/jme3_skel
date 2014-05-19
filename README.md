This project is my project skeleton for jmonkeyengine 3 (jme3).
I use it to experiment and to store common game behavior (not part of a lib).

# To Build

I use gradle to build, and run the project (the next build tool for jme 3.1).
I provide gradlew (gradle wrapper) that install gradle if not available.

## Requirements

* required java installed and available in $PATH
* required to have jme3 and lemur in local maven repository
  I use [jme3_lib2repo]() to install jme3, lemur into my local maven repository

## Command Line

```
cd jme3_skel
gradlew build
```

## Netbeans

* install gradle plugin
* open the project
* build