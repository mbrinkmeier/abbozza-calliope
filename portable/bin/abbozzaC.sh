#!/bin/bash

# The script has to be executed from the bin directory.

# Check if ../tools exists
#if [ -d ../tools ]; then
#  # Setting PATH
#  export PATH=`pwd`/../tools/bin:`pwd`../tools/arm-none-eabi/bin:`pwd`:$PATH
#
#  # Activate virtualenv
#  cd ../tools
#  . bin/activate
#
#  # start java
#  cd ../bin
#fi

set CP=../lib/jSerialComm-2.4.2.jar:../lib/rsyntaxtextarea.jar
set CP=$CP;:../lib/autocomplete.jar:../lib/abbozza-calliope.jar
set CP=$CP:../lib/commons-io-2.5.jar:../lib/websockets.jar:../lib/slf4j-api-1.7.25.jar

java -cp $CP de.uos.inf.did.abbozza.calliope.AbbozzaCalliopeC
