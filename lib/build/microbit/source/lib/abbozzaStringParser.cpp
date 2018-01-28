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
#include "abbozzaStringParser.h"

AbbozzaStringParser::AbbozzaStringParser(Abbozza* bit) {
    buffer = "";
    abbozza = bit;
}


void AbbozzaStringParser::setLine(ManagedString line) {
    buffer = new ManagedString(line);
}


ManagedString AbbozzaStringParser::parse_word() {
    int pos = indexOf(buffer,' ');
    if ( pos < 0 ) pos = buffer.length();
    ManagedString word = buffer.substring(0,pos);
    // currentCommand.remove(0,pos);
    buffer = buffer.substring(pos,buffer.length()-pos);
    buffer = trim(buffer);    
    return word;
}

ManagedString AbbozzaStringParser::parse_string() {
    int pos = 0;
    ManagedString result = "";
    if ( buffer.charAt(0) != '"') return parse_word();
    do {
        pos = indexOf(buffer,'"',pos+1);
    } while ( (pos != -1) && (buffer.charAt(pos-1) == '\\' ));
    if ( pos == -1 ) pos = buffer.length();
    result = buffer.substring(1,pos-1);
    // currentCommand.remove(0,pos+1);
    buffer = buffer.substring(pos+1,buffer.length()-pos);
    return result;
}



int AbbozzaStringParser::parse_int() {
    ManagedString word = parse_word();
    return (int) atoi(word.toCharArray());
}

long AbbozzaStringParser::parse_long() {
    ManagedString word = parse_word();
    return atoi(word.toCharArray());
}

float AbbozzaStringParser::parse_float() {
    ManagedString word = parse_word();
    return atof(word.toCharArray());
}

double AbbozzaStringParser::parse_double() {
    ManagedString word = parse_word();
    return atof(word.toCharArray());
}



ManagedString AbbozzaStringParser::replace(ManagedString str, char from, char to) {
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


ManagedString AbbozzaStringParser::toUpperCase(ManagedString str) {
   ManagedString res("");

   for (int i = 0; i < str.length(); i++) {
      res = res + ((char) toupper((char) str.charAt(i)));
   }

   return res;
}

ManagedString AbbozzaStringParser::trim(ManagedString str) {
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


int AbbozzaStringParser::indexOf(ManagedString haystack, char needle, int start) {
   int pos = start;

   while ( (pos < haystack.length()) && ((char) haystack.charAt(pos) != needle)) {
      pos++; 
   } 

   if ( pos >= haystack.length() ) pos = -1;

   return pos;
}

int AbbozzaStringParser::indexOf(ManagedString haystack, char needle) {
   return indexOf(haystack,needle,0);
}

int AbbozzaStringParser::find(ManagedString haystack, const char* needle) {
   const char* hay = haystack.toCharArray();
   char* pos = strstr(hay,needle);
   if ( pos == NULL ) return -1;
   return pos-hay;
}
