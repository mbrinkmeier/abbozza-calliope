#!/bin/bash

# The script has to be executed from the bin directory.

# Check if ../tools exists
if [ -d ../tools ]; then
  # Setting PATH
  export PATH=`pwd`/../tools/bin:`pwd`../tools/arm-none-eabi/bin:`pwd`:$PATH

  # Activate virtualenv
  cd ../tools
  . bin/activate

  # start java
  cd ../bin
fi

java -cp ../lib/jssc-2.8.0.jar:../lib/rsyntaxtextarea.jar:../lib/autocomplete.jar:../lib/abbozza-calliope.jar:..\lib\commons-io-2.5.jar de.uos.inf.did.abbozza.calliope.AbbozzaCalliopeC
