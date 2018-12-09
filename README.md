# abbozza-calliope

The project provides the implementation and interface to use abbozza! for the 
[Calliope Mini](https://calliope.cc).  It requires
[abbozza-common](https://github.com/mbrinkmeier/abbozza-common).

A precompiled version, including an installation guide, can be found at 
[the projects homepage](http://inf-didaktik.rz.uos.de/abbozza/calliope).

## About abbozza!

[abbozza!](http://inf-didaktik.rz.uos.de/abbozza) is a graphical and educational
programming language for physical computing. Currently it supports the following platforms:
* [Arduino](https://arduino.cc) and clones
* [Calliope Mini](https://calliope.cc)

In addittion there exists [abbozza! Worlds](https://inf-didaktik.rz.uos.de/abbozza/worlds).

It allows graphical programming with [scratch-like blocks](http://scratch.mit.edu)
and is based on Googles [Blockly](https://developers.google.com/blockly/)

![An example program](http://inf-didaktik.rz.uos.de/abbozza/img/binaer.png)

[abbozza!](http://inf-didaktik.rz.uos.de/abbozza) is being developed by the working group 
[Didactics of Computer Science](https://www.inf.uni-osnabrueck.de/arbeitsgruppen/didaktik.html)
at the [University Osnabrück, Germany](https://uos.de).

## How to use

**abbozza-common** provides classes and files for the platform-specific IDEs.
As such it is a prerequisite for the platform specific projects
[abbozza-arduino](https://github.com/mbrinkmeier/abbozza-arduino) and 
[abbozza-calliope](https://github.com/mbrinkmeier/abbozza-calliope).

**abbozza-calliope** provides the interface and IDE for the
[Calliope Mini](https://calliope.cc) and an installer.

## How to build

**abbozza-common** is a Netbeans Project. It provides the common classes and
files for the projects
[abbozza-arduino](https://github.com/mbrinkmeier/abbozza-arduino),  
[abbozza-calliope](https://github.com/mbrinkmeier/abbozza-calliope) and
[abbozza-worlds](https://github.com/mbrinkmeier/abbozza-worlds), which implement
IDEs for different platforms. 

### Building

#### Preparations
Prepare a directory which may hold all required projects. In addition `ant`,
and Java Developement Kit (at least JDK 8) need to be installed.

#### 1st Step - Get abbozza-common
Clone the repository [abbozza-common](https://github.com/mbrinkmeier/abbozza-common):
`git clone https://github.com/mbrinkmeier/abbozza-common.git`.

#### 2nd Step - Get abbozza-calliope
Clone the repository [abbozza-calliope](https://github.com/mbrinkmeier/abbozza-calliope) to the same directory
you cloned [abbozza-common](https://github.com/mbrinkmeier/abbozza-common) to (i.e. the project direcorties
abboza-common and abbozza-calliope are at the same level):
`git clone https://github.com/mbrinkmeier/abbozza-calliope.git`.
Change into `abbozza-calliope` and execute `ant`.

#### 3rd Step - Build abozza-common
Go to the directory `abbozza-common` and execute `ant`.

#### 4th step - Build abbozza-calliope
Change to `../abbozza-calliope`. Using ant you can build the following versions:
* `ant installer-win` builds a Jar-file providing an installer for Windows.
* `ant installer-linux` builds a Jar-file providing an installer for Linux.
* `ant installer-macos` builds a Jar-file providing an installer for MacOs.
* `ant portable` builds a ZIP-archive, containing a ready to run Windows installation.

### Installation

### The Portable Version
