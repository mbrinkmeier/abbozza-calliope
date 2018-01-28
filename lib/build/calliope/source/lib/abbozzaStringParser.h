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

#ifndef _AbbozzaStringParser_h
#define _AbbozzaStringParser_h

class AbbozzaStringParser {
    
public:
    AbbozzaStringParser(Abbozza* bit);
    void setLine(ManagedString cmd);

    ManagedString parse_word();
    int parse_int();
    long parse_long();
    float parse_float();
    double parse_double();
    ManagedString parse_string();

    ManagedString replace(ManagedString str, char from, char to);
    ManagedString toUpperCase(ManagedString str);
    ManagedString trim(ManagedString str);
    int indexOf(ManagedString haystack,char needle);
    int indexOf(ManagedString haystack,char needle, int start);
    int find(ManagedString haystack,const char * needle);
    
private:
    Abbozza* abbozza;
    ManagedString buffer;
};

#endif
