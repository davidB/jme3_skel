# Packaging :

* javapackager (previously javafxpackager) can not create bundle for other platform than the current one.
* flow is native -run-> getdown -run-> final app
* indirection via getdown allow :
  * to download the require jre in background (for app provider site) without asking user to download from Oracle and start the installation
  * to download app’s update
* getdown require that the app provider host a web site with :
  * app 
  * jre bundled as jar


## Mac OS X

* see how jmonkeyplatform use nbproject/macapp-impl.xml + resources/macapp
* https://developer.apple.com/library/mac/documentation/corefoundation/conceptual/cfbundles/BundleTypes/BundleTypes.html
* http://sveinbjorn.org/platypus

## Windows

* can use .cmd/.bat/.vbs to launch the app but can’t change the icon.
* can’t provide shortcut because shortcut (.lnk) require absolute path (for application, working directory, icon)
* try to use launch4j (like jmonkeyplatform) but to launch getdown and using ant task and download launch4j from maven central
  http://launch4j.sourceforge.net/docs.html

