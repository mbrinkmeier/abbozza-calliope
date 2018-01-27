/**
 * @license 
 * abbozza!
 * 
 * File: Abbozza.js
 * 
 * A parser for the serial connection
 * 
 * Copyright 2017 Michael Brinkmeier
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


#include <ctype.h>
#include <string.h>
#include "MicroBit.h"
#include "abbozzaDevice.h"
#include "abbozzaParser.h"

AbbozzaParser::AbbozzaParser(Abbozza* bit) {
    buffer = "";
    currentCommand = "";
    remainder = "";
    debug = false;
    abbozza = bit;
    abbozza->serial = abbozza->serial;
}


void AbbozzaParser::check(int tx, int rx) {
    int start, end;

    ManagedString newBuf;
    ManagedString prefix;
    ManagedString currentLine;
    
    currentCommand = "";
    cmd = "";
    cmdId = "";

    _tx = tx;
    _rx = rx;
    
    abbozza->serialRedirect(_tx,_rx);
    
    newBuf = abbozza->serial.read(1024,ASYNC);
    if ( !(newBuf == "")) {
        buffer = buffer + newBuf;
    }
        currentLine = "";
        start = find(buffer,"[[");
        if ( start >= 0 ) {
            end = find(buffer,"]]");
            if ( end >= 0 ) {
                prefix = buffer.substring(0,start);
                remainder = remainder + prefix;
                currentLine = buffer.substring(start+2,end-start-2);
                currentLine = replace(currentLine,'\n',' ');
                currentLine = replace(currentLine,'\t',' ');
                currentLine = trim(currentLine);
                // buffer.remove(0,end+2);
                buffer = buffer.substring(end+2,buffer.length()-end-2);
                setCommand(currentLine);
            }
        }
}


void AbbozzaParser::setCommand(ManagedString line) {
    if (debug) {
        abbozza->serialRedirect(_tx,_rx);
        abbozza->serial.send("-> executing " + line + "\n");
    }
    currentCommand = line;
    cmdId = "";
    cmd = "";
    if ( currentCommand.charAt(0) == '_' ) {
        cmdId = parse_word();
    }
    cmd = parse_word();
    cmd = toUpperCase(cmd);
}


void AbbozzaParser::sendResponse(ManagedString resp) {
    if ( !(cmdId == "") ) {
        resp = "[[" + cmdId + " " + resp + "]]";
    } else {
        resp = "[["+resp+"]]";
    }
    abbozza->serialRedirect(_tx,_rx);
    abbozza->serial.send(resp + "\n");
    cmdId = "";
}


ManagedString AbbozzaParser::parse_word() {
    int pos = indexOf(currentCommand,' ');
    if ( pos < 0 ) pos = currentCommand.length();
    ManagedString word = currentCommand.substring(0,pos);
    // currentCommand.remove(0,pos);
    currentCommand = currentCommand.substring(pos,currentCommand.length()-pos);
    currentCommand = trim(currentCommand);    
    return word;
}

ManagedString AbbozzaParser::parse_string() {
    int pos = 0;
    ManagedString result = "";
    if ( currentCommand.charAt(0) != '"') return parse_word();
    do {
        pos = indexOf(currentCommand,'"',pos+1);
    } while ( (pos != -1) && (currentCommand.charAt(pos-1) == '\\' ));
    if ( pos == -1 ) pos = currentCommand.length();
    result = currentCommand.substring(1,pos-1);
    // currentCommand.remove(0,pos+1);
    currentCommand = currentCommand.substring(pos+1,buffer.length()-pos);
    return result;
}


ManagedString AbbozzaParser::getCmd() {
    return cmd;
}


int AbbozzaParser::parse_int() {
    ManagedString word = parse_word();
    return (int) atoi(word.toCharArray());
}

long AbbozzaParser::parse_long() {
    ManagedString word = parse_word();
    return atoi(word.toCharArray());
}

float AbbozzaParser::parse_float() {
    ManagedString word = parse_word();
    return atof(word.toCharArray());
}

double AbbozzaParser::parse_double() {
    ManagedString word = parse_word();
    return atof(word.toCharArray());
}

void AbbozzaParser::execute() {
  if ( cmd == "" ) return;

  ManagedString command;
  ManagedString arg;
  int pin, value;

  if ( cmd == "SCROLL" ) {
    arg = parse_string();
    abbozza->serialRedirect(_tx,_rx);
    abbozza->serial.send(arg);
    abbozza->display.scroll(arg); 
  } else if ( cmd == "RGB") {
      int red = parse_int();
      int green = parse_int();
      int blue = parse_int();
      
      abbozza->rgb.setColour(red,green,blue,0);
  } else if ( cmd == "DSET" ) {
    pin = parse_int();
    pin = abbozza->getPin(pin);
    
    value = parse_int();
  
    if ( value > 0 ) {
      (abbozza->io.pin[pin]).setDigitalValue(1);
    } else {
      (abbozza->io.pin[pin]).setDigitalValue(0);
    }
  } else if ( cmd == "ASET" ) {
      pin = parse_int();
      pin = abbozza->getPin(pin);

      value = parse_int();

      (abbozza->io.pin[pin]).setAnalogValue(value);
  } else if ( cmd == "DGET" ) {
      pin = parse_int();
      pin = abbozza->getPin(pin);
      value = (abbozza->io.pin[pin]).getDigitalValue() > 0 ? 1 : 0;
      sendResponse("DVAL " + ManagedString(pin) + " " + ManagedString(value));
  }  else if ( cmd == "AGET" ) {
    pin = parse_int();
    pin = abbozza->getPin(pin);

    value = (abbozza->io.pin[pin]).getAnalogValue();
    sendResponse("AVAL " + ManagedString(pin) + " " + ManagedString(value));
  } else if ( cmd == "DEBUG" ) {
      value = parse_int();
      if ( value == 0) {
          debug = false;
      } else {
          debug = true;
      }
  }
}


ManagedString AbbozzaParser::replace(ManagedString str, char from, char to) {
   ManagedString res("");
   
   for (int i=0; i < str.length(); i++) {
      if ( ((char) str.charAt(i)) == from ) {
         res = res + to;
      } else {
         res = res + ((char) str.charAt(i));
      }
   }
   return res;
}


ManagedString AbbozzaParser::toUpperCase(ManagedString str) {
   ManagedString res("");

   for (int i = 0; i < str.length(); i++) {
      res = res + ((char) toupper((char) str.charAt(i)));
   }

   return res;
}

ManagedString AbbozzaParser::trim(ManagedString str) {
   ManagedString res("");
   int start = 0;
   int end = str.length()-1;

   while ( isspace((char) str.charAt(start)) && (start <= end) ) {  
      start++;
   }
   while ( isspace((char) str.charAt(end)) && (start <= end)) {
      end--;
   } 

   res = str.substring(start,end-start+1);

   return res;
}


int AbbozzaParser::indexOf(ManagedString haystack, char needle, int start) {
   int pos = start;

   while ( (pos < haystack.length()) && ((char) haystack.charAt(pos) != needle)) {
      pos++; 
   } 

   if ( pos >= haystack.length() ) pos = -1;

   return pos;
}

int AbbozzaParser::indexOf(ManagedString haystack, char needle) {
   return indexOf(haystack,needle,0);
}

int AbbozzaParser::find(ManagedString haystack, const char* needle) {
   const char* hay = haystack.toCharArray();
   char* pos = strstr(hay,needle);
   if ( pos == NULL ) return -1;
   return pos-hay;
}
