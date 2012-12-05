AeroGear Reddit Reader
================

This project demonstrates the new auth library as well as interacting with a third party REST API

This leans on the auth2 branch of AeroGear at https://github.com/secondsun/aerogear-android/tree/auth2

Prerequisites
-------------

* Android Maven SDK Deployer

Note that to build this project with Maven, you'll need to use the
Android Maven SDK Deployer
(https://github.com/mosabua/maven-android-sdk-deployer) in order to
install the Google extension APIs into your local Maven repository.
To do this, just do

```bash
$ cd <wherever you installed the android-sdk-deployer>
$ mvn install -P 4.1
```

from the maven-android-sdk-deployer root directory.

NOTE: make sure you've already installed all the relevant Android
packages to your local SDK installation via the "android" tool, as
described in the documentation at
https://github.com/mosabua/maven-android-sdk-deployer/blob/master/README.markdown.

* AeroGear-Android

Until we have the [Android Libary](http://github.com/aerogear/aerogear-android)
set up to publish to a central Maven repo, you'll need to go get that project,
and build it with "mvn install" in order to put the library jar into your
local Maven repository.

For this project, until it is merged, please use the [auth2 branch](https://github.com/secondsun/aerogear-android/tree/auth2).

Building
--------

After the above, running "mvn install" from the root directory
of this project should successfully build everything, run the
tests, and generate you a shiny new APK all set to install on
your phone.
