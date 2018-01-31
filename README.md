# abbozza-calliope

The project provides the implementation and interface to use abbozza! for the [Calliope Mini](https://calliope.cc). It requires [abbozza-common](https://github.com/mbrinkmeier/abbozza-common). For installation [abbozza-calliope-install](https://github.com/mbrinkmeier/abbozza-common-install) is required additionally.

A precompiled version, including an installation guide, can be found at [here](http://inf-didaktik.rz.uos.de/abbozza/calliope).

## About abbozza!

[abbozza!](http://inf-didaktik.rz.uos.de/abbozza) is a graphical and educational programming language for physical computing.
Currently it supports the following platforms:
* [Arduino](https://arduino.cc) and clones
* [micro:bit](https://microbit.org)
* [Calliope Mini](https://calliope.cc)

It allows graphical programming with [scratch-like blocks](http://scratch.mit.edu) and is based on Googles [Blockly](https://developers.google.com/blockly/)

![An example program](http://inf-didaktik.rz.uos.de/abbozza/img/binaer.png)

[abbozza!](http://inf-didaktik.rz.uos.de/abbozza) is being developed by the working group [Didactics of Computer Science](https://www.inf.uni-osnabrueck.de/arbeitsgruppen/didaktik.html) at the [University Osnabrück, Germany](https://uos.de).

## How to use

**abbozza-common** provides classes and files for the platform-specific IDEs. As such it is a prerequisite for the platform specific projects [abbozza-arduino](https://github.com/mbrinkmeier/abbozza-arduino) and 
[abbozza-calliope](https://github.com/mbrinkmeier/abbozza-calliope).

**abbozza-calliope** provides the interface and IDE for the [Calliope Mini](https://calliope.cc).

## How to build

**abbozza-common** is a Netbeans Project. It provides the common classes and files for the projects
[abbozza-arduino](https://github.com/mbrinkmeier/abbozza-arduino) and 
[abbozza-calliope](https://github.com/mbrinkmeier/abbozza-calliope), which implement the IDEs for
the specific platforms. The latter additionally requires the installer provided by [abbozza-calliope-install](https://github.com/mbrinkmeier/abbozza-calliope-install).

### Manual build

#### 1st Step
Clone the repository [abbozza-common](https://github.com/mbrinkmeier/abbozza-common) and build it with ant.

#### 2nd Step
Clone the repository [abbozza-calliope](https://github.com/mbrinkmeier/abbozza-calliope) to the same directory
you cloned [abbozza-common](https://github.com/mbrinkmeier/abbozza-common) to (i.e. the project direcorties
abboza-common and abbozza-calliope are at the same level). Build it with ant.

#### 3rd Step
Clone the repository [abbozza-calliope-install](https://github.com/mbrinkmeier/abbozza-calliope-install)
to the same directory you cloned [abbozza-common](https://github.com/mbrinkmeier/abbozza-common). Build it with ant.
The installer jar can be found in abbozza-calliope-install/dist. Run it.

#### Additional steps
In addition you have to install [yotta](http://yottabuilder.org) and the build system. You can follow the
instructions at [the abbozza! projects homepage](http://inf-didaktik.rz.uos.de/abbozza/calliope).

